package curso.rest.parametros.servicios;

import java.util.List;
import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.GET;
import javax.ws.rs.MatrixParam;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.PathSegment;
import javax.ws.rs.core.UriInfo;
import curso.rest.parametros.dominio.Color;

/**
 *
 * @author usuario
 */
@Path("/autos")
@ApplicationScoped
public class RecursoPathParam {


    @GET
    @Path("/matrix/{marca}/{submarca}/{anio}")
    @Produces("text/plain")
    public String obtenDesdeMatrixParam(@PathParam("marca") String marca,
            @PathParam("submarca") PathSegment submarca,
            @MatrixParam("color") String color,
            @PathParam("anio") String anio) {
        return "Color:" + color + " / año:" + anio + " / marca:" + marca + " / submarca:" + submarca.getPath();
    }

    @GET
    @Path("/segment/{marca}/{submarca}/{anio}")
    @Produces("text/plain")
    public String obtenDesdePathSegment(@PathParam("marca") String marca,
            @PathParam("submarca") PathSegment submarca,
            @PathParam("anio") String anio) {
        String color = submarca.getMatrixParameters().getFirst("color");
        return "Color:" + color + " / año:" + anio + " / marca:" + marca + " / submarca:" + submarca.getPath();
    }

    @GET
    @Path("/segments/{marca}/{submarca : .+}/anio/{anio}")
    @Produces("text/plain")
    public String obtenDesdeMultiplesPathSegments(@PathParam("marca") String marca,
            @PathParam("submarca") List<PathSegment> submarca,
            @PathParam("anio") String anio) {
        String salida = "Año:" + anio + " / marca:" + marca + " / submarca: ";
        salida = submarca.stream().map((segment) -> "-" + segment.getPath()).reduce(salida, String::concat);
        return salida;
    }

    @GET
    @Path("/uriinfo/{marca}/{submarca}/{anio}")
    @Produces("text/plain")
    public String getFromUriInfo(@Context UriInfo info) {
        String marca = info.getPathParameters().getFirst("marca");
        String anio = info.getPathParameters().getFirst("anio");
        PathSegment submarca = info.getPathSegments().get(3);
        String color = submarca.getMatrixParameters().getFirst("color");

        return "A " + color + " " + anio + " " + marca + " " + submarca.getPath();
    }
}
