package curso.rest.seguridad.tokens.servicios;

import java.util.HashSet;
import java.util.Set;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

/**
 * Servidor de autenticación
 */
@ApplicationPath("/servicio-de-autenticacion")
public class ServicioDeAutenticacionApplication extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> clazzes = new HashSet();
        clazzes.add(RecursoAutenticacion.class);
        return clazzes;
    }

}
