package it.unifi.swam.assignment_restful_architecture.Model.Company;

import javax.persistence.Column;
import javax.persistence.Entity;

import it.unifi.swam.assignment_restful_architecture.Model.BaseEntity;

@Entity
public class Server extends BaseEntity{
	@Column(nullable = false, unique = true)
	private String name;
	private Boolean active;
	
	Server () {}
	
	public Server(String uuid, String name) {
		super(uuid);
		this.name = name;
		this.active = true;
	}
	
	public Boolean getActive() {
		return this.active;
	}
	
	public void setActive(Boolean active) {
		this.active = active;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	
}
