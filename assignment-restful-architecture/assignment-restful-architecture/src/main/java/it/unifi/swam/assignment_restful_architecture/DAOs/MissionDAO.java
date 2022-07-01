package it.unifi.swam.assignment_restful_architecture.DAOs;

import java.io.Serializable;
import java.util.List;

import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.Default;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import it.unifi.swam.assignment_restful_architecture.Model.Game.Mission;

@SessionScoped
@Default
public class MissionDAO implements Serializable {
	static final long serialVersionUID = 1L;
	
	@PersistenceContext
	private EntityManager entityManager;
	
	public void save(Mission mission) {
		if(mission.getId() != null) {
			entityManager.merge(mission);
		}else {
			entityManager.persist(mission);
		}
	}
	
	public void delete(Mission mission) {
		this.entityManager.remove(
				this.entityManager.contains(mission) ? mission : 
				this.entityManager.merge(mission));
	}
	
	public Mission findById(Long id) {
		return entityManager.find(Mission.class, id);
	}
	
	public List<Mission> getAllMissions() {
		return this.entityManager.createQuery(
				"FROM Mission",Mission.class)
				.getResultList();
	}
	
	public List<Mission> getCompletedMissions() {
		return this.entityManager.createQuery(
				"FROM Mission WHERE accomplished=1",Mission.class)
				.getResultList();
	}
	
}