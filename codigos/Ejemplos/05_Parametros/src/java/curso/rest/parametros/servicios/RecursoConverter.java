package curso.rest.parametros.servicios;

import java.time.LocalDate;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import curso.rest.parametros.dominio.Color;

@Path("/pruebas")

public class RecursoConverter {

    @GET
    @Path("/converter/")
    public String testIndex(@QueryParam("fecha") LocalDate date) {
        return "hello, " + date;
    }

    @GET
    @Path("/converter/{fecha}")
    public String test(@PathParam("fecha") LocalDate date) {
        return "hello, " + date;
    }

        @GET
    @Path("/converter/{color}/{colorStr}")
    @Produces("text/plain")
    public String pruebaConverter(@PathParam("color") Color color,
            @PathParam("colorStr") String colorStr) {
        return "Color:" + color + " / colorStr:" + colorStr;
    }
    
}
