package it.unifi.swam.assignment_restful_architecture.controller_test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.Before;
import org.junit.Test;
import org.springframework.security.crypto.bcrypt.BCrypt;

import it.unifi.swam.assignment_restful_architecture.Controllers.WorkerController;
import it.unifi.swam.assignment_restful_architecture.DAOs.WorkerDAO;
import it.unifi.swam.assignment_restful_architecture.Model.ModelFactory;
import it.unifi.swam.assignment_restful_architecture.Model.Company.Worker;

public class WorkerControllerTest {
	private WorkerController workerController;
	private WorkerDAO workerDao;
	private Worker fakeWorker;
	
	@Before
	public void setup() throws IllegalAccessException {
		workerController = new WorkerController();
		
		workerDao = mock(WorkerDAO.class); //Mock instance (defines behaviour manually)
		
		fakeWorker =  ModelFactory.worker();
		fakeWorker.setName("Fake Worker Name");
		fakeWorker.setEmail("fakeworkeremail@gmail.com");
		fakeWorker.setPassword("FakeWorkerPassword",false);
		
		FieldUtils.writeField(workerController, "workerDao", workerDao, true); //Mock instance injected in the controller manually
	}
	
	@Test
	public void testGetWorker() {
		when(workerDao.findById(1L)).thenReturn(fakeWorker);
		
		Worker retrievedWorker = workerController.getById(1L);
		assertEquals(retrievedWorker.getName(), fakeWorker.getName());
		assertEquals(retrievedWorker.getEmail(), fakeWorker.getEmail());
		assertEquals(retrievedWorker.getPassword(), fakeWorker.getPassword());
		assertEquals(retrievedWorker, fakeWorker);
	}
	
	@Test
	public void testGetAllWorkers() {
		Worker fakeWorker2 = ModelFactory.worker();
		fakeWorker2.setName("Fake Worker Name2");
		fakeWorker2.setEmail("fakeworkeremail2@gmail.com");
		fakeWorker2.setPassword("FakeWorkerPassword2",false);
		
		List<Worker> workers = new ArrayList<Worker>();
		workers.add(fakeWorker);
		workers.add(fakeWorker2);
		when(workerDao.getAllWorkers()).thenReturn(workers);
		
		List<Worker> retrievedWorkers = workerController.getAll();
		assertEquals(retrievedWorkers, workers);
		assertEquals(retrievedWorkers.get(0), fakeWorker);
		assertEquals(retrievedWorkers.get(1), fakeWorker2);
	}
	
	@Test
	public void testUpdateWorker() {
		Worker newWorker = ModelFactory.worker();
		newWorker.setName("Updated Worker Name");
		newWorker.setEmail("updatedworkeremail@gmail.com");
		newWorker.setPassword("UpdatedWorkerPassword",false);
		
		workerController.updateWorker(fakeWorker, newWorker, true);
		assertEquals(fakeWorker.getName(), newWorker.getName());
		assertEquals(fakeWorker.getPassword(), newWorker.getPassword());
		assertEquals(fakeWorker.getName(), "Updated Worker Name");
		assertTrue(BCrypt.checkpw("UpdatedWorkerPassword", fakeWorker.getPassword()));
	}
}
