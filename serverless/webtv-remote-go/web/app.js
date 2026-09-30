(() => {
  'use strict';

  const $ = (id) => document.getElementById(id);
  const message = $('msg');
  const result = $('result');
  const origin = window.location.origin;
  let devices = [];
  let commandAbort = null;
  let busy = false;

  $('origin').textContent = origin;

  function groupHeaders() {
    const token = $('group').value.trim();
    return token ? { Authorization: `Bearer ${token}` } : {};
  }

  async function api(path, options = {}) {
    if (!path.startsWith('/api/')) throw new Error('invalid_request_path');
    const headers = { Accept: 'application/json', ...groupHeaders(), ...(options.headers || {}) };
    if (options.body !== undefined) headers['Content-Type'] = 'application/json';
    const response = await fetch(path, { ...options, headers, credentials: 'same-origin' });
    let value;
    try { value = await response.json(); } catch (_) { throw new Error('invalid_server_response'); }
    if (!response.ok) throw new Error(value.error || 'request_failed');
    return value;
  }

  function show(value) {
    result.textContent = typeof value === 'string' ? value : JSON.stringify(value, null, 2);
  }

  function setMessage(text) { message.textContent = text; }

  function setBusy(value) {
    busy = value;
    document.querySelectorAll('button[data-action], button[data-control], #load, #claim, #revoke')
      .forEach((button) => { button.disabled = value || (button.id === 'revoke' && !selectedDevice()); });
  }

  function selectedDevice() {
    const id = $('device').value;
    return devices.find((device) => device.deviceId === id);
  }

  function escapeHtml(value) {
    return String(value).replace(/[&<>"']/g, (character) => ({
      '&': '&amp;', '<': '&lt;', '>': '&gt;', '"': '&quot;', "'": '&#39;'
    }[character]));
  }

  function renderDevices() {
    $('device').innerHTML = devices.length
      ? devices.map((device) => `<option value="${escapeHtml(device.deviceId)}">${escapeHtml(device.name || device.deviceId)} · ${device.online ? '在线' : '离线'}</option>`).join('')
      : '<option value="">没有已授权设备</option>';
    $('revoke').disabled = !selectedDevice() || busy;
  }

  async function loadDevices() {
    if (busy) return;
    setBusy(true);
    try {
      const response = await api('/api/devices');
      devices = response.devices || [];
      renderDevices();
      setMessage(`已连接 ${devices.length} 台设备`);
    } catch (error) {
      setMessage(error.message);
    } finally {
      setBusy(false);
      renderDevices();
    }
  }

  async function claim() {
    const code = $('code').value.trim();
    if (!/^\d{8}$/.test(code)) { setMessage('请输入8位数字配对码'); return; }
    setBusy(true);
    try {
      const response = await api('/api/groups/claim', {
        method: 'POST',
        body: JSON.stringify({ code })
      });
      $('group').value = response.groupToken || $('group').value;
      $('newToken').textContent = response.groupToken || '';
      $('tokenNotice').hidden = !response.groupToken;
      $('code').value = '';
      setMessage('设备已加入授权组。请立即复制并安全保存组令牌。');
      setBusy(false);
      await loadDevices();
    } catch (error) {
      setMessage(error.message);
      setBusy(false);
    } finally {
      renderDevices();
    }
  }

  function describeCommand(command) {
    const labels = {
      queued: '排队中', delivered: '设备已收到', done: '已完成', failed: '设备执行失败',
      expired: '已过期', revoked: '已撤销'
    };
    const text = labels[command.status] || command.status;
    if (command.status === 'done' && command.result) return `${text}\n${JSON.stringify(command.result, null, 2)}`;
    if (command.result) return `${text}\n${JSON.stringify(command.result, null, 2)}`;
    return `${text}（命令 ${command.id}）`;
  }

  async function waitForCommand(commandId, signal) {
    const deadline = Date.now() + 125000;
    while (Date.now() < deadline) {
      if (signal.aborted) throw new DOMException('cancelled', 'AbortError');
      const response = await api(`/api/commands/${encodeURIComponent(commandId)}`);
      show(describeCommand(response.command));
      if (['done', 'failed', 'expired', 'revoked'].includes(response.command.status)) return response.command;
      await new Promise((resolve, reject) => {
        const timer = setTimeout(resolve, 700);
        signal.addEventListener('abort', () => { clearTimeout(timer); reject(new DOMException('cancelled', 'AbortError')); }, { once: true });
      });
    }
    throw new Error('command_timeout');
  }

  async function command(type, payload) {
    const device = selectedDevice();
    if (!device || busy) { setMessage('请先选择在线设备'); return; }
    if (commandAbort) commandAbort.abort();
    commandAbort = new AbortController();
    setBusy(true);
    try {
      const response = await api('/api/commands', {
        method: 'POST',
        body: JSON.stringify({
          targetDeviceId: device.deviceId,
          type,
          payload,
          idempotencyKey: crypto.randomUUID(),
          ttlSeconds: 30
        })
      });
      show(`命令已发送，等待设备响应…\n${response.commandId}`);
      const completed = await waitForCommand(response.commandId, commandAbort.signal);
      setMessage(describeCommand(completed).split('\n')[0]);
    } catch (error) {
      if (error.name !== 'AbortError') { setMessage(error.message); show(error.message); }
    } finally {
      setBusy(false);
      commandAbort = null;
      renderDevices();
    }
  }

  async function revoke() {
    const device = selectedDevice();
    if (!device || busy) return;
    if (!window.confirm(`确定撤销“${device.name || device.deviceId}”在当前组的授权吗？待执行命令也会撤销。`)) return;
    setBusy(true);
    try {
      await api(`/api/devices/${encodeURIComponent(device.deviceId)}`, { method: 'DELETE' });
      setMessage('设备授权已撤销');
      await loadDevices();
    } catch (error) {
      setMessage(error.message);
    } finally {
      setBusy(false);
      renderDevices();
    }
  }

  $('claim').onclick = claim;
  $('load').onclick = loadDevices;
  $('revoke').onclick = revoke;
  $('device').onchange = () => { if (commandAbort) commandAbort.abort(); renderDevices(); };
  $('copyToken').onclick = async () => {
    try { await navigator.clipboard.writeText($('newToken').textContent); setMessage('组令牌已复制，请粘贴到安全密码管理器。'); }
    catch (_) { setMessage('复制失败，请手动复制组令牌。'); }
  };
  $('dismissToken').onclick = () => { $('tokenNotice').hidden = true; $('newToken').textContent = ''; };
  document.querySelectorAll('[data-action]').forEach((button) => {
    button.onclick = () => {
      const action = button.dataset.action;
      if (action === 'status') return command('device.status', {});
      if (action === 'search') return command('action.search', { word: $('word').value });
      if (action === 'push') return command('action.push', { url: $('url').value });
    };
  });
  document.querySelectorAll('[data-control]').forEach((button) => {
    button.onclick = () => command('action.control', { action: button.dataset.control });
  });
})();
