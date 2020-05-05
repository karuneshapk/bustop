How to run application:
-------

- create build of app using maven command: 

```
mvn clean install -DskipTests  
```

- add data to input file `/src/main/resources/inputFile.txt`


- deploy application:

```
java -jar target/bus-stop-1.0-SNAPSHOT.jar
```

- than you will see next text in your console:

******
```
Available commands: 
 1: 'exit' 
 2: 'parse input file'


Enter the number of your command: 
```
******

- please enter the number of the command and press ENTER

- see the result in the `/src/main/resources/results` directory
