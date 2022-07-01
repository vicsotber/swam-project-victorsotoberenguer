package it.unifi.swam.assignment_restful_architecture.rest_test;

import static com.jayway.restassured.RestAssured.given;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.response.Response;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import it.unifi.swam.assignment_restful_architecture.Model.ModelFactory;
import it.unifi.swam.assignment_restful_architecture.Model.Company.Sector;
import it.unifi.swam.assignment_restful_architecture.Model.Company.SectorType;
import it.unifi.swam.assignment_restful_architecture.Model.Company.Worker;
import it.unifi.swam.assignment_restful_architecture.Utils.Populate;

public class SectorEndpointTest {
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
	public void getSectorByIdTest() {
		Response response  = given().header("Authorization", "Bearer "+ token).pathParam("id","1").get(baseURL + "sectorendpoint/" + "{id}");
		response.then().statusCode(200)
		.body("type",org.hamcrest.Matchers.equalTo("Development"))
		.body("leader", org.hamcrest.Matchers.nullValue());
	}
	
	@Test
	public void addSectorTest() {
		Sector sector = ModelFactory.sector();
		sector.setType(SectorType.Maintenance);
		
		Response response = given()
				.header("Authorization", "Bearer "+ token)
				.contentType("application/json")
				.body(sector)
				.when().post(baseURL + "sectorendpoint/");
		
				response.then().statusCode(201);
				
				Long id = Long.valueOf(response.body().asString().replace("Object created with ID: ", "").replace('"', ' ').trim());
				
				response  = given().header("Authorization", "Bearer "+ token).pathParam("id",id).get(baseURL + "sectorendpoint/" + "{id}");
				response.then().statusCode(200)
				.body("type",org.hamcrest.Matchers.equalTo(sector.getType().toString()));
	}
	
	@Test
	public void deleteSectorTest() {
		Sector sector = ModelFactory.sector();
		sector.setType(SectorType.Maintenance);
		
		Response response = given()
				.header("Authorization", "Bearer "+ token)
				.contentType("application/json")
				.body(sector)
				.when().post(baseURL + "sectorendpoint/");
				
				response.then().statusCode(201);
				
				Long id = Long.valueOf(response.body().asString().replace("Object created with ID: ", "").replace('"', ' ').trim());
				
				response = given()
						.header("Authorization", "Bearer "+ token)
						.when().delete(baseURL + "sectorendpoint/" + id);
						
						response.then().statusCode(200);
						
						response  = given().header("Authorization", "Bearer "+ token).pathParam("id",id).get(baseURL + "sectorendpoint/" + "{id}");
						response.then().statusCode(404);		
	}
	
	@Test
	public void setLeader() {
		Sector sector = ModelFactory.sector();
		sector.setType(SectorType.Development);
		
		Response response = given()
				.header("Authorization", "Bearer "+ token)
				.contentType("application/json")
				.body(sector)
				.when().post(baseURL + "sectorendpoint/");
				
				response.then().statusCode(201);
				
				Long sectorId = Long.valueOf(response.body().asString().replace("Object created with ID: ", "").replace('"', ' ').trim());
				
				response = given()
						.header("Authorization", "Bearer "+ token)
						.contentType("application/json")
						.body(sector)
						.when().put(baseURL + "sectorendpoint/" + sectorId + "/1");
						
						response.then().statusCode(200);
				

						response  = given().header("Authorization", "Bearer "+ token).pathParam("id",sectorId).get(baseURL + "sectorendpoint/" + "{id}");
						response.then().statusCode(200)
						.body("type",org.hamcrest.Matchers.equalTo(sector.getType().toString()))
						.body("leader.email", org.hamcrest.Matchers.equalTo(worker.getEmail()));
	}
}
