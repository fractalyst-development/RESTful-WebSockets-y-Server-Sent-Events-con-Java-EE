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
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.annotation.security.DeclareRoles;
import javax.ejb.Stateless;
import javax.ws.rs.POST;
import javax.ws.rs.core.SecurityContext;

//@Stateless
@Path("/seguridad/anotaciones/clientes")
@DeclareRoles({"administrador", "cliente"})
public class RecursoClienteAnotaciones {

    private static final Map<Integer, Cliente> DB_CLIENTE = new ConcurrentHashMap<Integer, Cliente>();
    private static final AtomicInteger IDENTIFICADOR = new AtomicInteger();

    public RecursoClienteAnotaciones() {
        cargaClientesPrueba(10);
    }

    @GET
    @Path("/cuantos")
    @Produces(MediaType.TEXT_PLAIN)
    public String testCuantos() {
        return DB_CLIENTE.size() + " (N)";
    }

    @GET
    @RolesAllowed("cliente")
    @Path("/cuantos/cliente")
    @Produces(MediaType.TEXT_PLAIN)
    public String testCuantosCliente(@Context SecurityContext sc) {
        System.out.println("sc." + sc.getUserPrincipal().getName());
        System.out.println("sc." + sc.getAuthenticationScheme());
        System.out.println("sc." + sc.isSecure());
        System.out.println("sc.isUserInRole(\"cliente\")" + sc.isUserInRole("cliente"));
        return DB_CLIENTE.size() + " (C)";
    }

    @GET
    @RolesAllowed("administrador")
    @Path("/cuantos/administrador")
    @Produces(MediaType.TEXT_PLAIN)
    public String testCuantosAdministradir(@Context SecurityContext sc) {
        System.out.println("sc." + sc.getUserPrincipal().getName());
        System.out.println("sc." + sc.getAuthenticationScheme());
        System.out.println("sc." + sc.isSecure());
        System.out.println("sc.isUserInRole(\"administrador\")" + sc.isUserInRole("administrador"));
        return DB_CLIENTE.size() + " (A)";
    }

    //
    @POST
    @RolesAllowed("administrador")
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
    @PermitAll
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
