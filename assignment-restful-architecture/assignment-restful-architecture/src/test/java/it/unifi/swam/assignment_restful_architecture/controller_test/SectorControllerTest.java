package it.unifi.swam.assignment_restful_architecture.controller_test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.Before;
import org.junit.Test;

import it.unifi.swam.assignment_restful_architecture.Controllers.SectorController;
import it.unifi.swam.assignment_restful_architecture.DAOs.SectorDAO;
import it.unifi.swam.assignment_restful_architecture.Model.ModelFactory;
import it.unifi.swam.assignment_restful_architecture.Model.Company.Sector;
import it.unifi.swam.assignment_restful_architecture.Model.Company.SectorType;
import it.unifi.swam.assignment_restful_architecture.Model.Company.Worker;

public class SectorControllerTest {
	private SectorController sectorController;
	private SectorDAO sectorDao;
	private Sector fakeSector;
	
	@Before
	public void setup() throws IllegalAccessException {
		sectorController = new SectorController();
		
		sectorDao = mock(SectorDAO.class); //Mock instance (defines behaviour manually)
		
		fakeSector =  ModelFactory.sector();
		fakeSector.setType(SectorType.Development);
		
		FieldUtils.writeField(sectorController, "sectorDao", sectorDao, true); //Mock instance injected in the controller manually
	}
	
	@Test
	public void testGetSector() {
		when(sectorDao.findById(1L)).thenReturn(fakeSector);
		
		Sector retrievedSector = sectorController.getById(1L);
		assertEquals(retrievedSector.getType(), fakeSector.getType());
		assertNull(retrievedSector.getLeader());
		assertEquals(retrievedSector.getNumWorkers(), 0);
		assertEquals(retrievedSector, fakeSector);
	}
	
	@Test
	public void testUpdateSector() {
		Sector newSector = ModelFactory.sector();
		newSector.setType(SectorType.Maintenance);
		
		sectorController.updateSector(fakeSector, newSector);
		assertEquals(fakeSector.getType(), newSector.getType());
		assertEquals(fakeSector.getType(), SectorType.Maintenance);
	}
	
	@Test
	public void testSetLeader() {
		Worker fakeWorker = ModelFactory.worker();
		fakeWorker.setName("Fake Worker Name");
		fakeWorker.setEmail("fakeworkeremail@gmail.com");
		fakeWorker.setPassword("FakeWorkerPassword", false);
		
		sectorController.setLeader(fakeSector, fakeWorker);
		assertEquals(fakeSector.getLeader(), fakeWorker);
		assertEquals(fakeSector.getNumWorkers(), 1);
	}
	
	@Test
	public void testAddWorker() {
		Worker fakeWorker = ModelFactory.worker();
		fakeWorker.setName("Fake Worker Name");
		fakeWorker.setEmail("fakeworkeremail@gmail.com");
		fakeWorker.setPassword("FakeWorkerPassword", false);
		
		assertEquals(fakeSector.getNumWorkers(), 0);
		sectorController.addWorker(fakeSector, fakeWorker);
		assertEquals(fakeSector.getNumWorkers(), 1);
	}
	
	@Test
	public void testDeleteWorker() {
		Worker fakeWorker = ModelFactory.worker();
		fakeWorker.setName("Fake Worker Name");
		fakeWorker.setEmail("fakeworkeremail@gmail.com");
		fakeWorker.setPassword("FakeWorkerPassword", false);
		
		sectorController.addWorker(fakeSector, fakeWorker);
		assertEquals(fakeSector.getNumWorkers(), 1);
		
		sectorController.deleteWorker(fakeSector, fakeWorker);
		assertEquals(fakeSector.getNumWorkers(), 0);
	}
}
