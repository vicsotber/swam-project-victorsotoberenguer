package it.unifi.swam.assignment_restful_architecture.Model.Company;

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
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;

import it.unifi.swam.assignment_restful_architecture.Model.BaseEntity;

@Entity
public class Company extends BaseEntity{
	@Column(nullable = false, unique = true)
	private String name;
	
	//could be embedded but worker can be useful
	@OneToOne(cascade = CascadeType.PERSIST)
	@JoinColumn(
	        name = "president", 
	        foreignKey = @ForeignKey(
	            name = "FK_PRESIDENT",
	            foreignKeyDefinition = "FOREIGN KEY (president) REFERENCES worker(id) ON DELETE SET NULL"
	    ))
	private Worker president;
	
	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinTable(name="company_sectors",joinColumns=@JoinColumn(name="companyId"),inverseJoinColumns=@JoinColumn(name="sectorId"))
	@OrderBy
	private List<Sector> sectors;
	
	Company() {}
	
	public Company(String uuid,Worker president,String name) {
		super(uuid);
		this.president = president;
		this.sectors = new ArrayList<Sector>();
		this.name = name;
	}
	
	public void addSector(Sector sector) {
		sectors.add(sector);
	}
	
	public List<Sector> getSectors() {
		if(sectors==null) {
			return null;
		}else {
			return new ArrayList<Sector>(sectors);
		}
	}
	
	public void setSectors(List<Sector> sectors) {
		this.sectors = new ArrayList<Sector>(sectors);
	}

	public Worker getPresident() {
		return president;
	}

	public void setPresident(Worker president) {
		this.president = president;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void deleteSector(Sector sector) {
		this.sectors.remove(sector);
	}

}