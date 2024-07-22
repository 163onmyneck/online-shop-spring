
# Online Shop - Spring

## Description

This is a Spring-based online book-shop application. It provides a platform for users to browse, search, and purchase books online. The application includes features like user authentication, product management, shopping cart, and order processing.

## Features

- User registration and authentication
- Book listing and searching
- Shopping cart management
- Order processing
- Admin panel for managing products and orders

## Technologies Used

- **Backend:**
  - Spring Boot
  - Spring Security
  - Spring Data JPA
  - Hibernate
  - MySQL
- **Build Tool:**
  - Maven

## Installation

### Prerequisites

- Java 11 or higher
- Maven
- MySQL

### Steps

1. Clone the repository:
   \`\`\`bash
   git clone https://github.com/163onmyneck/online-shop-spring.git
   \`\`\`

2. Navigate to the project directory:
   \`\`\`bash
   cd online-shop-spring
   \`\`\`

3. Create a MySQL database:
   \`\`\`sql
   CREATE DATABASE online_shop;
   \`\`\`

4. Update the \`env.template\` file with your MySQL database credentials;

5. Build the project using Maven:
   \`\`\`bash
   mvn clean package
   \`\`\`

6. Run the application:
   \`\`\`bash
   mvn spring-boot:run
   \`\`\`

7. Open your browser and go to \`http://localhost:8080\`.

## Usage

1. **User Registration:** Users can register by url; `http://localhost:8081/auth/registration`.
2. **Books Browsing:** Users can get the list of available products by url: `http://localhost:8081/books`.
3. **Search Books:** Users can search for books by titles or authors.`http://localhost:8081/books/search`
4. **Shopping Cart:** Users can add products to their shopping cart and proceed to checkout.`http://localhost:8081/cart`
5. **Order Management:** Users can view their order history. `http://localhost:8081/orders/get-all`
6. **Admin Panel:** Admins can manage books and orders.

## Contributing

Contributions are welcome! Please follow these steps:

1. Fork the repository.
2. Create a new branch (\`git checkout -b feature-branch\`).
3. Make your changes.
4. Commit your changes (\`git commit -m 'Add some feature'\`).
5. Push to the branch (\`git push origin feature-branch\`).
6. Open a pull request.

## Contact

For any questions or suggestions, feel free to reach out:

- GitHub: [163onmyneck](https://github.com/163onmyneck)
- email: olegtrusskiykcm01@gmail.com
