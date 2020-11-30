
package curso.rest.parametros.servicios;

import java.util.HashSet;
import java.util.Set;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import curso.rest.parametros.dominio.ColorConverterProvider;
import curso.rest.parametros.dominio.LocalDateParamConverterProvider;

/**
 *
 * @author usuario
 */
@ApplicationPath("/servicios")
public class ParametrosApplication extends Application {
   private Set<Object> singletons = new HashSet<Object>();
   private Set<Class<?>> empty = new HashSet<Class<?>>();
   public ParametrosApplication() {
      singletons.add(new RecursoPathParam());
      singletons.add(new RecursoParams());
      singletons.add(new RecursoConverter());
      singletons.add(new ColorConverterProvider());
      singletons.add(new LocalDateParamConverterProvider());
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