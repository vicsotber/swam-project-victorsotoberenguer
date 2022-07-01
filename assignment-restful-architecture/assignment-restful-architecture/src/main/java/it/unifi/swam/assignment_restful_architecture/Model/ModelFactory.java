package it.unifi.swam.assignment_restful_architecture.Model;

import java.util.ArrayList;
import java.util.UUID;

import it.unifi.swam.assignment_restful_architecture.Model.Company.Company;
import it.unifi.swam.assignment_restful_architecture.Model.Company.Sector;
import it.unifi.swam.assignment_restful_architecture.Model.Company.Server;
import it.unifi.swam.assignment_restful_architecture.Model.Company.Worker;
import it.unifi.swam.assignment_restful_architecture.Model.Game.Character;
import it.unifi.swam.assignment_restful_architecture.Model.Game.Mission;
import it.unifi.swam.assignment_restful_architecture.Model.Game.Task;
import it.unifi.swam.assignment_restful_architecture.Model.Player.PlayerAccount;

public class ModelFactory {
	private ModelFactory() {}
	
	public static Company company() {
		return new Company(UUID.randomUUID().toString(), null, null);
	}
	
	public static Worker worker() {
		return new Worker(UUID.randomUUID().toString(), "", "", "");
	}
	
	public static Worker worker(Worker worker) {
		return new Worker(worker);
	}
	
	public static Sector sector() {
		return new Sector(UUID.randomUUID().toString(), null);
	}
	
	public static Server server() {
		return new Server(UUID.randomUUID().toString(), null);
	}
	
	public static PlayerAccount playeracc() {
		return new PlayerAccount(UUID.randomUUID().toString());
	}
	
	public static PlayerAccount playeracc(String username, String email, String password) {
		return new PlayerAccount(UUID.randomUUID().toString(), username, email, password);
	}
	
	public static Character character() {
		return new Character(UUID.randomUUID().toString());
	}
	
	public static Mission mission() {
		return new Mission(UUID.randomUUID().toString(), null, new ArrayList<Task>());
	}
	
	public static Task task() {
		return new Task(UUID.randomUUID().toString(), null, null, null);
	}
}
