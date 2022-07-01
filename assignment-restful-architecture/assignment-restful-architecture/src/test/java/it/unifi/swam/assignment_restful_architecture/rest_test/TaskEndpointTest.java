package it.unifi.swam.assignment_restful_architecture.rest_test;

import static com.jayway.restassured.RestAssured.given;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.response.Response;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import it.unifi.swam.assignment_restful_architecture.Model.ModelFactory;
import it.unifi.swam.assignment_restful_architecture.Model.Company.Worker;
import it.unifi.swam.assignment_restful_architecture.Model.Game.Task;
import it.unifi.swam.assignment_restful_architecture.Utils.Populate;

public class TaskEndpointTest {
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
	public void getTaskByIdTest() {
		Response response  = given().header("Authorization", "Bearer "+ token).pathParam("id","1").get(baseURL + "taskendpoint/" + "{id}");
		response.then().statusCode(200)
		.body("description",org.hamcrest.Matchers.equalTo("test task"))
		.body("place", org.hamcrest.Matchers.equalTo("test task place"))
		.body("time", org.hamcrest.Matchers.equalTo(30));
	}
	
	@Test
	public void createTaskTest() {
		Task task =  ModelFactory.task();
		task.setDescription("test task 2");
		task.setPlace("test task 2 place");
		task.setTime(0);
		
		Response response = given()
		.header("Authorization", "Bearer "+ token)
		.contentType("application/json")
		.body(task)
		.when().post(baseURL + "taskendpoint/");
		
		response.then().statusCode(201);
		
		Long id = Long.valueOf(response.body().asString().replace("Object created with ID: ", "").replace('"', ' ').trim());
		
		response  = given().header("Authorization", "Bearer "+ token).pathParam("id",id).get(baseURL + "taskendpoint/" + "{id}");
		response.then().statusCode(200)
		.body("description",org.hamcrest.Matchers.equalTo("test task 2"))
		.body("place", org.hamcrest.Matchers.equalTo("test task 2 place"))
		.body("time", org.hamcrest.Matchers.equalTo(0));
	}
	
	@Test
	public void updateTaskTest() {
		Task task =  ModelFactory.task();
		task.setDescription("test task 3");
		task.setPlace("test task 3 place");
		task.setTime(0);
		
		Response response = given()
				.header("Authorization", "Bearer "+ token)
				.contentType("application/json")
				.body(task)
				.when().post(baseURL + "taskendpoint/");
				
		response.then().statusCode(201);
		
		Long id = Long.valueOf(response.body().asString().replace("Object created with ID: ", "").replace('"', ' ').trim());
		
		response  = given().header("Authorization", "Bearer "+ token).pathParam("id",id).get(baseURL + "taskendpoint/" + "{id}");
		response.then().statusCode(200)
		.body("description",org.hamcrest.Matchers.equalTo("test task 3"))
		.body("place", org.hamcrest.Matchers.equalTo("test task 3 place"))
		.body("time", org.hamcrest.Matchers.equalTo(0));
		
		task =  ModelFactory.task();
		task.setDescription("test task 3 update");
		task.setPlace("test task 3 place update");
		task.setTime(10);
		
		response = given()
				.header("Authorization", "Bearer "+ token)
				.contentType("application/json")
				.body(task)
				.when().put(baseURL + "taskendpoint/" + id);
				
		response.then().statusCode(204);
		
		response  = given().header("Authorization", "Bearer "+ token).pathParam("id",id).get(baseURL + "taskendpoint/" + "{id}");
		response.then().statusCode(200)
		.body("description",org.hamcrest.Matchers.equalTo("test task 3 update"))
		.body("place", org.hamcrest.Matchers.equalTo("test task 3 place update"))
		.body("time", org.hamcrest.Matchers.equalTo(10));
	}
	
	@Test
	public void deleteTaskTest() {
		Task task =  ModelFactory.task();
		task.setDescription("test task 5");
		task.setPlace("test task 5 place");
		task.setTime(0);
		
		Response response = given()
				.header("Authorization", "Bearer "+ token)
				.contentType("application/json")
				.body(task)
				.when().post(baseURL + "taskendpoint/");
				
		response.then().statusCode(201);
		
		Long id = Long.valueOf(response.body().asString().replace("Object created with ID: ", "").replace('"', ' ').trim());
		
		response = given()
				.header("Authorization", "Bearer "+ token)
				.when().delete(baseURL + "taskendpoint/" + id);
				
				response.then().statusCode(200);
				
				response  = given().header("Authorization", "Bearer "+ token).pathParam("id",id).get(baseURL + "taskendpoint/" + "{id}");
				response.then().statusCode(404);
	}
	
	@Test
	public void accomplishTaskTest() {
		Task task =  ModelFactory.task();
		task.setDescription("test task 4");
		task.setPlace("test task 4 place");
		task.setTime(0);
		task.setAccomplished(false);
		
		Response response = given()
				.header("Authorization", "Bearer "+ token)
				.contentType("application/json")
				.body(task)
				.when().post(baseURL + "taskendpoint/");
				
		response.then().statusCode(201);
		
		Long id = Long.valueOf(response.body().asString().replace("Object created with ID: ", "").replace('"', ' ').trim());
		
		response = given()
				.header("Authorization", "Bearer "+ token)
				.when().put(baseURL + "taskendpoint/" + id + "/accomplish");
		
		response.then().statusCode(200);
		
		response  = given().header("Authorization", "Bearer "+ token).pathParam("id",id).get(baseURL + "taskendpoint/" + "{id}");
		response.then().statusCode(200)
			.body("accomplished", org.hamcrest.Matchers.equalTo(true));
	}
}
