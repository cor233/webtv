#!/bin/bash
# Runs on the APK mirror host (Baota), deployed as /root/pull_apk.sh and driven
# by cron every 5 minutes. This copy is the versioned source of record.
#
# Pull the latest APKs + manifests from the CNB mirror (rebuilt on every release)
# into the web-served directory /www/wwwroot/apkmirror.
#
# Two phases, because a full clone carries ~940 MB of APKs and takes ~77 s:
#   1. probe the published manifest through CNB raw (a few hundred bytes)
#   2. only when the published version differs from the local one: clone + rsync
# On any failure the previous files stay in place (keep-alive).
set -u

DEST=/www/wwwroot/apkmirror
MARKER=mobile-arm64_v8a.json
RAW="https://cnb.cool/code_free/webtv/-/git/raw/main/apk/$MARKER"
LOCK=/var/lock/pull_apk.lock
HEARTBEAT=/root/.pull_apk_last
LOG=/var/log/pull_apk.log

stamp() { date "+%F %T"; }

# Never let a slow clone pile up behind the 5-minute schedule.
exec 9>"$LOCK"
if ! flock -n 9; then
  echo "$(stamp) another pull is still running, skipping" >&2
  exit 0
fi
touch "$HEARTBEAT"

version_of() {
  sed -n 's/.*"versionName"[[:space:]]*:[[:space:]]*"\([^"]*\)".*/\1/p' "$1" 2>/dev/null | head -1
}

WANT=$(curl -fsS --max-time 25 "$RAW" 2>/dev/null | sed -n 's/.*"versionName"[[:space:]]*:[[:space:]]*"\([^"]*\)".*/\1/p' | head -1)
if [ -z "$WANT" ]; then
  echo "$(stamp) version probe failed (CNB unreachable?), keeping current files" >&2
  exit 1
fi

HAVE=""
[ -f "$DEST/$MARKER" ] && HAVE=$(version_of "$DEST/$MARKER")
if [ -n "$HAVE" ] && [ "$HAVE" = "$WANT" ]; then
  # Up to date: stay quiet, this runs every 5 minutes and the log stays small.
  exit 0
fi

TMP=/home/.apk_pull.$$
rm -rf "$TMP"
# The clone carries ~940 MB and the web dir lives on the small root filesystem
# (/ is 20G), so stage the clone on /home and refuse to start when space is
# short — a full disk mid-rsync would leave a half-synced mirror behind.
for check in /home /; do
  avail=$(df -Pk "$check" | awk 'NR==2 {print $4}')
  if [ -z "${avail:-}" ] || [ "$avail" -lt 1500000 ]; then
    echo "$(stamp) not enough free space on $check (${avail:-?}KB), keeping current files" >&2
    exit 1
  fi
done
if ! git clone --depth 1 -q https://cnb.cool/code_free/webtv.git "$TMP"; then
  echo "$(stamp) clone failed (want $WANT, have ${HAVE:-none}), keeping current files" >&2
  rm -rf "$TMP"
  exit 1
fi
if [ ! -d "$TMP/apk" ]; then
  echo "$(stamp) mirror has no apk dir, keeping current files" >&2
  rm -rf "$TMP"
  exit 1
fi

mkdir -p "$DEST"
rsync -a --delete --chmod=D755,F644 --exclude=/index.html "$TMP/apk/" "$DEST/"
[ -f "$TMP/apk/index.html" ] && cp "$TMP/apk/index.html" "$DEST/index.html"
rm -rf "$TMP"

# Verify every APK matches the size its manifest declares: catches a truncated
# rsync (e.g. full disk) which would otherwise serve a broken download.
fail=0
for manifest in "$DEST"/*.json; do
  [ -f "$manifest" ] || continue
  apk=$(sed -n 's/.*"apk"[[:space:]]*:[[:space:]]*"\([^"]*\)".*/\1/p' "$manifest" | head -1)
  size=$(sed -n 's/.*"size"[[:space:]]*:[[:space:]]*\([0-9][0-9]*\).*/\1/p' "$manifest" | head -1)
  [ -n "$apk" ] && [ -n "$size" ] || continue
  actual=$(stat -c%s "$DEST/$apk" 2>/dev/null || echo 0)
  if [ "$actual" != "$size" ]; then
    echo "$(stamp) size mismatch: $apk expected $size got $actual" >&2
    fail=1
  fi
done

# Optional: purge the EdgeOne edge cache right after a release lands. Only runs
# when /root/eo_purge.sh exists, so it stays a no-op until EO credentials are set.
if [ "$fail" -eq 0 ] && [ -x /root/eo_purge.sh ]; then
  /root/eo_purge.sh --prefix "https://pan.imotao.com/file/apk/" || echo "$(stamp) EdgeOne purge failed" >&2
fi

if [ "$fail" -eq 0 ]; then
  echo "$(stamp) synced ${HAVE:-none} -> $WANT, all APK sizes verified" >> "$LOG"
else
  echo "$(stamp) synced ${HAVE:-none} -> $WANT, SIZE VERIFICATION FAILED (see stderr)" >> "$LOG"
fi
exit $fail
