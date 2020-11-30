package curso.rest.escalabilidad.servicios;

import java.net.URI;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.CacheControl;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.EntityTag;
import javax.ws.rs.core.Link;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;
import curso.rest.escalabilidad.dominio.Cliente;
import curso.rest.escalabilidad.dominio.Clientes;
import curso.rest.escalabilidad.dominio.Direccion;

@Path("/escalabilidad")

public class RecursoEscalabilidad {

    private static final Map<Integer, Cliente> DB_CLIENTE = new ConcurrentHashMap<Integer, Cliente>();
    private static final AtomicInteger IDENTIFICADOR = new AtomicInteger(875);

    public RecursoEscalabilidad() {
        cargaClientesPrueba(10);
    }

    @GET
    @Path("/cuantosClientes")
    @Produces(MediaType.TEXT_PLAIN)
    public String testCuantos() {
        return DB_CLIENTE.size() + " ";
    }

    @GET
    @Path("/clientes/Expires/{id}")
    @Produces({"application/xml", MediaType.APPLICATION_JSON})
    public Response testExpires(@PathParam("id") int id) {
        Cliente c = consultaCliente(id);
        ResponseBuilder builder = Response.ok(c);
        builder.expires(java.sql.Timestamp.valueOf(LocalDateTime.now().plusMinutes(15)));
        return builder.build();
    }

    @GET
    @Path("/clientes/Cache-Control/{id}")
    @Produces({"application/xml", MediaType.APPLICATION_JSON})
    public Response testCacheControl(@PathParam("id") int id) {
        Cliente c = consultaCliente(id);
        CacheControl cc = new CacheControl();
        cc.setMaxAge(300);
        cc.setPrivate(true);
        cc.setNoStore(true);
        ResponseBuilder builder = Response.ok(c, "application/xml");
        builder.cacheControl(cc);
        return builder.build();
    }

    @GET
    @Path("/clientes/revalidacion/{id}")
    @Produces({"application/xml", MediaType.APPLICATION_JSON})
    public Response testRevalidacion(@PathParam("id") int id,
            @Context Request request) {
        Cliente c = consultaCliente(id);
        EntityTag etag = new EntityTag(Integer.toString(c.hashCode()));
        ResponseBuilder builder = request.evaluatePreconditions(etag);
        if (builder == null) { // 200
            builder = Response.ok(c);
            builder.tag(etag);
            return builder.build();
        } else {
            // Precondiciones no cumplidas 304
            return builder.build();
        }
    }

    @PUT
    @Path("/clientes/actualizacionCondicional/{id}")
    @Produces({"application/xml", MediaType.APPLICATION_JSON})
    public Response testPUTCondicional(@PathParam("id") int id,
            Cliente clienteNuevosDatos,
            @Context Request request) {
        Cliente c = consultaCliente(id);
        EntityTag etag = new EntityTag(Integer.toString(c.getNombre().length()));
        //Date ahora = java.sql.Timestamp.valueOf(LocalDateTime.now());
        ResponseBuilder builder = request.evaluatePreconditions(etag);
        if (builder == null) { // 200
            c.setNombre(clienteNuevosDatos.getNombre());
            DB_CLIENTE.put(id, c);
            EntityTag nuevaETag = new EntityTag(Integer.toString(c.getNombre().length()));
            builder = Response.ok(c);
            builder.tag(nuevaETag);
            return builder.build();
        } else {
            // Precondiciones no cumplidas 412
            return builder.build();
        }
    }

    @PUT
    @Path("/clientes/actualizacion/{id}")
    @Produces({"application/xml", MediaType.APPLICATION_JSON})
    public Response testPUT(@PathParam("id") int id,
            Cliente clienteNuevosDatos,
            @Context Request request) {
        Cliente c = consultaCliente(id);
        c.setNombre(clienteNuevosDatos.getNombre());
        DB_CLIENTE.put(id, c);
        ResponseBuilder builder = Response.ok(c);
        return builder.build();
    }

    @GET
    @Path("/clientes/consultaCliente/{id}")
    @Produces({"application/xml", MediaType.APPLICATION_JSON})
    public Response testConsultaCliente(@PathParam("id") int id) {
        Cliente c = consultaCliente(id);
        EntityTag etag = new EntityTag(Integer.toString(c.getNombre().length()));
        ResponseBuilder builder = Response.ok(c);
        builder.tag(etag);
//        builder.lastModified(java.sql.Timestamp.valueOf(LocalDateTime.now()));
        return builder.build();
    }

    @GET
    @Path("/clientes/noHateoas")
    @Produces({"application/xml", MediaType.APPLICATION_JSON})
    public List<Cliente> testconsultaTodosLosClientes() {
        ArrayList<Cliente> clientes = new ArrayList<>(DB_CLIENTE.values());
        return clientes;
    }

    @GET
    @Path("/clientes/Link/Incrustado")
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

    //
    private Cliente consultaCliente(int id) {
        return DB_CLIENTE.values().stream().filter(c -> c.getId() == id).findFirst().orElse(new Cliente());
    }
}
