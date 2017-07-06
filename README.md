# ad-application
Crawler is an application that collects products info from Amazon.

#### Run
```aidl
git clone https://github.com/ljbschen/ad-application.git
mvn clean install
java -jar crawler/target/crawler-1.0.0.jar
```

#### Note
Defualt input feed file is ```rawQuery2.txt``` which has full query list.
For quick demo purpose, feel free to change the input file to ```rawQuery.txt``` and run the code in the same.