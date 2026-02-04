# Online Quiz System - Backend

A robust Spring Boot backend API for an online quiz and assignment management system. This RESTful API provides comprehensive endpoints for managing questions, assignments, and quiz attempts with timed auto-submission functionality.

## Features

- **Question Management API**: CRUD operations for multiple-choice questions
- **Assignment Management**: Create and schedule timed assignments
- **Attempt Tracking**: Monitor student quiz attempts with real-time answer submission
- **Auto-Scoring**: Automatic calculation of quiz scores
- **Time-based Availability**: Assignment scheduling with start/end time constraints
- **Data Validation**: Duration validation against assignment time windows
- **CORS Support**: Configured for frontend integration
- **PostgreSQL Database**: Supabase-compatible schema with JPA/Hibernate

## Prerequisites

- **Java**: JDK 17 or higher
- **Maven**: Version 3.6 or higher
- **PostgreSQL**: Version 12 or higher (or Supabase account)
- **IDE**: IntelliJ IDEA, Eclipse, or VS Code with Java extensions (recommended)

## Installation

1. **Clone the repository**

   ```bash
   git clone https://github.com/Geethx/Online_Quiz_System_Backend.git
   cd Online_Quiz_System_Backend
   ```

2. **Configure Database**

   Update `src/main/resources/application.properties` with your database credentials:

   ```properties
   # Database Configuration
   spring.datasource.url=jdbc:postgresql://localhost:5432/quiz_db
   spring.datasource.username=your_username
   spring.datasource.password=your_password

   # JPA/Hibernate
   spring.jpa.hibernate.ddl-auto=update
   spring.jpa.show-sql=true
   spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
   spring.jpa.properties.hibernate.format_sql=true

   # Server Port
   server.port=8080
   ```

   **For Supabase:**

   ```properties
   spring.datasource.url=jdbc:postgresql://[project-ref].supabase.co:5432/postgres
   spring.datasource.username=postgres
   spring.datasource.password=[your-password]
   ```

3. **Create Database Schema**

   The application will auto-create tables on first run, or you can manually create them:

   ```sql
   CREATE TABLE questions (
       id BIGSERIAL PRIMARY KEY,
       text TEXT NOT NULL,
       option_a VARCHAR(500) NOT NULL,
       option_b VARCHAR(500) NOT NULL,
       option_c VARCHAR(500) NOT NULL,
       option_d VARCHAR(500) NOT NULL,
       correct_option INTEGER NOT NULL,
       difficulty VARCHAR(20) NOT NULL,
       points INTEGER NOT NULL,
       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
   );

   CREATE TABLE assignments (
       id BIGSERIAL PRIMARY KEY,
       name VARCHAR(255) NOT NULL,
       description TEXT,
       start_time TIMESTAMP NOT NULL,
       end_time TIMESTAMP NOT NULL,
       duration INTEGER NOT NULL,
       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
   );

   CREATE TABLE assignment_questions (
       assignment_id BIGINT REFERENCES assignments(id),
       question_id BIGINT REFERENCES questions(id),
       PRIMARY KEY (assignment_id, question_id)
   );

   CREATE TABLE attempts (
       id BIGSERIAL PRIMARY KEY,
       assignment_id BIGINT REFERENCES assignments(id),
       student_name VARCHAR(255) NOT NULL,
       start_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
       end_time TIMESTAMP,
       score DOUBLE PRECISION,
       total_points INTEGER,
       is_submitted BOOLEAN DEFAULT FALSE
   );

   CREATE TABLE answers (
       id BIGSERIAL PRIMARY KEY,
       attempt_id BIGINT REFERENCES attempts(id),
       question_id BIGINT REFERENCES questions(id),
       selected_option INTEGER,
       is_correct BOOLEAN,
       marked_for_review BOOLEAN DEFAULT FALSE,
       answered_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
   );
   ```

## Running the Application

### Using Maven Wrapper (Recommended)

**Windows:**

```bash
mvnw.cmd spring-boot:run
```

**Linux/Mac:**

```bash
./mvnw spring-boot:run
```

### Using Maven Directly

```bash
mvn spring-boot:run
```

### Using IDE

1. Open the project in your IDE
2. Navigate to `src/main/java/com/onlinequiz/online_quiz/OnlineQuizApplication.java`
3. Run the main method

The application will start on `http://localhost:8080`

## Building for Production

### Create JAR file

```bash
mvnw clean package
```

The JAR file will be in `target/online-quiz-0.0.1-SNAPSHOT.jar`

### Run the JAR

```bash
java -jar target/online-quiz-0.0.1-SNAPSHOT.jar
```

## API Endpoints

### Questions API

| Method | Endpoint                                 | Description                 |
| ------ | ---------------------------------------- | --------------------------- |
| GET    | `/api/questions`                         | Get all questions           |
| GET    | `/api/questions/{id}`                    | Get question by ID          |
| GET    | `/api/questions/difficulty/{difficulty}` | Get questions by difficulty |
| POST   | `/api/questions`                         | Create new question         |
| PUT    | `/api/questions/{id}`                    | Update question             |
| DELETE | `/api/questions/{id}`                    | Delete question             |

### Assignments API

| Method | Endpoint                     | Description                         |
| ------ | ---------------------------- | ----------------------------------- |
| GET    | `/api/assignments`           | Get all assignments                 |
| GET    | `/api/assignments/{id}`      | Get assignment by ID                |
| GET    | `/api/assignments/available` | Get currently available assignments |
| POST   | `/api/assignments`           | Create new assignment               |
| PUT    | `/api/assignments/{id}`      | Update assignment                   |
| DELETE | `/api/assignments/{id}`      | Delete assignment                   |

