package curso.rest.servicios;

import curso.rest.dominio.Cliente;
import curso.rest.dominio.dao.ClienteDAO;
import java.net.URI;
import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

@Path("/clientes")
public class RecursoCliente {

    private ClienteDAO dao;

    public RecursoCliente() {
        dao = new ClienteDAO();
    }

    @POST
    @Consumes({"application/xml", "application/json"})
    @Produces({"application/xml", "application/json"})
    public Response altaCliente(Cliente c) {
        Cliente nuevoCliente = dao.altaCliente(c);
        return Response.created(URI.create("/clientes/" + nuevoCliente.getId())).build();
    }

    @DELETE
    @Path("{id}")
    @Produces("application/xml")
    public void bajaCliente(@PathParam("id") int id) {
        dao.bajaCliente(id);

    }

    @PUT
    @Consumes({"application/xml", "application/json"})
    @Produces({"application/xml", "application/json"})
    public Response cambiosCliente(Cliente c) {
        Cliente nuevoCliente = dao.cambiosCliente(c);
        return Response.created(URI.create("/clientes/" + nuevoCliente.getId())).build();
    }

    @GET
    @Path("{id}")
    @Produces({"application/xml", "application/json"})
    public Cliente obtenCliente(@PathParam("id") int id) {
        return dao.consultaCliente(id);
    }

    final String TAMANHIO = "5";

    @GET
    @Produces({"application/xml", "application/json"})
    public List<Cliente> consultaClientes(@DefaultValue("id") @QueryParam("ordenarPor") List<String> ordenarPor,
            @DefaultValue(TAMANHIO) @QueryParam("tamanhio") int tamanhio,
            @DefaultValue("1") @QueryParam("pagina") int pagina) {
        return dao.consultaClientes(tamanhio, pagina, ordenarPor);
    }
}
