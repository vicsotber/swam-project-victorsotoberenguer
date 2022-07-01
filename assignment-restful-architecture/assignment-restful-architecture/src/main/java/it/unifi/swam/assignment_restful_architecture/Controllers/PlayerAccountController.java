package it.unifi.swam.assignment_restful_architecture.Controllers;

import java.io.Serializable;
import java.util.List;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import it.unifi.swam.assignment_restful_architecture.DAOs.PlayerAccountDAO;
import it.unifi.swam.assignment_restful_architecture.Model.ModelFactory;
import it.unifi.swam.assignment_restful_architecture.Model.Player.PlayerAccount;
import it.unifi.swam.assignment_restful_architecture.Model.Game.Character;

@SessionScoped
@Named
public class PlayerAccountController implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Inject
	PlayerAccountDAO playeraccDao;
	
	@Inject
	CharacterController characterController;

	public PlayerAccount getById(Long id) {
		if(id==null) {
			throw new IllegalArgumentException("Id cannot be null");
		}else {
			return playeraccDao.findById(id);
		}
	}

	public PlayerAccount savePlayer(PlayerAccount playeracc, Boolean encrypted) {
		PlayerAccount playeraccToPersist = ModelFactory.playeracc();
		playeraccToPersist.setUsername(playeracc.getUsername());
		playeraccToPersist.setEmail(playeracc.getEmail());
		playeraccToPersist.setPassword(playeracc.getPassword(), encrypted);
		
		if(playeracc.getCharacter()==null) {
			playeraccToPersist.setCharacter(null);
		}else {
			Character character =  ModelFactory.character();
			character.setLevel(character.getLevel());
			playeraccToPersist.setCharacter(character);
		}
		
		playeraccDao.save(playeraccToPersist);
		return playeraccToPersist;
	}

	public void updatePlayerAccount(PlayerAccount playeraccToUpdate, PlayerAccount updates, Boolean encrypted) {
		if(updates.getEmail()!=playeraccToUpdate.getEmail() && updates.getEmail()!=null) {
			playeraccToUpdate.setEmail(updates.getEmail());
		}
		
		if(updates.getUsername()!=playeraccToUpdate.getUsername() && updates.getUsername()!=null) {
			playeraccToUpdate.setUsername(updates.getUsername());
		}
		
		if(updates.getPassword()!=playeraccToUpdate.getPassword() && updates.getPassword()!=null) {
			playeraccToUpdate.setPassword(updates.getPassword(), encrypted);
		}
		
		playeraccDao.save(playeraccToUpdate);
	}

	public void delete(PlayerAccount playeracc) {
		playeraccDao.delete(playeracc);
	}

	public void followAccount(PlayerAccount playeracc, PlayerAccount playeracc2) {
		playeracc.followFriend(playeracc2);
	}

	public Boolean unfollowAccount(PlayerAccount playeracc, PlayerAccount playeracc2) {
		if(playeracc.getFriends().contains(playeracc2)) {
			playeracc.unfollowFriend(playeracc2);
			return true;
		}else {
			return false;
		}
	}

	public Character createCharacter(PlayerAccount playeracc) {
		if(playeracc.getCharacter()!=null) {
			characterController.delete(playeracc.getCharacter());
		}
		
		Character character = ModelFactory.character();
		Character persistedCharacter = characterController.saveCharacter(character);
		playeracc.setCharacter(persistedCharacter);
		return persistedCharacter;	
	}

	public List<PlayerAccount> getAll() {
		return playeraccDao.getAllPlayers();
	}
	
	public PlayerAccount getPlayerByEmail(String email) {
		return playeraccDao.getPlayerByEmail(email);
	}

}
