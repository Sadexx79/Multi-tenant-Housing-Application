# Multi-Tenant Housing Manager

## Project Overview

Multi-Tenant Housing Manager is a JavaFX desktop application created to help property managers organize and manage tenant information efficiently. The application allows users to manage tenant payment information, utility costs, lease details, and visualize rent data through charts using a modern graphical interface.

The system includes CRUD operations, dynamic searching, dark/light mode support, rent visualization charts, CSV export functionality, and tenant management tools.

---

## Features

- Add tenant records
- Edit tenant information
- Delete tenant records
- Clear all tenant records
- Search tenants dynamically by name
- Display tenant information using a TableView
- Visualize rent data using a Bar Chart
- Export tenant data to CSV files
- Toggle between Dark Mode and Light Mode
- Display total tenant count
- Display total monthly payment column
- Input validation and confirmation alerts

---

## Technologies Used

- Java
- JavaFX SDK 21
- Visual Studio Code
- Git & GitHub

---

## Project Structure

```text
multi_tenant_project/
│
├── src/
│   ├── app/
│   │   └── App.java
│   │
│   ├── controller/
│   │   └── TenantController.java
│   │
│   ├── model/
│   │   └── Tenant.java
│   │
│   ├── util/
│   │   └── FileHandler.java
│   │
│   └── view/
│       └── MainApp.java
│
├── .vscode/
│   ├── launch.json
│   └── settings.json
│
├── .gitignore
└── README.md

Application Interface

The graphical interface includes:

-User input fields
-Add, Edit, Delete buttons
-Clear All functionality
-Search functionality
-Tenant information table
-Rent visualization chart
-CSV export functionality
-Dark and Light mode toggle
-Tenant counter label

How to Run
-Install Java JDK 21 or higher
-Install JavaFX SDK 21
-Place the JavaFX SDK in: C:\javafx\lib
-Open the project folder in Visual Studio Code
-Run the JavaFX launch configuration

Current Functionality

The current version supports:

-Tenant management
-Dynamic searching
-Data visualization
-CSV export
-JavaFX graphical interface
-Dark/Light theme support
-Input validation
-Confirmation dialogs

Future Improvements
-Database integration
-User authentication
-Advanced analytics
-Additional chart types
-Improved UI animations
-Tenant payment history
-Monthly revenue reports