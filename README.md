# HealthPlus

HealthPlus is a management system designed to streamline operations within healthcare facilities. It offers functionalities such as patient registration, appointment scheduling, medical record management, pharmacy billing, and inventory control. Developed using Java and JavaFX, HealthPlus provides an intuitive interface to enhance administrative efficiency and improve patient care.

## The system includes
  - Registration of patients
  - Making appointments
  - Storing patient records
  - Billing in the pharmacy
  - Pharmacy stock controlling

## High Level System Architecture

<img src="https://github.com/heshanera/HealthPlus/blob/master/screenshots/componentInteractions.png" alt="componentInteractions" width="100%">

## Setting Up the Application

### Prerequisites
- [MariaDB](https://mariadb.org/download)
- [Java](https://openjdk.org/)
- [JavaFX](https://openjfx.io)

### Set Up the Database
- Create a new database `test_HMS2`
- Import the dummy data from `hms_db.sql`


### Build the App
- Resolve the dependencies
```shell
mvn dependency:resolve
```
- Build the app 
```shell
mvn clean install
```
- Run the app
```shell
mvn javafx:run
```

### Authentication
Since this was developed as a simple application, all the credentials are saved in a DB table in plain text format without any hashing or encryptions.

#### View all existing users list
```SQL
USE test_HMS2;
SELECT * FROM sys_user;
```

## User Interfaces

<img src="https://github.com/heshanera/HealthPlus/blob/master/screenshots/screenshots.png" alt="ScreenShots" width="100%">
