const wsStatusEl = document.getElementById("wsStatus");
const currentRoomEl = document.getElementById("currentRoom");
const currentUserEl = document.getElementById("currentUser");
const joinHintEl = document.getElementById("joinHint");
const roomPromptEl = document.getElementById("roomPrompt");
const logEl = document.getElementById("log");
const fileActionsSectionEl = document.getElementById("fileActionsSection");
const themeToggleEl = document.getElementById("themeToggle");

const displayNameEl = document.getElementById("displayName");
const roomIdEl = document.getElementById("roomId");
const uploadFileEl = document.getElementById("uploadFile");
const fileIdInputEl = document.getElementById("fileIdInput");

const connectBtn = document.getElementById("connectBtn");
const createRoomBtn = document.getElementById("createRoomBtn");
const joinRoomBtn = document.getElementById("joinRoomBtn");
const leaveRoomBtn = document.getElementById("leaveRoomBtn");
const uploadBtn = document.getElementById("uploadBtn");
const downloadBtn = document.getElementById("downloadBtn");
const deleteBtn = document.getElementById("deleteBtn");
const clearLogBtn = document.getElementById("clearLogBtn");

let socket = null;
const THEME_KEY = "socketdrop-theme";
const state = {
  connected: false,
  roomId: "",
  userId: "",
};

function getPreferredTheme() {
  const storedTheme = window.localStorage.getItem(THEME_KEY);
  if (storedTheme === "dark" || storedTheme === "light") {
    return storedTheme;
  }
  return window.matchMedia("(prefers-color-scheme: light)").matches ? "light" : "dark";
}

function applyTheme(theme) {
  document.documentElement.setAttribute("data-theme", theme);
  window.localStorage.setItem(THEME_KEY, theme);
  themeToggleEl.textContent = theme === "dark" ? "Light mode" : "Dark mode";
}

function toggleTheme() {
  const currentTheme = document.documentElement.getAttribute("data-theme") || "dark";
  applyTheme(currentTheme === "dark" ? "light" : "dark");
}

function log(message, payload) {
  const ts = new Date().toLocaleTimeString();
  const text = payload ? `${message} ${JSON.stringify(payload)}` : message;
  logEl.textContent = `[${ts}] ${text}\n${logEl.textContent}`;
}

function setWsStatus(text) {
  wsStatusEl.textContent = text;
  wsStatusEl.classList.remove("connected", "disconnected", "error");
  wsStatusEl.classList.add(text);
}

function isInRoom() {
  return Boolean(state.roomId && state.userId);
}

function renderState() {
  currentRoomEl.textContent = state.roomId || "-";
  currentUserEl.textContent = state.userId || "-";
  fileActionsSectionEl.hidden = !isInRoom();

  const hasFileId = Boolean((fileIdInputEl.value || "").trim());
  const hasFile = Boolean(uploadFileEl.files && uploadFileEl.files[0]);

  uploadBtn.disabled = !isInRoom() || !hasFile;
  downloadBtn.disabled = !isInRoom() || !hasFileId;
  deleteBtn.disabled = !hasFileId;
  leaveRoomBtn.disabled = !isInRoom();

  if (!state.connected) {
    joinHintEl.textContent = "Create or join a room to unlock uploads and downloads.";
    roomPromptEl.textContent = "Step 1: Connect. Step 2: Create a room or enter a room ID and join.";
  } else if (!isInRoom()) {
    joinHintEl.textContent = "Connected. You still need a room to continue.";
    roomPromptEl.textContent = "Use Create Room for a new room or Join Room with an existing room ID.";
  } else {
    joinHintEl.textContent = `Joined ${state.roomId}. File actions are now available.`;
    roomPromptEl.textContent = `You are in ${state.roomId}. Downloads are allowed only for joined members.`;
  }
}

function wsUrl() {
  const protocol = window.location.protocol === "https:" ? "wss" : "ws";
  return `${protocol}://${window.location.host}/room`;
}

function ensureConnected() {
  if (socket && socket.readyState === WebSocket.OPEN) {
    return Promise.resolve();
  }

  return new Promise((resolve, reject) => {
    socket = new WebSocket(wsUrl());

    socket.onopen = () => {
      state.connected = true;
      setWsStatus("connected");
      renderState();
      log("Connected to /room");
      resolve();
    };

    socket.onclose = () => {
      state.connected = false;
      state.roomId = "";
      state.userId = "";
      setWsStatus("disconnected");
      renderState();
      log("Socket closed");
    };

    socket.onerror = () => {
      setWsStatus("error");
      log("Socket error");
      reject(new Error("WebSocket error"));
    };

    socket.onmessage = (event) => {
      try {
        const data = JSON.parse(event.data);
        handleServerMessage(data);
      } catch (error) {
        log("Non-JSON WS message", event.data);
      }
    };
  });
}

function sendWs(payload) {
  if (!socket || socket.readyState !== WebSocket.OPEN) {
    log("Cannot send WS message: socket not connected");
    return;
  }

  socket.send(JSON.stringify(payload));
  log("Sent", payload);
}

