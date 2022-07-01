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
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import it.unifi.swam.assignment_restful_architecture.Controllers.SectorController;
import it.unifi.swam.assignment_restful_architecture.Controllers.WorkerController;
import it.unifi.swam.assignment_restful_architecture.Model.Company.Sector;
import it.unifi.swam.assignment_restful_architecture.Model.Company.Worker;

@Path("sectorendpoint")
public class SectorEndpoint {
	
	@Inject
	SectorController sectorController;
	
	@Inject
	WorkerController workerController;
	
	@GET
	@Path("/ping")
	public Response ping() {
		return Response.ok().entity("SectorEndpoint is ready").build();
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
			Sector sector = sectorController.getById(id);
			if(sector == null) {
				return Response.ok(mapper.writeValueAsString("No object found with this id: " + id),MediaType.APPLICATION_JSON).status(Status.NOT_FOUND).build();
			}else {
				String json = mapper.writeValueAsString(sector);
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
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		try {
		      Sector sector = mapper.readValue(json, Sector.class);
		      if(sector.getType()==null) {
		    	  return Response.status(Status.BAD_REQUEST).entity(mapper.writeValueAsString("Sector Type cannot be null")).build();
		      }
		      Sector sectorPersisted = sectorController.saveSector(sector);
		      if(sectorPersisted.getId()!=null) {
		    	  String jsonResult = mapper.writeValueAsString("Object created with ID: " + sectorPersisted.getId());
		    	  return Response.status(Status.CREATED).entity(jsonResult).build();  
		      }else {
		    	  return Response.serverError().entity(mapper.writeValueAsString("An unexpected error ocurred")).build();
		      }
		    } catch (IOException e) {
		       e.printStackTrace();
		       return Response.serverError().entity(e.getMessage()).build();
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
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		try {
			Sector sectorToUpdate = sectorController.getById(id);
			if(sectorToUpdate==null) {
				return Response.ok(mapper.writeValueAsString("No object found with this id: " + id),MediaType.APPLICATION_JSON).status(Status.NOT_FOUND).build();
			}else {
				Sector updates = mapper.readValue(json, Sector.class);
				sectorController.updateSector(sectorToUpdate,updates);
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
			Sector sector = sectorController.getById(id);
			if(sector == null) {
				return Response.ok(mapper.writeValueAsString("No object found with this ID: " + id),MediaType.APPLICATION_JSON).status(Status.NOT_FOUND).build();
			}else {
				sectorController.delete(sector);
				return Response.ok().entity(mapper.writeValueAsString("Object with ID " + id + " deleted correctly")).build();
			}
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			return Response.serverError().entity("An unexpected error ocurred").build();
		}
	}
	
	@PUT
	@Path("/{sectorId}/{workerId}")
	@Produces({MediaType.APPLICATION_JSON})
	@Transactional
	@RolesAllowed({"All","Developer","Mainteinance"})
	public Response setLeader(@PathParam("sectorId") Long sectorId, @PathParam("workerId") Long workerId) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			Sector sector = sectorController.getById(sectorId);
			Worker worker = workerController.getById(workerId);
			if(sector == null) {
				return Response.ok(mapper.writeValueAsString("No Sector found with this ID: " + sectorId),MediaType.APPLICATION_JSON).status(Status.NOT_FOUND).build();
			}else if(worker == null) {
				return Response.ok(mapper.writeValueAsString("No Worker found with this ID: " + workerId),MediaType.APPLICATION_JSON).status(Status.NOT_FOUND).build();
			}else {
				Boolean setted = sectorController.setLeader(sector,worker);
				if(setted==false) {
					return Response.status(Status.BAD_REQUEST).entity(mapper.writeValueAsString("Something went wrong")).build();
				}
				return Response.ok().entity(mapper.writeValueAsString("Sector with ID " + sectorId + " now has Leader with ID " + workerId)).build();
			}
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			return Response.serverError().entity("An unexpected error ocurred").build();
		}
	}
	
	@PUT
	@Path("/{sectorId}/{workerId}/addworker")
	@Produces({MediaType.APPLICATION_JSON})
	@Transactional
	@RolesAllowed({"All","Developer","Mainteinance"})
	public Response addWorker(@PathParam("sectorId") Long sectorId, @PathParam("workerId") Long workerId) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			Sector sector = sectorController.getById(sectorId);
			Worker worker = workerController.getById(workerId);
			if(sector == null) {
				return Response.ok(mapper.writeValueAsString("No Sector found with this ID: " + sectorId),MediaType.APPLICATION_JSON).status(Status.NOT_FOUND).build();
			}else if(worker == null) {
				return Response.ok(mapper.writeValueAsString("No Worker found with this ID: " + workerId),MediaType.APPLICATION_JSON).status(Status.NOT_FOUND).build();
			}else {
				Boolean added = sectorController.addWorker(sector,worker);
				if(added==false) {
					return Response.status(Status.BAD_REQUEST).entity(mapper.writeValueAsString("The Sector with Id " + sectorId + " already has the Worker with ID " + workerId)).build();
				}
				return Response.ok().entity(mapper.writeValueAsString("Worker with ID " + workerId + " added at Sector with ID " + sectorId)).build();
			}
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			return Response.serverError().entity("An unexpected error ocurred").build();
		}
	}
	
	@DELETE
	@Path("/{sectorId}/{workerId}/deleteworker")
	@Produces({MediaType.APPLICATION_JSON})
	@Transactional
	@RolesAllowed({"All","Developer","Mainteinance"})
	public Response deleteWorker(@PathParam("sectorId") Long sectorId, @PathParam("workerId") Long workerId) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			Sector sector = sectorController.getById(sectorId);
			Worker worker = workerController.getById(workerId);
			if(sector == null) {
				return Response.ok(mapper.writeValueAsString("No Sector found with this ID: " + sectorId),MediaType.APPLICATION_JSON).status(Status.NOT_FOUND).build();
			}else if(worker == null) {
				return Response.ok(mapper.writeValueAsString("No Worker found with this ID: " + workerId),MediaType.APPLICATION_JSON).status(Status.NOT_FOUND).build();
			}else {
				Boolean deleted = sectorController.deleteWorker(sector,worker);
				if(deleted==false) {
					return Response.status(Status.BAD_REQUEST).entity(mapper.writeValueAsString("Sector with ID " + sectorId + " has no Worker with ID " + workerId)).build();
				}
				return Response.ok().entity(mapper.writeValueAsString("Worker with ID " + workerId + " deleted from Sector with ID " + sectorId)).build();
			}
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			return Response.serverError().entity("An unexpected error ocurred").build();
		}
	}
}
