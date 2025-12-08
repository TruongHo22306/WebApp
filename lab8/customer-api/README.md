# Customer API

## Student Information
- **Name:** [Ho Thien Truong]
- **Student ID:** [ITCSIU22306]
- **Class:** [Web Application Lab]

## API Endpoints

### Base URL
`http://localhost:8080/api/customers`

### Endpoints Implemented
- ✅ GET `/api/customers` - Get all customers (with pagination & sorting)
- ✅ GET `/api/customers/{id}` - Get by ID
- ✅ POST `/api/customers` - Create customer
- ✅ PUT `/api/customers/{id}` - Update customer
- ✅ PATCH `/api/customers/{id}` - Partial update
- ✅ DELETE `/api/customers/{id}` - Delete customer
- ✅ GET `/api/customers/search?keyword={keyword}` - Search
- ✅ GET `/api/customers/status/{status}` - Filter by status
- ✅ GET `/api/customers/advanced-search` - Advanced search (name/email/status)
- ✅ Pagination and sorting
- [ ] Bonus features

## How to Run
1. Create database: `customer_management`
2. Update `application.properties` with your MySQL credentials
3. Run: `mvn spring-boot:run`
4. Test: use Postman or Thunder Client
5. Import collection: `Customer_API.postman_collection.json`

## Testing
All endpoints tested with Postman/Thunder Client. See any saved screenshots or collection requests for examples.

## Features Implemented
- DTO pattern for request/response
- Validation with `@Valid`
- Exception handling with `@RestControllerAdvice`
- Custom exceptions (404, 409)
- Proper HTTP status codes
- Search and filter
- Advanced search
- Pagination
- Sorting
- Partial update (PATCH)

## Known Issues
- None observed in manual testing; additional automated tests recommended.

## Time Spent
- Approximately 6 hours (development, manual testing, documentation)
