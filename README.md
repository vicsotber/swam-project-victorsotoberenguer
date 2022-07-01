# swam-project-victorsotoberenguer
## Introduction
In this project I am extending the application developed for the course of Ingegneria del Software of the Laurea Triennale as the back end of a Restful architecture.

The project is connected to a <b>database</b> in localhost called “assignment-restful-architecture” with <b>user</b> “java-client” and <b>password</b> “password”.

## Tools
The application was developed in Java with the IDE Eclipse (for Enterprise Java and Web developers), as a Maven project.

JPA was used to define how the entities of the Domain Model should be mapped in the database.

CDI was used to manage the lifecycle of different components.

JAX-RS was used to expose endpoint services.

Finally, JUnit was used to perform unit tests, along with several tools like Hamcrest, Rest Assured, or Mockito.

Maven dependencies used are located in the <b>pom.xml</b> file:
```java
<dependencies>
	<dependency>
		<groupId>junit</groupId>
		<artifactId>junit</artifactId>
		<version>4.12</version>
		<scope>test</scope>
	</dependency>
	<dependency>
		<groupId>javax</groupId>
		<artifactId>javaee-api</artifactId>
		<version>8.0.1</version>
		<scope>provided</scope>
	</dependency>
	<dependency>
		<groupId>org.hibernate</groupId>
		<artifactId>hibernate-core</artifactId>
		<version>5.4.13.Final</version>
		<scope>provided</scope>
	</dependency>
	<dependency>
		<groupId>com.h2database</groupId>
		<artifactId>h2</artifactId>
		<version>1.4.195</version>
		<scope>test</scope>
	</dependency>
	<dependency>
		<groupId>org.apache.commons</groupId>
		<artifactId>commons-lang3</artifactId>
		<version>3.12.0</version>
	</dependency>
	<dependency>
		<groupId>com.fasterxml.jackson.core</groupId>
		<artifactId>jackson-databind</artifactId>
		<version>2.9.6</version>
	</dependency>
	<dependency>
		<groupId>com.jayway.restassured</groupId>
		<artifactId>rest-assured</artifactId>
		<version>2.9.0</version>
		<scope>test</scope>
	</dependency>
	<dependency>
		<groupId>org.hamcrest</groupId>
		<artifactId>hamcrest-all</artifactId>
		<version>1.3</version>
	</dependency>
	<dependency>
		<groupId>org.mockito</groupId>
		<artifactId>mockito-core</artifactId>
		<version>3.9.0</version>
		<scope>test</scope>
	</dependency>
	<dependency>
		<groupId>com.auth0</groupId>
		<artifactId>java-jwt</artifactId>
		<version>3.19.2</version>
	</dependency>
	<dependency>
		<groupId>io.jsonwebtoken</groupId>
		<artifactId>jjwt</artifactId>
		<version>0.7.0</version>
	</dependency>
	<dependency>
		<groupId>com.lambdaworks</groupId>
		<artifactId>scrypt</artifactId>
		<version>1.4.0</version>
	</dependency>
	<dependency>
		<groupId>org.springframework.security</groupId>
		<artifactId>spring-security-crypto</artifactId>
		<version>5.7.1</version>
	</dependency>
	<dependency>
		<groupId>org.json</groupId>
		<artifactId>json</artifactId>
		<version>20220320</version>
	</dependency>
</dependencies>

Elements added to the classpath in Java Build Path:
java-jwt-3.19.2.jar
json-20220320.jar
mysql-connector-java-8.0.19.jar
spring-security-crypto-5.7.1.jar
