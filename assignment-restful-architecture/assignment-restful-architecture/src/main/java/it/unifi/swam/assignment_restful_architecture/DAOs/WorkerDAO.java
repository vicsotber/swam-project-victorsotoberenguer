package it.unifi.swam.assignment_restful_architecture.DAOs;

import java.io.Serializable;
import java.util.List;

import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.Default;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import it.unifi.swam.assignment_restful_architecture.Model.Company.Worker;
import it.unifi.swam.assignment_restful_architecture.Model.Company.WorkerRol;

@SessionScoped
@Default
public class WorkerDAO implements Serializable {
	static final long serialVersionUID = 1L;
	
	@PersistenceContext
	private EntityManager entityManager;
	
	public void save(Worker worker) {
		if(worker.getId() != null) {
			entityManager.merge(worker);
		}else {
			entityManager.persist(worker);
		}
	}
	
	public void delete(Worker worker) {
		this.entityManager.remove(
				this.entityManager.contains(worker) ? worker : 
				this.entityManager.merge(worker));
	}
	
	public Worker findById(Long id) {
		return entityManager.find(Worker.class, id);
	}
	
	public List<Worker> getAllWorkers() {
		return this.entityManager.createQuery(
				"FROM Worker", Worker.class)
				.getResultList();
	}
	
	public List<Worker> findWorkerByName(String name) {
		List<Worker> result = this.entityManager.createQuery(
				"FROM Worker WHERE name = :name", Worker.class)
		.setParameter("name", name)
		.getResultList();
		
		if(result.isEmpty()) {
			return null;
		}else {
			return result;
		}
	}
	
	public Worker getWorkerByEmail(String email) {
		List<Worker> result = this.entityManager.createQuery(
				"FROM Worker WHERE email = :email", Worker.class)
		.setParameter("email", email)
		.getResultList();
		
		if(result.isEmpty()) {
			return null;
		}else {
			return result.get(0);
		}
	}
	
	public List<Worker> getWorkersByRol(WorkerRol rol) {
		List<Worker> result = this.entityManager.createQuery(
				"FROM Worker WHERE workerRol = :rol", Worker.class)
		.setParameter("rol", rol)
		.getResultList();
		
		return result;
	}
}