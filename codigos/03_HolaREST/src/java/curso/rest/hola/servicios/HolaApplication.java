
package curso.rest.hola.servicios;

import java.util.HashSet;
import java.util.Set;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

/**
 *
 * @author usuario
 */
@ApplicationPath("/servicios")
public class HolaApplication extends Application {
   private Set<Object> singletons = new HashSet<Object>();
   private Set<Class<?>> empty = new HashSet<Class<?>>();
   public HolaApplication() {
      singletons.add(new RecursoCliente());
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