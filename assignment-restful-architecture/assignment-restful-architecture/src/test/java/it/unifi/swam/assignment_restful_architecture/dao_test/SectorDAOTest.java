package it.unifi.swam.assignment_restful_architecture.dao_test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.Test;

import it.unifi.swam.assignment_restful_architecture.DAOs.SectorDAO;
import it.unifi.swam.assignment_restful_architecture.Model.ModelFactory;
import it.unifi.swam.assignment_restful_architecture.Model.Company.Sector;
import it.unifi.swam.assignment_restful_architecture.Model.Company.SectorType;
import it.unifi.swam.assignment_restful_architecture.Model.Company.Worker;

public class SectorDAOTest extends JPATest {
	private SectorDAO sectorDao;
	private Sector sector;
	private Worker leader;

	//concrete init() method
	@Override
	protected void init() throws IllegalAccessException {
		System.out.println("Start init custom for SectorDAOTest");
		sector = ModelFactory.sector();
		sector.setType(SectorType.Development);
		entityManager.persist(sector); //Persisted manually, without using the DAO
		//this is done to test the retrieve afterwards
		sectorDao = new SectorDAO();
		leader = ModelFactory.worker();
		leader.setName("leader");
		leader.setEmail("leader@gmail.com");
		FieldUtils.writeField(sectorDao,"entityManager",entityManager,true);
	}
	
	//retieve test
	//check that the entity retrieved is the same as the one defined in the init()
	@Test
	public void testFindById() {
		System.out.println("Perform testFindById in SectorDAOTest");
		Sector result = sectorDao.findById(sector.getId()); //It has an ID since it has been persisted in the init()
		assertEquals(sector.getId(), result.getId());
		assertEquals(sector.getType(), result.getType());
		assertNull(result.getLeader());
	}
	
	
	//In this case we check persisting the data through the DAO
	@Test
	public void testSave() {
		System.out.println("Perform testSave in SectorDAOTest");
		Sector sectorToPersist = ModelFactory.sector();
		sectorToPersist.setType(SectorType.Maintenance);
		sectorDao.save(sectorToPersist);
		Sector manuallyRetrievedSector =  entityManager.
				createQuery("FROM Sector WHERE uuid = :uuid", Sector.class)
				.setParameter("uuid", sectorToPersist.getUuid())
				.getSingleResult();
		assertEquals(sectorToPersist, manuallyRetrievedSector);
	}
	
	@Test
	public void testDelete() {
		System.out.println("Perform testDelete in SectorDAOTest");
		Sector sectorToDelete = ModelFactory.sector();
		sectorToDelete.setType(SectorType.Marketing);
		entityManager.persist(sectorToDelete);
		sectorDao.delete(sectorToDelete);
		List<Sector> manuallyRetrievedSector =  entityManager.
				createQuery("FROM Sector WHERE uuid = :uuid", Sector.class)
				.setParameter("uuid", sectorToDelete.getUuid())
				.getResultList();
		assertTrue(manuallyRetrievedSector.isEmpty());
	}
	
	@Test
	public void testGetSectorWitLeader() {
		System.out.println("Perform testGetSectorWithLeader in SectorDAOTest");
		Sector sectorToPersist = ModelFactory.sector();;
		sectorToPersist.setLeader(leader);
		sectorToPersist.setType(SectorType.Development);
		Sector sectorToPersist2 = ModelFactory.sector();
		sectorToPersist2.setType(SectorType.Maintenance);
		entityManager.persist(sectorToPersist); //cascade is applied so the Worker is also persisted
		entityManager.persist(sectorToPersist2);
		Sector retrievedSector = sectorDao.getSectorByLeader(leader);
		assertEquals(retrievedSector.getLeader(), leader);
	}
	
}