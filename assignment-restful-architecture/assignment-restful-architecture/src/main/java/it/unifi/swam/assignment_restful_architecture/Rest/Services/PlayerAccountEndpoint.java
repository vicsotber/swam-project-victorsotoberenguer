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

import it.unifi.swam.assignment_restful_architecture.Controllers.PlayerAccountController;
import it.unifi.swam.assignment_restful_architecture.Model.Player.PlayerAccount;
import it.unifi.swam.assignment_restful_architecture.Rest.JWTService;
import it.unifi.swam.assignment_restful_architecture.Model.Game.Character;

@Path("/playeraccountendpoint")
public class PlayerAccountEndpoint {
	@Inject
	PlayerAccountController playeraccController;
	
	@GET
	@Path("/ping")
	public Response ping() {
		return Response.ok().entity("PlayerAccountEndpoint is ready").build();
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
		    PlayerAccount playeracc = mapper.readValue(json, PlayerAccount.class);
		    if(playeracc.getEmail()==null||playeracc.getPassword()==null) {
		    	return Response.status(Status.BAD_REQUEST).entity(mapper.writeValueAsString("Email and Password cannot be null")).build();
		    }
		    
		    JSONObject obj = new JSONObject(json);
		    String password = obj.getString("password");
		    
		    PlayerAccount retrievedPlayer = playeraccController.getPlayerByEmail(playeracc.getEmail());
		    if(retrievedPlayer==null) {
		    	return Response.status(Status.NOT_FOUND).entity(mapper.writeValueAsString("No Player found with this email")).build();
		    }else if(!BCrypt.checkpw(password, retrievedPlayer.getPassword())) {
		    	return Response.status(Status.BAD_REQUEST).entity(mapper.writeValueAsString("Incorrect password")).build();
		    }
		    
		    JWTService jwtService = new JWTService();
	        String token = jwtService.generateJWTToken(playeracc.getEmail(), "User");
	        return Response.ok(token, MediaType.TEXT_PLAIN).build();
		    
		    } catch (IOException e) {
		       e.printStackTrace();
		       return Response.serverError().entity("An unexpected error ocurred").build();
		    }
	}
	

	
	@GET
	@Produces({MediaType.APPLICATION_JSON})
	@Transactional
	@RolesAllowed({"All","Developer","Mainteinance"})
	public Response getAllPlayers() {
		ObjectMapper mapper = new ObjectMapper();
		try {
			List<PlayerAccount> players = playeraccController.getAll();
			String json = mapper.writeValueAsString(players);
			return Response.ok(json, MediaType.APPLICATION_JSON).build();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			return Response.serverError().entity("An unexpected error ocurred").build();
		}
	}
	
	@GET
	@Path("/{id}")
	@Produces({MediaType.APPLICATION_JSON})
	@Transactional
	@RolesAllowed({"All","Developer","Mainteinance"})
	public Response getById(@PathParam("id") Long id) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			if(id == null) {
				return Response.serverError().entity(mapper.writeValueAsString("The id cannot be null")).build();
			}
			PlayerAccount playeracc = playeraccController.getById(id);
			if(playeracc == null) {
				return Response.ok(mapper.writeValueAsString("No object found with this id: " + id),MediaType.APPLICATION_JSON).status(Status.NOT_FOUND).build();
			}else {
				String json = mapper.writeValueAsString(playeracc);
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
				return Response.status(Status.BAD_REQUEST).entity(mapper.writeValueAsString("Username, Email and Password cannot be null")).build();
			}
		      PlayerAccount playeracc = mapper.readValue(json, PlayerAccount.class);
		      if(playeracc.getEmail()==null||playeracc.getUsername()==null||playeracc.getPassword()==null) {
		    	  return Response.status(Status.BAD_REQUEST).entity(mapper.writeValueAsString("Username, Email and Password cannot be null")).build();
		      }
		      PlayerAccount playeraccPersisted = playeraccController.savePlayer(playeracc, true);
		      if(playeraccPersisted.getId()!=null) {
		    	  JWTService jwtService = new JWTService();
			      String token = jwtService.generateJWTToken(playeraccPersisted.getEmail(), "User");
		    	  String jsonResult = mapper.writeValueAsString("Object created with ID: " + playeraccPersisted.getId() + ". TOKEN: " + token);
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
			PlayerAccount playeraccToUpdate = playeraccController.getById(id);
			if(playeraccToUpdate==null) {
				return Response.ok(mapper.writeValueAsString("No object found with this id: " + id),MediaType.APPLICATION_JSON).status(Status.NOT_FOUND).build();
			}else {
				PlayerAccount updates = mapper.readValue(json, PlayerAccount.class);
				playeraccController.updatePlayerAccount(playeraccToUpdate,updates, true);
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
			PlayerAccount playeracc = playeraccController.getById(id);
			if(playeracc == null) {
				return Response.ok(mapper.writeValueAsString("No object found with this ID: " + id),MediaType.APPLICATION_JSON).status(Status.NOT_FOUND).build();
			}else {
				playeraccController.delete(playeracc);
				return Response.ok().entity(mapper.writeValueAsString("Object with ID " + id + " deleted correctly")).build();
			}
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			return Response.serverError().entity("An unexpected error ocurred").build();
		}
	}
	
	@PUT
	@Path("/{id}/{friendId}")
	@Produces({MediaType.APPLICATION_JSON})
	@Transactional
	@RolesAllowed({"All","Developer","User"})
	public Response followAccount(@PathParam("id") Long id, @PathParam("friendId") Long friendId) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			if(id.equals(friendId)) {
				return Response.status(Status.BAD_REQUEST).entity(mapper.writeValueAsString("Players cannot follow themselves")).build();
			}
			PlayerAccount playeracc = playeraccController.getById(id);
			PlayerAccount playeracc2 = playeraccController.getById(friendId);
			if(playeracc == null) {
				return Response.ok(mapper.writeValueAsString("No player found with this ID: " + id),MediaType.APPLICATION_JSON).status(Status.NOT_FOUND).build();
			}else if(playeracc2 == null) {
				return Response.ok(mapper.writeValueAsString("No player found with this ID: " + friendId),MediaType.APPLICATION_JSON).status(Status.NOT_FOUND).build();
			}else {
				playeraccController.followAccount(playeracc,playeracc2);
				return Response.ok().entity(mapper.writeValueAsString("Player with ID " + id + " followed player with ID " + friendId)).build();
			}
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			return Response.serverError().entity("An unexpected error ocurred").build();
		}
	}
	
	@DELETE
	@Path("/{id}/{friendId}")
	@Produces({MediaType.APPLICATION_JSON})
	@Transactional
	@RolesAllowed({"All","Developer","User"})
	public Response unfollowAccount(@PathParam("id") Long id, @PathParam("friendId") Long friendId) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			PlayerAccount playeracc = playeraccController.getById(id);
			PlayerAccount playeracc2 = playeraccController.getById(friendId);
			if(playeracc == null) {
				return Response.ok(mapper.writeValueAsString("No player found with this ID: " + id),MediaType.APPLICATION_JSON).status(Status.NOT_FOUND).build();
			}else if(playeracc2 == null) {
				return Response.ok(mapper.writeValueAsString("No player found with this ID: " + friendId),MediaType.APPLICATION_JSON).status(Status.NOT_FOUND).build();
			}else {
				Boolean unfollowed = playeraccController.unfollowAccount(playeracc,playeracc2);
				if(unfollowed==false) {
					return Response.status(Status.BAD_REQUEST).entity(mapper.writeValueAsString("Player with ID " + id + " is not following player with ID " + friendId)).build();
				}else {
					return Response.ok().entity(mapper.writeValueAsString("Player with ID " + id + " unfollowed player with ID " + friendId)).build();
				}
			}
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			return Response.serverError().entity("An unexpected error ocurred").build();
		}
	}
	
	@PUT
	@Path("/{id}/character")
	@Produces({ MediaType.APPLICATION_JSON})
	@Transactional
	@RolesAllowed({"All","Developer","User"})
	public Response createCharacter(@PathParam("id") Long id) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			PlayerAccount playeracc = playeraccController.getById(id);
			if(playeracc == null) {
				return Response.ok(mapper.writeValueAsString("No player found with this ID: " + id),MediaType.APPLICATION_JSON).status(Status.NOT_FOUND).build();
			}else {
				Character persistedCharacter = playeraccController.createCharacter(playeracc);
				return Response.ok().entity(mapper.writeValueAsString("Player with ID " + id + " has created Character with ID " + persistedCharacter.getId() + " correctly")).build();
			}
		    } catch (IOException e) {
		       e.printStackTrace();
		       return Response.serverError().entity("An unexpected error ocurred").build();
		    }
	}
}
