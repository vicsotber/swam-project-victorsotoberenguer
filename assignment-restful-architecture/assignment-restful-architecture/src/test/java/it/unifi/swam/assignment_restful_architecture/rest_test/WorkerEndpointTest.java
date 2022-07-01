package it.unifi.swam.assignment_restful_architecture.rest_test;

import static com.jayway.restassured.RestAssured.given;
import static org.junit.Assert.assertTrue;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.response.Response;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import it.unifi.swam.assignment_restful_architecture.Model.ModelFactory;
import it.unifi.swam.assignment_restful_architecture.Model.Company.Worker;
import it.unifi.swam.assignment_restful_architecture.Model.Company.WorkerRol;
import it.unifi.swam.assignment_restful_architecture.Utils.Populate;

public class WorkerEndpointTest {
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
		worker.setPassword("testdeveloper",true);
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
	public void getAllWorkersTest() {
		Response response  = given().header("Authorization", "Bearer "+ token).get(baseURL + "workerendpoint/");
		response.then().statusCode(200);
	}
	
	@Test
	public void getWorkerById() {
		Response response  = given().header("Authorization", "Bearer "+ token).pathParam("id","1").get(baseURL + "workerendpoint/" + "{id}");
		response.then().statusCode(200)
		.body("email",org.hamcrest.Matchers.equalTo("testdeveloper@gmail.com"))
		.body("name", org.hamcrest.Matchers.equalTo("test worker developer"))
		.body("workerRol", org.hamcrest.Matchers.equalTo("Developer"));
	}
	
	@Test
	public void signupTest() {
		Worker worker = ModelFactory.worker();
		worker.setEmail("testsignup@gmail.com");
		worker.setName("test signup");
		worker.setPassword("testsignup",false);
		worker.setWorkerRol(WorkerRol.Developer);
		
		Response response = given()
				.contentType("application/json")
				.body(worker)
				.when().post(baseURL + "workerendpoint/signup");
				
				response.then().statusCode(201);
				
		assertTrue(response.body().asString().contains("TOKEN"));
	}
	
}
