# ad-application
Crawler is an application that collects products info from Amazon.

#### Requirements
* Java Platform (JDK) 8
* Apache Maven
* Docker
* Docker compose

#### MySQL Configuration
* First start docker container for MySQL
    ```aidl
    cd IndexBuilderOfflineService
    docker-compose up
    ```
* Create user and grant privileges
    ```aidl
    CREATE USER 'test'@'%' IDENTIFIED BY 'test';
    GRANT ALL PRIVILEGES ON ad_application.* TO 'test'@'%';
    ```

#### Run
```aidl
cd IndexBuilderOfflineService
mvn clean install
java -jar target/IndexBuilder-Service-1.0.0.jar
```