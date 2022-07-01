package it.unifi.swam.assignment_restful_architecture.Controllers;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import it.unifi.swam.assignment_restful_architecture.DAOs.SectorDAO;
import it.unifi.swam.assignment_restful_architecture.Model.ModelFactory;
import it.unifi.swam.assignment_restful_architecture.Model.Company.Sector;
import it.unifi.swam.assignment_restful_architecture.Model.Company.Worker;

@SessionScoped
@Named
public class SectorController implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Inject
	SectorDAO sectorDao;

	public Sector getById(Long id) {
		if(id==null) {
			throw new IllegalArgumentException("Id cannot be null");
		}else {
			return sectorDao.findById(id);
		}
	}

	public Sector saveSector(Sector sector) {
		Sector sectorToPersist = ModelFactory.sector();
		
		sectorToPersist.setType(sector.getType());
		
		if(sector.getWorkers()==null || sector.getWorkers().isEmpty()) {
			List<Worker> workers = new ArrayList<Worker>();
			sectorToPersist.setWorkers(workers);
		}else {
			List<Worker> workers = new ArrayList<Worker>(sector.getWorkers());
			sectorToPersist.setWorkers(workers);
		}
		
		if(sector.getLeader()==null) {
			sectorToPersist.setLeader(null);
		}else {
			Worker leader = ModelFactory.worker();
			leader.setName(sector.getLeader().getName());
			sectorToPersist.setLeader(leader);
			sectorToPersist.addWorker(leader);
		}
		
		sectorDao.save(sectorToPersist);
		return sectorToPersist;
	}

	public void updateSector(Sector sectorToUpdate, Sector updates) {
		if(updates.getType()!=sectorToUpdate.getType() && updates.getType()!=null) {
			sectorToUpdate.setType(updates.getType());
		}
		
		if(updates.getLeader()!=sectorToUpdate.getLeader() && updates.getLeader()!=null) {
			Worker leader = ModelFactory.worker();
			leader.setName(updates.getLeader().getName());
			sectorToUpdate.setLeader(leader);
			sectorToUpdate.addWorker(leader);
		}
		
		sectorDao.save(sectorToUpdate);
	}

	public void delete(Sector sector) {
		sectorDao.delete(sector);
	}

	public Boolean setLeader(Sector sector, Worker worker) {
		sector.setLeader(worker);
		addWorker(sector, worker);
		if(sector.getLeader().equals(worker)) {
			return true;
		}else {
			return false;
		}
	}

	public Boolean addWorker(Sector sector, Worker worker) {
		if(sector.getWorkers().contains(worker)) {
			return false;
		}else {
			sector.addWorker(worker);
			return true;
		}
	}

	public Boolean deleteWorker(Sector sector, Worker worker) {
		if(sector.getWorkers().contains(worker)) {
			sector.deleteWorker(worker);
			if(sector.getLeader()!=null && sector.getLeader().equals(worker)) {
				sector.setLeader(null);
			}
			return true;
		}else {
			return false;
		}
	}

}
