# HealthPlus - Intelligent Healthcare Facility Management System

[![Java](https://img.shields.io/badge/Java-OpenJDK%2017-007396)](https://openjdk.org/) &nbsp;
[![JavaFX](https://img.shields.io/badge/JavaFX-v17.0.10-52d3aa)](https://openjfx.io)&nbsp;
[![Gemma 4](https://img.shields.io/badge/Gemma%204-Open%20Model%20MoE-4285F4)](https://ai.google.dev/gemma)&nbsp;
[![MariaDB](https://img.shields.io/badge/MariaDB-v2.7.14-4e629a)](https://mariadb.org/download)&nbsp;
[![Maven](https://img.shields.io/badge/Maven-3.9.9-C71A36)](https://maven.apache.org/)&nbsp;
[![License](https://img.shields.io/badge/License-Apache%202.0-4CAF50)](https://github.com/heshanera/HealthPlus/blob/master/LICENSE)&nbsp;

**HealthPlus** is an open-source Hospital and Health Care Facility Management System built with Java and JavaFX. It streamlines core operational workflows across healthcare facilities: patient registration, appointment scheduling, electronic medical records (EMR), pharmacy dispensing, stock control, and billing.

> 🌟 **MLH Hacktoberfest Edition: Built with Google Gemma 4**  
> HealthPlus now integrates **Google Gemma 4** (Google's latest open-weight model with 31B Dense power and 26B-A4B MoE efficiency) directly into clinical and pharmacy workflows. Leveraging Gemma's native multi-step **Thinking Reasoning**, HealthPlus provides doctors and pharmacists with an intelligent clinical copilot for diagnostic assistance, allergy & drug interaction safety verification, generic drug substitutions, and patient counseling.

---

## 🚀 Key Features

### 1. 🧠 Gemma 4 Clinical & Pharmacy AI Co-Pilot (New!)
* **🩺 Diagnostic Decision Support:** Evaluates presenting patient symptoms and demographics to generate prioritized differential diagnoses, red-flag alerts, and recommends matching hospital diagnostic lab tests (CBC, Lipid Test, Liver Function, Renal Function, CPK, Urine Full Report).
* **🔍 Multi-Step Clinical Thinking:** Displays Gemma 4's transparent step-by-step internal reasoning process in an expandable view, ensuring medical explainability.
* **🛡️ Drug-Drug Interaction & Allergy Safety Check:** Cross-references prescribed medications with the patient's recorded allergies (e.g. penicillin, sulfonamides, NSAIDs) and active conditions before saving, preventing adverse drug reactions.
* **💊 Pharmacy Generic Substitution:** When prescribed drugs are out of stock in the pharmacy, Gemma 4 suggests bioequivalent generic alternatives and dosing precautions.
* **🗣️ Patient Medication Counseling:** Automatically generates clear, everyday patient guidance (dosing schedule, with/without meals, foods to avoid, and common side effects).
* **📋 Clinical Visit Summarizer:** Structures consultations into standardized SOAP (Subjective, Objective, Assessment, Plan) chart summaries.

### 2. Core Facility Management Modules
* **Doctor Module:** Patient history review, diagnosis entry, prescription creation, diagnostic test ordering, and availability scheduling.
* **Pharmacy Module:** Prescription dispensing, brand & generic stock tracking, supplier management, and billing.
* **Receptionist Module:** Patient registration, appointment booking, search by ID / Name / NIC, and doctor schedules.
* **Laboratory Module:** Test report generation (CBC, Lipid, Liver Function, Renal, CPK, Blood Grouping).
* **Cashier & Billing:** Invoicing, automated fee calculation, and refunds.
* **Admin & Analytics:** User management, role-based access, and interactive stock / financial reporting charts.

---

## 🛠️ Issues & Bug Fixes Resolved

This release resolves several community-reported issues and bugs in the repository:
* **Issue #3 ("Pharmacy"):** Fixed pharmacy patient search by Name and NIC (previously commented out). Added null-safe prescription parsing to prevent `NullPointerException` crashes when patients have no active prescription.
* **Issue #8 ("Possible Null pointer dereference"):** Fixed unchecked dereferences in `Admin.getSupplierNames()` and `Pharmacist.getSupplierNames()`.
* **Issue #9 ("String comparison safety"):** Validated and secured string comparisons across user management methods.
* **Issue #15 ("Database Access & Resilience"):** Enhanced configuration loading in `SystemConfiguration` with support for environment variables and graceful connection handling.
* **Issue #17 / Dependabot PR #18 ("Vulnerable Dependencies"):** Upgraded `mariadb-java-client` from `1.1.7` to `2.7.14`, patching critical security vulnerabilities (CONJ-1315).
* **Comparator General Contract Fix:** Resolved `IllegalArgumentException` in `PharmacistController.fillBarChart()` by implementing standard `Integer.compare()`.
* **Chart NPE Fix:** Fixed null/empty collection crashes in `ReportsController.fillSupplierChart()` and `PharmacistController.fillPieChart()`.

---

## ⚙️ Configuration

System and AI settings are managed via [`config.properties`](config.properties) or environment variables:

```properties
# Database Properties
dbClassName=org.mariadb.jdbc.Driver
connection=jdbc:mysql://127.0.0.1/
user=heshan
password=pass
database=test_HMS2

# Google Gemma 4 AI Assistant Configuration
gemma.api.key=YOUR_GEMINI_API_KEY
gemma.model=gemma-4-26b-a4b-it
gemma.thinking.level=high
gemma.enabled=true
```

You can also export your API key as an environment variable:
```bash
export GEMINI_API_KEY="your-api-key"
# or on Windows PowerShell:
$env:GEMINI_API_KEY="your-api-key"
```

Supported Gemma 4 models on Gemini API:
* `gemma-4-26b-a4b-it` (MoE efficiency, fast reasoning)
* `gemma-4-31b-it` (Dense power)

---

## 🏗️ Setting Up the Application

### Prerequisites
* [Java 17+ (OpenJDK)](https://openjdk.org/)
* [JavaFX 17+](https://openjfx.io)
* [MariaDB](https://mariadb.org/download) or MySQL
* [Maven 3.8+](https://maven.apache.org/)

### 1. Database Setup
1. Create a database named `test_HMS2`:
   ```sql
   CREATE DATABASE test_HMS2;
   ```
2. Import schema and sample clinical data from [`database/hms_db.sql`](database/hms_db.sql):
   ```bash
   mysql -u root -p test_HMS2 < database/hms_db.sql
   ```
3. Update database credentials in [`config.properties`](config.properties).

### 2. Build & Test
Resolve dependencies and run tests:
```bash
mvn clean test
```

Build the application:
```bash
mvn clean install
```

### 3. Launch HealthPlus
Run via JavaFX Maven plugin:
```bash
mvn javafx:run
```

---

## 🔒 Authentication (Default Credentials)

Default test credentials from `sys_user`:

| Role | Username | Password |
|---|---|---|
| Admin | `user001` | `1234` |
| Doctor | `user005` | `1234` |
| Pharmacist | `user007` | `1234` |
| Receptionist | `user008` | `1234` |
| Lab Assistant | `user013` | `1234` |

---

## 📄 License
This project is licensed under the [Apache License 2.0](LICENSE).
