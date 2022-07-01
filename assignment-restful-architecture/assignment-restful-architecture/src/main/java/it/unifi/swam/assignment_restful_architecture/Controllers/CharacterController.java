package it.unifi.swam.assignment_restful_architecture.Controllers;

import java.io.Serializable;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import it.unifi.swam.assignment_restful_architecture.DAOs.CharacterDAO;
import it.unifi.swam.assignment_restful_architecture.Model.ModelFactory;
import it.unifi.swam.assignment_restful_architecture.Model.Game.Character;
import it.unifi.swam.assignment_restful_architecture.Model.Game.Mission;

@SessionScoped
@Named
public class CharacterController implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Inject
	CharacterDAO characterDao;

	public Character getById(Long id) {
		if(id==null) {
			throw new IllegalArgumentException("Id cannot be null");
		}else {
			return characterDao.findById(id);
		}
	}

	public Character saveCharacter(Character character) {
		Character characterToPersist = ModelFactory.character();
		
		characterToPersist.setLevel(character.getLevel());
		
		characterDao.save(characterToPersist);
		return characterToPersist;
	}

	public void updateCharacter(Character characterToUpdate, Character updates) {
		if(updates.getLevel()!=characterToUpdate.getLevel()) {
			characterToUpdate.setLevel(updates.getLevel());
		}
		
		characterDao.save(characterToUpdate);
	}

	public void delete(Character character) {
		characterDao.delete(character);
	}

	public Boolean acceptMission(Character character, Mission mission) {
		if(character.getAcceptedMissions().contains(mission)) {
			return false;
		}else {
			character.acceptMission(mission);
			return true;
		}	
	}

	public Boolean unacceptMission(Character character, Mission mission) {
		if(character.getAcceptedMissions().contains(mission)) {
			character.deleteAcceptedMission(mission);
			return true;
		}else {
			return false;
		}
	}

	public void levelUp(Character character) {
		character.levelUp();
	}

}
