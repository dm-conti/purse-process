package it.tmp.restentrypoint.pursecdm.service;

import it.tmp.restentrypoint.pursecdm.dto.CreatePurseDTO;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * This is an example of JAX-RS annotated interface for
 * implementing the Rest Façade. 
 * 
 * @author Domenico Conti [domenico.conti@quigroup.it]
 * 
 */
public class CreatePurseFacadeService {
	
	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response getSomething(@PathParam(value = "id") Long id)
	{
		return null;
	}
	
	@POST
	@Path("/{dtoName}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response postSomething(@PathParam("dtoName") String dtoName, CreatePurseDTO createPurseDTO)
	{
		return null;
	}
	
	@POST
	@Path("/")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response postSomething(CreatePurseDTO createPurseDTO)
	{
		return null;
	}
}
