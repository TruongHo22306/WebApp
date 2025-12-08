# Customer API Documentation

## Base URL
`http://localhost:8080/api/customers`

## Endpoints

### 1. Get All Customers (paged/sorted)
**GET** `/api/customers?page=0&size=10&sortBy=fullName&sortDir=asc`

**Response:** 200 OK
```json
{
  "customers": [
    {
      "id": 1,
      "customerCode": "C001",
      "fullName": "John Doe",
      "email": "john.doe@example.com",
      "phone": "+1-555-0101",
      "address": "123 Main St",
      "status": "ACTIVE",
      "createdAt": "2025-12-02T17:02:07"
    }
  ],
  "currentPage": 0,
  "totalItems": 12,
  "totalPages": 2
}
```

### 2. Get Customer by ID
**GET** `/api/customers/{id}`

**Response:** 200 OK
```json
{
  "id": 1,
  "customerCode": "C001",
  "fullName": "John Doe",
  "email": "john.doe@example.com",
  "phone": "+1-555-0101",
  "address": "123 Main St",
  "status": "ACTIVE",
  "createdAt": "2025-12-02T17:02:07"
}
```

### 3. Create Customer
**POST** `/api/customers`

Body (JSON):
```json
{
  "customerCode": "C010",
  "fullName": "New Customer",
  "email": "new@example.com",
  "phone": "+1-555-0000",
  "address": "123 Any St"
}
```

**Response:** 201 Created (customer payload)

### 4. Update Customer (full)
**PUT** `/api/customers/{id}`

Body (JSON):
```json
{
  "customerCode": "C001",
  "fullName": "Updated Name",
  "email": "updated@example.com",
  "phone": "+1-555-1111",
  "address": "Updated Address"
}
```

**Response:** 200 OK (updated payload)

### 5. Partial Update Customer
**PATCH** `/api/customers/{id}`

Body (JSON): include only fields to change
```json
{
  "fullName": "Partially Updated Name"
}
```

**Response:** 200 OK (updated payload)

### 6. Delete Customer
**DELETE** `/api/customers/{id}`

**Response:** 200 OK (confirmation message)

### 7. Search Customers
**GET** `/api/customers/search?keyword=john`

**Response:** 200 OK (array of matches)

### 8. Filter by Status
**GET** `/api/customers/status/ACTIVE` (or `INACTIVE`)

**Response:** 200 OK (array of matches)

### 9. Advanced Search (optional filters)
**GET** `/api/customers/advanced-search?name=john&email=@example.com&status=ACTIVE`

**Response:** 200 OK (array of matches)

## Error Responses

### 404 Not Found
```json
{
  "timestamp": "2025-12-03T10:00:00",
  "status": 404,
  "error": "Resource Not Found",
  "message": "Customer not found with id: 99",
  "path": "/api/customers/99"
}
```

## Status Code Examples

### 200 OK
Successful read/update/delete/search.
```json
{
  "id": 1,
  "customerCode": "C001",
  "fullName": "John Doe",
  "email": "john.doe@example.com",
  "phone": "+1-555-0101",
  "address": "123 Main St",
  "status": "ACTIVE",
  "createdAt": "2025-12-02T17:02:07"
}
```

### 201 Created
Resource created (POST).
```json
{
  "id": 10,
  "customerCode": "C010",
  "fullName": "New Customer",
  "email": "new@example.com",
  "phone": "+1-555-0000",
  "address": "123 Any St",
  "status": "ACTIVE",
  "createdAt": "2025-12-09T10:00:00"
}
```

### 400 Bad Request (validation)
Missing/invalid fields on create/update.
```json
{
  "status": 400,
  "error": "Validation Error",
  "message": "Invalid input",
  "path": "/api/customers",
  "details": [
    "email: must be a well-formed email address",
    "fullName: must not be blank"
  ]
}
```

### 404 Not Found
Resource not found.
```json
{
  "timestamp": "2025-12-03T10:00:00",
  "status": 404,
  "error": "Resource Not Found",
  "message": "Customer not found with id: 99",
  "path": "/api/customers/99"
}
```

### 409 Conflict (duplicate)
Duplicate unique field (e.g., email or customerCode).
```json
{
  "status": 409,
  "error": "Duplicate Resource",
  "message": "Email already exists",
  "path": "/api/customers"
}
```

### 500 Internal Server Error
Unexpected server error.
```json
{
  "status": 500,
  "error": "Internal Server Error",
  "message": "NullPointerException",
  "path": "/api/customers",
  "details": null
}
```
