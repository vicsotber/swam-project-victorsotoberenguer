package it.unifi.swam.assignment_restful_architecture.controller_test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.Before;
import org.junit.Test;

import it.unifi.swam.assignment_restful_architecture.Controllers.MissionController;
import it.unifi.swam.assignment_restful_architecture.DAOs.MissionDAO;
import it.unifi.swam.assignment_restful_architecture.Model.ModelFactory;
import it.unifi.swam.assignment_restful_architecture.Model.Game.Mission;

public class MissionControllerTest {
	private MissionController missionController;
	private MissionDAO missionDao;
	private Mission fakeMission;
	
	@Before
	public void setup() throws IllegalAccessException {
		missionController = new MissionController();
		
		missionDao = mock(MissionDAO.class); //Mock instance (defines behaviour manually)
		
		fakeMission =  ModelFactory.mission();
		fakeMission.setName("Fake Mission Name");
		
		FieldUtils.writeField(missionController, "missionDao", missionDao, true); //Mock instance injected in the controller manually
	}
	
	@Test
	public void testGetMission() {
		when(missionDao.findById(1L)).thenReturn(fakeMission);
		
		Mission retrievedMission = missionController.getById(1L);
		assertEquals(retrievedMission.getName(), fakeMission.getName());
		assertEquals(retrievedMission.getAccomplished(), fakeMission.getAccomplished());
		assertEquals(retrievedMission, fakeMission);
	}
	
	@Test
	public void testAccomplishMission() {
		missionController.accomplish(fakeMission);
		
		assertTrue(fakeMission.getAccomplished());
	}
	
	@Test
	public void testUpdateMission() {
		Mission newMission = ModelFactory.mission();
		newMission.setName("Updated Mission Name");
		
		missionController.updateMission(fakeMission, newMission);
		assertEquals(fakeMission.getName(), newMission.getName());
		assertEquals(fakeMission.getName(), "Updated Mission Name");
	}
	
//	@Test
//	public void testAddTask() {
//		Task fakeTask =  ModelFactory.task();
//		fakeTask.setDescription("fake task description");
//		fakeTask.setPlace("fake task place");
//		fakeTask.setTime(30);
//		
//		missionController.addTask(fakeMission, fakeTask);
//		assertEquals(fakeMission.getTasks().size(), 1);
//	}
}
