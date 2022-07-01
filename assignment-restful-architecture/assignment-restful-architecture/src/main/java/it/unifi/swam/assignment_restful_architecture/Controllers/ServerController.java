package it.unifi.swam.assignment_restful_architecture.Controllers;

import java.io.Serializable;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import it.unifi.swam.assignment_restful_architecture.DAOs.ServerDAO;
import it.unifi.swam.assignment_restful_architecture.Model.ModelFactory;
import it.unifi.swam.assignment_restful_architecture.Model.Company.Server;

@SessionScoped
@Named
public class ServerController implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Inject
	ServerDAO serverDao;

	public Server getById(Long id) {
		if(id==null) {
			throw new IllegalArgumentException("Id cannot be null");
		}else {
			return serverDao.findById(id);
		}
	}

	public Server saveServer(Server server) {
		Server serverToPersist = ModelFactory.server();
		serverToPersist.setName(server.getName());
		serverDao.save(serverToPersist);
		return serverToPersist;
	}

	public void updateServer(Server serverToUpdate, Server updates) {
		if(updates.getName()!=serverToUpdate.getName() && updates.getName()!=null) {
			serverToUpdate.setName(updates.getName());
		}
		
		if(updates.getActive()!=serverToUpdate.getActive() && updates.getActive()!=null) {
			serverToUpdate.setActive(updates.getActive());
		}
		
		serverDao.save(serverToUpdate);
	}

	public void delete(Server server) {
		serverDao.delete(server);
	}

	public Boolean activate(Server server) {
		server.setActive(true);
		return server.getActive();
	}

	public Boolean deactivate(Server server) {
		server.setActive(false);
		return !server.getActive();
	}

}
