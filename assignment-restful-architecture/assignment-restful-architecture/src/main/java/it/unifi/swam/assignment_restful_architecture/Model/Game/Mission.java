package it.unifi.swam.assignment_restful_architecture.Model.Game;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;

import it.unifi.swam.assignment_restful_architecture.Model.BaseEntity;

@Entity
public class Mission extends BaseEntity{
	@Column(nullable = false, unique = true)
	private String name;
	
	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinTable(name="mission_tasks",joinColumns=@JoinColumn(name="missionId"),inverseJoinColumns=@JoinColumn(
	        name = "taskId", 
	        foreignKey = @ForeignKey(
	            name = "FK_TASKID",
	            foreignKeyDefinition = "FOREIGN KEY (taskId) REFERENCES task(id) ON DELETE CASCADE"
	    )))
	@OrderBy
	private List<Task> tasks;
	
	private Boolean accomplished;
	
	Mission() {}
	
	public Mission(String uuid, String name, List<Task> tasks) {
		super(uuid);
		this.name = name;
		this.tasks = tasks;
		this.setAccomplished(false);
	}

	public void addTask(Task task) {
		this.tasks.add(task);
	}
	
	public String getName() {
		return this.name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public List<Task> getTasks() {
		if(tasks==null) {
			return null;
		}else {
			return new ArrayList<Task>(tasks);
		}
	}
	
	public void setTasks(List<Task> tasks) {
		this.tasks = new ArrayList<Task>(tasks);
	}


	public Boolean getAccomplished() {
		return accomplished;
	}


	public void setAccomplished(Boolean accomplished) {
		this.accomplished = accomplished;
	}

	public void removeTask(Task task) {
		this.tasks.remove(task);
	}
}