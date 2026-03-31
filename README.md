# SocketDrop

SocketDrop is a lightweight real-time file sharing app built with Java and Spring Boot.  
Users connect via WebSockets, join rooms, and share files with live progress updates — without authentication or a database.

## Features
- WebSocket-based real-time communication
- Room-based file sharing
- File upload, download, and delete via HTTP
- Live transfer progress updates
- QR-code based room join (URL)
- In-memory storage only

## HTTP API
- `POST /file/uploads` (multipart `file`, optional `roomId` for progress broadcast)
- `GET /file/downloads/{fileId}`
- `DELETE /file/downloads/{fileId}`

## WebSocket API (`/room`)
- Client messages:
  - `{ "type": "CREATE_ROOM", "displayName": "alice" }`
  - `{ "type": "JOIN_ROOM", "roomId": "room_x", "displayName": "bob" }`
  - `{ "type": "LEAVE_ROOM" }`
- Server messages:
  - `ROOM_CREATED`
  - `ROOM_JOINED`
  - `UPLOAD_PROGRESS` (`STARTED`, `COMPLETED`, `FAILED`)
  - `ERROR`

## Frontend
- Static single-page UI is available at `/` via `src/main/resources/static/index.html`.
- Assets:
  - `src/main/resources/static/app.js`
  - `src/main/resources/static/styles.css`
- Supports create/join/leave room, room-scoped upload, room-authorized download, and delete.

## Tech Stack
- Java 17
- Spring Boot
- Spring WebSocket
- Spring Web (REST)
- Maven

## Notes
- No authentication
- No database
- Designed for learning and demonstration purposes

## Author
**Prince Pal**

Created by Prince Pal with love.

LinkedIn: https://www.linkedin.com/in/princepal-dev/

