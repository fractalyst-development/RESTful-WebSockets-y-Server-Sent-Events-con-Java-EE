package curso.rest.deploy.servicios;

import java.util.HashSet;
import java.util.Set;
import javax.servlet.ServletContext;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.Context;

/**
 *
 * @author usuario
 */
@ApplicationPath("/servicios")
public class ParametroContextoApplication extends Application {

    private Set<Object> singletons = new HashSet<Object>();
    private Set<Class<?>> classes = new HashSet<Class<?>>();

    public ParametroContextoApplication() {
//        singletons.add(new RecursoParametroContexto());
    }

    @Override
    public Set<Class<?>> getClasses() {
        return classes;
    }

    @Context
    ServletContext sc;

    @Override
    public Set<Object> getSingletons() {

        int paramFromContext = 0;
        try {
            String valorParam = sc.getInitParameter("parametroInicio");
            paramFromContext = Integer.parseInt(valorParam);
            RecursoParametroContexto rA = new RecursoParametroContexto();
            rA.setParametroContexto(paramFromContext);
            singletons.add(rA);
        } catch (Exception e) {
            System.out.println("Ex: " + e.getMessage());
        }

        return singletons;
    }
}
