package it.unifi.swam.assignment_restful_architecture.DAOs;

import java.io.Serializable;
import java.util.List;

import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.Default;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import it.unifi.swam.assignment_restful_architecture.Model.Company.Company;
import it.unifi.swam.assignment_restful_architecture.Model.Company.Worker;

@SessionScoped
@Default
public class CompanyDAO implements Serializable {
	static final long serialVersionUID = 1L;
	
	@PersistenceContext
	private EntityManager entityManager;
	
	public void save(Company company) {
		if(company.getId() != null) {
			entityManager.merge(company);
		}else {
			entityManager.persist(company);
		}
	}
	
	public void delete(Company company) {
		this.entityManager.remove(
				this.entityManager.contains(company) ? company : 
				this.entityManager.merge(company));
	}
	
	public Company findById(Long id) {
		return entityManager.find(Company.class, id);
	}
	
	public Company getCompanyWithPresident(Worker president) {
		List<Company> result = this.entityManager.createQuery(
				"FROM Company WHERE president = :president", Company.class)
		.setParameter("president", president)
		.getResultList();
		
		if(result.isEmpty()) {
			return null;
		}else {
			return result.get(0);
		}
	}
}