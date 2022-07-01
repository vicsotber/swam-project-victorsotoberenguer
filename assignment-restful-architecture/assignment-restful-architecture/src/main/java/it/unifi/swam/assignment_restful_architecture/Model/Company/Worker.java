package it.unifi.swam.assignment_restful_architecture.Model.Company;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import org.springframework.security.crypto.bcrypt.BCrypt;

import it.unifi.swam.assignment_restful_architecture.Model.BaseEntity;

@Entity
public class Worker extends BaseEntity {
	@Column(nullable = false)
	private String name;
	
	@Column(nullable = false, unique = true)
	private String email;
	
	@Column(nullable = false)
	private String password;
	
	@Enumerated(EnumType.STRING)
	private WorkerRol workerRol;
	
	Worker() {}
	
	public Worker(String uuid, String name, String email, String password) {
		super(uuid);
		this.name = name;
		this.email = email;
		this.password = password;
		this.setWorkerRol(null);
	}
	
	public Worker(Worker worker) {
		super(worker.getUuid());
		this.name = worker.name;
	}

	public String getName() {
		return this.name;
	}
	
	public void setName(String name) {
		this.name=name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	private void setPassword(String password) {
		String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt(12));
		this.password = hashedPassword;
	}
	
	public void setPassword(String password, Boolean encrypted) {
		if(encrypted) {
			this.password = password;
		}else {
			setPassword(password);
		}
	}

	public WorkerRol getWorkerRol() {
		return workerRol;
		
	}

	public void setWorkerRol(WorkerRol workerRol) {
		this.workerRol = workerRol;
	}	
}