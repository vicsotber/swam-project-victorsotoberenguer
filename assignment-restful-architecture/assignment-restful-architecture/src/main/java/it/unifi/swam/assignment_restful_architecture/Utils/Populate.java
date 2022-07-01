package it.unifi.swam.assignment_restful_architecture.Utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;


import it.unifi.swam.assignment_restful_architecture.Model.ModelFactory;
import it.unifi.swam.assignment_restful_architecture.Model.Company.Company;
import it.unifi.swam.assignment_restful_architecture.Model.Company.Sector;
import it.unifi.swam.assignment_restful_architecture.Model.Company.Server;
import it.unifi.swam.assignment_restful_architecture.Model.Company.Worker;
import it.unifi.swam.assignment_restful_architecture.Model.Game.Character;
import it.unifi.swam.assignment_restful_architecture.Model.Game.Mission;
import it.unifi.swam.assignment_restful_architecture.Model.Game.Task;
import it.unifi.swam.assignment_restful_architecture.Model.Player.PlayerAccount;

public class Populate {
	static final String DB_URL = "jdbc:mysql://localhost:3306/assignment-restful-architecture?serverTimezone=UTC";
	static final String USER = "java-client";
	static final String PASS = "password";
	
	public static void truncate() {
		try(Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
		    Statement stmt = conn.createStatement();
		 ) {	

			String sql = "SET FOREIGN_KEY_CHECKS = 0";
			stmt.executeUpdate(sql);
			
			sql = "TRUNCATE TABLE server";
			stmt.executeUpdate(sql);
			
			sql = "TRUNCATE TABLE sector;";
			stmt.executeUpdate(sql);
			
			sql = "TRUNCATE TABLE worker;";
			stmt.executeUpdate(sql);
			
			sql = "TRUNCATE TABLE playeraccount;";
			stmt.executeUpdate(sql);
			
			sql = "TRUNCATE TABLE playercharacter;";
			stmt.executeUpdate(sql);
			
			sql = "TRUNCATE TABLE company;";
			stmt.executeUpdate(sql);
			
			sql = "TRUNCATE TABLE mission;";
			stmt.executeUpdate(sql);
			
			sql = "TRUNCATE TABLE task;";
			stmt.executeUpdate(sql);
			
			sql = "TRUNCATE TABLE characters_missions;";
			stmt.executeUpdate(sql);
			
			sql = "TRUNCATE TABLE company_sectors;";
			stmt.executeUpdate(sql);
			
			sql = "TRUNCATE TABLE mission_tasks;";
			stmt.executeUpdate(sql);
			
			sql = "TRUNCATE TABLE sector_workers;";
			stmt.executeUpdate(sql);
			
			sql = "SET FOREIGN_KEY_CHECKS = 1";
			stmt.executeUpdate(sql);
			
			System.out.println("Truncate done");   	  
		} catch (SQLException e) {
		    e.printStackTrace();
		} 
	}
	
	public static void populate() {
		Server server = ModelFactory.server();
		Worker worker = ModelFactory.worker();
		worker.setPassword("testdeveloper",false);
		Worker worker2 = ModelFactory.worker();
		worker2.setPassword("testmainteinance",false);
		Task task = ModelFactory.task();
		Company company = ModelFactory.company();
		Mission mission = ModelFactory.mission();
		Sector sector = ModelFactory.sector();
		PlayerAccount player = ModelFactory.playeracc();
		player.setPassword("testplayer", false);
		Character character = ModelFactory.character();
		
		// Open a connection
	      try(Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
	         Statement stmt = conn.createStatement();
	      ) {	
	         String sql = "INSERT INTO server(uuid,name,active) VALUES ('" + server.getUuid() + "','test server', 0)";
	         stmt.executeUpdate(sql);
	         
	         sql = "INSERT INTO worker(uuid,email,name,password,workerRol) VALUES ('" + 
	        		 worker.getUuid() + "','testdeveloper@gmail.com', 'test worker developer', '" + worker.getPassword() + "', 'Developer'),"
	        		 + " ('" + worker2.getUuid() + "','testmainteinance@gmail.com', 'test worker mainteinance', '" +  worker2.getPassword() + "', 'Mainteinance')";
	         stmt.executeUpdate(sql);
	         
	         sql = "INSERT INTO task(uuid,accomplished,description,place,time) VALUES ('" + task.getUuid() + "',0, 'test task', 'test task place', 30)";
	         stmt.executeUpdate(sql);
	         
	         sql = "INSERT INTO company(uuid,name) VALUES ('" + company.getUuid() + "', 'test company')";
	         stmt.executeUpdate(sql);
	         
	         sql = "INSERT INTO mission(uuid,accomplished,name) VALUES ('" + mission.getUuid() + "', 0, 'test mission')";
	         stmt.executeUpdate(sql);
	         
	         sql = "INSERT INTO sector(uuid,type) VALUES ('" + sector.getUuid() + "', 'Development')";
	         stmt.executeUpdate(sql);
	         
	         sql = "INSERT INTO playercharacter(uuid,level) VALUES ('" + character.getUuid() + "', 23)";
	         stmt.executeUpdate(sql);
	         
	         sql = "INSERT INTO playeraccount(uuid,email,password,username,playerCharacter) VALUES ('" + player.getUuid() + "', 'testplayer@gmail.com', '" + player.getPassword() + "', 'test player', 1)";
	         stmt.executeUpdate(sql);
	         
	         System.out.println("Population done");   	  
	      } catch (SQLException e) {
	         e.printStackTrace();
	      } 
	}
	
	public static void main(String[] args) {
		truncate();
		populate();
		
	}

}
