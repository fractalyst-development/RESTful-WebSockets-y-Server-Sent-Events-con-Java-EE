package curso.rest.clientes;

import curso.rest.dominio.Cliente;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class ClienteREST {

    private final static String URL_BASE = "http://localhost:7001/TiendaREST_13/servicios/";
    private static int ID_CLIENTE = 1;
    private final static String URL_COMPLETO = URL_BASE + "clientes/future/" + ID_CLIENTE;
    private static final int PAUSA_MS = 5000;

    public static void main(String[] args) {
        ClienteREST ref = new ClienteREST();
        //ref.getFuture();
        //ref.getCallback();
        ref.postAsyncResponsePriorizacion();
    }

    public void getFuture() {
        Client clienteRest = ClientBuilder.newClient();
        try {
            Future<Cliente> f1 = clienteRest.target(URL_COMPLETO).request().async().get(Cliente.class);
            int i = 0;
            while (!f1.isDone()) {
                System.out.println(i++ + " Esperando");
            }
            System.out.println("Cliente:" + f1.get());
        } catch (InterruptedException | ExecutionException ex) {
            Logger.getLogger(ClienteREST.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            clienteRest.close();
        }

    }

    public void getCallback() {
        Client clienteRest = null;
        try {
            clienteRest = ClientBuilder.newClient();
            System.out.println("1. Llamada realizada");
            Future<Response> f1 = clienteRest.target(URL_COMPLETO).request().async().get(new ClienteCallback());
            System.out.println("2. Hacer otra cosa");
            Thread.sleep(PAUSA_MS);
            System.out.println("3. Termina esa otra cosa");
        } catch (InterruptedException ex) {
            Logger.getLogger(ClienteREST.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (clienteRest != null) {
                clienteRest.close();
            }
        }

    }

    public void postAsyncResponsePriorizacion() {

        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(URL_BASE);
        Response respuesta = target.path("/clientes/" + ID_CLIENTE)
                .request(MediaType.APPLICATION_JSON)
                .get();
        if (respuesta.getStatus() == 200) {
            Cliente cliente2 = respuesta.readEntity(Cliente.class);
            System.out.println("------------------------------------");
            System.out.println("|.1:" + respuesta.getStatusInfo() + ": " + cliente2.getNombre());
            // Actualización normal
            cliente2.setNombre("Alta Asincrona 2");
            respuesta = target.path("/clientes/async-response-priorizacion")
                    .request(MediaType.APPLICATION_XML)
                    .post(Entity.xml(cliente2));
            System.out.println("| Respuesta HTTP:" + respuesta);
            System.out.println("| Código de Respuesta HTTP:" + respuesta.getStatus());
            System.out.println("| Código de Respuesta HTTP:" + respuesta.readEntity(Cliente.class).getNombre());

            System.out.println("------------------------------------");

        }

    }

}
