# CPH Petal Studio - Flower Shop Application

## Overview

CPH Petal Studio is a web application for a flower shop, built with Spring Boot and Thymeleaf. It provides functionality for user authentication, product browsing, and order management.

## Setup

1. Clone the repository:
   ```
   git clone https://github.com/yourusername/cph-petal-studio.git
   ```

2. Navigate to the project directory:
   ```
   cd cph-petal-studio
   ```

3. Ensure you have Java 17 and Maven installed.

4. Configure the database in `src/main/resources/application.properties`.

5. Build the project:
   ```
   mvn clean install
   ```

6. Run the application:
   ```
   java -jar target/cph-petal-studio-0.0.1-SNAPSHOT.jar
   ```

7. Access the application at `http://localhost:8080`

## Key Features

- User authentication (login/register)
- Role-based access control (Admin/User)
- Product catalog with bouquets and subscriptions
- Shopping cart functionality
- User dashboard
- Admin dashboard for managing products and orders

## Technologies Used

- Backend: Spring Boot, Spring Security, Spring Data JPA
- Frontend: Thymeleaf, HTML, CSS (Tailwind CSS), JavaScript
- Database: MySQL (configurable)
- Authentication: JWT (JSON Web Tokens)

## Contact

For any inquiries or support, please contact:

Asger
Email: asgervb@gmail.com

## Contributing

We welcome contributions to improve CPH Petal Studio. Please feel free to submit pull requests or open issues on our GitHub repository.

## License

This project is licensed under the MIT License.
