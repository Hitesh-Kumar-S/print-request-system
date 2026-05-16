Print Request Management System

A full-stack enterprise-style print request management application built using Spring Boot, Spring Security, Thymeleaf, and MySQL.
The system allows users to upload print requests while enabling admins to manage approvals, completion, payments, and automated email notifications through a streamlined workflow.

🚀 Features
👤 User Features
User registration and login
Secure authentication using Spring Security
Submit print requests with PDF upload
Print configuration options:
Color / Black & White
Single-sided / Double-sided
Number of pages
Number of copies
Request confirmation workflow
View personal request history
Dark mode support
Responsive UI
🛠️ Admin Features
View all print requests
Approve or reject requests
Mark requests as completed
Mark payments as paid
Search requests by user email
Filter requests by:
Status
Payment status
Pagination support
Dashboard analytics cards
Status badges and workflow action buttons
Responsive admin dashboard
📧 Automated Email Workflow
User → Admin
Uploaded PDF files are automatically emailed to the admin inbox.
Admin → User Notifications
Approval notification
Rejection notification
Completion notification
Payment confirmation notification
🎨 UI/UX Enhancements
Dark mode support
Analytics dashboard cards
Responsive tables
Status badges
Interactive action buttons
Friendly user notifications
Improved confirmation pages
Mobile responsive layout
🧠 Workflow Lifecycle
User Uploads Request
        ↓
Admin Receives PDF via Email
        ↓
Admin Approves / Rejects
        ↓
User Gets Notification
        ↓
Admin Completes Request
        ↓
User Gets Pickup Notification
        ↓
Admin Marks Payment as Paid
        ↓
User Gets Payment Confirmation
🏗️ Tech Stack
Backend
Java 17
Spring Boot
Spring MVC
Spring Security
Spring Data JPA
Hibernate
Frontend
Thymeleaf
HTML5
CSS3
JavaScript
Database
MySQL
Email Service
Gmail SMTP
Build Tool
Maven
Deployment (Planned)
Docker
Render
PostgreSQL
📂 Project Structure
src
 ├── main
 │   ├── java
 │   │   └── com.example.printapp
 │   │       ├── controller
 │   │       ├── model
 │   │       ├── repository
 │   │       ├── security
 │   │       └── service
 │   │
 │   └── resources
 │       ├── static
 │       ├── templates
 │       └── application.properties
 │
 └── test
🔐 Security Features
Role-based authentication
Session-based authorization
Protected admin routes
Secure login/logout handling
Validation for:
File uploads
Form fields
Request inputs
📦 Installation & Setup
1️⃣ Clone Repository
git clone <your-repository-url>
2️⃣ Navigate to Project
cd print-request-system
3️⃣ Configure Environment Variables

Create a .env file in the project root:

DB_URL=jdbc:mysql://localhost:3306/Print_Management

DB_USERNAME=root

DB_PASSWORD=your_mysql_password

MAIL_USERNAME=your_email@gmail.com

MAIL_PASSWORD=your_gmail_app_password
4️⃣ Configure Database

Create a MySQL database:

CREATE DATABASE Print_Management;
5️⃣ Run the Application
mvn spring-boot:run
📧 Gmail SMTP Setup
Enable 2-Step Verification in your Google account.
Generate an App Password.
Use the generated password in:
MAIL_PASSWORD
📸 Screenshots

Add screenshots here after deployment.

Example:

Login page
User dashboard
Admin dashboard
Analytics cards
Request workflow pages
🌐 Deployment

Deployment setup planned using:

Docker
Render
PostgreSQL

The application is designed using a deployment-friendly temporary file processing workflow.

🚀 Future Enhancements
Cloud storage integration (AWS S3 / Cloudinary)
OTP verification
Live status updates
Payment gateway integration
Docker Compose support
Admin analytics charts
Mobile app integration
👨‍💻 Author

Hitesh Kumar S

Java Full Stack Developer
Backend & Workflow System Enthusiast
📄 License

This project is developed for educational and portfolio purposes.