package curso.rest.servicios;

import curso.rest.dominio.Cliente;
import curso.rest.dominio.dao.ClienteDAO;
import java.util.List;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
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

    @GET
    @Path("{id}")
    @Produces({"application/xml", "application/json"})
    public Response obtenCliente(@PathParam("id") int id, @Context Request request) {

        Cliente c = dao.consultaCliente(id);

        if (c != null) {
            ResponseBuilder rb = Response.ok(c);
            return rb.build();
        } else {
            throw new NotFoundException("Cliente no localizado");
        }

    }

    @GET
    @Path("/condicional/{id}")
    @Produces({"application/xml", "application/json"})
    public Response obtenClienteCondicional(@PathParam("id") int id, @Context Request request) {
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
