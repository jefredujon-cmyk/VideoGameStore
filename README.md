Video Game Store API
Overview

This project is a backend API for a Video Game Store built with Spring Boot and MySQL. Users can register, log in, and browse video games. Admin users can manage games and categories.

Features

JWT-based user login and registration

Browse and search video games

Admin CRUD for products and categories

Fixed bugs in search and product updates

Tech Stack

Java / Spring Boot

MySQL

JWT Authentication

Postman

Setup
Run create_database.sql in MySQL

Update database credentials in application.properties

Start the app:

mvn spring-boot:run

API Examples

POST /login

GET /products

GET /products?cat=1&minPrice=20

POST /categories (Admin only)
