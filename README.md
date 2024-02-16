# Delivery Tracker

## Requirements
Before getting started, make sure you have the following prerequisites installed:

a. **MySQL Database:** Set up a MySQL database of your choice.

b. **Java 17:** Install Java 17 on your system.

c. **Redis:** Ensure Redis is installed.

## Setting up the Project

1. **Database Configuration:**
    - Create a MySQL database of your choice.
    - Update the database configurations in the `.env` file or use the provided one.

2. **Build the Project:**
    - Open a terminal and run the following command in the root directory of the project:
      ```bash
      mvn clean install
      ```

3. **Run the Project:**
    - If you're using an IDE like IntelliJ, click "Run" to start the project.
    - Alternatively, run the following command in the terminal:
      ```bash
      mvn spring-boot:run
      ```

4. **Environment File:**
    - Make sure you have added the `.env` file in the root folder of the project before executing steps 1-3.

5. **Access Swagger UI:**
    - Once the project is running, access the Swagger UI at [http://localhost:8081/swagger-ui.html](http://localhost:8081/swagger-ui.html).

## Additional Notes
- Customize other project configurations as needed.
- For troubleshooting or additional information, refer to the project documentation or contact the maintainers.
