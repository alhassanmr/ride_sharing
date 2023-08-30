RideSharing Platform
Welcome to the RideSharing platform, a convenient solution for connecting drivers and customers for seamless ride experiences.

Getting Started
To get started with the RideSharing platform, follow these steps:

Prerequisites
Before you begin, ensure you have the following prerequisites:

Java Development Kit (JDK) 8 or higher
Maven
PostgreSQL Database
Installation
Clone the repository:

bash
Copy code
git clone https://github.com/alhassanmr/ridesharing.git
Navigate to the project directory:

bash
Copy code
cd ridesharing-platform
Install project dependencies using Maven:

bash
Copy code
mvn clean install
Configuration
Configure the database connection in src/main/resources/application.properties:

properties
Copy code
spring.datasource.url=jdbc:postgresql://localhost:5432/ridesharingdb
spring.datasource.username=yourusername
spring.datasource.password=yourpassword
Configure JWT secret key and expiration in the same file:

properties
Copy code
jwt.secret=yourjwtsecret
jwt.expirationMs=86400000  # 24 hours
Database Initialization
Run the database initialization script located in src/main/resources/data.sql to create necessary tables and initial data.
Running the Application
Build and run the application using Maven:

bash
Copy code
mvn spring-boot:run
The application will be accessible at http://localhost:8080.

Usage
Visit http://localhost:8080/swagger-ui.html to access the API documentation and test the endpoints.
Contributing
Contributions are welcome! To contribute to this project, follow these steps:

Fork the repository.
Create a new branch for your feature or bug fix.
Make your changes and commit them.
Push the changes to your fork.
Submit a pull request to the main repository.