package curso.rest.seguridad.tokens.servicios;


import curso.rest.seguridad.tokens.filtros.JWTAuthFilter;
import curso.rest.seguridad.tokens.filtros.JWTResponseFilter;
import java.util.HashSet;
import java.util.Set;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

/**
 */
@ApplicationPath("/tokens")
public class ClienteApplication extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> clazzes = new HashSet();
        clazzes.add(JWTAuthFilter.class);
        clazzes.add(RecursoCliente.class);
        clazzes.add(JWTResponseFilter.class);

        return clazzes;
    }
}
