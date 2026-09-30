# Military Asset Management System

## Overview

Military Asset Management System is a full-stack web application for managing the movement, assignment, purchase, and expenditure of critical military assets such as vehicles, weapons, and ammunition across multiple bases.

The system provides a centralized dashboard, secure role-based access, purchase and transfer management, assignment and expenditure tracking, and audit logging.

## Technology Stack

### Frontend
- React.js
- Vite
- React Router
- JavaScript
- CSS

### Backend
- Java 21
- Spring Boot
- Spring Security
- Spring Data JPA
- Maven

### Database
- MySQL

## Why These Technologies

### React.js
React provides a component-based frontend that makes it easier to build responsive and interactive pages.

### Spring Boot
Spring Boot was selected for building secure REST APIs and for its integration with Spring Data JPA and Spring Security.

### MySQL
MySQL is a relational database suitable for storing structured military asset, purchase, transfer, assignment, expenditure, user, and audit data.

### Spring Security
Spring Security is used to protect REST APIs and implement role-based access control.

## Main Features

### Dashboard
- Opening Balance
- Closing Balance
- Net Movement
- Assigned Assets
- Expended Assets
- Date filter
- Base filter
- Equipment Type filter

Net Movement is calculated as:

Purchases + Transfer In - Transfer Out

### Purchases
- Record purchases for a specific base
- View purchase history
- Filter purchases by date and equipment type

### Transfers
- Transfer assets between bases
- Record source and destination bases
- Record equipment type and quantity
- Maintain transfer history with timestamps

### Assignments
- Assign assets to personnel
- Track assigned quantities
- Maintain assignment records

### Expenditures
- Record expended assets
- Track expenditure reason and quantity

### Audit Logs
The system records important transactions such as:
- Purchase creation, update and deletion
- Transfer creation, update and deletion
- Assignment creation, update and deletion
- Expenditure creation, update and deletion

## Role-Based Access Control

### Admin
- Full access to all system operations and data
- User management
- Dashboard
- Purchases
- Transfers
- Assignments
- Expenditures
- Audit Logs

### Base Commander
- Access to operations related to the assigned base
- Dashboard
- Purchases
- Transfers involving the assigned base
- Assignments for the assigned base
- Expenditures for the assigned base
- Audit Logs

### Logistics Officer
- Dashboard
- Purchases
- Transfers
- Audit Logs
- No access to assignments, expenditures, or user management

## Database Design

MySQL is used as the relational database.

The application stores separate records for:
- Users
- Purchases
- Transfers
- Assignments
- Expenditures
- Audit Logs

The database structure supports tracking asset movements between bases and maintaining transaction history.

## Project Structure

```text
Military-Asset-Management-System/
│
├── frontend/
│   └── React + Vite application
│
├── backend/
│   └── Spring Boot REST API
│
└── README.md