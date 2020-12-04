package curso.rest.escalabilidad.cliente;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import curso.rest.escalabilidad.dominio.Cliente;

/**
 *
 * @author usuario
 */
public class ApiCliente {
    
    private final static String URL_BASE = "http://localhost:7001/13_Asincrono/servicios";
    
    public static void main(String[] args) {
        ApiCliente ref = new ApiCliente();
        ref.getRegistro();
    }
    
    public void getRegistro() {
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(URL_BASE);
        // ObtenCliente
        Response respuesta = target.path("/asincrono/p-s/registro")
                .request(MediaType.APPLICATION_JSON)
                .get();
        if (respuesta.getStatus() == 200) {
            //Cliente clienteOriginal = respuesta.readEntity(Cliente.class);
            System.out.println("RESPUESTA:" + respuesta.readEntity(String.class));

            try {
                Thread.sleep(500);
            } catch (InterruptedException ex) {
                Logger.getLogger(ApiCliente.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        respuesta.close();
        client.close();
    }
}
