package it.unifi.swam.assignment_restful_architecture.controller_test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.Before;
import org.junit.Test;

import it.unifi.swam.assignment_restful_architecture.Controllers.PlayerAccountController;
import it.unifi.swam.assignment_restful_architecture.DAOs.PlayerAccountDAO;
import it.unifi.swam.assignment_restful_architecture.Model.ModelFactory;
import it.unifi.swam.assignment_restful_architecture.Model.Player.PlayerAccount;

public class PlayerAccountControllerTest {
	private PlayerAccountController playerController;
	private PlayerAccountDAO playerDao;
	private PlayerAccount fakePlayer;
	
	@Before
	public void setup() throws IllegalAccessException {
		playerController = new PlayerAccountController();
		
		playerDao = mock(PlayerAccountDAO.class); //Mock instance (defines behaviour manually)
		
		fakePlayer =  ModelFactory.playeracc();
		fakePlayer.setUsername("Fake Player Username");
		fakePlayer.setEmail("fakeplayeremail@gmail.com");
		fakePlayer.setPassword("Fake Player Password", false);
		
		FieldUtils.writeField(playerController, "playeraccDao", playerDao, true); //Mock instance injected in the controller manually
	}
	
	@Test
	public void testGetPlayer() {
		when(playerDao.findById(1L)).thenReturn(fakePlayer);
		
		PlayerAccount retrievedPlayer = playerController.getById(1L);
		assertEquals(retrievedPlayer.getUsername(), fakePlayer.getUsername());
		assertEquals(retrievedPlayer.getEmail(), fakePlayer.getEmail());
		assertEquals(retrievedPlayer.getPassword(), fakePlayer.getPassword());
		assertEquals(retrievedPlayer, fakePlayer);
	}
	
	@Test
	public void testGetAllPlayers() {
		PlayerAccount fakePlayer2 =  ModelFactory.playeracc();
		fakePlayer2.setUsername("Fake Player Username2");
		fakePlayer2.setEmail("fakeplayeremail2@gmail.com");
		fakePlayer2.setPassword("Fake Player Password2", false);
		List<PlayerAccount> players = new ArrayList<PlayerAccount>();
		players.add(fakePlayer);
		players.add(fakePlayer2);
		when(playerDao.getAllPlayers()).thenReturn(players);
		
		List<PlayerAccount> retrievedPlayers = playerController.getAll();
		assertEquals(retrievedPlayers.size(), 2);
		assertEquals(retrievedPlayers, players);
		assertEquals(retrievedPlayers.get(0), fakePlayer);
		assertEquals(retrievedPlayers.get(1), fakePlayer2);
	}
	
	@Test
	public void testUpdatePlayer() {
		PlayerAccount newPlayer = ModelFactory.playeracc();
		newPlayer.setUsername("Updated Username Player");
		
		playerController.updatePlayerAccount(fakePlayer, newPlayer,true);
		assertEquals(fakePlayer.getUsername(), newPlayer.getUsername());
		assertEquals(fakePlayer.getUsername(), "Updated Username Player");
	}
	
	@Test
	public void testFollowFriend() {
		PlayerAccount fakePlayer2 =  ModelFactory.playeracc();
		fakePlayer2.setUsername("Fake Player Username2");
		fakePlayer2.setEmail("fakeplayeremail2@gmail.com");
		fakePlayer2.setPassword("Fake Player Password2", false);
		
		playerController.followAccount(fakePlayer, fakePlayer2);
		assertEquals(fakePlayer.getFriends().size(), 1);
		assertEquals(fakePlayer.getFriends().get(0), fakePlayer2);
	}
	
	@Test
	public void testUnfollowFriend() {
		PlayerAccount fakePlayer2 =  ModelFactory.playeracc();
		fakePlayer2.setUsername("Fake Player Username2");
		fakePlayer2.setEmail("fakeplayeremail2@gmail.com");
		fakePlayer2.setPassword("Fake Player Password2", false);
		
		playerController.followAccount(fakePlayer, fakePlayer2);
		assertEquals(fakePlayer.getFriends().size(), 1);
		assertEquals(fakePlayer.getFriends().get(0), fakePlayer2);
		
		playerController.unfollowAccount(fakePlayer, fakePlayer2);
		assertEquals(fakePlayer.getFriends().size(), 0);
	}
}
