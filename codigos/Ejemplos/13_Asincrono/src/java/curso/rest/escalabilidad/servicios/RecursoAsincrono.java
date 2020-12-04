package curso.rest.escalabilidad.servicios;

import java.net.URI;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.CacheControl;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.EntityTag;
import javax.ws.rs.core.Link;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;
import curso.rest.escalabilidad.dominio.Cliente;
import curso.rest.escalabilidad.dominio.Clientes;
import curso.rest.escalabilidad.dominio.Direccion;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;

@Path("/asincrono")

public class RecursoAsincrono {

    protected static List<AsyncResponse> responses = new ArrayList<>();

    public RecursoAsincrono() {

    }

    @GET
    @Path("/ping")
    @Produces(MediaType.TEXT_PLAIN)
    public String testCuantos() {
        return " ...pong ";
    }

    @GET
    @Path("/p-s/registro")
    @Produces({MediaType.WILDCARD})
    public synchronized void testPublishSubscribeRegistro(@Suspended AsyncResponse response) {
        responses.add(response);
    }

    @POST
    @Path("/p-s/envio")
    @Consumes({"*/*"})
    @Produces({"*/*"})
    public synchronized void testPublishSubscribeEnvia(String mensaje) {
        for (AsyncResponse aR : responses) {
            aR.resume(Response.ok(mensaje).build());
        }
    }

}
