# assignment
IoT Assingment

Create IBM IoT Bluemix free account to implement the use cases. Use IBM liberty Java or Node JS Runtime to implement Use case 1,2 and 3 Create test data in IBM Cloudant to demonstrate usecase 1, 2 and 3. Please implement the code and do not use any code generation tools to do the assingment.

Create Cloudant database service

bx service create cloudantNoSQLDB Lite cloudant-service

Provision application to IBM cloud according to manifest.yml

cf push

Bind Application to Cloudant service

cf bs assignment-iot cloudant-service

cf restage assignment-iot