### Attempts API

| Method | Endpoint                           | Description                |
| ------ | ---------------------------------- | -------------------------- |
| POST   | `/api/attempts/start`              | Start new attempt          |
| POST   | `/api/attempts/{attemptId}/answer` | Submit answer for question |
| POST   | `/api/attempts/{attemptId}/submit` | Submit entire attempt      |
| GET    | `/api/attempts/{attemptId}`        | Get attempt details        |
| GET    | `/api/attempts/{attemptId}/result` | Get attempt result         |

## Request/Response Examples

### Create Question

```json
POST /api/questions
{
  "text": "What is 2 + 2?",
  "optionA": "3",
  "optionB": "4",
  "optionC": "5",
  "optionD": "6",
  "correctOption": 1,
  "difficulty": "EASY",
  "points": 5
}
```

### Create Assignment

```json
POST /api/assignments
{
  "name": "Math Quiz 1",
  "description": "Basic arithmetic",
  "startTime": "2026-02-04T09:00:00",
  "endTime": "2026-02-04T10:00:00",
  "duration": 30,
  "questionIds": [1, 2, 3]
}
```

### Start Attempt

```json
POST /api/attempts/start
{
  "assignmentId": 1,
  "studentName": "John Doe"
}
```

### Submit Answer

```json
POST /api/attempts/1/answer
{
  "questionId": 1,
  "selectedOption": 1,
  "markedForReview": false
}
```

## Project Structure

```
online-quiz/
├── src/
│   ├── main/
│   │   ├── java/com/onlinequiz/online_quiz/
│   │   │   ├── controller/          # REST Controllers
│   │   │   │   ├── QuestionController.java
│   │   │   │   ├── AssignmentController.java
│   │   │   │   └── AttemptController.java
│   │   │   ├── service/             # Business Logic Layer
│   │   │   │   ├── QuestionService.java
│   │   │   │   ├── AssignmentService.java
│   │   │   │   └── AttemptService.java
│   │   │   ├── repository/          # Data Access Layer
│   │   │   │   ├── QuestionRepository.java
│   │   │   │   ├── AssignmentRepository.java
│   │   │   │   ├── AttemptRepository.java
│   │   │   │   └── AnswerRepository.java
│   │   │   ├── entity/              # JPA Entities
│   │   │   │   ├── Question.java
│   │   │   │   ├── Assignment.java
│   │   │   │   ├── Attempt.java
│   │   │   │   └── Answer.java
│   │   │   ├── dto/                 # Data Transfer Objects
│   │   │   │   ├── QuestionDTO.java
│   │   │   │   ├── AssignmentDTO.java
│   │   │   │   ├── AttemptDTO.java
│   │   │   │   └── ...
│   │   │   ├── config/              # Configuration Classes
│   │   │   │   └── WebConfig.java
│   │   │   └── OnlineQuizApplication.java
│   │   └── resources/
│   │       ├── application.properties
│   │       └── static/
│   └── test/                        # Unit Tests
├── pom.xml                          # Maven Dependencies
└── README.md
```

## Technologies Used

- **Spring Boot 3.5.10** - Application framework
- **Spring Data JPA** - Data persistence
- **Hibernate** - ORM framework
- **PostgreSQL Driver** - Database connectivity
- **Spring Web** - RESTful web services
- **Spring Validation** - Data validation
- **Maven** - Dependency management and build tool
- **Java 17** - Programming language

## Configuration

### CORS Configuration

The application is configured to allow requests from `http://localhost:5173` (frontend). To modify:

Edit `src/main/java/com/onlinequiz/online_quiz/config/WebConfig.java`

```java
@Override
public void addCorsMappings(CorsRegistry registry) {
    registry.addMapping("/api/**")
            .allowedOrigins("http://localhost:5173")
            .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
            .allowedHeaders("*")
            .allowCredentials(true);
}
```

### Database Connection Pool

For production, configure connection pooling in `application.properties`:

```properties
spring.datasource.hikari.maximum-pool-size=10
spring.datasource.hikari.minimum-idle=5
spring.datasource.hikari.connection-timeout=30000
```

## Validation Rules

### Assignment Validation

- Start time must be in the future
- End time must be after start time
- Duration cannot exceed time window between start and end
- At least one question must be selected

### Question Validation

- All options must be provided
- Correct option must be between 0-3
- Points must be positive
- Difficulty must be EASY, MEDIUM, or HARD

## Testing

Run tests using Maven:

```bash
mvnw test
```

## Troubleshooting

### Database Connection Failed

- Verify PostgreSQL is running
- Check credentials in `application.properties`
- Ensure database exists
- Check firewall settings for port 5432

### Port 8080 Already in Use

Change the port in `application.properties`:

```properties
server.port=8081
```

### CORS Errors

- Verify frontend URL in `WebConfig.java`
- Check that `@CrossOrigin` annotations are present on controllers

### Hibernate Schema Issues

Set to `create` for fresh start (WARNING: drops existing tables):

```properties
spring.jpa.hibernate.ddl-auto=create
```

## Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/new-feature`)
3. Commit changes (`git commit -m 'Add new feature'`)
4. Push to branch (`git push origin feature/new-feature`)
5. Open a Pull Request

## License

This project is part of an academic assignment.

## Related Repositories

- **Frontend**: [Online_Quiz_System_Frontend](https://github.com/Geethx/Online_Quiz_System_Frontend)

## Support

For issues or questions, please create an issue in the GitHub repository.

## Acknowledgments

- Spring Boot Documentation
- Supabase for PostgreSQL hosting
- Stack Overflow community
