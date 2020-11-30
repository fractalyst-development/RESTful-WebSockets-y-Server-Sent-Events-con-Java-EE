
package curso.rest.contenido.servicios;

import java.util.HashSet;
import java.util.Set;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

/**
 *
 * @author usuario
 */
@ApplicationPath("/servicios")
public class ContenidoApplication extends Application {
   private Set<Object> singletons = new HashSet<Object>();
   private Set<Class<?>> classes = new HashSet<Class<?>>();
   public ContenidoApplication() {
      singletons.add(new RecursoContenido());
      classes.add(JAXBMarshaller.class);
      classes.add(JAXBUnmarshaller.class);
      classes.add(JavaMarshaller.class);

   }
   @Override
   public Set<Class<?>> getClasses() {
      return classes;
   }
   @Override
   public Set<Object> getSingletons() {
      return singletons;
   }
}