package com.fongmi.android.tv.utils;

import android.text.TextUtils;

import java.net.InetAddress;
import java.net.URI;

/**
 * SSRF guard for outbound URLs taken from remote/untrusted sources.
 *
 * <p>Only permits public http/https targets; rejects private, loopback, link-local and
 * site-local addresses to prevent requests reaching internal services, the device itself,
 * or cloud metadata endpoints.</p>
 */
public class UrlSafety {

    private UrlSafety() {
    }

    public static boolean isSafeHttpUrl(String url) {
        if (TextUtils.isEmpty(url)) return false;
        String scheme;
        String host;
        try {
            URI uri = URI.create(url);
            scheme = uri.getScheme();
            host = uri.getHost();
        } catch (Throwable e) {
            return false;
        }
        if (scheme == null || host == null || host.isEmpty()) return false;
        if (!"http".equalsIgnoreCase(scheme) && !"https".equalsIgnoreCase(scheme)) return false;
        try {
            for (InetAddress address : InetAddress.getAllByName(host)) if (isPrivate(address)) return false;
            return true;
        } catch (Throwable e) {
            return false;
        }
    }

    private static boolean isPrivate(InetAddress address) {
        return address.isAnyLocalAddress() || address.isLoopbackAddress() || address.isLinkLocalAddress() || address.isSiteLocalAddress();
    }

    /**
     * Relaxed guard for media stream URLs (HLS/DASH segments). LAN streams (site-local
     * addresses) are legitimate playback sources, but the loopback device and link-local
     * (cloud metadata 169.254.169.254) targets remain blocked.
     */
    public static boolean isSafeMediaUrl(String url) {
        if (TextUtils.isEmpty(url)) return false;
        String scheme;
        String host;
        try {
            URI uri = URI.create(url);
            scheme = uri.getScheme();
            host = uri.getHost();
        } catch (Throwable e) {
            return false;
        }
        if (scheme == null || host == null || host.isEmpty()) return false;
        if (!"http".equalsIgnoreCase(scheme) && !"https".equalsIgnoreCase(scheme)) return false;
        try {
            for (InetAddress address : InetAddress.getAllByName(host)) {
                if (address.isAnyLocalAddress() || address.isLoopbackAddress() || address.isLinkLocalAddress()) return false;
            }
            return true;
        } catch (Throwable e) {
            return false;
        }
    }

    /**
     * Returns true only for a loopback origin matching the app's local server port. Used to
     * decide whether a browser Origin may receive a credentialed CORS response.
     */
    public static boolean isLoopbackOrigin(String origin, int proxyPort) {
        if (TextUtils.isEmpty(origin) || "null".equals(origin)) return false;
        try {
            URI uri = URI.create(origin);
            String host = uri.getHost();
            if (host == null) return false;
            int port = uri.getPort();
            if ("http".equalsIgnoreCase(uri.getScheme())) {
                if ("127.0.0.1".equals(host) || "localhost".equalsIgnoreCase(host) || "::1".equals(host) || "[::1]".equals(host)) {
                    return port == -1 || port == proxyPort;
                }
            }
            if ("https".equalsIgnoreCase(uri.getScheme()) && "localhost".equalsIgnoreCase(host)) {
                return port == -1;
            }
            return false;
        } catch (Throwable e) {
            return false;
        }
    }
}
