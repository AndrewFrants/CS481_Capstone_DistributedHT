# Kubernetes application based on tutorial

# Prepared by Andrew Frantsuzov for Distributed Service Capstone

# Setup instructions

1. Open folder in VS Code
2. Install Docker extension for VS Code
3. Right click on Dockerfile and click Build, check image was created (docker images)
4. Run kubectl create -f dht-service.yaml
5. Verify Kubernetes cluster was deployed by opening website (http://localhost:8888/api/hello-world)

# For more details

See file "Kubernetes Instructions.docx"

# Miscelaneous

### HOW TO RUN WEB SERVER from Command line

1. Find Java-JDK directory
    This may help: java -XshowSettings:properties -version

3. Configure the Java Home JDK folder, Run:

    set JAVA_HOME=C:\Program Files\Java\jdk1.8.0_211
    Note: The path from step 1; your path may be different!

2. Open command prompt, navigate to folder DistributedHashtableUI (e.g.)
    pushd C:\Users\andreyf\OneDrive\BC\Fall 19\Capstone\DistributedHashtable\DistributedHashtableUI

3. Run:
    mvnw spring-boot:run

3a Run with a specific port:
    mvnw spring-boot:run -Drun.arguments="--server.port=9000"

3b Run with specifying a specific port and custom command line arguments

    mvnw spring-boot:run -Drun.arguments="--server.port=9000,--first=false"

4. When you see "Started DhtWebService in 3.6 seconds" that means it started!
    set JAVA_HOME=C:\Program Files\Java\jdk1.8.0_211
    pushd C:\Users\andreyf\OneDrive\BC\Fall 19\Capstone\DistributedHashtable\DistributedHashtableUI
    mvnw spring-boot:run -Drun.arguments="--server.port=8080,--join=true"


5. How to run. Start the first webservice on port 8080 always!

mvnw spring-boot:run -Drun.arguments="--server.port=8080,--join"

Subsequent instances, run on other ports such as 8081, 8082, etc

mvnw spring-boot:run -Drun.arguments="--server.port=8081,--join"
