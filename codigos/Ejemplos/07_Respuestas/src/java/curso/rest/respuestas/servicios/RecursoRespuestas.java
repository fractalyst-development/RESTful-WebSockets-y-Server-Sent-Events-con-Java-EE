package curso.rest.respuestas.servicios;

import javax.persistence.EntityNotFoundException;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import curso.rest.respuestas.dominio.Cliente;
import javax.ws.rs.core.MediaType;

@Path("/respuestas")

public class RecursoRespuestas {

    @POST
    @Produces("application/json")
    @Consumes("application/json")
    public Cliente altaCliente(Cliente nuevoCliente) {
        return null;
    }

    @Path("{id}")
    @DELETE
    public void bajaCliente(@PathParam("id") int id) {
    }

    @PUT
    @Path("{id}")
    @Consumes("application/json")
    public void cambioCliente(@PathParam("id") int id, Cliente cliente) {
    }

    @Path("{id}")
    @GET
    @Produces("application/json")
    public Cliente consultaCliente(@PathParam("id") int id) {
        return null;
    }

    @GET
    @Path("/NewCookie")
    public Response testNewCookie() {
        NewCookie cookie = new NewCookie("nombreCookie", "valorCookie");
        ResponseBuilder builder = Response.ok("Hola NewCookie", "text/plain");
        return builder.cookie(cookie).build();
    }

    @GET
    @Path("/test2")
    public Response test2() {

        throw new WebApplicationException(Response
                .status(400)
                .type(MediaType.TEXT_PLAIN)
                .entity("Invalid state transition")
                .build());
    }

    @GET
    @Path("/test3")
    public Response test3() {

//        throw new WebApplicationException(Response.ok().type(MediaType.TEXT_PLAIN)
//                .entity("Invalid state sdfsdfsdf")
//                .build());
        throw new NotFoundException(Response.ok().type(MediaType.TEXT_PLAIN)
                .entity("Invalid state sdfsdfsdf")
                .build());
   
        

    }

    @GET
    @Path("/test4")
    public Response test4() {
        throw new WebApplicationException();
    }

    @GET
    @Path("/NotFound/{id}")
    @Produces("application/xml")
    public Cliente consultaClienteNotFound(@PathParam("id") int id) {

        Cliente cliente = null;//consultaCliente(id);
        if (cliente == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        return cliente;
    }

    @GET
    @Path("/ExceptionMapper/{id}")
    @Produces("application/xml")
    public Cliente consultaClienteExceptionMapper(@PathParam("id") int id) {

        Cliente cliente = null;//consultaCliente(id);
        if (cliente == null) {
            throw new EntityNotFoundException();
        }
        return cliente;
    }

    @GET
    @Path("/NotFoundException/{id}")
    @Produces("application/xml")
    public Cliente consultaClienteNotFoundException(@PathParam("id") int id) {

        Cliente cliente = null;//consultaCliente(id);
        if (cliente == null) {
            throw new NotFoundException();
        }
        return cliente;
    }

}
