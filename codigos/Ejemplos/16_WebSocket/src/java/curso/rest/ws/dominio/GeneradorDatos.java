package curso.rest.ws.dominio;

import java.util.concurrent.ThreadLocalRandom;

/**
 *
 * @author usuario
 */
public class GeneradorDatos {

    private static GeneradorDatos instance = new GeneradorDatos();
    private static int valor = 0;
    private static int cuantos = 0;

    private GeneradorDatos() {
    }
    //

    public static GeneradorDatos getInstance() {
        return instance;
    }
    //

    public int getValor() {
        valor = ThreadLocalRandom.current().nextInt();
        return valor;
    }

    public int getCuantos() {
        cuantos = ThreadLocalRandom.current().nextInt();
        return cuantos;
    }

    public static String aJSON() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        sb.append("'indice':").append(GeneradorDatos.getInstance().getCuantos());
        sb.append(", 'valor':").append(GeneradorDatos.getInstance().getValor());
        sb.append("}");
        return sb.toString();
    }
}
