package curso.rest.subrecurso.servicios;

import java.io.InputStream;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;

public interface RecursoClienteRegion {

    @POST
    @Consumes("application/xml")
    public abstract Response altaCliente(InputStream is);

    @GET
    @Path("{id}")
    @Produces("application/xml")
    public abstract StreamingOutput obtenCliente(@PathParam("id") int id);

    @PUT
    @Path("{id}")
    @Consumes("application/xml")
    public abstract void cambioCliente(@PathParam("id") int id, InputStream is);

}
