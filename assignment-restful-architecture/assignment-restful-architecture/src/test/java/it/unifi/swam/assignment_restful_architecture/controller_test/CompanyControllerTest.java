package it.unifi.swam.assignment_restful_architecture.controller_test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.Before;
import org.junit.Test;

import it.unifi.swam.assignment_restful_architecture.Controllers.CompanyController;
import it.unifi.swam.assignment_restful_architecture.DAOs.CompanyDAO;
import it.unifi.swam.assignment_restful_architecture.Model.ModelFactory;
import it.unifi.swam.assignment_restful_architecture.Model.Company.Company;
import it.unifi.swam.assignment_restful_architecture.Model.Company.Worker;

public class CompanyControllerTest {
	private CompanyController companyController;
	private CompanyDAO companyDao;
	private Company fakeCompany;
	
	@Before
	public void setup() throws IllegalAccessException {
		companyController = new CompanyController();
		
		companyDao = mock(CompanyDAO.class); //Mock instance (defines behaviour manually)
		
		fakeCompany =  ModelFactory.company();
		fakeCompany.setName("Fake Company Name");
		
		FieldUtils.writeField(companyController, "companyDao", companyDao, true); //Mock instance injected in the controller manually
	}
	
	@Test
	public void testGetCompany() {
		when(companyDao.findById(1L)).thenReturn(fakeCompany);
		
		Company retrievedCompany = companyController.getById(1L);
		assertEquals(retrievedCompany.getName(), fakeCompany.getName());
		assertNull(retrievedCompany.getPresident());
		assertEquals(retrievedCompany, fakeCompany);
	}
	
	@Test
	public void testSetPresident() {
		Worker fakeWorker = ModelFactory.worker();
		fakeWorker.setName("Fake Worker Name");
		fakeWorker.setEmail("fakeworkeremail@gmail.com");
		fakeWorker.setPassword("FakeWorkerPassword", false);
		
		companyController.setPresident(fakeCompany, fakeWorker);
		assertEquals(fakeCompany.getPresident(), fakeWorker);
	}
	
	@Test
	public void testUpdateCompany() {
		Company newCompany = ModelFactory.company();
		newCompany.setName("Updated Company Name");
		
		companyController.updateCompany(fakeCompany, newCompany);
		assertEquals(fakeCompany.getName(), newCompany.getName());
		assertEquals(fakeCompany.getName(), "Updated Company Name");
	}
}
