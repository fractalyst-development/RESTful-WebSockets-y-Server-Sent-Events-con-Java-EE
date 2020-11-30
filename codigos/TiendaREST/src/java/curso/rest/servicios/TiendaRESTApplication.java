
package curso.rest.servicios;

import java.util.HashSet;
import java.util.Set;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

/**
 *
 * @author usuario
 */
@ApplicationPath("/servicios")
public class TiendaRESTApplication extends Application {
   private Set<Object> singletons = new HashSet<Object>();
   private Set<Class<?>> classes = new HashSet<Class<?>>();
   public TiendaRESTApplication() {
      singletons.add(new RecursoCliente());
      singletons.add(new RecursoPedido());
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