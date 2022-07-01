package it.unifi.swam.assignment_restful_architecture.model_test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.util.UUID;

import org.junit.Before;
import org.junit.Test;

//We test correct initialization as well as identity and equality between Objects
public class BaseEntityTest {
	private FakeBaseEntity entity1;
	private FakeBaseEntity entity2;
	private FakeBaseEntity entity3;
	
	
	//Before = BeforeEach of junit5 (in junit4)
	@Before
	public void setup() {
		System.out.println("Perform the setup...");
		String uuid1 = UUID.randomUUID().toString();
		String uuid2 = UUID.randomUUID().toString();
		
		entity1 = new FakeBaseEntity(uuid1);
		entity2 = new FakeBaseEntity(uuid2);
		entity3 = new FakeBaseEntity(uuid1);
	}
	
	//Expected exception when trying to create an object without uuid
	//In juni5 exists Assertions.assertThrows(...)
	@Test(expected = IllegalArgumentException.class)
	public void testNullUUID() {
		System.out.println("Perform testNullUUID");
		new FakeBaseEntity(null);
	}
	
	@Test
	public void testEquals() {
		System.out.println("Perform testEquals");
		assertEquals(entity1, entity1); //Check Identity
		assertEquals(entity1, entity3); //Check Equality
		assertNotEquals(entity1, entity2); //Check Not Equality
	}
}