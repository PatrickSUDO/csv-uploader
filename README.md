# CSV Uploader Application

A Spring Boot application that allows for uploading, fetching, and deleting CSV data stored in an H2 in-memory database.

## Getting Started

### Prerequisites

- Java 17
- Maven (if running outside of an IDE)

### Starting the Application

1. **Via IDE (Eclipse, IntelliJ IDEA, etc.)**:
    - Open the project in your IDE.
    - Locate the main application class (`com.psu.Main` or similar) that is annotated with `@SpringBootApplication`.
    - Run this class as a Java application.

2. **Via Command Line**:
    - Navigate to the root directory of the project.
    - Run the following command:
      ```shell
      ./mvnw spring-boot:run
      ```

The application will start and be accessible at `http://localhost:8080`.

## API Endpoints

### Upload CSV File

- **POST** `/api/data/upload-csv`
- Uploads a CSV file to be stored in the database.
- Use form-data with the key `file` to attach the CSV file.
- **cURL Example**:
  ```shell
  curl -F "file=@path_to_your_csv_file.csv" http://localhost:8080/api/data/upload-csv
  ```

### Fetch All Data

- **GET** `/api/data`
- Retrieves all records from the database.
- **cURL Example**:
  ```shell
  curl http://localhost:8080/api/data
  ```

### Fetch Data by Code

- **GET** `/api/data/{code}`
- Retrieves a specific record by its `code`.
- **cURL Example**:
  ```shell
  curl http://localhost:8080/api/data/271636001
  ```

### Delete All Data

- **DELETE** `/api/data/delete-all`
- Deletes all records from the database.
- **cURL Example**:
  ```shell
  curl -X DELETE http://localhost:8080/api/data/delete-all
  ```

## Testing

After starting the application, you can test these endpoints using tools like cURL, Postman, or any HTTP client of your choice.
Ensure you have a CSV file prepared for testing the upload functionality. The CSV file should match the expected format that your application processes.
