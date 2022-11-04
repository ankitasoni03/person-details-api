# person-details-api
API for Personal Details

## Tools and Technologies
* Java 1.8
* Spring Boot
* Spring Data JPA
* H2 Database
* Swagger UI
* Junit 5
* Gradle

## API endpoints

### Hobby Controller

| HTTP method | API URL | Description |
| ----------- | ------- | ----------- |
| GET | /api/hobbies | Fetch list of hobbies |
| GET | /api/hobby/{hobbyId} | Fetch hobby by hobbyId |
| POST | /api/hobby | Create hobby |
| PUT | /api/hobby/{hobbyId} | Update hobby |
| DELETE | /api/hobby/{hobbyId} | Delete hobby by hobbyId |

### Person Detail Controller

| HTTP method | API URL | Description |
| ----------- | ------- | ----------- |
| GET | /api/persons | Fetch list of persons |
| GET | /api/person/{personId} | Fetch person by personId |
| POST | /api/person | Create person |
| PUT | /api/person/{personId} | Update person |
| DELETE | /api/person/{personId} | Delete person by personId |
| PUT | /api/person/{personId}/hobby/{hobbyId} | Add hobby for person |
| DELETE | /api/person/{personId}/hobby/{hobbyId} | Delete hobby for person |

## Databse Entities

### Person Entity

| Sr. No. | Column |
| ------- | ------ |
| 1 | person_id |
| 2 | first_name |
| 3 | last_name |
| 4 | age |
| 5 | favourite_color |

### Hobby Entity
| Sr. No. | Column |
| ------- | ------ |
| 1 | hobby_id |
| 2 | hobby |
| 3 | person_id |

## How to run APIs

**1. Clone repository**
git clone https://github.com/ankitasoni03/person-details-api.git

**2. Run below command for build and run the application**
gradle clean build bootrun

The application will be running in port 8080. We can test the API using swagger ui or postman. Swagger UI also generates curl commands for API testing.

Swagger UI - http://localhost:8080/swagger-ui.html

