# PersonApi

## Overview

## Getting Started
## Clone the repository
````
  git clone https://github.com/dbZrg/PersonApi.git
````

## Build the project
````
  ./gradlew clean build
````

## Run the application
````
  ./gradlew bootRun  
````
After running this command, the application will be accessible at http://localhost:8080.

## Usage

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