
package curso.rest.respuestas.servicios;

import java.util.HashSet;
import java.util.Set;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

/**
 *
 * @author usuario
 */
@ApplicationPath("/servicios")
public class RespuestasApplication extends Application {
   private Set<Object> singletons = new HashSet<Object>();
   private Set<Class<?>> classes = new HashSet<Class<?>>();
   public RespuestasApplication() {
      singletons.add(new RecursoRespuestas());
      singletons.add(new EntityNotFoundMapper());
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