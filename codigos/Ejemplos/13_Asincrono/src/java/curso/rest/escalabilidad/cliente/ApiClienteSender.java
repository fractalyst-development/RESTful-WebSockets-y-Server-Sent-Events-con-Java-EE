package curso.rest.escalabilidad.cliente;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.EntityTag;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import curso.rest.escalabilidad.dominio.Cliente;
import curso.rest.escalabilidad.dominio.Direccion;

/**
 *
 * @author usuario
 */
public class ApiClienteSender {

    private final static String URL_BASE = "http://localhost:7001/13_Asincrono/servicios";

    public static void main(String[] args) {
        ApiClienteSender ref = new ApiClienteSender();
        ref.postEnvio();
    }

    public void postEnvio() {
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(URL_BASE);
        // ObtenCliente
        Response respuesta;
        respuesta = target.path("/asincrono/p-s/envio")
                .request(MediaType.TEXT_PLAIN)
                .post(Entity.entity("mensaje", MediaType.TEXT_PLAIN));
        System.out.println("respuesta: " + respuesta.getStatus());

        respuesta.close();
        client.close();
    }

    public void actualizaEnOtroHilo() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Client client = ClientBuilder.newClient();
                WebTarget target = client.target(URL_BASE);
                Response respuesta = target.path("/clientes/consultaCliente/" + 876)
                        .request(MediaType.APPLICATION_JSON)
                        .get();
                if (respuesta.getStatus() == 200) {
                    Cliente cliente2 = respuesta.readEntity(Cliente.class);
                    System.out.println("OTRO HILO.1:" + respuesta.getStatusInfo() + ": " + cliente2.getNombre());
                    // Actualización normal
                    cliente2.setNombre("Nombre CONCURRENT");
                    respuesta = target.path("/clientes/actualizacion/" + 876)
                            .request(MediaType.APPLICATION_XML)
                            .put(Entity.xml(cliente2));
                    System.out.println("OTRO HILO.2: Código de Respuesta HTTP:" + respuesta.getStatus());
                    respuesta = target.path("/clientes/consultaCliente/" + 876)
                            .request(MediaType.APPLICATION_JSON)
                            .get();
                    if (respuesta.getStatus() == 200) {
                        cliente2 = respuesta.readEntity(Cliente.class);
                        System.out.println("OTRO HILO.3:" + respuesta.getStatusInfo() + ": " + cliente2.getNombre());
                    }

                }
            }
        }).start();

    }
}
