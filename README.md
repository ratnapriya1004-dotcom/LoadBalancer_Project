
# Round Robin Load Balancer

A lightweight load balancing system built using **Java and Spring Boot**. This project distributes incoming HTTP requests across multiple backend server instances using the **Round Robin Algorithm** and provides a real-time web dashboard for monitoring server health and request distribution.

## 🚀 Project Overview

The project consists of three Spring Boot applications:

| Component | Port | Description |
|---|---|---|
| Load Balancer | 8080 | Receives and distributes client requests |
| Backend Server 1 | 8081 | Processes forwarded requests |
| Backend Server 2 | 8082 | Processes forwarded requests |

The client communicates with the load balancer, which forwards requests to backend servers sequentially.

## ✨ Key Features

- **Round Robin Algorithm:** Distributes incoming requests sequentially across backend servers.
- **Multiple Backend Servers:** Supports two independent backend instances.
- **Live Monitoring Dashboard:** Displays backend server availability and request statistics.
- **Request Distribution:** Shows the number and percentage of requests handled by each server.
- **Test Request Functionality:** Allows users to send requests directly through the dashboard.
- **Recent Activity:** Displays responses received from backend servers.
- **Server Availability Monitoring:** Identifies unavailable backend servers and reports service errors.

## 🛠️ Technology Stack

- Java
- Spring Boot
- Maven
- HTML
- CSS
- Java HttpClient
- Git and GitHub

## 📁 Project Structure

```text
LoadBalancerProject/
│
├── backend-server/
│   └── backend-server/
│       └── src/main/java/
│           └── com/loadbalancer/backend/
│               ├── BackendServerApplication.java
│               └── controller/
│                   └── ServerController.java
│
├── load-balancer/
│   └── load-balancer/
│       └── src/main/java/
│           └── com/loadbalancer/proxy/
│               ├── LoadBalancerApplication.java
│               ├── controller/
│               │   └── LoadBalancerController.java
│               └── service/
│                   └── RoundRobinService.java
│
│       └── src/main/resources/
│           ├── application.properties
│           └── static/
│               └── index.html
│
├── .gitignore
├── README.md
└── README_DASHBOARD.md
```

## ⚙️ Prerequisites

- Java JDK installed
- Maven (Maven Wrapper is included)
- Git
- Web browser
- VS Code or any Java-supported IDE

Check Java installation:

```bash
java -version
```

## ▶️ How to Run the Project

Open three separate terminals from the main project folder.

### 1. Start Backend Server 1

```cmd
cd backend-server\backend-server
.\mvnw.cmd spring-boot:run
```

### 2. Start Backend Server 2

Open another terminal:

```cmd
cd backend-server\backend-server
.\mvnw.cmd spring-boot:run "-Dspring-boot.run.arguments=--server.port=8082"
```

### 3. Start the Load Balancer

Open a third terminal:

```cmd
cd load-balancer\load-balancer
.\mvnw.cmd spring-boot:run
```

## 🌐 Access the Application

| Service | URL |
|---|---|
| Load Balancer Dashboard | http://localhost:8080/ |
| Request Endpoint | http://localhost:8080/api/request |
| Backend Server 1 | http://localhost:8081 |
| Backend Server 2 | http://localhost:8082 |

## 🔄 Working of Round Robin Algorithm

1. The client sends an HTTP request to the load balancer.
2. The load balancer receives the request.
3. The Round Robin algorithm selects the next backend server.
4. The request is forwarded to the selected server.
5. The backend server processes the request and returns a response.
6. The load balancer forwards the response to the client.

Example request distribution:

```text
Request 1  → Server 1 (8081)
Request 2  → Server 2 (8082)
Request 3  → Server 1 (8081)
Request 4  → Server 2 (8082)
```

## 🧪 Testing

1. Start all three Spring Boot applications.
2. Open the dashboard in your browser.
3. Click **Send Test Request** multiple times.
4. Observe the request count and distribution.
5. Stop one backend server and refresh the dashboard to observe its availability status.

## 🔮 Future Enhancements

- Automatic failover and retry mechanism.
- Support for additional backend servers.
- Advanced health monitoring.
- Response-time analysis.
- Improved error handling and logging.

## 👩‍💻 Author

**Ratnapriya**

GitHub: [LoadBalancer Project](https://github.com/ratnapriya1004-dotcom/LoadBalancer_Project)
