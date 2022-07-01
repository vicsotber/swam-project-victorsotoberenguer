package it.unifi.swam.assignment_restful_architecture.Model.Game;

import javax.persistence.Column;
import javax.persistence.Entity;

import it.unifi.swam.assignment_restful_architecture.Model.BaseEntity;

@Entity
public class Task extends BaseEntity{
	@Column(nullable = false, unique = true)
	private String description;
	@Column(nullable = false)
	private String place;
	@Column(nullable = false)
	private Integer time;
	private Boolean accomplished;
	
	Task() {}
	
	public Task(String uuid, String description, String place, Integer time) {
		super(uuid);
		this.description = description;
		this.place = place;
		this.time = time;
		this.accomplished = false;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getPlace() {
		return place;
	}

	public void setPlace(String place) {
		this.place = place;
	}

	public Integer getTime() {
		return time;
	}

	public void setTime(Integer time) {
		this.time = time;
	}

	public Boolean getAccomplished() {
		return accomplished;
	}

	public void setAccomplished(Boolean accomplished) {
		this.accomplished = accomplished;
	}
	
	
	
}