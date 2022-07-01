package it.unifi.swam.assignment_restful_architecture.rest_test;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.response.Response;

import static com.jayway.restassured.RestAssured.given;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import it.unifi.swam.assignment_restful_architecture.Model.ModelFactory;
import it.unifi.swam.assignment_restful_architecture.Model.Company.Company;
import it.unifi.swam.assignment_restful_architecture.Model.Company.Worker;
import it.unifi.swam.assignment_restful_architecture.Utils.Populate;

public class CompanyEndpointTest {
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
	public void getByIdTest() {
		Response response  = given().header("Authorization", "Bearer "+ token).pathParam("id","1").get(baseURL + "companyendpoint/" + "{id}");
		response.then().statusCode(200)
		.body("name",org.hamcrest.Matchers.equalTo("test company"));
	}
	
	@Test
	public void saveTest() {
		Company company =  ModelFactory.company();
		company.setName("Test company 2");
		
		Response response = given()
		.header("Authorization", "Bearer "+ token)
		.contentType("application/json")
		.body(company)
		.when().post(baseURL + "companyendpoint/");
		
		response.then().statusCode(201);
	}
	
	@Test
	public void updateTest() {
		Company company =  ModelFactory.company();
		company.setName("Test company 3");
		
		Response response = given()
				.header("Authorization", "Bearer "+ token)
				.contentType("application/json")
				.body(company)
				.when().post(baseURL + "companyendpoint/");
		
		response.then().statusCode(201);
		
		Long id = Long.valueOf(response.body().asString().replace("Object created with ID: ", "").replace('"', ' ').trim());
		
		company =  ModelFactory.company();
		company.setName("Test company 3 update");
		
		response = given()
		.header("Authorization", "Bearer "+ token)
		.contentType("application/json")
		.body(company)
		.when().put(baseURL + "companyendpoint/" + id);
		
		response.then().statusCode(204);
		
		response  = given().header("Authorization", "Bearer "+ token).pathParam("id", id).get(baseURL + "companyendpoint/" + "{id}");
		response.then().statusCode(200)
		.body("name",org.hamcrest.Matchers.equalTo("Test company 3 update"));
	}
	
	@Test
	public void deleteTest() {
		Company company =  ModelFactory.company();
		company.setName("Test company 4");
		
		Response response = given()
				.header("Authorization", "Bearer "+ token)
				.contentType("application/json")
				.body(company)
				.when().post(baseURL + "companyendpoint/");
		
		response.then().statusCode(201);
		
		Long id = Long.valueOf(response.body().asString().replace("Object created with ID: ", "").replace('"', ' ').trim());
		
		response = given()
				.header("Authorization", "Bearer "+ token)
				.when().delete(baseURL + "companyendpoint/" + id);
				
				response.then().statusCode(200);
				
				response  = given().header("Authorization", "Bearer "+ token).pathParam("id",id).get(baseURL + "companyendpoint/" + "{id}");
				response.then().statusCode(404);
	}
	
	@Test
	public void setPresidentTest() {
		Response response = given()
				.header("Authorization", "Bearer "+ token)
				.when().put(baseURL + "companyendpoint/1/1");
				
				response.then().statusCode(200);
				
				response  = given().header("Authorization", "Bearer "+ token).pathParam("id","1").get(baseURL + "companyendpoint/" + "{id}");
				response.then().statusCode(200)
				.body("president.email",org.hamcrest.Matchers.equalTo(worker.getEmail()));
	}
}