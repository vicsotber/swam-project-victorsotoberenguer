package it.unifi.swam.assignment_restful_architecture.dao_test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.Test;

import it.unifi.swam.assignment_restful_architecture.DAOs.TaskDAO;
import it.unifi.swam.assignment_restful_architecture.Model.ModelFactory;
import it.unifi.swam.assignment_restful_architecture.Model.Game.Task;

public class TaskDAOTest extends JPATest {
	private Task task;
	private TaskDAO taskDao;

	//concrete init() method
	@Override
	protected void init() throws IllegalAccessException {
		System.out.println("Start init custom for TaskDAOTest");
		task = ModelFactory.task();
		task.setDescription("Test task 1");
		task.setPlace("test task 1");
		task.setTime(30);
		entityManager.persist(task); //Persisted manually, without using the DAO
		//this is done to test the retrieve afterwards
		taskDao = new TaskDAO();
		FieldUtils.writeField(taskDao,"entityManager",entityManager,true);
	}
	
	//retrieve test
	//check that the entity retrieved is the same as the one defined in the init()
	@Test
	public void testFindById() {
		System.out.println("Perform testFindById in TaskDAOTest");
		Task result = taskDao.findById(task.getId()); //It has an ID since it has been persisted in the init()
		assertEquals(task.getId(), result.getId());
		assertEquals(task.getDescription(), result.getDescription());
	}
	
	
	//In this case we check persisting the data through the DAO
	@Test
	public void testSave() {
		System.out.println("Perform testSave in TaskDAOTest");
		Task taskToPersist = ModelFactory.task();
		taskToPersist.setDescription("Test task 2");
		taskToPersist.setPlace("test task 2");
		taskToPersist.setTime(0);
		taskDao.save(taskToPersist);
		Task manuallyRetrievedTask =  entityManager.
				createQuery("FROM Task WHERE uuid = :uuid", Task.class)
				.setParameter("uuid", taskToPersist.getUuid())
				.getSingleResult();
		assertEquals(taskToPersist, manuallyRetrievedTask);
	}
	
	@Test
	public void testDelete() {
		System.out.println("Perform testDelete in TaskDAOTest");
		Task taskToDelete = ModelFactory.task();
		taskToDelete.setDescription("Test description 3");
		taskToDelete.setPlace("test place 3");
		taskToDelete.setTime(47);
		entityManager.persist(taskToDelete);
		taskDao.delete(taskToDelete);
		List<Task> manuallyRetrievedTask =  entityManager.
				createQuery("FROM Task WHERE uuid = :uuid", Task.class)
				.setParameter("uuid", taskToDelete.getUuid())
				.getResultList();
		assertTrue(manuallyRetrievedTask.isEmpty());
	}
	
	@Test
	public void testSearchTasksByPlace() {
		Task task = ModelFactory.task();
		task.setPlace("Test place");
		task.setDescription("test description");
		task.setTime(30);
		entityManager.persist(task);
		List<Task> result = this.entityManager.createQuery(
				"FROM Task WHERE place = :place", Task.class)
		.setParameter("place", task.getPlace())
		.getResultList();
		assertEquals(result.size(), 1);
		assertEquals(result.get(0).getPlace(), task.getPlace());
	}
	
}