
package curso.rest.seguridad.servicios;

import java.util.HashSet;
import java.util.Set;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

/**
 *
 * @author usuario
 */
@ApplicationPath("/servicios")
public class SeguridadApplication extends Application {
   private Set<Object> singletons = new HashSet<Object>();
   private Set<Class<?>> classes = new HashSet<Class<?>>();
   public SeguridadApplication() {
      classes.add(RecursoClienteAnotaciones.class);
      classes.add(RecursoClienteDD.class);
      classes.add(RecursoPedido.class);
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