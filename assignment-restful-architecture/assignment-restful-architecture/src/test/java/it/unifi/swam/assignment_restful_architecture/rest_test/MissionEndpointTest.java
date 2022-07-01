package it.unifi.swam.assignment_restful_architecture.rest_test;

import static com.jayway.restassured.RestAssured.given;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.response.Response;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import it.unifi.swam.assignment_restful_architecture.Model.ModelFactory;
import it.unifi.swam.assignment_restful_architecture.Model.Company.Worker;
import it.unifi.swam.assignment_restful_architecture.Model.Game.Mission;
import it.unifi.swam.assignment_restful_architecture.Model.Game.Task;
import it.unifi.swam.assignment_restful_architecture.Utils.Populate;

public class MissionEndpointTest {
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
	public void getMissionByIdTest() {
		Response response  = given().header("Authorization", "Bearer "+ token).pathParam("id","1").get(baseURL + "missionendpoint/" + "{id}");
		response.then().statusCode(200)
		.body("name",org.hamcrest.Matchers.equalTo("test mission"));
	}
	
	@Test
	public void addMissionTest() {
		Mission misison = ModelFactory.mission();
		misison.setName("test mission 2");
		
		Response response = given()
				.header("Authorization", "Bearer "+ token)
				.contentType("application/json")
				.body(misison)
				.when().post(baseURL + "missionendpoint/");
				
				response.then().statusCode(201);
				
				Long id = Long.valueOf(response.body().asString().replace("Object created with ID: ", "").replace('"', ' ').trim());
				
				response  = given().header("Authorization", "Bearer "+ token).pathParam("id",id).get(baseURL + "missionendpoint/" + "{id}");
				response.then().statusCode(200)
				.body("name",org.hamcrest.Matchers.equalTo("test mission 2"));
	}
	
	@Test
	public void deleteMissionTest() {
		Mission misison = ModelFactory.mission();
		misison.setName("test mission 3");
		
		Response response = given()
				.header("Authorization", "Bearer "+ token)
				.contentType("application/json")
				.body(misison)
				.when().post(baseURL + "missionendpoint/");
				
				response.then().statusCode(201);
				
				Long id = Long.valueOf(response.body().asString().replace("Object created with ID: ", "").replace('"', ' ').trim());
				
		response = given()
						.header("Authorization", "Bearer "+ token)
						.when().delete(baseURL + "missionendpoint/" + id);
						
						response.then().statusCode(200);
						
						response  = given().header("Authorization", "Bearer "+ token).pathParam("id",id).get(baseURL + "missionendpoint/" + "{id}");
						response.then().statusCode(404);		
	}
	
	@Test
	public void addTaskToMissionTest() {
		Mission misison = ModelFactory.mission();
		misison.setName("test mission 4");
		
		Response response = given()
				.header("Authorization", "Bearer "+ token)
				.contentType("application/json")
				.body(misison)
				.when().post(baseURL + "missionendpoint/");
				
				response.then().statusCode(201);
				
				Long missionId = Long.valueOf(response.body().asString().replace("Object created with ID: ", "").replace('"', ' ').trim());
		
		Task task =  ModelFactory.task();
		task.setDescription("test task add");
		task.setPlace("test task add place");
		task.setTime(0);
		
		
		response = given()
				.header("Authorization", "Bearer "+ token)
				.contentType("application/json")
				.body(task)
				.when().put(baseURL + "missionendpoint/" + missionId + "/addtask");
				
				response.then().statusCode(200);	
				
		
				response = given()
						.header("Authorization", "Bearer "+ token)
						.when().get(baseURL + "missionendpoint/" + missionId);
						
						response.then().statusCode(200)
						.body("tasks", org.hamcrest.Matchers.hasSize(1));
	}
}
