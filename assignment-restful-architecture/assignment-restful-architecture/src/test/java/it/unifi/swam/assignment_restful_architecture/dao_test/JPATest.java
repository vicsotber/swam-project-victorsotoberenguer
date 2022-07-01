package it.unifi.swam.assignment_restful_architecture.dao_test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

//includes the four annotations of junit (@Before, @BeforeClass, @After, @AfterClass)
//initializes the EntityManager and factory
public abstract class JPATest {
	private static EntityManagerFactory entityFactory;
	protected EntityManager entityManager;
	
	//creates EntityManagerFactory
	//once for every Test Suite (costful operation)
	@BeforeClass
	public static void setupEM() {
		System.out.println("Creates EntityManagerFactory");
		//not real DB system, "in memory" to give the DAO something to work with
		entityFactory = Persistence.createEntityManagerFactory("test");
	}
	
	//initializes EntityManager and calls init() method
	//Performed before each single TestCase
	@Before
	public void setup() throws IllegalAccessException {
		System.out.println("Creates EntityManager");
		entityManager = entityFactory.createEntityManager();
		entityManager.getTransaction().begin(); //starts cleaning transaction
		
		//cleans DB keeping the tables
		String sql = "SET FOREIGN_KEY_CHECKS = 0";
		entityManager.createNativeQuery(sql).executeUpdate();
		
		sql = "TRUNCATE TABLE server";
		entityManager.createNativeQuery(sql).executeUpdate();
		
		sql = "TRUNCATE TABLE sector;";
		entityManager.createNativeQuery(sql).executeUpdate();
		
		sql = "TRUNCATE TABLE worker;";
		entityManager.createNativeQuery(sql).executeUpdate();
		
		sql = "TRUNCATE TABLE playeraccount;";
		entityManager.createNativeQuery(sql).executeUpdate();
		
		sql = "TRUNCATE TABLE playercharacter;";
		entityManager.createNativeQuery(sql).executeUpdate();
		
		sql = "TRUNCATE TABLE company;";
		entityManager.createNativeQuery(sql).executeUpdate();
		
		sql = "TRUNCATE TABLE mission;";
		entityManager.createNativeQuery(sql).executeUpdate();
		
		sql = "TRUNCATE TABLE task;";
		entityManager.createNativeQuery(sql).executeUpdate();
		
		sql = "SET FOREIGN_KEY_CHECKS = 1";
		entityManager.createNativeQuery(sql).executeUpdate();
		
		
		entityManager.getTransaction().commit(); //closes cleaning transaction
		entityManager.getTransaction().begin(); //starts transaction for custom init
		System.out.println("Calls method init");
		//this method is abstract and it is specialized in each concrete class
		init();
		entityManager.getTransaction().commit();
		entityManager.clear();
		entityManager.getTransaction().begin();
		System.out.println("Setup completed");
	}
	
	//Closes the transaction of the EntityManager
	//Performed after each single TestCase
	@After
	public void close() {
		if(entityManager.getTransaction().isActive()) {
			entityManager.getTransaction().rollback();
			//If there is active transaction, perform a rollback
		}
		System.out.println("Closes EntityManager");
		entityManager.close();
	}
	
	//Closes the EntityManagerFactory
	//Performed after every TestSuite
	@AfterClass
	public static void tearDownDB() {
		System.out.println("Closes EntityManagerFactory");
		entityFactory.close();
	}
	
	//Abstract method that will be defined for each ConcreteClass extending JPATest
	protected abstract void init() 
		throws IllegalAccessException;
	
}