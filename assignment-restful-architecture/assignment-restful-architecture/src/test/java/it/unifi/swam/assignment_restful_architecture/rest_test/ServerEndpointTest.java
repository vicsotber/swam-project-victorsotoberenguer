package it.unifi.swam.assignment_restful_architecture.rest_test;

import static com.jayway.restassured.RestAssured.given;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.response.Response;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import it.unifi.swam.assignment_restful_architecture.Model.ModelFactory;
import it.unifi.swam.assignment_restful_architecture.Model.Company.Server;
import it.unifi.swam.assignment_restful_architecture.Model.Company.Worker;
import it.unifi.swam.assignment_restful_architecture.Utils.Populate;

public class ServerEndpointTest {
	private final static String baseURL = "assignment-restful-architecture/rest/";
	private static String token;
	private static Worker worker;
	
	@BeforeClass
	public static void setup() throws IllegalAccessException {
		RestAssured.baseURI = "http://localhost/";
		RestAssured.port = 8080;
		Populate.truncate();
		Populate.populate();
		
		worker = ModelFactory.worker();
		worker.setEmail("testdeveloper@gmail.com");
		worker.setPassword("testdeveloper", true);
		Response response = given()
				.contentType("application/json")
				.body(worker)
				.when().post(baseURL + "workerendpoint/login");
		token = response.getBody().asString(); //token of an existing worker in the database with role Developer
	}
	
	@AfterClass
	public static void teardown() {
		Populate.truncate();
	}
	
	@Test
	public void getServerByIdTest() {
		Response response  = given().header("Authorization", "Bearer "+ token).pathParam("id","1").get(baseURL + "serverendpoint/" + "{id}");
		response.then().statusCode(200)
		.body("name",org.hamcrest.Matchers.equalTo("test server"));
	}
	
	@Test
	public void addServerTest() {
		Server server =  ModelFactory.server();
		server.setName("test server 2");
		
		Response response = given()
		.header("Authorization", "Bearer "+ token)
		.contentType("application/json")
		.body(server)
		.when().post(baseURL + "serverendpoint/");
		
		response.then().statusCode(201);
	}
	
	@Test
	public void updateServer() {
		Server server =  ModelFactory.server();
		server.setName("test server 3");
		
		Response response = given()
		.header("Authorization", "Bearer "+ token)
		.contentType("application/json")
		.body(server)
		.when().post(baseURL + "serverendpoint/");
		
		response.then().statusCode(201);
		
		Long id = Long.valueOf(response.body().asString().replace("Object created with ID: ", "").replace('"', ' ').trim());
		
		server =  ModelFactory.server();
		server.setName("test server 3 update");
		
		response = given()
		.header("Authorization", "Bearer "+ token)
		.contentType("application/json")
		.body(server)
		.when().put(baseURL + "serverendpoint/" + id);
		
		response.then().statusCode(204);
		
		response  = given().header("Authorization", "Bearer "+ token).pathParam("id",id).get(baseURL + "serverendpoint/" + "{id}");
		response.then().statusCode(200)
		.body("name",org.hamcrest.Matchers.equalTo("test server 3 update"));
	}
	
	@Test
	public void deleteServer() {
		Server server =  ModelFactory.server();
		server.setName("test server 4");
		
		Response response = given()
				.header("Authorization", "Bearer "+ token)
				.contentType("application/json")
				.body(server)
				.when().post(baseURL + "serverendpoint/");
		
		response.then().statusCode(201);
		
		Long id = Long.valueOf(response.body().asString().replace("Object created with ID: ", "").replace('"', ' ').trim());
		
		response = given()
				.header("Authorization", "Bearer "+ token)
				.when().delete(baseURL + "serverendpoint/" + id);
				
				response.then().statusCode(200);
				
				response  = given().header("Authorization", "Bearer "+ token).pathParam("id",id).get(baseURL + "serverendpoint/" + "{id}");
				response.then().statusCode(404);
	}
	
	@Test
	public void activateServer() {
		Server server =  ModelFactory.server();
		server.setName("test server 5");
		server.setActive(false);
		
		Response response = given()
				.header("Authorization", "Bearer "+ token)
				.contentType("application/json")
				.body(server)
				.when().post(baseURL + "serverendpoint/");
		
		response.then().statusCode(201);
		
		Long id = Long.valueOf(response.body().asString().replace("Object created with ID: ", "").replace('"', ' ').trim());
		
		response = given()
				.header("Authorization", "Bearer "+ token)
				.when().put(baseURL + "serverendpoint/" + id + "/deactivate");
				
		response.then().statusCode(403); //forbidden because of Worker of type Developer
	}
}
