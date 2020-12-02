package curso.rest.parametros.dominio;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import javax.ws.rs.ext.ParamConverter;
import javax.ws.rs.ext.ParamConverterProvider;
import javax.ws.rs.ext.Provider;

/**
 *
 * @author usuario
 */
@Provider
public class ColorConverterProvider implements ParamConverterProvider {

    @Override
    public <T> ParamConverter<T> getConverter(Class<T> rawType,
            Type genericType,
            Annotation[] annotations) {

        if (rawType.equals(curso.rest.parametros.dominio.Color.class)) {
            return (ParamConverter<T>) new ColorConverter();
        }

        return null;
    }
}
