package curso.rest.parametros.servicios;

import java.net.URI;
import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import javax.ws.rs.BeanParam;
import javax.ws.rs.CookieParam;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.Encoded;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import curso.rest.parametros.dominio.Cliente;
import curso.rest.parametros.dominio.ClienteEntradaBean;
import javax.ws.rs.Consumes;

/**
 *
 * @author usuario
 */
@Path("/clientes")
public class RecursoParams {

    private Map<Integer, Cliente> customerDB = new ConcurrentHashMap<Integer, Cliente>();
    private AtomicInteger idCounter = new AtomicInteger();

    public RecursoParams() {
    }

    @POST
    @Produces("text/html")
    public Response altaCliente(@FormParam("nombre") String nombre,
            @FormParam("apellidos") String apellidos) {
        Cliente cliente = new Cliente();
        cliente.setId(idCounter.incrementAndGet());
        cliente.setNombre(nombre);
        cliente.setApellidos(apellidos);
        customerDB.put(cliente.getId(), cliente);
        System.out.println("Cliente creado id:" + cliente.getId());
        String output = "Created customer <a href=\"clientes/" + cliente.getId() + "\">" + cliente.getId() + "</a>";
        String fechaUltimaVisita = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.LONG).format(new Date());
        URI location = URI.create("/clientes/" + cliente.getId());
        return Response.created(location)
                .entity(output)
                .cookie(new NewCookie("ultima-visita", fechaUltimaVisita))
                .build();

    }

    @GET
    @Path("{id}")
    @Produces("text/plain")
    public Response getCustomer(@PathParam("id") int id,
            @HeaderParam("User-Agent") String userAgent,
            @CookieParam("last-visit") String date) {
        final Cliente customer = customerDB.get(id);
        if (customer == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        String output = "User-Agent: " + userAgent + "\r\n";
        output += "Last visit: " + date + "\r\n\r\n";
        output += "Customer: " + customer.getNombre() + " " + customer.getApellidos();
        String lastVisit = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.LONG).format(new Date());
        return Response.ok(output)
                .cookie(new NewCookie("last-visit", lastVisit))
                .build();
    }

    @GET
    @Path("/query")
    @Produces("text/plain")
    public String obtenClientes(@QueryParam("inicio") int inicio,
            @QueryParam("tamanio") int tamanio) {
        return "Inicio:" + inicio + " / tamaño:" + tamanio;
    }

    @GET
    @Path("/query/uriinfo")
    @Produces("application/xml")
    public String obtenClientes(@Context UriInfo info) {
        String inicio = info.getQueryParameters().getFirst("inicio");
        String tamanio = info.getQueryParameters().getFirst("tamanio");
        return "Inicio:" + inicio + " / tamaño:" + tamanio;
    }

    @POST
    @Path("/form")
    @Produces("application/xml")
    public String altaClienteForm(@FormParam("nombre") String nombre,
            @FormParam("apellidos") String apellidos) {

        return "Nombre:" + nombre + " / apellidos:" + apellidos;
    }

    @GET
    @Path("/header")
    @Produces("application/xml")
    public String obtenEncabezado(@HeaderParam("Referer") String encabezadoReferer) {
        return "Encabezado Referer:" + encabezadoReferer;
    }

    @GET
    @Path("/header/httpheaders")
    @Produces("text/txt")
    public String get(@Context HttpHeaders headers) {
        StringBuilder encabezados = new StringBuilder();
        headers.getRequestHeaders().keySet().forEach((encabezado) -> {
            encabezados.append(encabezado).append(":").append(headers.getRequestHeader(encabezado).get(0)).append("\n");
        });
        return encabezados.toString();
    }

    @GET
    @Path("/cookie")
    @Produces("application/xml")
    public String obtenCookie(@CookieParam("idCliente") String id) {
        return "idCliente:" + id;
    }

    @GET
    @Path("/cookie/cookie")
    @Consumes("application/marco")
    @Produces("application/marco")
    public String obtenCookie(@CookieParam("idCliente") Cookie id) {
        return "idCliente:" + id;
    }

    //
    @POST
    @Path("/bean/{id}")
    public String altaCliente(@BeanParam ClienteEntradaBean cliente) {
        StringBuilder sb = new StringBuilder();
        sb.append("id(PathParam):").append(cliente.getId()).append("\n");
        sb.append("Nombre(QueryParam):").append(cliente.getNombre()).append("\n");
        sb.append("Apellidos(FormParam):").append(cliente.getApellidos()).append("\n");
        sb.append("Region(MatrixParam):").append(cliente.getRegion()).append("\n");
        sb.append("Tipo Cliente(HeaderParam):").append(cliente.getTipoCliente()).append("\n");
        sb.append("Role(CookieParam):").append(cliente.getRole()).append("\n");
        return sb.toString();
    }

    @GET
    @Path("/colecciones/query")
    public String obtenClientes(@QueryParam("ordenarPor") List<String> ordenarPor) {
        StringBuilder sb = new StringBuilder("Ordenar por: ");
        ordenarPor.forEach((s) -> {
            sb.append(s).append(" ");
        });
        return sb.toString();
    }

    @GET
    @Path("/predeterminado")
    public String obtenClientesConPredeterminados(@DefaultValue("1") @QueryParam("inicio") int inicio,
            @DefaultValue("10") @QueryParam("tamanio") int tamanio) {
        return "Inicio:" + inicio + " / tamaño:" + tamanio;
    }

    @GET
    @Path("/codificado/{datoCodificado}/{datoNoCodificado}")
    public String obtenConCodificados(@PathParam("datoCodificado") String datoCodificado,
            @Encoded @PathParam("datoNoCodificado") String datoNoCodificado) {
        return "datoCodificado:" + datoCodificado + " / datoNoCodificado:" + datoNoCodificado;
    }

}