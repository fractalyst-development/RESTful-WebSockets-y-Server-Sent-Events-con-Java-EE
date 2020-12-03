package curso.rest.servicios;

import curso.rest.dominio.Cliente;
import curso.rest.dominio.dao.ClienteDAO;
import java.net.URI;
import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.EntityTag;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

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
    @Path("{id}")
    @Consumes({"application/xml", "application/json"})
    @Produces({"application/xml", "application/json"})
    public Response cambiosCliente(@PathParam("id") int id, Cliente c) {
        c.setId(id);
        Cliente cC = dao.cambiosCliente(c);
        return Response.ok(cC).build();
    }

    @PUT
    @Path("/condicional/{id}")
    @Consumes({"application/xml", "application/json"})
    @Produces({"application/xml", "application/json"})
    public Response cambiosClienteCondicional(@PathParam("id") int id, Cliente clienteNuevosDatos, @Context Request request) {
        Cliente clienteEnServidor = dao.consultaCliente(id);
        EntityTag etag = new EntityTag(Integer.toString(clienteEnServidor.getNombre().length()));
        //Date ahora = java.sql.Timestamp.valueOf(LocalDateTime.now());
        ResponseBuilder builder = request.evaluatePreconditions(etag);
        if (builder == null) { // 200
            clienteEnServidor.setNombre(clienteNuevosDatos.getNombre());
            dao.cambiosCliente(clienteEnServidor);
            EntityTag nuevaETag = new EntityTag(Integer.toString(clienteEnServidor.getNombre().length()));
            builder = Response.ok(clienteEnServidor);
            builder.tag(nuevaETag);
            return builder.build();
        } else {
            // Precondiciones no cumplidas 412
            return builder.build();
        }

    }

    @GET
    @Path("{id}")
    @Produces({"application/xml", "application/json"})
    public Response obtenCliente(@PathParam("id") int id, @Context Request request) {

        Cliente c = dao.consultaCliente(id);

        if (c != null) {
            EntityTag etag = new EntityTag(Integer.toString(c.getNombre().length()));
            ResponseBuilder rb = Response.ok(c);
            rb.tag(etag);
            return rb.build();
        } else {
            throw new NotFoundException("Cliente no localizado");
        }

    }

    @GET
    @Path("/condicional/{id}")
    @Produces({"application/xml", "application/json"})
    public Response obtenClienteCondicional(@PathParam("id") int id, @Context Request request) {
        Cliente res = new Cliente();
        Cliente c = dao.consultaCliente(id);

        if (c != null) {
            EntityTag etag = new EntityTag(Integer.toString(c.hashCode()));
            ResponseBuilder rb = request.evaluatePreconditions(etag);
            if (rb == null) {   // 200
                rb = Response.ok(c);
                rb.tag(etag);
                return rb.build();
            } else {            // 304
                return rb.build();
            }
        } else {
            throw new NotFoundException("Cliente no localizado");
        }

//        CacheControl cc = new CacheControl();
//        cc.setMaxAge(300);
//        cc.setPrivate(true);
//        cc.setNoStore(true);
//        rb.cacheControl(cc);
        //return rb.build();
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
