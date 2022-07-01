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

import it.unifi.swam.assignment_restful_architecture.Controllers.CompanyController;
import it.unifi.swam.assignment_restful_architecture.Controllers.SectorController;
import it.unifi.swam.assignment_restful_architecture.Controllers.WorkerController;
import it.unifi.swam.assignment_restful_architecture.Model.Company.Company;
import it.unifi.swam.assignment_restful_architecture.Model.Company.Sector;
import it.unifi.swam.assignment_restful_architecture.Model.Company.Worker;

@Path("/companyendpoint")
public class CompanyEndpoint {
	
	@Inject
	CompanyController companyController;
	
	@Inject
	WorkerController workerController;
	
	@Inject
	SectorController sectorController;
	
	@GET
	@Path("/ping")
	public Response ping() {
		return Response.ok().entity("CompanyEndpoint is ready").build();
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
			Company company = companyController.getById(id);
			if(company == null) {
				return Response.ok(mapper.writeValueAsString("No object found with this id: " + id),MediaType.APPLICATION_JSON).status(Status.NOT_FOUND).build();
			}else {
				String json = mapper.writeValueAsString(company);
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
		      Company company = mapper.readValue(json, Company.class);
		      if(company.getName()==null) {
		    	  return Response.status(Status.BAD_REQUEST).entity(mapper.writeValueAsString("Name cannot be null")).build();
		      }
		      Company companyPersisted = companyController.saveCompany(company);
		      if(companyPersisted.getId()!=null) {
		    	  String jsonResult = mapper.writeValueAsString("Object created with ID: " + companyPersisted.getId());
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
			Company companyToUpdate = companyController.getById(id);
			if(companyToUpdate==null) {
				return Response.ok(mapper.writeValueAsString("No object found with this id: " + id),MediaType.APPLICATION_JSON).status(Status.NOT_FOUND).build();
			}else {
				Company updates = mapper.readValue(json, Company.class);
				companyController.updateCompany(companyToUpdate,updates);
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
	public Response delete(@PathParam("id") Long id) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			Company company = companyController.getById(id);
			if(company == null) {
				return Response.ok(mapper.writeValueAsString("No object found with this ID: " + id),MediaType.APPLICATION_JSON).status(Status.NOT_FOUND).build();
			}else {
				companyController.delete(company);
				return Response.ok().entity(mapper.writeValueAsString("Object with ID " + id + " deleted correctly")).build();
			}
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			return Response.serverError().entity("An unexpected error ocurred").build();
		}
	}
	
	@PUT
	@Path("/{companyId}/{workerId}")
	@Produces({MediaType.APPLICATION_JSON})
	@Transactional
	@RolesAllowed({"All","Developer","Mainteinance"})
	public Response setPresident(@PathParam("companyId") Long companyId, @PathParam("workerId") Long workerId) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			Company company = companyController.getById(companyId);
			Worker worker = workerController.getById(workerId);
			if(company == null) {
				return Response.ok(mapper.writeValueAsString("No Company found with this ID: " + companyId),MediaType.APPLICATION_JSON).status(Status.NOT_FOUND).build();
			}else if(worker == null) {
				return Response.ok(mapper.writeValueAsString("No Worker found with this ID: " + workerId),MediaType.APPLICATION_JSON).status(Status.NOT_FOUND).build();
			}else {
				Boolean setted = companyController.setPresident(company,worker);
				if(setted==false) {
					return Response.status(Status.BAD_REQUEST).entity(mapper.writeValueAsString("Something went wrong")).build();
				}
				return Response.ok().entity(mapper.writeValueAsString("Company with ID " + companyId + " now has President with ID " + workerId)).build();
			}
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			return Response.serverError().entity("An unexpected error ocurred").build();
		}
	}
	
	@PUT
	@Path("/{id}/addsector")
	@Consumes({ MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	@Transactional
	@RolesAllowed({"All","Developer","Mainteinance"})
	public Response addSectorToCompany(@PathParam("id") Long id, String json) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			if(json.isEmpty()) {
				return Response.status(Status.BAD_REQUEST).entity(mapper.writeValueAsString("Sector Type cannot be null")).build();
			}
			Sector sector = mapper.readValue(json, Sector.class);
			if(sector.getType()==null) {
				return Response.status(Status.BAD_REQUEST).entity(mapper.writeValueAsString("Sector Type cannot be null")).build();
		      }
			Company company = companyController.getById(id);
			if(company == null) {
				return Response.ok(mapper.writeValueAsString("No object found with this ID: " + id),MediaType.APPLICATION_JSON).status(Status.NOT_FOUND).build();
			}else {
				Sector persistedSector = companyController.addSector(company,sector);
				return Response.ok().entity(mapper.writeValueAsString("Sector with ID " + persistedSector.getId() + " added at Company with ID " + id + " correctly")).build();
			}
		} catch (IOException e) {
			e.printStackTrace();
			return Response.serverError().entity("An unexpected error ocurred").build();
		}
	}
	
	@DELETE
	@Path("/{companyId}/{sectorId}")
	@Produces({MediaType.APPLICATION_JSON})
	@Transactional
	@RolesAllowed({"All","Developer","Mainteinance"})
	public Response deleteSectorFromCompany(@PathParam("companyId") Long companyId, @PathParam("sectorId") Long sectorId) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			Company company = companyController.getById(companyId);
			Sector sector = sectorController.getById(sectorId);
			if(company == null) {
				return Response.ok(mapper.writeValueAsString("No Company found with this ID: " + companyId),MediaType.APPLICATION_JSON).status(Status.NOT_FOUND).build();
			}else if(sector == null) {
				return Response.ok(mapper.writeValueAsString("No Sector found with this ID: " + sectorId),MediaType.APPLICATION_JSON).status(Status.NOT_FOUND).build();
			}else {
				Boolean deleted = companyController.deleteSector(company,sector);
				if(deleted==false) {
					return Response.status(Status.BAD_REQUEST).entity(mapper.writeValueAsString("Sector with ID " + sectorId + " is not part of Company with ID " + companyId)).build();
				}else {
					return Response.ok().entity(mapper.writeValueAsString("Sector with ID " + sectorId + " unbounded of Company with ID " + companyId + " and deleted.")).build();
				}
			}
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			return Response.serverError().entity("An unexpected error ocurred").build();
		}
	}
}