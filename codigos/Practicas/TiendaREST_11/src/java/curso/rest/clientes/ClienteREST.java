package curso.rest.clientes;

import curso.rest.dominio.Cliente;
import curso.rest.dominio.RespuestaCliente;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.EntityTag;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class ClienteREST {

    private final static String URL_BASE = "http://localhost:7001/TiendaREST_11/servicios/";
    private static int ID_CLIENTE = 1;

    public static void main(String[] args) {
        ClienteREST ref = new ClienteREST();
        //ref.getCondicional();
        ref.putCondicional();
    }

    public void getCondicional() {
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(URL_BASE);

        // ObtenCliente
        Response respuesta = target.path("/clientes/condicional/" + ID_CLIENTE)
                .request(MediaType.APPLICATION_JSON)
                .get();
        if (respuesta.getStatus() == 200) {
            Cliente clienteOriginal = respuesta.readEntity(Cliente.class);
            EntityTag etag = respuesta.getEntityTag();
            System.out.println("CLIENTE ORIGINAL: " + respuesta.getStatus() + ": " + clienteOriginal.getNombre() + "/" + clienteOriginal.getNombre().length());
            // Actualización normal
            //this.actualizaEnOtroHilo();
            try {
                Thread.sleep(500);
            } catch (InterruptedException ex) {
                Logger.getLogger(ClienteREST.class.getName()).log(Level.SEVERE, null, ex);
            }
            // Actualización condicional
            respuesta = target.path("/clientes/condicional/" + ID_CLIENTE)
                    .request(MediaType.APPLICATION_JSON)
                    .header("If-None-Match", etag)
                    .get();
            if (respuesta.getStatus() == 200) {
                Cliente cliente2 = respuesta.readEntity(Cliente.class);
                System.out.println("200: Modificado:" + respuesta.getStatusInfo() + ": " + cliente2.getNombre());
            } else if (respuesta.getStatus() == 304) {
                System.out.println("304: No modificado");
            }

        }
        respuesta.close();
        client.close();
    }

    public void putCondicional() {
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(URL_BASE);
        // ObtenCliente
        Response respuesta = target.path("/clientes/" + ID_CLIENTE)
                .request(MediaType.APPLICATION_JSON)
                .get();
        if (respuesta.getStatus() == 200) {
            Cliente clienteOriginal = respuesta.readEntity(Cliente.class);
            System.out.println("CLIENTE ORIGINAL: " + respuesta.getStatus() + ": " + clienteOriginal.getNombre() + "/" + clienteOriginal.getNombre().length());
            // Actualización normal
            // this.actualizaEnOtroHilo();
            try {
                Thread.sleep(500);
            } catch (InterruptedException ex) {
                Logger.getLogger(ClienteREST.class.getName()).log(Level.SEVERE, null, ex);
            }
            // Get condicional
            EntityTag etag = new EntityTag(Integer.toString(clienteOriginal.getNombre().length()));
            //Date ahora = java.sql.Timestamp.valueOf(LocalDateTime.now());
            clienteOriginal.setNombre("NUEVO NOMBRE 74");
            respuesta = target.path("/clientes/condicional/" + ID_CLIENTE)
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
                Response respuesta = target.path("/clientes/" + ID_CLIENTE)
                        .request(MediaType.APPLICATION_JSON)
                        .get();
                if (respuesta.getStatus() == 200) {
                    Cliente cliente2 = respuesta.readEntity(Cliente.class);
                    System.out.println("-----------------OTRO HILO-------------------");
                    System.out.println("| OTRO HILO.1:" + respuesta.getStatusInfo() + ": " + cliente2.getNombre());
                    // Actualización normal
                    cliente2.setNombre("Nombre CONCURRENT");
                    respuesta = target.path("/clientes/" + ID_CLIENTE)
                            .request(MediaType.APPLICATION_XML)
                            .put(Entity.xml(cliente2));
                    System.out.println("| OTRO HILO.2: Código de Respuesta HTTP:" + respuesta.getStatus());
                    respuesta = target.path("/clientes/" + ID_CLIENTE)
                            .request(MediaType.APPLICATION_JSON)
                            .get();
                    if (respuesta.getStatus() == 200) {
                        cliente2 = respuesta.readEntity(Cliente.class);
                        System.out.println("| OTRO HILO.3:" + respuesta.getStatusInfo() + ": " + cliente2.getNombre());
                    }
                    System.out.println("-----------------/ OTRO HILO-------------------");

                }
            }
        }).start();

    }
}
