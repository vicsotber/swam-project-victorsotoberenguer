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

import it.unifi.swam.assignment_restful_architecture.Controllers.ServerController;
import it.unifi.swam.assignment_restful_architecture.Model.Company.Server;

@Path("/serverendpoint")
public class ServerEndpoint {

	@Inject
	ServerController serverController;
	
	@GET
	@Path("/ping")
	public Response ping() {
		return Response.ok().entity("ServerEndpoint is ready").build();
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
			Server server = serverController.getById(id);
			if(server == null) {
				return Response.ok(mapper.writeValueAsString("No object found with this id: " + id),MediaType.APPLICATION_JSON).status(Status.NOT_FOUND).build();
			}else {
				String json = mapper.writeValueAsString(server);
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
	@RolesAllowed({"All","Developer","Mainteinance"})
	public Response save(String json) {
		ObjectMapper mapper = new ObjectMapper();
		try {
				if(json.isEmpty()) {
					return Response.status(Status.BAD_REQUEST).entity(mapper.writeValueAsString("Name cannot be null")).build();
				}
			      Server server = mapper.readValue(json, Server.class);
			      if(server.getName()==null) {
			    	  return Response.status(Status.BAD_REQUEST).entity(mapper.writeValueAsString("Name cannot be null")).build();
			      }
			      Server serverPersisted = serverController.saveServer(server);
			      if(serverPersisted.getId()!=null) {
			    	  String jsonResult = mapper.writeValueAsString("Object created with ID: " + serverPersisted.getId());
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
				Server serverToUpdate = serverController.getById(id);
				if(serverToUpdate==null) {
					return Response.ok(mapper.writeValueAsString("No object found with this id: " + id),MediaType.APPLICATION_JSON).status(Status.NOT_FOUND).build();
				}else {
					Server updates = mapper.readValue(json, Server.class);
					serverController.updateServer(serverToUpdate,updates);
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
				Server server = serverController.getById(id);
				if(server == null) {
					return Response.ok(mapper.writeValueAsString("No object found with this ID: " + id),MediaType.APPLICATION_JSON).status(Status.NOT_FOUND).build();
				}else {
					serverController.delete(server);
					return Response.ok().entity(mapper.writeValueAsString("Object with ID " + id + " deleted correctly")).build();
				}
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			return Response.serverError().entity("An unexpected error ocurred").build();
		}
	}
	
	@PUT
	@Path("/{id}/activate")
	@Produces({MediaType.APPLICATION_JSON})
	@Transactional
	@RolesAllowed({"All","Mainteinance"})
	public Response activateServer(@PathParam("id") Long id) {
		ObjectMapper mapper = new ObjectMapper();
		try {
				Server server = serverController.getById(id);
				if(server == null) {
					return Response.ok(mapper.writeValueAsString("No object found with this ID: " + id),MediaType.APPLICATION_JSON).status(Status.NOT_FOUND).build();
				}else {
					Boolean activated = serverController.activate(server);
					if(activated) {
						return Response.ok().entity(mapper.writeValueAsString("Server with ID " + id + " activated correctly")).build();
					}else {
						return Response.status(Status.BAD_REQUEST).entity(mapper.writeValueAsString("Something went wrong")).build();
					}
				}
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			return Response.serverError().entity("An unexpected error ocurred").build();
		}
	}
	
	@PUT
	@Path("/{id}/deactivate")
	@Produces({MediaType.APPLICATION_JSON})
	@Transactional
	@RolesAllowed({"All","Mainteinance"})
	public Response deactivateServer(@PathParam("id") Long id) {
		ObjectMapper mapper = new ObjectMapper();
		try {
				Server server = serverController.getById(id);
				if(server == null) {
					return Response.ok(mapper.writeValueAsString("No object found with this ID: " + id),MediaType.APPLICATION_JSON).status(Status.NOT_FOUND).build();
				}else {
					Boolean deactivated = serverController.deactivate(server);
					if(deactivated) {
						return Response.ok().entity(mapper.writeValueAsString("Server with ID " + id + " deactivated correctly")).build();
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
