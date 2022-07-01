package it.unifi.swam.assignment_restful_architecture.Rest.Services;

import java.io.IOException;

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

import it.unifi.swam.assignment_restful_architecture.Controllers.TaskController;
import it.unifi.swam.assignment_restful_architecture.Model.Game.Task;

@Path("/taskendpoint")
public class TaskEndpoint {
	@Inject
	TaskController taskController;

	@GET
	@Path("/ping")
	public Response ping() {
		return Response.ok().entity("TaskEndpoint is ready").build();
	}
	
	@GET
	@Path("/{id}")
	@Produces({MediaType.APPLICATION_JSON})
	@RolesAllowed({"All","Developer","User"})
	public Response getById(@PathParam("id") Long id) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			if(id == null) {
				return Response.serverError().entity(mapper.writeValueAsString("The id cannot be null")).build();
			}
				Task task = taskController.getById(id);
				if(task == null) {
					return Response.ok(mapper.writeValueAsString("No object found with this id: " + id),MediaType.APPLICATION_JSON).status(Status.NOT_FOUND).build();
				}else {
					String json = mapper.writeValueAsString(task);
					return Response.ok(json, MediaType.APPLICATION_JSON).build();
				}
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			return Response.serverError().entity("An unexpected error ocurred").build();
		}
	}
	
	
	@POST
	@Consumes({ MediaType.APPLICATION_JSON})
	@Produces({ MediaType.APPLICATION_JSON})
	@Transactional
	@RolesAllowed({"All","Developer"})
	public Response save(String json) {
		ObjectMapper mapper = new ObjectMapper();
		try {
				if(json.isEmpty()) {
					return Response.status(Status.BAD_REQUEST).entity(mapper.writeValueAsString("Description, Place and Time cannot be null")).build();
				}
			      Task task = mapper.readValue(json, Task.class);
			      if(task.getDescription()==null||task.getPlace()==null||task.getTime()==null) {
			    	  return Response.status(Status.BAD_REQUEST).entity(mapper.writeValueAsString("Description, Place and Time cannot be null")).build();
			      }
			      Task taskPersisted = taskController.saveTask(task);
			      if(taskPersisted.getId()!=null) {
			    	  String jsonResult = mapper.writeValueAsString("Object created with ID: " + taskPersisted.getId());
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
	@RolesAllowed({"All","Developer"})
	public Response update(@PathParam("id") Long id, String json) {
		ObjectMapper mapper = new ObjectMapper();
		try {
				Task taskToUpdate = taskController.getById(id);
				if(taskToUpdate==null) {
					return Response.ok(mapper.writeValueAsString("No object found with this id: " + id),MediaType.APPLICATION_JSON).status(Status.NOT_FOUND).build();
				}else {
					Task updates = mapper.readValue(json, Task.class);
					taskController.updateTask(taskToUpdate,updates);
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
	@RolesAllowed({"All","Developer"})
	public Response delete(@PathParam("id") Long id) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			Task task = taskController.getById(id);
			if(task == null) {
				return Response.ok(mapper.writeValueAsString("No object found with this ID: " + id),MediaType.APPLICATION_JSON).status(Status.NOT_FOUND).build();
			}else {
				taskController.delete(task);
				return Response.ok().entity(mapper.writeValueAsString("Object with ID " + id + " deleted correctly")).build();
			}
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			return Response.serverError().entity("An unexpected error ocurred").build();
		}
	}
	
	@PUT
	@Path("/{id}/accomplish")
	@Produces({MediaType.APPLICATION_JSON})
	@Transactional
	@RolesAllowed({"All","Developer","User"})
	public Response accomplishTask(@PathParam("id") Long id) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			Task task = taskController.getById(id);
			if(task == null) {
				return Response.ok(mapper.writeValueAsString("No object found with this ID: " + id),MediaType.APPLICATION_JSON).status(Status.NOT_FOUND).build();
			}else {
				Boolean accomplished = taskController.accomplish(task);
				if(accomplished) {
					return Response.ok().entity(mapper.writeValueAsString("Task with ID " + id + " accomplished correctly")).build();
				}else {
					return Response.status(Status.BAD_REQUEST).entity(mapper.writeValueAsString("Something went wrong")).build();
				}
			}
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			return Response.serverError().entity("An unexpected error ocurred").build();
		}
	}
}
