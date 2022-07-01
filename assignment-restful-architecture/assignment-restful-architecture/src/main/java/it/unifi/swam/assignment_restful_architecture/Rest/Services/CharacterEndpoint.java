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

import it.unifi.swam.assignment_restful_architecture.Controllers.CharacterController;
import it.unifi.swam.assignment_restful_architecture.Controllers.MissionController;
import it.unifi.swam.assignment_restful_architecture.Model.ModelFactory;
import it.unifi.swam.assignment_restful_architecture.Model.Game.Character;
import it.unifi.swam.assignment_restful_architecture.Model.Game.Mission;

@Path("/characterendpoint")
public class CharacterEndpoint {
	
	@Inject
	CharacterController characterController;
	
	@Inject
	MissionController missionController;
	
	@GET
	@Path("/ping")
	public Response ping() {
		return Response.ok().entity("CharacterEndpoint is ready").build();
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
			Character character = characterController.getById(id);
			if(character == null) {
				return Response.ok(mapper.writeValueAsString("No object found with this id: " + id),MediaType.APPLICATION_JSON).status(Status.NOT_FOUND).build();
			}else {
				String json = mapper.writeValueAsString(character);
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
			Character character;
			if(json.isEmpty()) {
				character = ModelFactory.character();
			}else {
				character = mapper.readValue(json, Character.class);
			}
			Character characterPersisted = characterController.saveCharacter(character);
		      if(characterPersisted.getId()!=null) {
		    	  String jsonResult = mapper.writeValueAsString("Object created with ID: " + characterPersisted.getId());
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
	@RolesAllowed({"All","Developer","User"})
	public Response update(@PathParam("id") Long id, String json) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			Character characterToUpdate = characterController.getById(id);
			if(characterToUpdate==null) {
				return Response.ok(mapper.writeValueAsString("No object found with this id: " + id),MediaType.APPLICATION_JSON).status(Status.NOT_FOUND).build();
			}else {
				Character updates = mapper.readValue(json, Character.class);
				characterController.updateCharacter(characterToUpdate,updates);
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
	@RolesAllowed({"All","Developer","User"})
	public Response delete(@PathParam("id") Long id) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			Character character = characterController.getById(id);
			if(character == null) {
				return Response.ok(mapper.writeValueAsString("No object found with this ID: " + id),MediaType.APPLICATION_JSON).status(Status.NOT_FOUND).build();
			}else {
				characterController.delete(character);
				return Response.ok().entity(mapper.writeValueAsString("Object with ID " + id + " deleted correctly")).build();
			}
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			return Response.serverError().entity("An unexpected error ocurred").build();
		}
	}
	
	@PUT
	@Path("/{characterId}/{missionId}")
	@Produces({MediaType.APPLICATION_JSON})
	@Transactional
	@RolesAllowed({"All","Developer","User"})
	public Response acceptMission(@PathParam("characterId") Long characterId, @PathParam("missionId") Long missionId) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			Character character = characterController.getById(characterId);
			Mission mission = missionController.getById(missionId);
			if(character == null) {
				return Response.ok(mapper.writeValueAsString("No character found with this ID: " + characterId),MediaType.APPLICATION_JSON).status(Status.NOT_FOUND).build();
			}else if(mission == null) {
				return Response.ok(mapper.writeValueAsString("No mission found with this ID: " + missionId),MediaType.APPLICATION_JSON).status(Status.NOT_FOUND).build();
			}else {
				Boolean accepted = characterController.acceptMission(character,mission);
				if(accepted==false) {
					return Response.status(Status.BAD_REQUEST).entity(mapper.writeValueAsString("Character with ID " + missionId + " has already accepted mission " + missionId)).build();
				}
				return Response.ok().entity(mapper.writeValueAsString("Character with ID " + characterId + " accepted misison with ID " + missionId)).build();
			}
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			return Response.serverError().entity("An unexpected error ocurred").build();
		}
	}
	
	@DELETE
	@Path("/{characterId}/{missionId}")
	@Produces({MediaType.APPLICATION_JSON})
	@Transactional
	@RolesAllowed({"All","Developer","User"})
	public Response deleteAcceptedMission(@PathParam("characterId") Long characterId, @PathParam("missionId") Long missionId) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			Character character = characterController.getById(characterId);
			Mission mission = missionController.getById(missionId);
			if(character == null) {
				return Response.ok(mapper.writeValueAsString("No character found with this ID: " + characterId),MediaType.APPLICATION_JSON).status(Status.NOT_FOUND).build();
			}else if(mission == null) {
				return Response.ok(mapper.writeValueAsString("No mission found with this ID: " + missionId),MediaType.APPLICATION_JSON).status(Status.NOT_FOUND).build();
			}else {
				Boolean unaccepted = characterController.unacceptMission(character,mission);
				if(unaccepted==false) {
					return Response.status(Status.BAD_REQUEST).entity(mapper.writeValueAsString("Character with ID " + missionId + " doesn't haven accepted mission " + missionId)).build();
				}else {
					return Response.ok().entity(mapper.writeValueAsString("Character with ID " + characterId + " unaccepted misison with ID " + missionId)).build();
				}
			}
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			return Response.serverError().entity("An unexpected error ocurred").build();
		}
	}
	
	@PUT
	@Path("/{characterId}/levelup")
	@Produces({MediaType.APPLICATION_JSON})
	@Transactional
	@RolesAllowed({"All","Developer","User"})
	public Response levelUpCharacter(@PathParam("characterId") Long characterId) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			Character character = characterController.getById(characterId);
			if(character == null) {
				return Response.ok(mapper.writeValueAsString("No character found with this ID: " + characterId),MediaType.APPLICATION_JSON).status(Status.NOT_FOUND).build();
			}else {
				characterController.levelUp(character);
				return Response.ok().entity(mapper.writeValueAsString("Character with ID " + characterId + " has leveled up!")).build();
			}
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			return Response.serverError().entity("An unexpected error ocurred").build();
		}
	}
}
