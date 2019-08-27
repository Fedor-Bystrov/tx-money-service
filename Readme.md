# tx-money-service 

An RESTful API for money transfers between accounts. 
Provides 6 preinitialized accounts, 5 example money transactions 
and an interface to fetch account balance, transaction details and create new transactions.

An executable jar with project can be downloaded from [link](https://github.com/Fedor-Bystrov/tx-money-service/raw/master/jars/tx-money-service-0.1.0.jar).

## Getting Started

### Run prebuild jar file

Executing `java -jar tx-money-service-0.1.0.jar` will start application on `http://localhost:3137`
Jar file with application located in `./jars`

### Build and run from sources

In the project root directory run:
 - `./gradlew clean build run` to build and run project
 - `./gradlew clean jar` to build executable jar archive and then `java -jar ./build/libs/tx-money-service-0.1.0.jar`
