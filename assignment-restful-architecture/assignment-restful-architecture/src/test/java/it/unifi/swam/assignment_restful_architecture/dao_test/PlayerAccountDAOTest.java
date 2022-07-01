package it.unifi.swam.assignment_restful_architecture.dao_test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.Test;

import it.unifi.swam.assignment_restful_architecture.DAOs.PlayerAccountDAO;
import it.unifi.swam.assignment_restful_architecture.Model.ModelFactory;
import it.unifi.swam.assignment_restful_architecture.Model.Player.PlayerAccount;

public class PlayerAccountDAOTest extends JPATest {
	private PlayerAccount playeracc;
	private PlayerAccountDAO playeraccDao;

	//concrete init() method
	@Override
	protected void init() throws IllegalAccessException {
		System.out.println("Start init custom for PlayerAccountDAOTest");
		playeracc = ModelFactory.playeracc();
		playeracc.setEmail("testUser1@gmail.com");
		playeracc.setUsername("testUser1");
		playeracc.setPassword("testUser1", false);
		entityManager.persist(playeracc); //Persisted manually, without using the DAO
		//this is done to test the retrieve afterwards
		playeraccDao = new PlayerAccountDAO();
		FieldUtils.writeField(playeraccDao,"entityManager",entityManager,true);
	}
	
	//retieve test
	//check that the entity retrieved is the same as the one defined in the init()
	@Test
	public void testFindById() {
		System.out.println("Perform testFindById in PlayerAccountDAOTest");
		PlayerAccount result = playeraccDao.findById(playeracc.getId()); //It has an ID since it has been persisted in the init()
		assertEquals(playeracc.getId(), result.getId());
		assertEquals(playeracc.getEmail(), result.getEmail());
	}
	
	
	//In this case we check persisting the data through the DAO
	@Test
	public void testSave() {
		System.out.println("Perform testSave in PlayerAccountDAOTest");
		PlayerAccount playeraccToPersist = ModelFactory.playeracc();
		playeraccToPersist.setEmail("testUser2@gmail.com");
		playeraccToPersist.setUsername("testUser2");
		playeraccToPersist.setPassword("testUser2", false);
		playeraccDao.save(playeraccToPersist);
		PlayerAccount manuallyRetrievedPlayer =  entityManager.
				createQuery("FROM PlayerAccount WHERE uuid = :uuid", PlayerAccount.class)
				.setParameter("uuid", playeraccToPersist.getUuid())
				.getSingleResult();
		assertEquals(playeraccToPersist, manuallyRetrievedPlayer);
	}
	
	@Test
	public void testDelete() {
		System.out.println("Perform testDelete in PlayerAccountDAOTest");
		PlayerAccount playerToDelete = ModelFactory.playeracc();
		playerToDelete.setEmail("testUser3@gmail.com");
		playerToDelete.setUsername("testUser3");
		playerToDelete.setPassword("testUser3", false);
		entityManager.persist(playerToDelete);
		playeraccDao.delete(playerToDelete);
		List<PlayerAccount> manuallyRetrievedPlayer =  entityManager.
				createQuery("FROM PlayerAccount WHERE uuid = :uuid", PlayerAccount.class)
				.setParameter("uuid", playerToDelete.getUuid())
				.getResultList();
		assertTrue(manuallyRetrievedPlayer.isEmpty());
	}
	
	@Test
	public void testGetPlayerByEmail() {
		System.out.println("Perform testGetPlayerByEmail in PlayerAccountDAOTest");
		PlayerAccount playeraccToPersist = ModelFactory.playeracc();
		playeraccToPersist.setEmail("testgetbyemail@gmail.com");
		playeraccToPersist.setUsername("testgetbyemail");
		playeraccToPersist.setPassword("testgetbyemail", false);
		
		PlayerAccount playeraccToPersist2 = ModelFactory.playeracc();
		playeraccToPersist2.setEmail("testgetbyemail2@gmail.com");
		playeraccToPersist2.setUsername("testgetbyemail2");
		playeraccToPersist2.setPassword("testgetbyemail2", false);

		entityManager.persist(playeraccToPersist); //cascade is applied so the Worker is also persisted
		entityManager.persist(playeraccToPersist2);
		PlayerAccount retrievedPlayer = playeraccDao.getPlayerByEmail("testgetbyemail@gmail.com");
		assertEquals(retrievedPlayer.getEmail(), "testgetbyemail@gmail.com");
	}
	
}