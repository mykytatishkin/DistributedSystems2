# Distributed Systems Homework

This is a Spring Boot application developed for the Distributed Systems Homework assignment. The application provides a RESTful API for managing student records with support for both JSON and XML formats.

## Entity-Relationship (ER) Diagram

The data model for this application is simple but can be extended. Below is a textual representation of the ER diagram:

```
+----------------+
|    Student     |
+----------------+
| id (PK)        |
| firstName      |
| lastName       |
| email          |
| age            |
+----------------+
```

In a more complex application, this model could be extended with related entities such as:
- Courses (with a many-to-many relationship to Students)
- Departments (with a one-to-many relationship to Students)
- Addresses (with a one-to-one relationship with Students)

## Technologies Used

- Spring Boot 2.7.5
- Spring Data JPA
- H2 Database (in-memory)
- Spring Actuator
- JAXB (for XML processing)

## Getting Started

### Prerequisites
- Java 11 or higher
- Maven

### Running the Application

1. Clone the repository
2. Navigate to the project directory
3. Run the application:
   ```bash
   mvn spring-boot:run
   ```

The application will start on port 8080.

## API Endpoints

### Student API

#### Regular CRUD Endpoints
- **GET /api/students** - Get all students
- **GET /api/students/{id}** - Get student by ID
- **POST /api/students** - Create a new student
- **PUT /api/students/{id}** - Update an existing student
- **DELETE /api/students/{id}** - Delete a student
- **GET /api/students/search?lastName={lastName}** - Find students by last name
- **GET /api/students/search?minAge={age}** - Find students with age greater than specified value

#### XML Export Endpoints
- **GET /api/students/export/all** - Export all students to XML and return the file path
- **GET /api/students/export?lastName={lastName}** - Export students by last name to XML and return the file path
- **GET /api/students/export?minAge={age}** - Export students by minimum age to XML and return the file path

### Content Types
All regular endpoints support both JSON and XML formats. You can specify the desired format using the `Accept` header:
- For JSON: `Accept: application/json`
- For XML: `Accept: application/xml`

## Database

The application uses an H2 in-memory database. You can access the H2 console at:
- URL: http://localhost:8080/h2-console
- JDBC URL: jdbc:h2:mem:testdb
- Username: sa
- Password: password

## XML Export

The application can export student data to XML files:
1. The XML files are saved to the `xml-output` directory relative to the application's working directory
2. Each file has a unique name based on the export parameters and a UUID
3. The export endpoints return the absolute path to the saved XML file

## Monitoring

The application uses Spring Actuator for monitoring. You can access:
- Health info: http://localhost:8080/actuator/health
- All actuator endpoints: http://localhost:8080/actuator

## Testing

The application includes comprehensive unit tests for all major components:
- Service layer tests
- Controller tests
- XML processing tests

Run the tests with:
```bash
mvn test
```

## Sample Student JSON

```json
{
  "firstName": "John",
  "lastName": "Doe",
  "email": "john.doe@example.com",
  "age": 20
}
```

## Sample Student XML

```xml
<student>
  <firstName>John</firstName>
  <lastName>Doe</lastName>
  <email>john.doe@example.com</email>
  <age>20</age>
</student>
``` 