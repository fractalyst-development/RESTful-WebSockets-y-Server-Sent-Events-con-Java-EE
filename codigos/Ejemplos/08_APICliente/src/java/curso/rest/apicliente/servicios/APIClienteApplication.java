
package curso.rest.apicliente.servicios;

import java.util.HashSet;
import java.util.Set;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

/**
 *
 * @author usuario
 */
@ApplicationPath("/servicios")
public class APIClienteApplication extends Application {
   private Set<Object> singletons = new HashSet<Object>();
   private Set<Class<?>> classes = new HashSet<Class<?>>();
   public APIClienteApplication() {
      singletons.add(new RecursoAPICliente());
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