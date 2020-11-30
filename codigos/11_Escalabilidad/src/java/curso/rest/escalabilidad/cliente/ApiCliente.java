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
public class ApiCliente {

    private final static String URL_BASE = "http://localhost:7001/11_Escalabilidad/servicios/escalabilidad";

    public static void main(String[] args) {
        ApiCliente ref = new ApiCliente();
        ref.putCondicional();
    }

    public void putCondicional() {
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(URL_BASE);
        // ObtenCliente
        Response respuesta = target.path("/clientes/consultaCliente/" + 876)
                .request(MediaType.APPLICATION_JSON)
                .get();
        if (respuesta.getStatus() == 200) {
            Cliente clienteOriginal = respuesta.readEntity(Cliente.class);
            System.out.println("CLIENTE ORIGINAL: " + respuesta.getStatus() + ": " + clienteOriginal.getNombre()+ "/" + clienteOriginal.getNombre().length());
            // Actualización normal
            this.actualizaEnOtroHilo();
            try {
                Thread.sleep(500);
            } catch (InterruptedException ex) {
                Logger.getLogger(ApiCliente.class.getName()).log(Level.SEVERE, null, ex);
            }
            // Actualización condicional
            EntityTag etag = new EntityTag(Integer.toString(clienteOriginal.getNombre().length()));
            Date ahora = java.sql.Timestamp.valueOf(LocalDateTime.now());
            clienteOriginal.setNombre("NUEVO NOMBRE 3");
            respuesta = target.path("/clientes/actualizacionCondicional/" + 876)
                    .request(MediaType.APPLICATION_XML)
                    .header("If-Match", etag)
                    .put(Entity.xml(clienteOriginal));
            System.out.println("CONDICIONAL: Código de Respuesta HTTP:" + respuesta.getStatus());
        }
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
