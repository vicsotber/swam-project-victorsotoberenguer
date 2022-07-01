package it.unifi.swam.assignment_restful_architecture.DAOs;

import java.io.Serializable;
import java.util.List;

import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.Default;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import it.unifi.swam.assignment_restful_architecture.Model.Company.Sector;
import it.unifi.swam.assignment_restful_architecture.Model.Company.Worker;

@SessionScoped
@Default
public class SectorDAO implements Serializable {
	static final long serialVersionUID = 1L;
	
	@PersistenceContext
	private EntityManager entityManager;
	
	public void save(Sector sector) {
		if(sector.getId() != null) {
			entityManager.merge(sector);
		}else {
			entityManager.persist(sector);
		}
	}
	
	public void delete(Sector sector) {
		this.entityManager.remove(
				this.entityManager.contains(sector) ? sector : 
				this.entityManager.merge(sector));
	}
	
	public Sector findById(Long id) {
		return entityManager.find(Sector.class, id);
	}
	
	public Sector getSectorByLeader(Worker leader) {
		List<Sector> result = this.entityManager.createQuery(
				"FROM Sector WHERE leader = :leader", Sector.class)
		.setParameter("leader", leader)
		.getResultList();
		
		if(result.isEmpty()) {
			return null;
		}else {
			return result.get(0);
		}
	}
	
	//public Sector getSectorsByNumWorkers (Integer num) {
	//	List<Sector> result = this.entityManager.createQuery(
	//			"FROM Sector WHERE leader = :leader", Sector.class)
	//	.setParameter("leader", leader)
	//	.getResultList();
		
	//	if(result.isEmpty()) {
	//		return null;
	//	}else {
	//		return result.get(0);
	//	}
	//}
}