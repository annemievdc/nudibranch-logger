# 🐌 Nudibranch Logger


A full-stack web application for scuba diving enthusiasts to document their dives, record nudibranch sightings, and manage their underwater discoveries.

Built with **Java 21, Spring Boot, Thymeleaf, Spring Security, and H2**, the project demonstrates server-side web development, relational database design, authentication, role-based authorization, and cloud image storage.

## 🌊 About the Project

Nudibranch Logger is a diving logbook and marine-life observation application.

The project focuses on combining dive records with nudibranch sightings in a single web application. It was developed as a learning and portfolio project to gain practical experience with Java and the Spring ecosystem.

The application uses a server-rendered architecture with Thymeleaf templates and Spring Boot controllers.
## 📸 Screenshots

- Home page
<img width="1885" height="897" alt="image" src="https://github.com/user-attachments/assets/fa6df934-ede4-4007-a640-4d2bc20da4dd" />
- Nudis overview
<img width="1887" height="787" alt="image" src="https://github.com/user-attachments/assets/629b4ce5-3de2-4bb9-897e-c3d3bb774b56" />
- Nudi Details
<img width="1895" height="882" alt="image" src="https://github.com/user-attachments/assets/921c6678-3f8d-4966-b85d-41a0981747f7" />
- Friend Details
<img width="1896" height="883" alt="image" src="https://github.com/user-attachments/assets/84a5d5a0-f1e9-4cb2-96b7-2843f29f5fb0" />
- Dive log overview
<img width="1888" height="902" alt="image" src="https://github.com/user-attachments/assets/2ea2b91c-2314-4928-9117-b5139c706603" />
- Map
<img width="1891" height="901" alt="image" src="https://github.com/user-attachments/assets/25678601-4399-421e-b1a3-a1812b0e8806" />


## ✨ Features

- User registration and login
- Authentication and role-based access control
- Dive logging and management
- Nudibranch sighting records
- Image uploads using Supabase Storage
- Administrator functionality
- Relational data management with an H2 database
- Sample data for local demonstration

## 🛠️ Tech Stack

| Technology | Purpose |
|---|---|
| Java 21 | Backend programming language |
| Spring Boot | Application framework |
| Spring MVC | Web controllers and request handling |
| Spring Security | Authentication and authorization |
| Thymeleaf | Server-side HTML templates |
| H2 Database | In-memory relational database |
| Maven | Dependency management and build automation |
| Supabase Storage (S3-compatible API) | Cloud image storage |
| Docker | Containerization |

## 🏗️ Architecture

The application follows a traditional Spring Boot web application architecture:

- **Controllers** handle incoming HTTP requests and coordinate application functionality.
- **Application logic** processes user actions and manages the relevant data.
- **Database access** persists and retrieves dive logs, sightings, and user information.
- **Thymeleaf templates** render the user interface.
- **Spring Security** manages authentication and user permissions.
- **Supabase Storage** handles uploaded images.

## 🚀 Getting Started

### Prerequisites

To run the project locally, you will need:

- Java Development Kit (JDK) 21
- Maven, or the Maven Wrapper included in the project
- An IDE such as IntelliJ IDEA
- A Supabase project with S3-compatible Storage credentials for image-upload functionality

### 1. Clone the Repository

```bash
git clone https://github.com/annemie/nudibranch-logger.git
cd nudibranch-logger
```



### 2. Configure Environment Variables

The application uses environment variables to keep credentials out of the source code.

Configure the following variables in your IDE's run configuration or operating-system environment:

| Variable | Description |
|---|---|
| `H2_DB_PASSWORD` | Password for the local H2 database connection |
| `SUPABASE_S3_ACCESS_KEY` | Supabase Storage S3 access key |
| `SUPABASE_S3_SECRET_KEY` | Supabase Storage S3 secret key |

**Important:** Spring Boot does not automatically load a `.env` file. If you use one locally, you must explicitly configure your development environment to load it.

The repository does not contain real Supabase credentials. You must provide your own credentials to use the cloud storage integration.

Depending on the application's configuration, Supabase credentials may also be required at startup, even if you do not intend to upload images.

### 3. Run the Application

Open the project in IntelliJ IDEA and run the Spring Boot application class.

Once the application has started, open:

```text
http://localhost:8080
```

The default address assumes that no custom server port has been configured.

## 🗄️ Database

The project uses an **H2 in-memory database**.

Database tables and sample records are initialized from:

```text
src/main/resources/schema.sql
src/main/resources/data.sql
```

Because the database runs in memory:

- Each application instance has its own independent database.
- Sample data is recreated when the application starts.
- Changes made during a session are not preserved after the application stops.
- Cloning the repository does not provide access to the original developer's database.

The H2 web console is disabled.

## 🔐 Authentication and Security

Authentication and authorization are implemented using Spring Security.

User passwords are stored as **BCrypt hashes**, rather than plaintext passwords.

The demo includes predefined fictional users and an administrator account in the database seed data.

These accounts are intended for local demonstration and development. The application is a portfolio project, not a production deployment.

Before deploying the application publicly, administrator credentials and demo account access should be reviewed and configured appropriately.

## 📷 Image Storage

Uploaded images are stored using **Supabase Storage through its S3-compatible API**.

The application reads its storage credentials from environment variables. No active access keys or secret keys are included in the public repository.

To test image uploads, configure your own Supabase project and compatible storage settings.

## 🐳 Docker

A `Dockerfile` is included for containerizing the application.

Docker deployments must also supply the required environment variables. Credentials should never be embedded directly in the image or committed to version control.



## 🎯 What I Learned

This project allowed me to develop practical skills in:

- Building web applications with Java and Spring Boot
- Implementing authentication and authorization with Spring Security
- Designing and initializing relational databases
- Working with server-rendered views using Thymeleaf
- Integrating external cloud storage services
- Managing application configuration and environment variables
- Debugging file uploads and backend configuration
- Preparing a software project for version control and portfolio presentation

## 📌 Project Status

**Portfolio / educational project**

This application was developed to demonstrate my software development skills. It is intended primarily for local exploration and code review rather than production use.

## 📄 License

No license has been specified for this repository.
