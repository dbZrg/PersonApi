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
http://localhost:8080/v3/api-docs

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

This endpoint enables you to retrieve a person's record using their unique OIB identifier and generate a file containing their information.  
A new file will only be created under the following conditions:   
if the person does not already have an active file,  
or if the newFile parameter is set to true, indicating a forced creation of a new file.

Method: GET  
- URL: http://localhost:8080/api/person/oib?newFile=false  
- Path Variables:  
  - oib: The unique identifier of the person.  
- Query Parameters:  
  - newFile: A boolean parameter that specifies whether a new file should be created every time or only when person doesn't have active file.

### Delete person

This endpoint allows you to delete a person's record and modify the status of their active file.

- Method: DELETE  
- URL: http://localhost:8080/api/person/oib
- Path Variables:  
  - oib: The unique identifier of the person.  

## File Deletion

## Overview
The deletion logic is as follows:

If both oib and status are provided, the service deletes all files with given status for person.
If only oib is provided, the service deletes all files for one person.
If only status is provided, the service deletes all files with given status.
If neither oib nor status is provided, the service deletes all files.


- Method: `POST`
- URL: http://localhost:8080/api/person/oib?newFile=false
- Body:

````
{
  "oib": "string",
  "status": "string"
}
````
## Usage
To delete all files with a certain OIB and status:

````
{
"oib": "12345678901",
"status": "ACTIVE"
}
````
To delete all files regardless of OIB and status:


````
{
"oib": null,
"status": null
}
````
To delete all files with a certain status, regardless of OIB:


````
{
"oib": null,
"status": "ACTIVE"
}
````

