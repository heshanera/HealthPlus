# HealthPlus

[![Java](https://img.shields.io/badge/Java-OpenJDK%2017-007396)](https://openjdk.org/) &nbsp;
[![JavaFX](https://img.shields.io/badge/JavaFX-v17.0.10-52d3aa)](https://openjfx.io)&nbsp;
[![MariaDB](https://img.shields.io/badge/MariaDB-v11.6.2-4e629a)](https://mariadb.org/download)&nbsp;
[![Maven](https://img.shields.io/badge/Maven-3.9.9-C71A36)](https://maven.apache.org/)&nbsp;
[![License: MIT](https://img.shields.io/badge/License-Apache%202.0-4CAF50)](https://github.com/heshanera/HealthPlus/blob/master/LICENSE)&nbsp;

HealthPlus is a management system designed to streamline operations within healthcare facilities. It offers functionalities such as patient registration, appointment scheduling, medical record management, pharmacy billing, and inventory control. Developed using Java, JavaFX and uses MariaDB for database management

## The system includes
  - Registration of patients
  - Making appointments
  - Storing patient records
  - Billing in the pharmacy
  - Pharmacy stock controlling

## Workflow and Component Interactions

<img src="https://github.com/heshanera/HealthPlus/blob/master/screenshots/componentInteractions2.png" alt="componentInteractions" width="100%">

## Setting Up the Application

### Prerequisites
- [MariaDB](https://mariadb.org/download)
- [Java](https://openjdk.org/)
- [JavaFX](https://openjfx.io)

### Set Up the Database
- Set up a new database named `test_HMS2`
- Import the dummy data from [`hms_db.sql`](https://github.com/heshanera/HealthPlus/blob/master/database/hms_db.sql) to new DB
- Update the system configuration in [`config.properties`](https://github.com/heshanera/HealthPlus/blob/master/config.properties) with the database credentials


### Build the App
- Resolve the dependencies
```shell
mvn dependency:resolve
```
- Build 
```shell
mvn clean install
```
- Run
```shell
mvn javafx:run
```

### Authentication
Since this was developed as a simple application, all the credentials are saved in a database table in plain text format without any hashing or encryptions.

#### View all existing users with credentials
```SQL
USE test_HMS2;
SELECT * FROM sys_user;
```

## User Interfaces

<img src="https://github.com/heshanera/HealthPlus/blob/master/screenshots/screenshots.png" alt="ScreenShots" width="100%">
