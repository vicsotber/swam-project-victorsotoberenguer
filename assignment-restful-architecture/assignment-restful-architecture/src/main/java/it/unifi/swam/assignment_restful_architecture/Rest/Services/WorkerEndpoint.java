package it.unifi.swam.assignment_restful_architecture.Rest.Services;

import java.io.IOException;
import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONObject;
import org.springframework.security.crypto.bcrypt.BCrypt;

import it.unifi.swam.assignment_restful_architecture.Controllers.WorkerController;
import it.unifi.swam.assignment_restful_architecture.Model.Company.Worker;
import it.unifi.swam.assignment_restful_architecture.Rest.JWTService;

@Path("/workerendpoint")
public class WorkerEndpoint {
	
	@Inject
	WorkerController workerController;
	
	@GET
	@Path("/ping")
	public Response ping() {
		return Response.ok().entity("WorkerEndpoint is ready").build();
	}
	
	@POST
	@Path("/login")
	@Consumes({ MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public Response login(String json) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			if(json.isEmpty()) {
				return Response.status(Status.BAD_REQUEST).entity(mapper.writeValueAsString("Email and Password cannot be null")).build();
			}
			
		    Worker worker = mapper.readValue(json, Worker.class);
		    if(worker.getEmail()==null||worker.getPassword()==null) {
		    	return Response.status(Status.BAD_REQUEST).entity(mapper.writeValueAsString("Email and Password cannot be null")).build();
		    }
		    
		    JSONObject obj = new JSONObject(json);
		    String password = obj.getString("password"); //mapper already encrypts the password so it is not useful because then we will compare two different encrypted passwords
		    
		    Worker retrievedWorker = workerController.getWorkerByEmail(worker.getEmail());
		    if(retrievedWorker==null) {
		    	return Response.status(Status.NOT_FOUND).entity(mapper.writeValueAsString("No Worker found with this email")).build();
		    }else if(!BCrypt.checkpw(password, retrievedWorker.getPassword())) {
		    	return Response.status(Status.BAD_REQUEST).entity(mapper.writeValueAsString("Incorrect password")).build();
		    }
		    
		    JWTService jwtService = new JWTService();
		    String role;
		    if(retrievedWorker.getWorkerRol()==null) {
		    	role = "All";
		    }else {
		    	role = retrievedWorker.getWorkerRol().toString();
		    }
	        String token = jwtService.generateJWTToken(retrievedWorker.getEmail(), role);
	        return Response.ok(token, MediaType.TEXT_PLAIN).build();
		    
		    } catch (IOException e) {
		       e.printStackTrace();
		       return Response.serverError().entity("An unexpected error ocurred").build();
		    }
	}
	
	@GET
	@Produces({MediaType.APPLICATION_JSON})
	@RolesAllowed({"All","Developer","Mainteinance"})
	public Response getAllWorkers() {
		ObjectMapper mapper = new ObjectMapper();
		try {
			List<Worker> workers = workerController.getAll();
			String json = mapper.writeValueAsString(workers);
			return Response.ok(json, MediaType.APPLICATION_JSON).build();
			
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			return Response.serverError().entity("An unexpected error ocurred").build();
		}
	}
	
	@GET
	@Path("/{id}")
	@Produces({MediaType.APPLICATION_JSON})
	@RolesAllowed({"All","Developer","Mainteinance"})
	public Response getById(@PathParam("id") Long id) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			if(id == null) {
				return Response.serverError().entity(mapper.writeValueAsString("The id cannot be null")).build();
			}
			Worker worker = workerController.getById(id);
			if(worker == null) {
				return Response.ok(mapper.writeValueAsString("No object found with this id: " + id),MediaType.APPLICATION_JSON).status(Status.NOT_FOUND).build();
			}else {
				String json = mapper.writeValueAsString(worker);
				return Response.ok(json, MediaType.APPLICATION_JSON).build();
			}
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			return Response.serverError().entity("An unexpected error ocurred").build();
		}
	}
	
	
	@POST
	@Path("/signup")
	@Consumes({ MediaType.APPLICATION_JSON})
	@Produces({ MediaType.APPLICATION_JSON})
	@Transactional
	public Response save(String json) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			if(json.isEmpty()) {
				return Response.status(Status.BAD_REQUEST).entity(mapper.writeValueAsString("Name, Email and Password cannot be null")).build();
			}
		      Worker worker = mapper.readValue(json, Worker.class); //this already encrypts the password
		      if(worker.getEmail()==null||worker.getName()==null||worker.getPassword()==null) {
		    	  return Response.status(Status.BAD_REQUEST).entity(mapper.writeValueAsString("Name, Email and Password cannot be null")).build();
		      }
		      
		      Worker workerPersisted = workerController.saveWorker(worker,true);
		      if(workerPersisted.getId()!=null) {
		    	  JWTService jwtService = new JWTService();
				    String role;
				    if(workerPersisted.getWorkerRol()==null) {
				    	role = "All";
				    }else {
				    	role = workerPersisted.getWorkerRol().toString();
				    }
			      String token = jwtService.generateJWTToken(workerPersisted.getEmail(), role);
		    	  String jsonResult = mapper.writeValueAsString("Object created with ID: " + workerPersisted.getId() + ". TOKEN: " + token);
		    	  return Response.status(Status.CREATED).entity(jsonResult).build();  
		      }else {
		    	  return Response.serverError().entity(mapper.writeValueAsString("An unexpected error ocurred")).build();
		      }
		    } catch (IOException e) {
		       e.printStackTrace();
		       return Response.serverError().entity("An unexpected error ocurred").build();
		    }
	}
	
	@PUT
	@Path("/{id}")
	@Consumes({ MediaType.APPLICATION_JSON})
	@Produces({ MediaType.APPLICATION_JSON})
	@Transactional
	@RolesAllowed({"All","Developer","Mainteinance"})
	public Response update(@PathParam("id") Long id, String json) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			Worker workerToUpdate = workerController.getById(id);
			if(workerToUpdate==null) {
				return Response.ok(mapper.writeValueAsString("No object found with this id: " + id),MediaType.APPLICATION_JSON).status(Status.NOT_FOUND).build();
			}else {
				Worker updates = mapper.readValue(json, Worker.class);
				workerController.updateWorker(workerToUpdate,updates, true);
				return Response.status(Status.NO_CONTENT).build(); 
			}
		}catch (IOException e) {
		       e.printStackTrace();
		       return Response.serverError().entity("An unexpected error ocurred").build();
		    }
	}
	
	@DELETE
	@Path("/{id}")
	@Produces({MediaType.APPLICATION_JSON})
	@Transactional
	@RolesAllowed({"All","Developer","Mainteinance"})
	public Response delete(@PathParam("id") Long id) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			Worker worker = workerController.getById(id);
			if(worker == null) {
				return Response.ok(mapper.writeValueAsString("No object found with this ID: " + id),MediaType.APPLICATION_JSON).status(Status.NOT_FOUND).build();
			}else {
				workerController.delete(worker);
				return Response.ok().entity(mapper.writeValueAsString("Object with ID " + id + " deleted correctly")).build();
			}
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			return Response.serverError().entity("An unexpected error ocurred").build();
		}
	}
}