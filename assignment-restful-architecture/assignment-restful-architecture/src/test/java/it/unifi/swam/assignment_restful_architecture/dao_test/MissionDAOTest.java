package it.unifi.swam.assignment_restful_architecture.dao_test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.Test;

import it.unifi.swam.assignment_restful_architecture.DAOs.MissionDAO;
import it.unifi.swam.assignment_restful_architecture.Model.ModelFactory;
import it.unifi.swam.assignment_restful_architecture.Model.Game.Mission;

public class MissionDAOTest extends JPATest {
	private Mission mission;
	private MissionDAO missionDao;

	//concrete init() method
	@Override
	protected void init() throws IllegalAccessException {
		System.out.println("Start init custom for MissionDAOTest");
		mission = ModelFactory.mission();
		mission.setName("missionTest1");
		entityManager.persist(mission); //Persisted manually, without using the DAO
		//this is done to test the retrieve afterwards
		missionDao = new MissionDAO();
		FieldUtils.writeField(missionDao,"entityManager",entityManager,true);
	}
	
	//retieve test
	//check that the entity retrieved is the same as the one defined in the init()
	@Test
	public void testFindById() {
		System.out.println("Perform testFindById in MissionDAOTest");
		Mission result = missionDao.findById(mission.getId()); //It has an ID since it has been persisted in the init()
		assertEquals(mission.getId(), result.getId());
		assertEquals(mission.getName(), result.getName());
	}
	
	
	//In this case we check persisting the data through the DAO
	@Test
	public void testSave() {
		System.out.println("Perform testSave in MissionDAOTest");
		Mission missionToPersist = ModelFactory.mission();
		missionToPersist.setName("testMission2");
		missionDao.save(missionToPersist);
		Mission manuallyRetrievedMission =  entityManager.
				createQuery("FROM Mission WHERE uuid = :uuid", Mission.class)
				.setParameter("uuid", missionToPersist.getUuid())
				.getSingleResult();
		assertEquals(missionToPersist, manuallyRetrievedMission);
	}
	
	@Test
	public void testDelete() {
		System.out.println("Perform testDelete in MissionDAOTest");
		Mission missionToDelete = ModelFactory.mission();
		missionToDelete.setName("testMission3");
		entityManager.persist(missionToDelete);
		missionDao.delete(missionToDelete);
		List<Mission> manuallyRetrievedMission =  entityManager.
				createQuery("FROM Mission WHERE uuid = :uuid", Mission.class)
				.setParameter("uuid", missionToDelete.getUuid())
				.getResultList();
		assertTrue(manuallyRetrievedMission.isEmpty());
	}
	
	@Test
	public void testGetCompleted() {
		System.out.println("Perform testGetCompleted in MissionDAOTest");
		Mission mission1 = ModelFactory.mission();
		mission1.setName("testmission1");
		mission1.setAccomplished(true);
		
		Mission mission2 = ModelFactory.mission();
		mission2.setName("testmission2");
		
		entityManager.persist(mission1);
		entityManager.persist(mission2);
		
		List<Mission> result = missionDao.getCompletedMissions();
		assertEquals(result.size(), 1);
		assertTrue(result.get(0).getAccomplished());
	}
	
}