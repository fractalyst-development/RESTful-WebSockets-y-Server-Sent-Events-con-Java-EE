package curso.rest.seguridad.servicios;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;
import curso.rest.seguridad.dominio.Cliente;
import curso.rest.seguridad.dominio.Direccion;
import javax.ws.rs.POST;

@Path("/seguridad/dd/clientes")
public class RecursoClienteDD {

    private static final Map<Integer, Cliente> DB_CLIENTE = new ConcurrentHashMap<Integer, Cliente>();
    private static final AtomicInteger IDENTIFICADOR = new AtomicInteger();

    public RecursoClienteDD() {
        cargaClientesPrueba(10);
    }

    @GET
    @Path("/cuantos")
    @Produces(MediaType.TEXT_PLAIN)
    public String testCuantos() {
        return DB_CLIENTE.size() + " (N)";
    }

    @GET
    @Path("/cuantos/cliente")
    @Produces(MediaType.TEXT_PLAIN)
    public String testCuantosCliente() {
        return DB_CLIENTE.size() + " (C)";
    }

    @GET
    @Path("/cuantos/administrador")
    @Produces(MediaType.TEXT_PLAIN)
    public String testCuantosAdministradir() {
        return DB_CLIENTE.size() + " (A)";
    }

    //
    @POST
    @Produces({"application/xml", MediaType.APPLICATION_JSON})
    public Response altaCliente(Cliente c, @Context UriInfo uriInfo) {
        final int id = IDENTIFICADOR.incrementAndGet();
        c.setId(id);
        DB_CLIENTE.put(id, c);
        UriBuilder uriBuilder = uriInfo.getAbsolutePathBuilder();
        uriBuilder.path(Integer.toString(id));
        return Response.created(uriBuilder.build()).build();
    }

    @POST
    @Path("/confidential")
    @Produces({"application/xml", MediaType.APPLICATION_JSON})
    public Response altaClienteConfidential(Cliente c, @Context UriInfo uriInfo) {
        final int id = IDENTIFICADOR.incrementAndGet();
        c.setId(id);
        DB_CLIENTE.put(id, c);
        UriBuilder uriBuilder = uriInfo.getAbsolutePathBuilder();
        uriBuilder.path(Integer.toString(id));
        return Response.created(uriBuilder.build()).build();
    }

    @GET
    @Produces({"application/xml", MediaType.APPLICATION_JSON})
    public List<Cliente> consultaClientes() {
        ArrayList<Cliente> clientes = new ArrayList<>(DB_CLIENTE.values());
        return clientes;
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
