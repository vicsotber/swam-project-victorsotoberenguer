package it.unifi.swam.assignment_restful_architecture.Model.Company;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;

import it.unifi.swam.assignment_restful_architecture.Model.BaseEntity;

@Entity
public class Sector extends BaseEntity{
	@OneToOne(cascade = CascadeType.PERSIST)
	@JoinColumn(
	        name = "leader", 
	        foreignKey = @ForeignKey(
	            name = "FK_LEADER",
	            foreignKeyDefinition = "FOREIGN KEY (leader) REFERENCES worker(id) ON DELETE SET NULL"
	    ))
	private Worker leader;
	
	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinTable(name="sector_workers",joinColumns=@JoinColumn(
	        name = "sectorId", 
	        foreignKey = @ForeignKey(
	            name = "FK_SECTORID",
	            foreignKeyDefinition = "FOREIGN KEY (sectorId) REFERENCES sector(id) ON DELETE CASCADE"
	    )),inverseJoinColumns=@JoinColumn(
	        name = "workerId", 
	        foreignKey = @ForeignKey(
	            name = "FK_WORKERID",
	            foreignKeyDefinition = "FOREIGN KEY (workerId) REFERENCES worker(id) ON DELETE CASCADE"
	    )))
	@OrderBy
	private List<Worker> workers;
	
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private SectorType type;
	
	Sector() {}
	
	public Sector(String uuid, SectorType type) {
		super(uuid);
		this.leader = null;
		this.workers = new ArrayList<Worker>();
		this.type = type;
	}
	
	public Sector(Worker leader, SectorType type) {
		this.setLeader(leader);
		List<Worker> workers = new ArrayList<Worker>();
		workers.add(leader);
		this.setWorkers(workers);
		this.type =type;
	}
	
	public Sector(Sector sector) {
		this.setLeader(sector.leader);
		this.setWorkers(sector.workers);
		this.setType(sector.type);
	}

	public Worker getLeader() {
		return leader;
	}

	public void setLeader(Worker leader) {
		if(leader == null) {
			this.leader = null;
		}else {
			this.leader = leader;
		}
	}

	public List<Worker> getWorkers() {
		if(workers==null) {
			return null;
		}else {
			return new ArrayList<Worker>(workers);
		}
	}

	public void setWorkers(List<Worker> workers) {
		this.workers = new ArrayList<Worker>(workers);
	}

	public SectorType getType() {
		return type;
	}

	public void setType(SectorType type) {
		this.type = type;
	}
	
	public void addWorker(Worker worker) {
		this.workers.add(worker);
	}
	
	public int getNumWorkers() {
		return getWorkers().size();
	}

	public void deleteWorker(Worker worker) {
		this.workers.remove(worker);
	}
}
