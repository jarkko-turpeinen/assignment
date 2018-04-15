# assignment
<b>IoT Assingment</b>

Create IBM IoT Bluemix free account to implement the use cases.<p/> Use IBM liberty Java or Node JS Runtime to implement Use case 1,2 and 3 Create test data in IBM Cloudant to demonstrate usecase 1, 2 and 3.<p/> Please implement the code and do not use any code generation tools to do the assingment.<br/>

<b>Create Cloudant database service</b>
<br/>
bx login -u your.name@your.org.com -o your.name@your.org.com -s dev
<br/>
bx service create cloudantNoSQLDB Lite cloudant-service

<b>Create Liberty Server</b>
<br/>
gradle libertyCreate

<b>Run server in development</b>
<br/>
gradle libertyRun

<b>Provision application to IBM cloud according to manifest.yml</b>
<br/>
cf login -u your.name@your.org.com -o your.name@your.org.com -s dev
<br/>
cf push

<b>Bind Application to Cloudant service</b>
<br/>
cf bs assignment-app cloudant-service
</br>
cf restage assignment-app
<p/>
<b>Application endpoint url</b>
<br/>
http://assignment-app.eu-gb.mybluemix.net/assigment-app
<p/>
<b>Examples</b>
<p/>
<i>POST</i>
<br/>
curl http://assignment-app.eu-gb.mybluemix.net/assigment-app/equipment/ -H "Content-Type: application/json" -d "{\"equipmentNumber\":\"1000000123\"}"
<p/>
<i>GET search by equipment number</i>
<br/>
http://assignment-app.eu-gb.mybluemix.net/assigment-app/equipment/1000000123
<p/>
<i>GET list 10 equipments</i>
<br/>
http://assignment-app.eu-gb.mybluemix.net/assigment-app/equipment/search?limit=10
<br/>

