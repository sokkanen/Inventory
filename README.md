Inventory REST-API
=

This application is a part of academic work, demonstrating a distributed system. 

**NO SECURITY IS IN PLACE, SO NOT TO BE USED IN PRODUCTION!**

Running Dockerized PostgreSQL 13.5. alongside application:
-

Run PSQL with following commands:

    docker pull postgres:13.5

Following with:

    docker run --name inventorydb -p 5432:5432 -e POSTGRES_PASSWORD=password123 -e POSTGRES_USER=inventoryuser -e POSTGRES_DB=inventorydb postgres

Running JAR
-
    java -DINVENTORY_POSTGRES_URL="<postgres_host>" -jar <jarname>


API endpoints
-

**Multiple items:**

List all:

    GET /api/v1/products

Search by name:

    GET /api/v1/products/search?keyword=keyword

**Single item:**

Create a new item:

    POST /api/v1/products

Get single item:

    GET /api/v1/products/{id}

Modify single item:

    PUT /api/v1/products/{id}

Delete single item:

    DELETE /api/v1/products/{id}

**Ordering & Verifying:**

These endpoints will expect orders to be specified in request body as a list.
For example: 
    
    [{"id": 1, "amount": 5}, {"id": 3, "amount": 12}]

Endpoints are as follows:    

    POST /api/v1/products/order --> Actual order is performed

    POST /api/v1/products/verify --> Products and stock are verified

**Misc.**

/actuator/health --> Spring actuator Health.