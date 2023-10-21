# java-shareit
Template repository for Shareit project.

The ShareIt project is a microservices application for renting items.

Technology Stack: Java 11, Spring Boot, Maven, Lombok, JPA, Hibernate, H2, PostgreSQL, Mockito, Docker.

About the Project: A RESTful multi-module web-service for sharing items with Gateway-Server architecture. The application consists of three microservices: Gateway for request validation, Server containing business logic, and the database. Each microservice is deployed in its own Docker container.

Key Functionality: Registration, updating, and retrieval of users Addition, updating, retrieval, and searching of items Management of rental requests for items Handling requests to rent desired items Commenting on completed rentals

How to Use: Run mvn clean package. Start the application with docker-compose up -d
