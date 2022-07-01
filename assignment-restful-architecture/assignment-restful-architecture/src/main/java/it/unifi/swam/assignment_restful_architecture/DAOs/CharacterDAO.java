package it.unifi.swam.assignment_restful_architecture.DAOs;

import java.io.Serializable;

import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.Default;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import it.unifi.swam.assignment_restful_architecture.Model.Game.Character;

@SessionScoped
@Default
public class CharacterDAO implements Serializable {
	static final long serialVersionUID = 1L;
	
	@PersistenceContext
	private EntityManager entityManager;
	
	public void save(Character character) {
		if(character.getId() != null) {
			entityManager.merge(character);
		}else {
			entityManager.persist(character);
		}
	}
	
	public void delete(Character character) {
		this.entityManager.remove(
				this.entityManager.contains(character) ? character : 
				this.entityManager.merge(character));
	}
	
	public Character findById(Long id) {
		return entityManager.find(Character.class, id);
	}
	
	
}