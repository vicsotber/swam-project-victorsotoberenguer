package it.unifi.swam.assignment_restful_architecture.controller_test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.Before;
import org.junit.Test;

import it.unifi.swam.assignment_restful_architecture.Controllers.ServerController;
import it.unifi.swam.assignment_restful_architecture.DAOs.ServerDAO;
import it.unifi.swam.assignment_restful_architecture.Model.ModelFactory;
import it.unifi.swam.assignment_restful_architecture.Model.Company.Server;

public class ServerControllerTest {
	private ServerController serverController;
	private ServerDAO serverDao;
	private Server fakeServer;
	
	@Before
	public void setup() throws IllegalAccessException {
		serverController = new ServerController();
		
		serverDao = mock(ServerDAO.class); //Mock instance (defines behaviour manually)
		
		fakeServer =  ModelFactory.server();
		fakeServer.setName("Fake server name");
		
		FieldUtils.writeField(serverController, "serverDao", serverDao, true); //Mock instance injected in the controller manually
	}
	
	@Test
	public void testGetServer() {
		when(serverDao.findById(1L)).thenReturn(fakeServer);
		
		Server retrievedServer = serverController.getById(1L);
		assertEquals(retrievedServer.getName(), fakeServer.getName());
		assertEquals(retrievedServer.getActive(), fakeServer.getActive());
		assertEquals(retrievedServer, fakeServer);
	}
	
	@Test
	public void testActivateServer() {
		serverController.activate(fakeServer);
		
		assertTrue(fakeServer.getActive());
	}
	
	@Test
	public void testDeactivateServer() {
		serverController.deactivate(fakeServer);
		assertFalse(fakeServer.getActive());
	}
	
	@Test
	public void testUpdateServer() {
		Server newServer = ModelFactory.server();
		newServer.setName("Updated server name");
		newServer.setActive(true);
		
		serverController.updateServer(fakeServer, newServer);
		assertEquals(fakeServer.getName(), newServer.getName());
		assertEquals(fakeServer.getActive(), newServer.getActive());
		assertEquals(fakeServer.getName(), "Updated server name");
	}
}
