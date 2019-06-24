# User API
Author : Sena Bak

## Project Setup
   * SpringBoot 2.1.3 
   * Gradle 4 Wrapper
   * Java 8 or above
   * Hibernate 5
   * JUnit 4
   * H2 in-memory database


## Design points
* An API service that will work safely in a busy Microservice architecture
* It is most important to capture any errors may occur, and be able to detect the problems early on.
* Exceptions should be handled and resolved as much as possible within the service itself
* Each service application should respond to the others in a unified format across services.
* The above could be achieved with utilising Standard HTTP error codes with structured guidelines.

###### shortfalls
* logging is not implemented due to the time constraint. 
* tests are not covered for update and delete. They are not optimised with lower level unit tests.

## Prerequisites
  
   * Java 8: The project JDK version is 1.8 and the machine running the application also must have 1.8 or above. 
     https://docs.opsgenie.com/docs/setting-java_home

## Using the application locally

### To download project

    git clone https://github.com/senabak/user.git

### To start the application

#### With IDE
* Navigate to Main.main method and run it.

#### With command terminal             
##### Windows OS

   1. Make sure that JAVA_HOME variable in your environment is set to match the location of your Java installation.
      If you installed Java, it is likely to be C:\Program Files\Java\jdk1.8.0_211 (versions may vary)
      Alternatively, jdk path can be set for the project with IDE.
        
   2. Open a command terminal and navigate to project root directory.
   
   3. Build the project (runs all tests as part of it)
   
     gradlew.bat build
	
   4. Start the application.       
         
     gradlew.bat bootRun
	   
   it will build and run the project with the gradle wrapper jar already included in the project. So, gradle path doesn't need to be set separately.  
   
   5. To stop the application, enter terminate command, ctrl + c in the terminal. 
      Alternatively, Find the process running in port 8080 and forcefully end them with the following commands (replace < > with a specific value).
         
    netstat -ano | findstr :8080
    
    taskkill //PID <pid found from above> //F
    
    ex) netstat -ano | findstr :8080    
        
           TCP 0.0.0.0:8080 0.0.0.0:0 LISTENING 6344  
           TCP [::]:8080    [::]:0    LISTENING 6344                                                                                                                                                         
        
        C:\Users\feile>taskkill /PID 6344 /F  
        
           SUCCESS: The process with PID 6344 has been terminated.  

#### Unix OS

   1. Make sure that JAVA_HOME variable in your environment is set to match the location of your Java installation.
      Alternatively, jdk path can be set for the project with IDE.
        
   2. Open a command terminal and navigate to project root directory.
   
   3. Build the project (runs all tests as part of it)
   
     ./gradlew build
	
   4. Start the application.       
         
     ./gradlew bootRun
	   
   it will build and run the project with the gradle wrapper jar already included in the project. So, gradle path doesn't need to be set separately.  
   
   5. To stop the application, enter terminate command, ctrl + c in the terminal. 
      Alternatively, Find the process running in port 8080 and forcefully end them with the following commands (replace < > with a specific value).
         
    sudo netstat -ltnp | grep -w ':8080'
    
    kill <pid found from above>
    	 	
	
## Endpoints
    local base url                    http://localhost:8080
    
| Request Type | end point              | Status          | description                                    | 
| ------       | ------                 |------           |------                                          |
| GET          | /users                 | available       |list of users                                   |  
| GET          | /users/find/{id}       | available       |find a user by id                               |  
| POST         | /users/create          | available       |create a user                                   |
| PUT          | /users/update/{id}     | available       |update a user by id                             |
| DELETE       | /users/delete/{id}     | available       |delete a user by id                             |

   
## To run all tests only 
 navigate to the project root directory and run the following command. 
 
#### Windows OS
      
    gradlew.bat test
    
#### Unix OS
 
    ./gradlew test
 
### Interact with API
* replace the placeholders annotated < > with specific values. curl --help for more.

       
#### GET
###### curl -v <end-point>

* valid get

      curl -v http://localhost:8080/users
      curl -v http://localhost:8080/users/1

* invalid get: root parameter id is not negative (the same applies to other end points)

      non positive id
      curl -v http://localhost:8080/users/-2
      
      after creating a user
      curl -v -d "{\"username\":\"username01\" , \"name\":\"user\", \"email\":\"email@email.com\"}" -H "Content-Type: application/json" http://localhost:8080/users/create 
      id doesn't exist
      curl -v http://localhost:8080/users/-2
  
#### POST
###### curl -v -d "{\"username\":\"<username>\" , \"name\":\"<name>\" , \"email\":\"<email>\"}" -H "Content-Type: application/json" <end-point>

* valid post
      
      curl -v -d "{\"username\":\"username01\" , \"name\":\"user\", \"email\":\"email@email.com\"}" -H "Content-Type: application/json" http://localhost:8080/users/create

