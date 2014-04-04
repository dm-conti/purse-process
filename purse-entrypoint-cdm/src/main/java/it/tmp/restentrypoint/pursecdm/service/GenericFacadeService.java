package it.tmp.restentrypoint.pursecdm.service;

import it.tmp.restentrypoint.pursecdm.dto.SomethingDTO;
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
 * @author Mario Gallo [consulenti.mgallo@quigroup.it]
 * 
 */
public class GenericFacadeService {
	
	@GET
	@Path("/{id}")
	public Response getSomething(@PathParam(value = "id") Long id)
	{
		return null;
	}
	
	@POST
	@Path("/")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response postSomething(SomethingDTO somethingDto)
	{
		return null;
	}
}
