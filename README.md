# **Billing Software Backend**

![Java CI](https://github.com/Krishnendu/Billing_Software_Backend/actions/workflows/java.yml/badge.svg)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-2.7.16-green.svg)
![AWS S3](https://img.shields.io/badge/AWS_S3-Integrated-blue.svg)

---

## 🚀 **Overview**

A **fully-featured billing software backend** built with **Spring Boot**, **JPA/Hibernate**, and **AWS S3** integration. This project provides a robust API for managing **categories, items, orders, payments, and users** with **JWT-based authentication**, **Razorpay integration**, and **file uploads**.

### **Key Features**
✅ **User Authentication** – Secure login with JWT tokens
✅ **Category & Item Management** – CRUD operations with file uploads
✅ **Order Processing** – Create, fetch, and delete orders
✅ **Payment Integration** – Razorpay for secure transactions
✅ **AWS S3 Storage** – Scalable file storage for images
✅ **Dashboard Analytics** – Track sales, orders, and recent activity

### **Who is this for?**
- **Developers** looking for a **billing system backend**
- **Startups** needing a **scalable e-commerce solution**
- **Freelancers** building **custom billing applications**

---

## ✨ **Tech Stack**

| Category          | Technologies Used                     |
|-------------------|--------------------------------------|
| **Language**      | Java (Java 21)                       |
| **Framework**     | Spring Boot (3.x)                    |
| **Database**      | MySQL (JPA/Hibernate)                |
| **Authentication**| JWT, Spring Security                 |
| **Payments**      | Razorpay API                         |
| **File Storage**  | AWS S3                               |
| **Build Tool**    | Maven                                |

---

## 📦 **Installation**

### **Prerequisites**
- **Java 21** (or compatible)
- **Maven 3.8+**
- **MySQL Database** (or any supported RDBMS)
- **AWS S3 Bucket** (for file storage)
- **Razorpay API Keys** (for payment processing)

### **Quick Start**

#### **1. Clone the Repository**
```bash
git clone https://github.com/Krishnendu/Billing_Software_Backend.git
cd Billing_Software_Backend
```

#### **2. Set Up Environment Variables**
Create a `.env` file (or use `application.properties` directly):
```properties
# Database Config
DB_URL=jdbc:mysql://localhost:3306/billing_db
DB_USERNAME=root
DB_PASSWORD=your_password

# AWS S3 Config
AWS_ACCESS_KEY=your_access_key
AWS_SECRET_KEY=your_secret_key
AWS_REGION=us-east-1
AWS_BUCKET_NAME=your-bucket-name

# JWT Config
JWT_SECRET_KEY=your_jwt_secret_key

# Razorpay Config
RZP_KEY_ID=your_razorpay_key_id
RZP_SECRET=your_razorpay_secret_key

# Server Port
PORT=8080
```

#### **3. Run the Application**
```bash
mvn spring-boot:run
```
The API will start at `http://localhost:8080/api/v1.0`

---

## 🎯 **Usage**

### **1. Authentication (Login & JWT Token)**
```java
// Example: Login Request
POST /api/v1.0/login
{
    "email": "user@example.com",
    "password": "password123"
}

// Response: JWT Token
{
    "email": "user@example.com",
    "role": "ADMIN",
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

### **2. Create a New Category (Admin Only)**
```java
// Example: Add Category with Image
POST /api/v1.0/admin/categories
Headers: Authorization: Bearer <JWT_TOKEN>
Body (multipart/form-data):
- category: {"name": "Electronics", "description": "Electronic devices"}
- file: <image-file.jpg>
```

### **3. Create an Order**
```java
// Example: Create Order
POST /api/v1.0/orders
Headers: Authorization: Bearer <JWT_TOKEN>
Body:
{
    "customerName": "John Doe",
    "phoneNumber": "1234567890",
    "cartItems": [
        {
            "itemId": "item-123",
            "name": "Laptop",
            "price": 999.99,
            "quantity": 1
        }
    ],
    "subTotal": 999.99,
    "tax": 50.00,
    "grandTotal": 1049.99,
    "paymentMethod": "CASH"
}
```

### **4. Process Payment with Razorpay**
```java
// Example: Create Razorpay Order
POST /api/v1.0/payments/create-order
Headers: Authorization: Bearer <JWT_TOKEN>
Body:
{
    "amount": 1049.99,
    "currency": "INR"
}

// Response: Razorpay Order ID
{
    "id": "order_1234567890",
    "amount": 104999,
    "currency": "INR",
    "status": "created"
}
```

---

## 📁 **Project Structure**
```
src/
├── main/
│   ├── java/com/Krishnendu/BillingSoftware/
│   │   ├── config/          # Configuration classes
│   │   ├── controller/      # API Endpoints
│   │   ├── entity/          # Database Models
│   │   ├── filters/         # JWT Filter
│   │   ├── io/              # DTOs & Request/Response Objects
│   │   ├── repository/      # JPA Repositories
│   │   ├── service/         # Business Logic
│   │   └── utils/           # Helper Classes
│   └── resources/           # Config Files
│       ├── application.properties
│       └── static/           # Static Resources
└── test/                    # Unit & Integration Tests
```

---

## 🔧 **Configuration**

### **Environment Variables**
| Variable               | Description                          | Example Value                     |
|------------------------|--------------------------------------|-----------------------------------|
| `DB_URL`               | Database connection URL               | `jdbc:mysql://localhost:3306/db`  |
| `AWS_ACCESS_KEY`       | AWS S3 Access Key                    | `AKIAIOSFODNN7EXAMPLE`            |
| `JWT_SECRET_KEY`       | JWT Secret Key                       | `your_secure_jwt_key`             |
| `RZP_KEY_ID`           | Razorpay API Key                     | `rzp_test_key_id`                 |

### **Customization**
- Modify `application.properties` for **database, AWS, and JWT settings**.
- Extend **controllers** to add new API endpoints.
- Adjust **security rules** in `SecurityConfig.java`.

---

## 🤝 **Contributing**

We welcome contributions! Here’s how you can help:

1. **Fork the Repository**
2. **Create a Feature Branch**
   ```bash
   git checkout -b feature/your-feature
   ```
3. **Commit Changes**
   ```bash
   git commit -m "Add new payment method"
   ```
4. **Push & Open a Pull Request**

### **Development Setup**
```bash
# Install dependencies
mvn clean install

# Run tests
mvn test
```

### **Code Style Guidelines**
- Follow **Java Best Practices** (Lombok for boilerplate reduction).
- Use **Spring Boot conventions** for REST APIs.
- Write **unit tests** for critical logic.

---

## 👥 **Authors & Contributors**
👤 **Krishnendu** – [GitHub](https://github.com/Krishnendu)

---

## 🐛 **Issues & Support**
- **Report bugs** via [GitHub Issues](https://github.com/Krishnendu/Billing_Software_Backend/issues).
- **Need help?** Open a discussion or contact me directly.

---

## 🗺️ **Roadmap**
✅ **Core Features** – Complete (Authentication, CRUD, Payments)
🚧 **Next Steps** – Add **WebSocket for real-time updates**
🔜 **Future Improvements** – Multi-currency support, advanced analytics

---

### **💡 Star & Follow for Updates!**
🌟 **[Star this repo](https://github.com/Krishnendu/Billing_Software_Backend)** to support the project!

---
*"Build powerful billing systems with ease!"* 🚀
