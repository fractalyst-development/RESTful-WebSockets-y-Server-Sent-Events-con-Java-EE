package curso.rest.servicios;

import curso.rest.jaxrs.ext.CacheControlResponseFilter;
import curso.rest.jaxrs.ext.NotFoundExceptionMapper;
import curso.rest.jaxrs.ext.TestAllServerFilter;
import java.util.HashSet;
import java.util.Set;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

@ApplicationPath("servicios")
public class TiendaApplication extends Application {

    private Set<Object> singletons = new HashSet<Object>();
    private Set<Class<?>> empty = new HashSet<Class<?>>();

    public TiendaApplication() {
        singletons.add(new RecursoCliente());
        singletons.add(new NotFoundExceptionMapper());
        singletons.add(new CacheControlResponseFilter());
        singletons.add(new TestAllServerFilter());
    }

    @Override
    public Set<Class<?>> getClasses() {
        return empty;
    }

    @Override
    public Set<Object> getSingletons() {
        return singletons;
    }
}
