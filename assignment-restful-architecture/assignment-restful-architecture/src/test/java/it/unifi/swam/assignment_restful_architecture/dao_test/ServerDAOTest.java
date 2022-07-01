package it.unifi.swam.assignment_restful_architecture.dao_test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.Test;

import it.unifi.swam.assignment_restful_architecture.DAOs.ServerDAO;
import it.unifi.swam.assignment_restful_architecture.Model.ModelFactory;
import it.unifi.swam.assignment_restful_architecture.Model.Company.Server;

public class ServerDAOTest extends JPATest {
	private Server server;
	private ServerDAO serverDao;

	//concrete init() method
	@Override
	protected void init() throws IllegalAccessException {
		System.out.println("Start init custom for ServerDAOTest");
		server = ModelFactory.server();
		server.setName("testserver");
		entityManager.persist(server); //Persisted manually, without using the DAO
		//this is done to test the retrieve afterwards
		serverDao = new ServerDAO();
		FieldUtils.writeField(serverDao,"entityManager",entityManager,true);
	}
	
	//retieve test
	//check that the entity retrieved is the same as the one defined in the init()
	@Test
	public void testFindById() {
		System.out.println("Perform testFindById in ServerDAOTest");
		Server result = serverDao.findById(server.getId()); //It has an ID since it has been persisted in the init()
		assertEquals(server.getId(), result.getId());
		assertEquals(server.getActive(), result.getActive());
	}
	
	
	//In this case we check persisting the data through the DAO
	@Test
	public void testSave() {
		System.out.println("Perform testSave in ServerDAOTest");
		Server serverToPersist = ModelFactory.server();
		serverToPersist.setName("servertest2");
		serverDao.save(serverToPersist);
		Server manuallyRetrievedServer =  entityManager.
				createQuery("FROM Server WHERE uuid = :uuid", Server.class)
				.setParameter("uuid", serverToPersist.getUuid())
				.getSingleResult();
		assertEquals(serverToPersist, manuallyRetrievedServer);
	}
	
	@Test
	public void testDelete() {
		System.out.println("Perform testDelete in ServerDAOTest");
		Server serverToDelete = ModelFactory.server();
		serverToDelete.setName("servertest3");
		entityManager.persist(serverToDelete);
		serverDao.delete(serverToDelete);
		List<Server> manuallyRetrievedServer =  entityManager.
				createQuery("FROM Server WHERE uuid = :uuid", Server.class)
				.setParameter("uuid", serverToDelete.getUuid())
				.getResultList();
		assertTrue(manuallyRetrievedServer.isEmpty());
	}
	
	@Test
	public void testGetActiveServers() {
		System.out.println("Perform testGetActiveServers in ServerDAOTest");
		List<Server> result = serverDao.getActiveServers();
		Server serverToPersist = ModelFactory.server();
		serverToPersist.setName("testgetactiveservers");
		serverToPersist.setActive(false);
		entityManager.persist(serverToPersist);
		assertEquals(result.size(), 1);
	}
}