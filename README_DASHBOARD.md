# Load Balancer Project - Dashboard Edition

This version adds a web dashboard to the existing Spring Boot Round Robin load balancer.
The existing two-backend setup is retained.

## Project ports

| Component | Port |
|---|---:|
| Load Balancer / Dashboard | 8080 |
| Backend Server 1 | 8081 |
| Backend Server 2 | 8082 |

## Run the project in VS Code (Windows)

Open three separate terminals. Keep each application running in its own terminal.

### Terminal 1 - Backend Server 1

```powershell
cd backend-server\backend-server
.\mvnw.cmd spring-boot:run
```

### Terminal 2 - Backend Server 2

Open another terminal at the project root:

```powershell
cd backend-server\backend-server
.\mvnw.cmd spring-boot:run "-Dspring-boot.run.arguments=--server.port=8082"
```

If the second command does not override the port in your Maven setup, use:

```powershell
.\mvnw.cmd spring-boot:run -Dspring-boot.run.arguments=--server.port=8082
```

Confirm the terminal says Tomcat started on port 8082.

### Terminal 3 - Load Balancer

Open a third terminal:

```powershell
cd load-balancer\load-balancer
.\mvnw.cmd spring-boot:run
```

Wait until it says Tomcat started on port 8080.

## Open the dashboard

Visit:

**http://localhost:8080/**

The dashboard automatically refreshes backend status every five seconds.

## Test

1. Click **Send Test Request** several times.
2. The response identifies the backend server that handled each request.
3. The request counters and distribution bars update.
4. Stop one backend terminal with `Ctrl + C`. The dashboard should show that backend as offline.
5. Send another request. The load balancer tries the other backend if the selected one is unavailable.
6. Restart the stopped backend and click **Refresh Status** or wait for the next automatic refresh.

## API endpoints

- `GET /` - Dashboard page
- `GET /api/request` - Forward a request using Round Robin with fallback
- `GET /api/dashboard/status` - Return backend health and request counters

## Notes

- No database or additional frontend framework is required.
- Dashboard assets are served by Spring Boot from `src/main/resources/static`.
- Backend health checks call each backend's existing `/api/server` endpoint. Health checks do not increase the forwarded-request counters.
- Request counters are in memory and reset when the load balancer application restarts.
