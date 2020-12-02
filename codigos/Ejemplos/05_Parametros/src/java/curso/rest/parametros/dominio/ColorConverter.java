package curso.rest.parametros.dominio;

import javax.ws.rs.ext.ParamConverter;
import static curso.rest.parametros.dominio.Color.*;

/**
 *
 * @author usuario
 */
public class ColorConverter implements ParamConverter<Color> {

    @Override
    public Color fromString(String valor) {
             
        if (valor.equalsIgnoreCase(ROJO.toString())) {
            return ROJO;
        } else if (valor.equalsIgnoreCase(VERDE.toString())) {
            return VERDE;
        } else if (valor.equalsIgnoreCase(AZUL.toString())) {
            return AZUL;
        }
        return null;
        //throw new IllegalArgumentException("Color inválido: " + valor);
    }

    @Override
    public String toString(Color valor) {
        return valor.toString();
    }

}
