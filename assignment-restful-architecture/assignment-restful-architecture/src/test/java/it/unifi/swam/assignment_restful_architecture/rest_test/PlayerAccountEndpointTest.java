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
import it.unifi.swam.assignment_restful_architecture.Model.Player.PlayerAccount;
import it.unifi.swam.assignment_restful_architecture.Utils.Populate;

public class PlayerAccountEndpointTest {
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
	public void getAllPlayersTest() {
		Response response  = given().header("Authorization", "Bearer "+ token).get(baseURL + "playeraccountendpoint/");
		response.then().statusCode(200);
	}
	
	@Test
	public void getPlayerById() {
		Response response  = given().header("Authorization", "Bearer "+ token).pathParam("id","1").get(baseURL + "playeraccountendpoint/" + "{id}");
		response.then().statusCode(200)
		.body("email",org.hamcrest.Matchers.equalTo("testplayer@gmail.com"))
		.body("username", org.hamcrest.Matchers.equalTo("test player"));
	}
	
	@Test
	public void signupTest() {
		PlayerAccount player = ModelFactory.playeracc();
		player.setEmail("testplayer2@gmail.com");
		player.setPassword("testplayer2", true);
		player.setUsername("test player 2");
		
		Response response = given()
				.contentType("application/json")
				.body(player)
				.when().post(baseURL + "playeraccountendpoint/signup");
				
				response.then().statusCode(201);
				
		assertTrue(response.body().asString().contains("TOKEN"));
	}
	
	@Test
	public void createPlayerCharacter() {
		Response response = given()
				.header("Authorization", "Bearer "+ token)
				.when().put(baseURL + "playeraccountendpoint/1/character");
		
		response.then().statusCode(200);
		
		response  = given().header("Authorization", "Bearer "+ token).pathParam("id","1").get(baseURL + "playeraccountendpoint/" + "{id}");
		response.then().statusCode(200)
		.body("character.level", org.hamcrest.Matchers.equalTo(1));
	}
}
