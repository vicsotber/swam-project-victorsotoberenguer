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

import it.unifi.swam.assignment_restful_architecture.Controllers.MissionController;
import it.unifi.swam.assignment_restful_architecture.Controllers.TaskController;
import it.unifi.swam.assignment_restful_architecture.Model.Game.Mission;
import it.unifi.swam.assignment_restful_architecture.Model.Game.Task;

@Path("/missionendpoint")
public class MissionEndpoint {

	@Inject
	MissionController missionController;
	
	@Inject
	TaskController taskController;
	
	@GET
	@Path("/ping")
	public Response ping() {
		return Response.ok().entity("MissionEndpoint is ready").build();
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
			Mission mission = missionController.getById(id);
			if(mission == null) {
				return Response.ok(mapper.writeValueAsString("No object found with this id: " + id),MediaType.APPLICATION_JSON).status(Status.NOT_FOUND).build();
			}else {
				String json = mapper.writeValueAsString(mission);
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
				return Response.status(Status.BAD_REQUEST).entity(mapper.writeValueAsString("Name cannot be null")).build();
			}
		      Mission mission = mapper.readValue(json, Mission.class);
		      if(mission.getName()==null) {
		    	  return Response.status(Status.BAD_REQUEST).entity(mapper.writeValueAsString("Name cannot be null")).build();
		      }
		      Mission missionPersisted = missionController.saveMisison(mission);
		      if(missionPersisted.getId()!=null) {
		    	  String jsonResult = mapper.writeValueAsString("Object created with ID: " + missionPersisted.getId());
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
			Mission missionToUpdate = missionController.getById(id);
			if(missionToUpdate==null) {
				return Response.ok(mapper.writeValueAsString("No object found with this id: " + id),MediaType.APPLICATION_JSON).status(Status.NOT_FOUND).build();
			}else {
				Mission updates = mapper.readValue(json, Mission.class);
				missionController.updateMission(missionToUpdate,updates);
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
			Mission mission = missionController.getById(id);
			if(mission == null) {
				return Response.ok(mapper.writeValueAsString("No object found with this ID: " + id),MediaType.APPLICATION_JSON).status(Status.NOT_FOUND).build();
			}else {
				missionController.delete(mission);
				return Response.ok().entity(mapper.writeValueAsString("Object with ID " + id + " deleted correctly")).build();
			}
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			return Response.serverError().entity("An unexpected error ocurred").build();
		}
	}
	
	@PUT
	@Path("/{id}/addtask")
	@Consumes({ MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	@Transactional
	@RolesAllowed({"All","Developer"})
	public Response addTaskToMission(@PathParam("id") Long id, String json) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			if(json.isEmpty()) {
				return Response.status(Status.BAD_REQUEST).entity(mapper.writeValueAsString("Description, Place and Time cannot be null")).build();
			}
			Task task = mapper.readValue(json, Task.class);
			if(task.getDescription()==null||task.getPlace()==null||task.getTime()==null) {
		    	  return Response.status(Status.BAD_REQUEST).entity(mapper.writeValueAsString("Description, Place and Time cannot be null")).build();
		      }
			Mission mission = missionController.getById(id);
			if(mission == null) {
				return Response.ok(mapper.writeValueAsString("No object found with this ID: " + id),MediaType.APPLICATION_JSON).status(Status.NOT_FOUND).build();
			}else {
				Task persistedTask = missionController.addTask(mission,task);
				return Response.ok().entity(mapper.writeValueAsString("Mission with ID " + id + " added Task with ID " + persistedTask.getId() + " correctly")).build();
			}
		} catch (IOException e) {
			e.printStackTrace();
			return Response.serverError().entity("An unexpected error ocurred").build();
		}
	}
	
	@DELETE
	@Path("/{missionId}/{taskId}")
	@Produces({MediaType.APPLICATION_JSON})
	@Transactional
	@RolesAllowed({"All","Developer"})
	public Response unboundTask(@PathParam("missionId") Long missionId, @PathParam("taskId") Long taskId) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			Mission mission = missionController.getById(missionId);
			Task task = taskController.getById(taskId);
			if(mission == null) {
				return Response.ok(mapper.writeValueAsString("No mission found with this ID: " + missionId),MediaType.APPLICATION_JSON).status(Status.NOT_FOUND).build();
			}else if(task == null) {
				return Response.ok(mapper.writeValueAsString("No task found with this ID: " + taskId),MediaType.APPLICATION_JSON).status(Status.NOT_FOUND).build();
			}else {
				Boolean unbounded = missionController.unboundTask(mission,task);
				if(unbounded==false) {
					return Response.status(Status.BAD_REQUEST).entity(mapper.writeValueAsString("Task with ID " + taskId + " is not part of mission with ID " + missionId)).build();
				}else {
					return Response.ok().entity(mapper.writeValueAsString("Task with ID " + taskId + " unbounded of Mission with ID " + missionId + " and deleted.")).build();
				}
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
	public Response accomplishMission(@PathParam("id") Long id) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			Mission mission = missionController.getById(id);
			if(mission == null) {
				return Response.ok(mapper.writeValueAsString("No object found with this ID: " + id),MediaType.APPLICATION_JSON).status(Status.NOT_FOUND).build();
			}else {
				Boolean accomplished = missionController.accomplish(mission);
				if(accomplished) {
					return Response.ok().entity(mapper.writeValueAsString("Mission with ID " + id + " accomplished correctly")).build();
				}else {
					return Response.status(Status.BAD_REQUEST).entity(mapper.writeValueAsString("Cannot accomplish a Mission until all its Tasks have been accomplished as well")).build();
				}
			}
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			return Response.serverError().entity("An unexpected error ocurred").build();
		}
	}
}
