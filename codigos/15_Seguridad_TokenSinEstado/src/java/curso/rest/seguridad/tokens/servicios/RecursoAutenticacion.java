package curso.rest.seguridad.tokens.servicios;

import curso.rest.seguridad.tokens.jwt.JWTokenUtility;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

@Path("token")
public class RecursoAutenticacion {

    @Context
    SecurityContext sctx;

    @GET
    @Produces("text/plain")
    public Response auth() {
        
        System.out.println("Usuario autenticado: " + sctx.getUserPrincipal().getName());

        //this.sctx = sctx;
        String authenticatedUser = sctx.getUserPrincipal().getName();
        Response resp = Response.ok(authenticatedUser)
                .header("jwt", JWTokenUtility.buildJWT(authenticatedUser))
                .build();

        return resp;
    }

}
