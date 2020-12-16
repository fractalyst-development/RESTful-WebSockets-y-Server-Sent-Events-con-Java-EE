package curso.rest.seguridad.tokens.servicios;

import curso.rest.seguridad.tokens.dominio.Cliente;
import curso.rest.seguridad.tokens.dominio.Direccion;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

@Path("/clientes")
public class RecursoCliente {

    private static final Map<Integer, Cliente> DB_CLIENTE = new ConcurrentHashMap<Integer, Cliente>();
    private static final AtomicInteger IDENTIFICADOR = new AtomicInteger();
    
    public RecursoCliente() {
        cargaClientesPrueba(10);
    }

    @GET
    @Path("/{id}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response consultaCliente(@Context SecurityContext ctx, @PathParam("id") int id) {
        System.out.println("ctx.getUserPrincipal().getName(): " + ctx.getUserPrincipal().getName());
        final Cliente cliente = DB_CLIENTE.get(id);
        if (cliente == null) {
            throw new NotFoundException();
        }
        Response resp = Response.ok(cliente).build();
        return resp;
    }


    @GET
    @Path("/cuantos")
    @Produces(MediaType.TEXT_PLAIN)
    public String testCuantos() {
        return DB_CLIENTE.size() + " ";
    }

    //
    private void cargaClientesPrueba(int cantidadClientes) {
        for (int i = 0; i < cantidadClientes; i++) {
            Cliente c = this.creaCliente();
            DB_CLIENTE.put(c.getId(), c);
        }
    }

    //
    private Cliente creaCliente() {
        Cliente clienteSalida = new Cliente();
        clienteSalida.setId(IDENTIFICADOR.incrementAndGet());
        clienteSalida.setNombre("Romeo" + clienteSalida.getId());
        clienteSalida.setApellidos("Montesco");
        Direccion direccion = new Direccion();
        direccion.setCalle("Vía Capello, 23");
        direccion.setCiudad("Verona");
        direccion.setEstado("Véneto");
        direccion.setCodigoPostal(37100);
        clienteSalida.setDireccion(direccion);
        return clienteSalida;
    }
}
