package it.unifi.swam.assignment_restful_architecture.Model.Game;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import it.unifi.swam.assignment_restful_architecture.Model.BaseEntity;

//Character is a reserved name in SQL
@Entity
@Table(name = "playerCharacter")
public class Character extends BaseEntity{
	private Integer level;
	
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name="characters_missions",joinColumns=@JoinColumn(name="characterId"),inverseJoinColumns=@JoinColumn(
	        name = "missionId", 
	        foreignKey = @ForeignKey(
	            name = "FK_MISSIONID",
	            foreignKeyDefinition = "FOREIGN KEY (missionId) REFERENCES mission(id) ON DELETE CASCADE"
	    )))
	@OrderBy
	private List<Mission> acceptedMissions;
	
	Character() {}
	
	public Character(String uuid) {
		super(uuid);
		this.level = 1;
		this.acceptedMissions = new ArrayList<Mission>();
	}
	
	public Character(String uuid, Character character) {
		super(uuid);
		this.level = character.level;
		this.acceptedMissions = character.acceptedMissions;
	}
	
	public int getLevel() {
		return level;
	}
	
	public void setLevel(Integer level) {
		this.level = level;
	}

	public void levelUp() {
		this.level = this.level+1;
	}
	
	public List<Mission> getAcceptedMissions() {
		return new ArrayList<Mission>(acceptedMissions);
	}
	
	public void setAcceptedMissions(List<Mission> acceptedMissions) {
		this.acceptedMissions = new ArrayList<Mission>(acceptedMissions);
	}
	
	public void acceptMission(Mission mission) {
		this.acceptedMissions.add(mission);
	}
	
	public void deleteAcceptedMission(Mission mission) {
		this.acceptedMissions.remove(mission);
	}
}