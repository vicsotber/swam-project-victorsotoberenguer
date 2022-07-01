package it.unifi.swam.assignment_restful_architecture.Controllers;

import java.io.Serializable;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import it.unifi.swam.assignment_restful_architecture.DAOs.MissionDAO;
import it.unifi.swam.assignment_restful_architecture.Model.ModelFactory;
import it.unifi.swam.assignment_restful_architecture.Model.Game.Mission;
import it.unifi.swam.assignment_restful_architecture.Model.Game.Task;

@SessionScoped
@Named
public class MissionController implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Inject
	MissionDAO missionDao;
	
	@Inject
	TaskController taskController;

	public Mission getById(Long id) {
		if(id==null) {
			throw new IllegalArgumentException("Id cannot be null");
		}else {
			return missionDao.findById(id);
		}
	}

	public Mission saveMisison(Mission mission) {
		Mission missionToPersist = ModelFactory.mission();
		
		missionToPersist.setName(mission.getName());
		
//		if(mission.getTasks()==null || mission.getTasks().isEmpty()) {
//			List<Task> tasks = new ArrayList<Task>();
//			missionToPersist.setTasks(tasks);
//		}else {
//			List<Task> tasks = new ArrayList<Task>(mission.getTasks());
//			missionToPersist.setTasks(tasks);
//		}
		
		missionDao.save(missionToPersist);
		return missionToPersist;
	}

	public void updateMission(Mission missionToUpdate, Mission updates){
		if(updates.getName()!=missionToUpdate.getName() && updates.getName()!=null) {
			missionToUpdate.setName(updates.getName());
		}
		
		if(updates.getAccomplished()!=missionToUpdate.getAccomplished() && updates.getAccomplished()!=null) {
			missionToUpdate.setAccomplished(updates.getAccomplished());
		}
		
		missionDao.save(missionToUpdate);
	}

	public void delete(Mission mission) {
		missionDao.delete(mission);
	}

	public Task addTask(Mission mission, Task task) {
		Task persistedTask = taskController.saveTask(task);
		
		mission.addTask(persistedTask);
		return persistedTask;
	}

	public Boolean unboundTask(Mission mission, Task task) {
		if(mission.getTasks().contains(task)) {
			mission.removeTask(task);
			taskController.delete(task);
			return true;
		}else {
			return false;
		}
	}

	public Boolean accomplish(Mission mission) {
		Boolean allTasksCompleted = true;
		for(Task t:mission.getTasks()) {
			if(t.getAccomplished()==false) {
				allTasksCompleted = false;
				break;
			}
		}
		if(allTasksCompleted) {
			mission.setAccomplished(true);
			return true;
		}else {
			return false;
		}
	}

}
