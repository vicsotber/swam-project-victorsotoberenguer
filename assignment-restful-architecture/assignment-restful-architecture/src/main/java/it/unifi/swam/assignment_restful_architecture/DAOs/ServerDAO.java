package it.unifi.swam.assignment_restful_architecture.DAOs;

import java.io.Serializable;
import java.util.List;

import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.Default;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import it.unifi.swam.assignment_restful_architecture.Model.Company.Server;

@SessionScoped
@Default
public class ServerDAO implements Serializable {
	static final long serialVersionUID = 1L;
	
	@PersistenceContext
	private EntityManager entityManager;
	
	public void save(Server server) {
		if(server.getId() != null) {
			entityManager.merge(server);
		}else {
			entityManager.persist(server);
		}
	}
	
	public void delete(Server server) {
		this.entityManager.remove(
				this.entityManager.contains(server) ? server : 
				this.entityManager.merge(server));
	}
	
	public Server findById(Long id) {
		return entityManager.find(Server.class, id);
	}
	
	public List<Server> getActiveServers() {
		List<Server> result = this.entityManager.createQuery(
				"FROM Server WHERE active = 1", Server.class)
		.getResultList();
		
		if(result.isEmpty()) {
			return null;
		}else {
			return result;
		}
	}
	
}