package it.unifi.swam.assignment_restful_architecture.Controllers;

import java.io.Serializable;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import it.unifi.swam.assignment_restful_architecture.DAOs.CompanyDAO;
import it.unifi.swam.assignment_restful_architecture.Model.ModelFactory;
import it.unifi.swam.assignment_restful_architecture.Model.Company.Company;
import it.unifi.swam.assignment_restful_architecture.Model.Company.Sector;
import it.unifi.swam.assignment_restful_architecture.Model.Company.Worker;

@SessionScoped
@Named
public class CompanyController implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Inject
	CompanyDAO companyDao;
	
	@Inject
	SectorController sectorController;
	
	
	public Company getById(Long id) {
		if(id==null) {
			throw new IllegalArgumentException("Id cannot be null");
		}else {
			return companyDao.findById(id);
		}
	}
	
	public Company saveCompany(Company company) {
		Company companyToPersist = ModelFactory.company();
		
		companyToPersist.setName(company.getName());
		
		if(company.getPresident()==null) {
			companyToPersist.setPresident(null);
		}else {
			Worker president = ModelFactory.worker();
			president.setName(company.getPresident().getName());
			president.setEmail(company.getPresident().getEmail());
			companyToPersist.setPresident(president);
		}
		
//		if(company.getSectors()==null || company.getSectors().isEmpty()) {
//			List<Sector> sectors = new ArrayList<Sector>();
//			companyToPersist.setSectors(sectors);
//		}else {
//			List<Sector> sectors = new ArrayList<Sector>(company.getSectors());
//			companyToPersist.setSectors(sectors);
//		}
		
		companyDao.save(companyToPersist);
		return companyToPersist;
	}
	
	public void delete(Company company) {
		companyDao.delete(company);
	}

	public void updateCompany(Company companyToUpdate, Company updates) {
		if(updates.getName()!=companyToUpdate.getName() && updates.getName()!=null) {
			companyToUpdate.setName(updates.getName());
		}
		
		if(updates.getPresident()!=companyToUpdate.getPresident() && updates.getPresident()!=null) {
			Worker president = ModelFactory.worker();
			president.setName(updates.getPresident().getName());
			president.setEmail(updates.getPresident().getEmail());
			companyToUpdate.setPresident(president);
		}
		
		companyDao.save(companyToUpdate);
	}

	public Boolean setPresident(Company company, Worker worker) {
		company.setPresident(worker);
		if(company.getPresident().equals(worker)) {
			return true;
		}else {
			return false;
		}
	}

	public Sector addSector(Company company, Sector sector) {
		Sector persistedSector = sectorController.saveSector(sector);
		
		company.addSector(persistedSector);
		return persistedSector;
	}

	public Boolean deleteSector(Company company, Sector sector) {
		if(company.getSectors().contains(sector)) {
			company.deleteSector(sector);
			sectorController.delete(sector);
			return true;
		}else {
			return false;
		}
	}
}