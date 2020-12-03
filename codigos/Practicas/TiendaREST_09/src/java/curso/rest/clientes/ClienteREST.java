package curso.rest.clientes;

import curso.rest.dominio.Cliente;
import java.util.List;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;

public class ClienteREST {

    public static void main(String[] args) {
        Invocation i = ClientBuilder.newClient().target("http://localhost:7001/TiendaREST_09/servicios/clientes?tamanhio=3")
                .request()
                .accept(MediaType.APPLICATION_JSON).buildGet();

        List<Cliente> r = i.invoke(new GenericType<List<Cliente>>() { });
        r.forEach(c->System.out.println(c.getNombre()));
    }
}
