package curso.rest.clientes;

import curso.rest.dominio.Cliente;
import javax.ws.rs.client.InvocationCallback;
import javax.ws.rs.core.Response;

/**
 *
 * @author usuario
 */
public class ClienteCallback implements InvocationCallback<Response> {

    @Override
    public void completed(Response response) {
        if (response.getStatus() == 200) {
            System.out.println("Desde el callback: " + (response.readEntity(Cliente.class)).getNombre());
        } else {
            System.out.println("Status :" + response.getStatus());
        }

    }

    @Override
    public void failed(Throwable throwable) {
        System.out.println("ERROR: " + throwable.getMessage());
    }

}
