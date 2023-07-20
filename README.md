# PersonApi


## Getting Started
### Clone the repository
````
  git clone https://github.com/dbZrg/PersonApi.git
````

### Build the project
````
  ./gradlew clean build
````

### Run the application
````
  ./gradlew bootRun  
````
After running this command, the application will be accessible at http://localhost:8080.

## Usage
The H2 Console
http://localhost:8080/h2-ui

Swagger UI
http://localhost:8080/swagger-ui.html

Swagger Docs 
http://localhost:8080/v2/api-docs

### Postman Collection
The collection can be found in the resources/postman directory. A Postman collection is provided for this API. It contains pre-configured API requests that can be used for testing and interacting with the API.

### Simple Front End
A simple front end has been developed to provide a user interface for basic interactions with the API. It's built with HTML and JQuery and can be found in the resources/templates directory.

## API Endpoints
### Create new person  
This endpoint allows you to create a new person.

- Method: POST  
- URL: http://localhost:8080/api/person/  
- Body:

````
{
    "firstName": "John",
    "lastName": "Doe",
    "oib": "05286530086",
    "status": "ACTIVE"
}
````
### Get person
This endpoint allows you to retrieve a person record by its unique OIB identifier and generate file with person information.

Method: GET  
- URL: http://localhost:8080/api/person/oib?newFile=false  
- Path Variables:  
  - oib: The unique identifier of the person.  
- Query Parameters:  
  - newFile: A boolean parameter that specifies whether a new file should be created every time or only when person doesn't have active file.

### Delete person
This endpoint allows you to delete a person record and person active file(or all) by its unique OIB identifier.

- Method: DELETE  
- URL: http://localhost:8080/api/person/oib?deleteAll=false  
- Path Variables:  
  - oib: The unique identifier of the person.  
- Query Parameters:  
  - deleteAll: A boolean parameter that specifies whether all person files should be deleted. 