package curso.rest.sse.servicios;

import java.util.Set;
import javax.ws.rs.core.Application;
import org.glassfish.jersey.media.sse.SseFeature;


@javax.ws.rs.ApplicationPath("sse")
public class ItemStoreApplication extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> resources = new java.util.HashSet<>();
        addRestResourceClasses(resources);
        return resources;
    }

    /**
     * Do not modify addRestResourceClasses() method. It is automatically
     * populated with all resources defined in the project. If required, comment
     * out calling this method in getClasses().
     */
    private void addRestResourceClasses(Set<Class<?>> resources) {
        resources.add(curso.rest.sse.servicios.ItemStoreResource.class);
        resources.add(curso.rest.sse.servicios.RecursoSSE.class);
    }

}
