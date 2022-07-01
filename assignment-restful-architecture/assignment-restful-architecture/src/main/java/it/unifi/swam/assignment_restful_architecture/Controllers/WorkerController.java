package it.unifi.swam.assignment_restful_architecture.Controllers;

import java.io.Serializable;
import java.util.List;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import it.unifi.swam.assignment_restful_architecture.DAOs.WorkerDAO;
import it.unifi.swam.assignment_restful_architecture.Model.ModelFactory;
import it.unifi.swam.assignment_restful_architecture.Model.Company.Worker;

@SessionScoped
@Named
public class WorkerController implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Inject
	WorkerDAO workerDao;

	public Worker getById(Long id) {
		if(id==null) {
			throw new IllegalArgumentException("Id cannot be null");
		}else {
			return workerDao.findById(id);
		}
	}

	public Worker saveWorker(Worker worker, Boolean encrypted) {
		Worker workerToPersist = ModelFactory.worker();
		
		workerToPersist.setName(worker.getName());
		workerToPersist.setEmail(worker.getEmail());
		workerToPersist.setPassword(worker.getPassword(), encrypted);
		
		workerToPersist.setWorkerRol(worker.getWorkerRol());
		
		workerDao.save(workerToPersist);
		return workerToPersist;
	}

	public void updateWorker(Worker workerToUpdate, Worker updates, Boolean encrypted) {
		if(updates.getName()!=workerToUpdate.getName() && updates.getName()!=null) {
			workerToUpdate.setName(updates.getName());
		}
		
		if(updates.getEmail()!=workerToUpdate.getEmail() && updates.getEmail()!=null) {
			workerToUpdate.setEmail(updates.getEmail());
		}
		
		if(updates.getPassword()!=workerToUpdate.getPassword() && updates.getPassword()!=null) {
			workerToUpdate.setPassword(updates.getPassword(), encrypted);
		}
		
		if(updates.getWorkerRol()!=workerToUpdate.getWorkerRol() && updates.getWorkerRol()!=null) {
			workerToUpdate.setWorkerRol(updates.getWorkerRol());
		}
		
		workerDao.save(workerToUpdate);
	}

	public void delete(Worker worker) {
		workerDao.delete(worker);
	}

	public List<Worker> getAll() {
		return workerDao.getAllWorkers();
	}
	
	public Worker getWorkerByEmail(String email) {
		return workerDao.getWorkerByEmail(email);
	}

}
