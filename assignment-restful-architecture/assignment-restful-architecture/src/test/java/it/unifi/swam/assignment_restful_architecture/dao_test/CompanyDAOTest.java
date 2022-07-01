package it.unifi.swam.assignment_restful_architecture.dao_test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.Test;

import it.unifi.swam.assignment_restful_architecture.DAOs.CompanyDAO;
import it.unifi.swam.assignment_restful_architecture.Model.ModelFactory;
import it.unifi.swam.assignment_restful_architecture.Model.Company.Company;
import it.unifi.swam.assignment_restful_architecture.Model.Company.Worker;

public class CompanyDAOTest extends JPATest {
	private CompanyDAO companyDao;
	private Company company;
	private Worker president;

	//concrete init() method
	@Override
	protected void init() throws IllegalAccessException {
		System.out.println("Start init custom for CompanyDAOTest");
		company = ModelFactory.company();
		company.setName("testCompany1");
		entityManager.persist(company); //Persisted manually, without using the DAO
		//this is done to test the retrieve afterwards
		companyDao = new CompanyDAO();
		president = ModelFactory.worker();
		president.setName("president");
		president.setEmail("testpresident@gmail.com");
		FieldUtils.writeField(companyDao,"entityManager",entityManager,true);
	}
	
	//retieve test
	//check that the entity retrieved is the same as the one defined in the init()
	@Test
	public void testFindById() {
		System.out.println("Perform testFindById in CompanyDAOTest");
		Company result = companyDao.findById(company.getId()); //It has an ID since it has been persisted in the init()
		assertEquals(company.getId(), result.getId());
		assertEquals(company.getName(), result.getName());
		assertNull(result.getPresident());
	}
	
	
	//In this case we check persisting the data through the DAO
	@Test
	public void testSave() {
		System.out.println("Perform testSave in CompanyDAOTest");
		Company companyToPersist = ModelFactory.company();
		companyToPersist.setName("testCompany2");
		companyDao.save(companyToPersist);
		Company manuallyRetrievedCompany =  entityManager.
				createQuery("FROM Company WHERE uuid = :uuid", Company.class)
				.setParameter("uuid", companyToPersist.getUuid())
				.getSingleResult();
		assertEquals(companyToPersist, manuallyRetrievedCompany);
	}
	
	@Test
	public void testDelete() {
		System.out.println("Perform testDelete in CompanyDAOTest");
		Company companyToDelete = ModelFactory.company();
		companyToDelete.setName("testCompany3");
		entityManager.persist(companyToDelete);
		companyDao.delete(companyToDelete);
		List<Company> manuallyRetrievedCompany =  entityManager.
				createQuery("FROM Company WHERE uuid = :uuid", Company.class)
				.setParameter("uuid", companyToDelete.getUuid())
				.getResultList();
		assertTrue(manuallyRetrievedCompany.isEmpty());
	}
	
	@Test
	public void testGetCompanyWithPresident() {
		System.out.println("Perform testGetCompanyWithPresident in CompanyDAOTest");
		Company companyToPersist = ModelFactory.company();
		companyToPersist.setName("testCompany4");
		companyToPersist.setPresident(president);
		Company companyToPersist2 = ModelFactory.company();
		companyToPersist2.setName("testCompany5");
		entityManager.persist(companyToPersist); //cascade is applied so the Worker is also persisted
		entityManager.persist(companyToPersist2);
		Company retrievedCompany = companyDao.getCompanyWithPresident(president);
		assertEquals(retrievedCompany.getPresident(), president);
	}
	
}