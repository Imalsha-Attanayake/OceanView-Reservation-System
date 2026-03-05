# OceanView Reservation System

## Project Overview
The OceanView Reservation System is a Java-based hotel reservation management system developed using RESTful Web Services. The system allows hotel staff to manage room reservations efficiently through a user-friendly interface.

This application integrates Java Swing for the client interface, REST APIs for communication between client and server, and MySQL as the backend database.

The system demonstrates service-oriented architecture concepts by allowing different components of the system to communicate through web services.

---

## Technologies Used

Java (JDK 25)  
Java Swing (GUI Application)  
RESTful Web Services (JAX-RS)  
MySQL Database  
Maven (Project Management)  
JUnit (Testing)  
Postman (API Testing)  
GitHub (Version Control)

---

## System Features

User Login Authentication  
View Available Rooms  
Add Reservation  
View Reservation Details  
Update Reservation  
Delete Reservation  
Calculate Reservation Bill  
REST API Integration  
Database Connectivity

---

## System Architecture

The system follows a three-layer architecture:

Client Layer  
Java Swing desktop application used by the user.

Service Layer  
REST API endpoints handling reservation operations.

Data Layer  
MySQL database storing rooms, users, and reservation details.

---

## Database Structure

The system contains three main tables:

Users – Stores login credentials  
Rooms – Stores room information and availability  
Reservations – Stores booking information and calculated bills

---

## API Endpoints

GET /api/rooms  
Retrieve all rooms

POST /api/login  
Authenticate user login

POST /api/reservations  
Create a new reservation

GET /api/reservations/{id}  
Retrieve reservation details

PUT /api/reservations/{id}  
Update reservation information

DELETE /api/reservations/{id}  
Delete reservation

GET /api/reservations/calculate  
Calculate reservation bill

---

## Testing

The system was tested using:

Postman for REST API testing  
JUnit for automated testing

Test cases include:

Login validation  
Room retrieval  
Reservation creation  
Reservation update  
Reservation deletion  
Bill calculation

---

## GitHub Repository

This project is maintained using GitHub for version control and collaboration.

Repository Link:

## Development Versions

Version 1.0 – Initial project setup and database creation  
Version 1.1 – REST API implementation for reservation management  
Version 1.2 – Java Swing GUI implementation  
Version 1.3 – Testing and documentation improvements
