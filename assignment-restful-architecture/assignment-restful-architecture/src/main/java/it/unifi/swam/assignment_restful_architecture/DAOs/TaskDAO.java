package it.unifi.swam.assignment_restful_architecture.DAOs;

import java.io.Serializable;
import java.util.List;

import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.Default;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import it.unifi.swam.assignment_restful_architecture.Model.Game.Task;

@SessionScoped
@Default
public class TaskDAO implements Serializable {
	static final long serialVersionUID = 1L;
	
	@PersistenceContext
	private EntityManager entityManager;
	
	public void save(Task task) {
		if(task.getId() != null) {
			entityManager.merge(task);
		}else {
			entityManager.persist(task);
		}
	}
	
	public void delete(Task task) {
		this.entityManager.remove(
				this.entityManager.contains(task) ? task : 
				this.entityManager.merge(task));
	}
	
	public Task findById(Long id) {
		return entityManager.find(Task.class, id);
	}
	
	public List<Task> searchAllTasks() {
		List<Task> result = this.entityManager.createQuery(
				"FROM Task", Task.class)
		.getResultList();
		
		return result;
	}
	
	public List<Task> searchTasksByPlace(String place) {
		List<Task> result = this.entityManager.createQuery(
				"FROM Task WHERE place = :place", Task.class)
		.setParameter("place", place)
		.getResultList();
		
		if(result.isEmpty()) {
			return null;
		}else {
			return result;
		}
	}
	
	
}