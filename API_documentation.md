## API Documentation

### Swagger UI
Access interactive API documentation at:
- **URL**: http://localhost:8080/swagger-ui/index.html
- **OpenAPI Spec**: http://localhost:8080/v3/api-docs

### Available Endpoints

**Questions API**
- GET /api/questions - Get all questions
- POST /api/questions - Create question
- PUT /api/questions/{id} - Update question
- DELETE /api/questions/{id} - Delete question

**Assignments API**
- GET /api/assignments - Get all assignments
- GET /api/assignments/available - Get available assignments
- POST /api/assignments - Create assignment
- PUT /api/assignments/{id} - Update assignment

**Attempts API**
- POST /api/attempts/start - Start quiz attempt
- POST /api/attempts/{id}/answer - Submit answer
- POST /api/attempts/{id}/submit - Submit attempt
- GET /api/attempts/{id}/result - Get results