* invalid post: see constraints annotated in class User 
username or name contains special characters, username contains space, name contains numbers or more than one space, invalid email format, etc

      curl -v -d "{\"username\":\"username01\" , \"name\":\"user name has 11\", \"email\":\"email@email.com\"}" -H "Content-Type: application/json" http://localhost:8080/users/create
      
      curl -v -d "{\"username\":\"username01\" , \"name\":\"name\", \"email\":\"funnyemail.com\"}" -H "Content-Type: application/json" http://localhost:8080/users/create
      
      curl -v -d "{\"username\":\"user name no space allowed 01\" , \"name\":\"name\", \"email\":\"email@email.com\"}" -H "Content-Type: application/json" http://localhost:8080/users/create


#### PUT
###### curl -v -d "{\"id\":<id>, \"username\":\"<username>\" , \"name\":\"<name>\", \"email\":\"<email>\"}" -H "Content-Type: application/json" -X PUT <end-point with user id >
* valid put

       after creating a user with
       curl -v -d "{\"username\":\"username01\" , \"name\":\"Jane\", \"email\":\"email@email.com\"}" -H "Content-Type: application/json" http://localhost:8080/users/create
       
       change name
       curl -v -d "{\"id\":1, \"username\":\"username01\" , \"name\":\"Jane Changed\", \"email\":\"email@email.com\"}" -H "Content-Type: application/json" -X PUT http://localhost:8080/users/update/1

* invalid put: the same user validation for users/create will be applied for users/update/<id>. 

The additional validations : username is changed. email is changed but it already belongs to another user. id in the path parameter does not match the id in the request body. 
 
       
       after creating users
       curl -v -d "{\"username\":\"jane2019\" , \"name\":\"Jane Doe\", \"email\":\"jane@email.com\"}" -H "Content-Type: application/json" http://localhost:8080/users/create
       curl -v -d "{\"username\":\"john2019\" , \"name\":\"John Doe\", \"email\":\"john@email.com\"}" -H "Content-Type: application/json" http://localhost:8080/users/create
       
       attempt to change username
       curl -v -d "{\"id\":1, \"username\":\"jane2019changed\" , \"name\":\"Jane Doe\", \"email\":\"jane@email.com\"}" -H "Content-Type: application/json" -X PUT http://localhost:8080/users/update/1
       
       attempt to change email to the one already belongs to another user
       curl -v -d "{\"id\":1, \"username\":\"jane2019\" , \"name\":\"Jane Doe\", \"email\":\"john@email.com\"}" -H "Content-Type: application/json" -X PUT http://localhost:8080/users/update/1
       
       id exists, but id in the path paramter not match the id in request body
       curl -v -d "{\"id\":1, \"username\":\"jane2019\" , \"name\":\"Jane Doe Changed\", \"email\":\"jane@email.com\"}" -H "Content-Type: application/json" -X PUT http://localhost:8080/users/update/2
       
       id doesn't exist
       curl -v -d "{\"id\":1, \"username\":\"jane2019\" , \"name\":\"Jane Doe\", \"email\":\"john@email.com\"}" -H "Content-Type: application/json" -X PUT http://localhost:8080/users/update/3
       

    
#### DELETE
###### curl -X DELETE <end-point with user id>    

* valid delete: against existing user id

      curl -X DELETE http://localhost:8080/users/delete/1
     
              

#### Actuator GET endpoints


    curl -v http://localhost:8080/actuator/health
    curl -v http://localhost:8080/actuator/info
    

 
 
## Development notes


* If project dependencies change due to build.gradle content change, 'user_test' module may loose the dependency to 'user_main' module. 
To resolve the issue, navigate to the namespaces that have resource reference failure, alt+enter (opens recommendation list from IDE), select 'add dependency on module user_main'

* Springboot will auto-configure the compatible versions of the plugins for the most of them. 
However, the plugins specified with version numbers are the ones not handled by Springboot auto-configure. 
You can detect this by specifying plugins under 'dependency' without version number, build the project, then check the plugin versions in 'external libraries'.
If there are some plugins missing, visit repositories such as https://mvnrepository.com/, and look for the latest stable version of the plugins. Then append the version number
at the end of the plugin name followed by colon. e.g. implementation 'com.googlecode.json-simple:json-simple:1.1.1' 

* There can be mismatching dependencies between springboot - third party libraries, and also one third party to another. 

* There can be redundant plugin statements as springboot auto configuration made some third party plugins implicitly downloaded. 
Start with plugins prefixed with springboot-starter- and examine the plugins downloaded before adding other plugins explicitly. Spring constantly updates 
the auto-configured plugins. If the third party plugin versions need to be strictly controlled, specify version numbers.

* org.springframework.boot:spring-boot-starter-parent could not be imported by springboot nor 2.1.3 even though maven repository has the current version 2.1.3

* For build script, groovy 'compile' is deprecated from gradle 3.0. Use 'implementation' if you don't want to expose the plugins to the target machines classpaths, or 'api'.

* controller method arguments better to be Types rather than primitives, especially for HttpMessageNotWritableException: Could not write JSON 
