package it.unifi.swam.assignment_restful_architecture.dao_test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.Test;

import it.unifi.swam.assignment_restful_architecture.DAOs.WorkerDAO;
import it.unifi.swam.assignment_restful_architecture.Model.ModelFactory;
import it.unifi.swam.assignment_restful_architecture.Model.Company.Worker;
import it.unifi.swam.assignment_restful_architecture.Model.Company.WorkerRol;

public class WorkerDAOTest extends JPATest {
	private Worker worker;
	private WorkerDAO workerDao;

	//concrete init() method
	@Override
	protected void init() throws IllegalAccessException {
		System.out.println("Start init custom for WorkerDAOTest");
		worker = ModelFactory.worker();
		worker.setName("testWorker1");
		worker.setEmail("testWorker1@gmail.com");
		worker.setPassword("testWorker1",false);
		entityManager.persist(worker); //Persisted manually, without using the DAO
		//this is done to test the retrieve afterwards
		workerDao = new WorkerDAO();
		FieldUtils.writeField(workerDao,"entityManager",entityManager,true);
	}
	
	//retieve test
	//check that the entity retrieved is the same as the one defined in the init()
	@Test
	public void testFindById() {
		System.out.println("Perform testFindById in WorkerDAOTest");
		Worker result = workerDao.findById(worker.getId()); //It has an ID since it has been persisted in the init()
		assertEquals(worker.getId(), result.getId());
		assertEquals(worker.getName(), result.getName());
	}
	
	
	//In this case we check persisting the data through the DAO
	@Test
	public void testSave() {
		System.out.println("Perform testSave in WorkerDAOTest");
		Worker workerToPersist = ModelFactory.worker();
		workerToPersist.setName("testWorker2");
		workerToPersist.setEmail("testWorker2@gmail.com");
		workerToPersist.setPassword("testWorker2",false);
		workerDao.save(workerToPersist);
		Worker manuallyRetrievedWorker =  entityManager.
				createQuery("FROM Worker WHERE uuid = :uuid", Worker.class)
				.setParameter("uuid", workerToPersist.getUuid())
				.getSingleResult();
		assertEquals(workerToPersist, manuallyRetrievedWorker);
	}
	
	@Test
	public void testDelete() {
		System.out.println("Perform testDelete in WorkerDAOTest");
		Worker workerToDelete = ModelFactory.worker();
		workerToDelete.setName("testWorker3");
		workerToDelete.setEmail("testWorker3@gmail.com");
		workerToDelete.setPassword("testWorker3",false);
		entityManager.persist(workerToDelete);
		workerDao.delete(workerToDelete);
		List<Worker> manuallyRetrievedWorker =  entityManager.
				createQuery("FROM Worker WHERE uuid = :uuid", Worker.class)
				.setParameter("uuid", workerToDelete.getUuid())
				.getResultList();
		assertTrue(manuallyRetrievedWorker.isEmpty());
	}
	
	@Test
	public void testFindWorkerByName() {
		System.out.println("Perform testFindWorkerByName in WorkerDAOTest");
		Worker worker1 = ModelFactory.worker();
		worker1.setName("Victor");
		worker1.setEmail("testemail@gmail.com");
		worker1.setPassword("testworker1",false);
		Worker worker2 = ModelFactory.worker();
		worker2.setName("Victor");
		worker2.setEmail("testemail2@gmail.com");
		worker2.setPassword("testworker2",false);
		entityManager.persist(worker1);
		entityManager.persist(worker2);
		List<Worker> result = workerDao.findWorkerByName("Victor");
		assertEquals(result.size(), 2);
	}
	
	@Test
	public void testGetWorkerByEmail() {
		System.out.println("Perform testGetWorkerByEmail in WorkerDAOTest");
		Worker workerToPersist = ModelFactory.worker();
		workerToPersist.setEmail("testgetbyemail@gmail.com");
		workerToPersist.setName("testgetbyemail1");
		workerToPersist.setPassword("testgetbyemail1",false);
		
		Worker workerToPersist2 = ModelFactory.worker();
		workerToPersist2.setEmail("testgetbyemail2@gmail.com");
		workerToPersist2.setName("testgetbyemail2");
		workerToPersist2.setPassword("testgetbyemail2",false);

		entityManager.persist(workerToPersist); //cascade is applied so the Worker is also persisted
		entityManager.persist(workerToPersist2);
		Worker retrievedWorker = workerDao.getWorkerByEmail("testgetbyemail@gmail.com");
		assertEquals(retrievedWorker.getEmail(), "testgetbyemail@gmail.com");
	}
	
	@Test
	public void testGetByRol() {
		System.out.println("Perform testGetByRol in WorkerDAOTest");
		Worker worker1 = ModelFactory.worker();
		worker1.setName("Victor");
		worker1.setEmail("testemail@gmail.com");
		worker1.setPassword("testworker1",false);
		worker1.setWorkerRol(WorkerRol.Developer);
		Worker worker2 = ModelFactory.worker();
		worker2.setName("Victor");
		worker2.setEmail("testemail2@gmail.com");
		worker2.setPassword("testworker2",false);
		worker2.setWorkerRol(WorkerRol.Mainteinance);
		
		entityManager.persist(worker1);
		entityManager.persist(worker2);
		List<Worker> result = workerDao.getWorkersByRol(WorkerRol.Developer);
		assertEquals(result.size(), 1);
	}
	
}