function handleServerMessage(data) {
  switch (data.type) {
    case "ROOM_CREATED":
      if (data.roomId) {
        state.roomId = data.roomId;
        roomIdEl.value = data.roomId;
      }
      if (data.userId) {
        state.userId = data.userId;
      }
      renderState();
      log("Room created", data);
      break;
    case "ROOM_JOINED":
      if (data.roomId) {
        state.roomId = data.roomId;
        roomIdEl.value = data.roomId;
      }
      if (data.userId) {
        state.userId = data.userId;
      }
      renderState();
      log("Room joined", data);
      break;
    case "UPLOAD_PROGRESS":
      log("Upload progress", data);
      if (data.status === "COMPLETED" && data.fileId) {
        fileIdInputEl.value = data.fileId;
        renderState();
      }
      break;
    case "ERROR":
      log(`Server error: ${data.message || "unknown"}`);
      break;
    default:
      log("WS message", data);
  }
}

function requiredValue(element, field) {
  const value = (element.value || "").trim();
  if (!value) {
    throw new Error(`${field} is required`);
  }
  return value;
}

async function uploadFile() {
  if (!isInRoom()) {
    throw new Error("Join a room before uploading files");
  }

  const file = uploadFileEl.files && uploadFileEl.files[0];
  if (!file) {
    throw new Error("Select a file to upload");
  }

  const formData = new FormData();
  formData.append("file", file);

  const url = `/file/uploads?roomId=${encodeURIComponent(state.roomId)}`;

  const res = await fetch(url, {
    method: "POST",
    body: formData,
  });

  const body = await res.json().catch(() => ({}));
  if (!res.ok) {
    throw new Error(body.message || `Upload failed (${res.status})`);
  }

  fileIdInputEl.value = body.fileId || "";
  renderState();
  log("Upload success", body);
}

async function downloadFile() {
  if (!isInRoom()) {
    throw new Error("Join a room before downloading files");
  }

  const fileId = requiredValue(fileIdInputEl, "fileId");
  const query = new URLSearchParams({ roomId: state.roomId, userId: state.userId });
  const res = await fetch(`/file/downloads/${encodeURIComponent(fileId)}?${query.toString()}`);

  if (!res.ok) {
    const body = await res.json().catch(() => ({}));
    throw new Error(body.message || `Download failed (${res.status})`);
  }

  const blob = await res.blob();
  const disposition = res.headers.get("content-disposition") || "";
  const match = disposition.match(/filename="(.+)"/);
  const filename = match ? match[1] : `${fileId}.bin`;

  const url = window.URL.createObjectURL(blob);
  const a = document.createElement("a");
  a.href = url;
  a.download = filename;
  document.body.appendChild(a);
  a.click();
  a.remove();
  window.URL.revokeObjectURL(url);

  log(`Downloaded ${filename}`);
}

async function deleteFile() {
  const fileId = requiredValue(fileIdInputEl, "fileId");
  const res = await fetch(`/file/downloads/${encodeURIComponent(fileId)}`, {
    method: "DELETE",
  });

  if (!res.ok) {
    const body = await res.json().catch(() => ({}));
    throw new Error(body.message || `Delete failed (${res.status})`);
  }

  log(`Deleted ${fileId}`);
}

connectBtn.addEventListener("click", async () => {
  try {
    await ensureConnected();
  } catch (error) {
    log(error.message);
  }
});

createRoomBtn.addEventListener("click", async () => {
  try {
    const displayName = requiredValue(displayNameEl, "displayName");
    await ensureConnected();
    sendWs({ type: "CREATE_ROOM", displayName });
  } catch (error) {
    log(error.message);
  }
});

joinRoomBtn.addEventListener("click", async () => {
  try {
    const roomId = requiredValue(roomIdEl, "roomId");
    const displayName = requiredValue(displayNameEl, "displayName");
    await ensureConnected();
    sendWs({ type: "JOIN_ROOM", roomId, displayName });
  } catch (error) {
    log(error.message);
  }
});

leaveRoomBtn.addEventListener("click", () => {
  sendWs({ type: "LEAVE_ROOM" });
  state.roomId = "";
  state.userId = "";
  renderState();
  log("Left room locally");
});

uploadBtn.addEventListener("click", async () => {
  try {
    await uploadFile();
  } catch (error) {
    log(error.message);
  }
});

downloadBtn.addEventListener("click", async () => {
  try {
    await downloadFile();
  } catch (error) {
    log(error.message);
  }
});

deleteBtn.addEventListener("click", async () => {
  try {
    await deleteFile();
  } catch (error) {
    log(error.message);
  }
});

clearLogBtn.addEventListener("click", () => {
  logEl.textContent = "";
});

themeToggleEl.addEventListener("click", toggleTheme);

uploadFileEl.addEventListener("change", renderState);
fileIdInputEl.addEventListener("input", renderState);

applyTheme(getPreferredTheme());
setWsStatus("disconnected");
renderState();
log("UI ready");

