package curso.rest.servicios;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Link;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;
import curso.rest.dominio.Cliente;
import curso.rest.dominio.Clientes;
import curso.rest.dominio.Direccion;
import javax.ws.rs.POST;

@Path("/clientes")

public class RecursoCliente {

    private static final Map<Integer, Cliente> DB_CLIENTE = new ConcurrentHashMap<Integer, Cliente>();
    private static final AtomicInteger IDENTIFICADOR = new AtomicInteger();

    public RecursoCliente() {
        cargaClientesPrueba(10);
    }

    @GET
    @Path("/cuantosClientes")
    @Produces(MediaType.TEXT_PLAIN)
    public String testCuantos() {
        return DB_CLIENTE.size() + " ";
    }

    @GET
    @Path("/{id}")
    @Produces({"application/xml", MediaType.APPLICATION_JSON})
    public Response testLink(@PathParam("id") int id) {

        Link link = Link.fromUri("http://{host}/servicios/clientes/{id}")
                .rel("update").type("text/plain")
                .build("localhost", 876);

        System.out.println("link: " + link.toString());
        Response respuesta = Response.noContent()
                .links(link)
                .build();
        return respuesta;
    }

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

    @GET
    @Path("/noHateoas")
    @Produces({"application/xml", MediaType.APPLICATION_JSON})
    public List<Cliente> testconsultaTodosLosClientes() {

        ArrayList<Cliente> clientes = new ArrayList<>(DB_CLIENTE.values());

        return clientes;
    }

    @GET
    @Path("/Link/Incrustado")
    @Produces({"application/xml", MediaType.APPLICATION_JSON})
    public Clientes testLinkIncrustado(@QueryParam("inicio") int inicio,
            @QueryParam("tamanio") @DefaultValue("2") int tamanio,
            @Context UriInfo uriInfo) {

        UriBuilder builder = uriInfo.getAbsolutePathBuilder();
        builder.queryParam("inicio", "{inicio}");
        builder.queryParam("tamanio", "{tamanio}");

        List<Cliente> clientes = new ArrayList<>();
        List<Link> links = new ArrayList<>();
        Clientes clientesWrapper = new Clientes();
        synchronized (DB_CLIENTE) {
            int i = 0;
            for (Cliente cliente : DB_CLIENTE.values()) {
                if (i >= inicio && i < inicio + tamanio) {
                    clientes.add(cliente);
                }
                i++;
            }
            // next link
            if (inicio + tamanio < DB_CLIENTE.size()) {
                int next = inicio + tamanio;
                URI nextUri = builder.clone().build(next, tamanio);
                Link nextLink = Link.fromUri(nextUri).rel("next").build();
                links.add(nextLink);
            }
            // previous link
            if (inicio > 0) {
                int previous = inicio - tamanio;
                if (previous < 0) {
                    previous = 0;
                }
                URI previousUri = builder.clone().build(previous, tamanio);
                Link previousLink = Link.fromUri(previousUri).rel("previous").build();
                links.add(previousLink);
            }
            clientesWrapper.asignaClientes(clientes);
            clientesWrapper.setLinks(links);
        }
        return clientesWrapper;
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
