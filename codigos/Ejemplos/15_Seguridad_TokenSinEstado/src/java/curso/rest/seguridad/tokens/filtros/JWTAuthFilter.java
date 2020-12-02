package curso.rest.seguridad.tokens.filtros;

import curso.rest.seguridad.tokens.jwt.RsaKeyProducer;
import java.io.IOException;
import java.security.Principal;
import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import org.jose4j.jwk.RsaJsonWebKey;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwt.consumer.InvalidJwtException;
import org.jose4j.jwt.consumer.JwtConsumer;
import org.jose4j.jwt.consumer.JwtConsumerBuilder;

@Priority(Priorities.AUTHENTICATION)
public class JWTAuthFilter implements ContainerRequestFilter {

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {

        String encabezadoAutorizacion = requestContext.getHeaderString("Authorization") != null ? requestContext.getHeaderString("Authorization") : "";

        // Verifica que el encabezado inicie con el valor esperado
        if (encabezadoAutorizacion.startsWith("Bearer")) {
            try {
                System.out.println("Verificar la estructura");
                System.out.println("JWT: " + encabezadoAutorizacion.split(" ")[1]);
                final String subject = validate(encabezadoAutorizacion.split(" ")[1]);
                final SecurityContext securityContext = requestContext.getSecurityContext();
                if (subject != null) {
                    requestContext.setSecurityContext(new SecurityContext() {
                        @Override
                        public Principal getUserPrincipal() {
                            return new Principal() {
                                @Override
                                public String getName() {
                                    System.out.println("Principal programático: " + subject);
                                    return subject;
                                }
                            };
                        }

                        @Override
                        public boolean isUserInRole(String role) {
                            return securityContext.isUserInRole(role);
                        }

                        @Override
                        public boolean isSecure() {
                            return securityContext.isSecure();
                        }

                        @Override
                        public String getAuthenticationScheme() {
                            return securityContext.getAuthenticationScheme();
                        }
                    });
                }
            } catch (InvalidJwtException ijex) {
                System.out.println("JWT Exception:" + ijex);
                requestContext.setProperty("auth-failed", true);
                requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
            }
        } else {
            System.out.println("Sin Token JWT");
            requestContext.setProperty("auth-failed", true);
            requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
        }

    }

    private String validate(String jwt) throws InvalidJwtException {
        String subject = null;
        RsaJsonWebKey rsaJsonWebKey = RsaKeyProducer.produce();
        System.out.println("RSA hashcode: " + rsaJsonWebKey.hashCode());

        JwtConsumer jwtConsumer = new JwtConsumerBuilder()
                .setRequireSubject()                        // the JWT requiere un claim subject
                .setVerificationKey(rsaJsonWebKey.getKey()) // verifica la fimra con la llave pública
                .build();                                   // Crea la instancia de JwtConsumer
        try {
            //  REcuperación y validación de claims
            JwtClaims jwtClaims = jwtConsumer.processToClaims(jwt);
            subject = (String) jwtClaims.getClaimValue("sub");
            System.out.println("Validación exitosa de claims: " + jwtClaims);
        } catch (InvalidJwtException e) {
            e.printStackTrace(); // NO PARA PRODUCCIÓN
            throw e;
        }
        return subject;
    }

}
