# Luxurious Hotel Booking

The Luxurious Hotel Booking project is a web-based hotel reservation platform that allows users to search for, book, and manage hotel rooms. The project aims to provide users with a seamless experience when booking accommodations and offers a robust admin interface for managing hotel operations. This system implements real-time notifications, secure booking processes, and advanced room management features. It is designed to be scalable, efficient, and user-friendly.
## Technologies 

Frontend: HTML, CSS, JavaScript, Bootstrap, jQuery, Ajax

Backend: RESTFul API, Spring Boot, Spring JPA, MySQL, JWT, Redis, RabbitMQ, WebSocket

## Features
#### Guest Pages:
- Display available rooms
- Search for available rooms
- Book rooms
- Confirm bookings via email
- Search bookings using a phone number
#### Admin Pages:
- Login and logout
- Register an account and confirm via email
- Reset password
- Manage, add, edit, delete bookings
- Manage, add, edit, delete hotel rooms
- Manage, add, edit, delete guest
- Manage, add, edit, delete staff
- Real-time notifications for new bookings


## API Reference

Updating...

## Usage Guide
### Prerequisites
- JDK Java 17+
- Docker
### Setup Instructions
1. Clone the repository:

```bash
git clone https://github.com/nguyentrung875/luxurious.git
cd luxurious
```
2. Run docker-compose

```bash
docker compose up -d
```
3. Run mysql script
```bash
luxurious-hotel.sql
```
4. Run the backend server
```bash
http://localhost:9999
```
5. Run the frontend application with Live server (VSCode IDE)
```bash
http://localhost:5500
```
