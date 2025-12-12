# 📦 Password Vault – Java (Swing + JDBC)

![Java](https://img.shields.io/badge/Java-ED8B00?logo=java\&logoColor=white)
![MySQL](https://img.shields.io/badge/MySQL-005C84?logo=mysql\&logoColor=white)
![Platform](https://img.shields.io/badge/Desktop-App-blue)

A secure, lightweight **Password Vault** desktop application built in **Java Swing** with **MySQL** for persistent storage.
It features encrypted password saving, master-password authentication, auto-lock, and a user-friendly UI.

---

## 📸 Screenshots

> Replace the image links with your own screenshots later.

### 🔐 Login Screen

![Login Screen](https://via.placeholder.com/700x350?text=Login+Screen)

### 🗂️ Vault Dashboard

![Vault Screen](https://via.placeholder.com/700x350?text=Vault+Dashboard)

---

## 🚀 Features

* 🔐 **Master Password Login**
  </br>Users set a master password on first launch and use it for subsequent logins.

* 🛡️ **Password Encryption**
  </br>All stored passwords are encrypted using Base64 encoding before being saved.

* 📁 **Create / Read / Update / Delete (CRUD)**
  </br>Add, edit, delete, and view saved vault entries.

* ⏳ **Auto-Lock System**
  </br>An `AutoLockThread` logs out the user automatically after inactivity.

* 🗂️ **MySQL Database Integration**
  </br>Uses JDBC for connecting and storing vault data.

* 🖥️ **Java Swing UI**
  </br>Includes:

  * `LoginScreen`
  * `VaultScreen`

---


## 🛠️ Tech Stack

| Layer             | Tech             |
| ----------------- | ---------------- |
| **Frontend (UI)** | Java Swing       |
| **Backend**       | Java (OOP + MVC) |
| **Database**      | MySQL            |
| **Security**      | Base64 encoding  |
| **Threading**     | AutoLockThread   |

---

## 📂 Project Structure

```
src/
 ├── db/
 │    ├── DBConnection.java
 │    └── VaultDAO.java
 ├── model/
 │    └── VaultItem.java
 ├── security/
 │    └── EncryptionUtil.java
 ├── ui/
 │    ├── LoginScreen.java
 │    └── VaultScreen.java
 ├── utils/
 │    └── AutoLockThread.java
 └── Main.java
```

---

## ⚙️ Setup Instructions

### 1️⃣ Clone Repository

```bash
git clone https://github.com/your-username/password-vault.git
cd password-vault
```

### 2️⃣ Create MySQL Database

```sql
CREATE DATABASE password_vault;
USE password_vault;


CREATE TABLE master_password (
    id INT PRIMARY KEY AUTO_INCREMENT,
    password VARCHAR(255)
);


CREATE TABLE vault_items (
    id INT PRIMARY KEY AUTO_INCREMENT,
    service VARCHAR(100),
    username VARCHAR(100),
    password VARCHAR(255)
);
```

### 3️⃣ Update DB Credentials

Modify **DBConnection.java**:

```java
String url = "jdbc:mysql://localhost:3306/password_vault";
String user = "root";
String pass = "your_password";
```

### 4️⃣ Run the App

```bash
javac Main.java
java Main
```

---

## 🧩 How It Works

* User opens app → `LoginScreen` appears.
* If first time, app asks user to set master password.
* After login → `VaultScreen` loads stored encrypted passwords.
* Users can:

  * Add new credentials
  * Edit/update existing ones
  * Delete
  * Sort entries
* After inactivity, `AutoLockThread` returns to login screen.


---

## 🧑‍💻 Contributing

Pull Requests are welcome!
Feel free to submit issues, suggestions, or improvements.

---

## 📜 License

This project is licensed under the **MIT License** — free to use and modify.
