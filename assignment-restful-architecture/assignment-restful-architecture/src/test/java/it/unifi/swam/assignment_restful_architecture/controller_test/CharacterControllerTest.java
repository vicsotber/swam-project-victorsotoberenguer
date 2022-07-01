package it.unifi.swam.assignment_restful_architecture.controller_test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.Before;
import org.junit.Test;

import it.unifi.swam.assignment_restful_architecture.Controllers.CharacterController;
import it.unifi.swam.assignment_restful_architecture.DAOs.CharacterDAO;
import it.unifi.swam.assignment_restful_architecture.Model.ModelFactory;
import it.unifi.swam.assignment_restful_architecture.Model.Game.Character;
import it.unifi.swam.assignment_restful_architecture.Model.Game.Mission;

public class CharacterControllerTest {
	private CharacterController characterController;
	private CharacterDAO characterDao;
	private Character fakeCharacter;
	
	@Before
	public void setup() throws IllegalAccessException {
		characterController = new CharacterController();
		
		characterDao = mock(CharacterDAO.class); //Mock instance (defines behaviour manually)
		
		fakeCharacter =  ModelFactory.character();
		
		FieldUtils.writeField(characterController, "characterDao", characterDao, true); //Mock instance injected in the controller manually
	}
	
	@Test
	public void testGetCharacter() {
		when(characterDao.findById(1L)).thenReturn(fakeCharacter);
		
		Character retrievedCharacter = characterController.getById(1L);
		assertEquals(retrievedCharacter.getLevel(), fakeCharacter.getLevel());
		assertEquals(retrievedCharacter, fakeCharacter);
	}
	
	@Test
	public void testLevelUp() {
		Integer previousLevel = fakeCharacter.getLevel();
		characterController.levelUp(fakeCharacter);
		
		assertEquals(fakeCharacter.getLevel(), previousLevel+1);
	}
	
	@Test
	public void testUpdateCharacter() {
		Character newCharacter = ModelFactory.character();
		newCharacter.setLevel(12);
		
		characterController.updateCharacter(fakeCharacter, newCharacter);
		assertEquals(fakeCharacter.getLevel(), newCharacter.getLevel());
		assertEquals(fakeCharacter.getLevel(), 12);
	}
	
	@Test
	public void testAcceptMission() {
		Mission fakeMission = ModelFactory.mission();
		fakeMission.setName("Fake Mission");
		
		characterController.acceptMission(fakeCharacter, fakeMission);
		assertEquals(fakeCharacter.getAcceptedMissions().size(), 1);
		assertEquals(fakeCharacter.getAcceptedMissions().get(0), fakeMission);
	}
	
	@Test
	public void testUnacceptMission() {
		Mission fakeMission = ModelFactory.mission();
		fakeMission.setName("Fake Mission");
		
		characterController.acceptMission(fakeCharacter, fakeMission);
		assertEquals(fakeCharacter.getAcceptedMissions().size(), 1);
		assertEquals(fakeCharacter.getAcceptedMissions().get(0), fakeMission);
		
		characterController.unacceptMission(fakeCharacter, fakeMission);
		assertEquals(fakeCharacter.getAcceptedMissions().size(), 0);
	}
}
