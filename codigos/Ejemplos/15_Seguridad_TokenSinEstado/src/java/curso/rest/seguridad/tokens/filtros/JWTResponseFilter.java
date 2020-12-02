package curso.rest.seguridad.tokens.filtros;

import curso.rest.seguridad.tokens.jwt.JWTokenUtility;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;

public class JWTResponseFilter implements ContainerResponseFilter {

    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) throws IOException {

        if (requestContext.getProperty("auth-failed") != null) {
            Boolean failed = (Boolean) requestContext.getProperty("auth-failed");
            if (failed) {
                System.out.println("Autenticación incorrecta. Sin token de retorno.");
                return;
            }
        }

        List<Object> jwt = new ArrayList<>();
        jwt.add(JWTokenUtility.buildJWT(requestContext.getSecurityContext().getUserPrincipal().getName()));
        // jwt.add(requestContext.getHeaderString("Authorization").split(" ")[1]);
        System.out.println("Se adiciona el header de respuesta 'jwt'");
        responseContext.getHeaders().put("jwt", jwt);
    }
}
