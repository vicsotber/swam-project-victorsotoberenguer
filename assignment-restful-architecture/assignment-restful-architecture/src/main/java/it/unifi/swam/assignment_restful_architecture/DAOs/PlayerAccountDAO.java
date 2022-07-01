package it.unifi.swam.assignment_restful_architecture.DAOs;

import java.io.Serializable;
import java.util.List;

import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.Default;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import it.unifi.swam.assignment_restful_architecture.Model.Player.PlayerAccount;

@SessionScoped
@Default
public class PlayerAccountDAO implements Serializable {
	static final long serialVersionUID = 1L;
	
	@PersistenceContext
	private EntityManager entityManager;
	
	public void save(PlayerAccount playeracc) {
		if(playeracc.getId() != null) {
			entityManager.merge(playeracc);
		}else {
			entityManager.persist(playeracc);
		}
	}
	
	public void delete(PlayerAccount playeracc) {
		this.entityManager.remove(
				this.entityManager.contains(playeracc) ? playeracc : 
				this.entityManager.merge(playeracc));
	}
	
	public PlayerAccount findById(Long id) {
		return entityManager.find(PlayerAccount.class, id);
	}
	
	public List<PlayerAccount> getAllPlayers() {
		return this.entityManager.createQuery(
				"FROM PlayerAccount", PlayerAccount.class)
				.getResultList();
	}
	
	public PlayerAccount getPlayerByEmail(String email) {
		List<PlayerAccount> result = this.entityManager.createQuery(
				"FROM PlayerAccount WHERE email = :email", PlayerAccount.class)
		.setParameter("email", email)
		.getResultList();
		
		if(result.isEmpty()) {
			return null;
		}else {
			return result.get(0);
		}
	}
	
	public PlayerAccount getPlayerByUsername(String username) {
		List<PlayerAccount> result = this.entityManager.createQuery(
				"FROM PlayerAccount WHERE username = :username", PlayerAccount.class)
		.setParameter("username", username)
		.getResultList();
		
		if(result.isEmpty()) {
			return null;
		}else {
			return result.get(0);
		}
	}
	
}