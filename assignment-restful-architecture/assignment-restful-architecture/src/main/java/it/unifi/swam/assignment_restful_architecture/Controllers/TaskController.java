package it.unifi.swam.assignment_restful_architecture.Controllers;

import java.io.Serializable;
import java.util.List;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import it.unifi.swam.assignment_restful_architecture.DAOs.TaskDAO;
import it.unifi.swam.assignment_restful_architecture.Model.ModelFactory;
import it.unifi.swam.assignment_restful_architecture.Model.Game.Task;

@SessionScoped
@Named
public class TaskController implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Inject
	TaskDAO taskDao;

	public Task getById(Long id) {
		if(id==null) {
			throw new IllegalArgumentException("Id cannot be null");
		}else {
			return taskDao.findById(id);
		}
	}

	public Task saveTask(Task task) {
		Task taskToPersist = ModelFactory.task();
		
		taskToPersist.setDescription(task.getDescription());
		taskToPersist.setPlace(task.getPlace());
		taskToPersist.setTime(task.getTime());
		
		taskDao.save(taskToPersist);
		return taskToPersist;
	}

	public void updateTask(Task taskToUpdate, Task updates) {		
		if(updates.getDescription()!=taskToUpdate.getDescription() && updates.getDescription()!=null) {
			taskToUpdate.setDescription(updates.getDescription());
		}
		
		if(updates.getPlace()!=taskToUpdate.getPlace() && updates.getPlace()!=null) {
			taskToUpdate.setPlace(updates.getPlace());
		}
		
		if(updates.getTime()!=taskToUpdate.getTime() && updates.getTime()!=null) {
			taskToUpdate.setTime(updates.getTime());
		}
		
		if(updates.getAccomplished()!=taskToUpdate.getAccomplished() && updates.getAccomplished()!=null) {
			taskToUpdate.setAccomplished(updates.getAccomplished());
		}
		
		taskDao.save(taskToUpdate);
	}

	public void delete(Task task) {
		taskDao.delete(task);
	}

	public Boolean accomplish(Task task) {
		task.setAccomplished(true);
		return task.getAccomplished();
	}
	
	public List<Task> getAllTasks() {
		return taskDao.searchAllTasks();
	}

}
