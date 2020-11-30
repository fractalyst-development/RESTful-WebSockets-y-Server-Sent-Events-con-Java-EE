package curso.rest.servicios;

import curso.rest.dominio.Articulo;
import curso.rest.dominio.Cliente;
import curso.rest.dominio.Direccion;
import curso.rest.dominio.Pedido;
import curso.rest.dominio.Pedidos;
import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.HEAD;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Link;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import javax.ws.rs.PUT;

@Path("/pedidos")
public class RecursoPedido {

    private final static Map<Integer, Pedido> DB_PEDIDOS = new HashMap<Integer, Pedido>();
    private final static AtomicInteger IDENTIFICADOR = new AtomicInteger();

    public RecursoPedido() {
        cargaPedidosPrueba(5);
    }

    @POST
    @Consumes("application/xml")
    public Response altaPedido(Pedido pedido, @Context UriInfo uriInfo) {
        pedido.setId(IDENTIFICADOR.incrementAndGet());
        DB_PEDIDOS.put(pedido.getId(), pedido);
        System.out.println("Pedido creado: " + pedido.getId());
        UriBuilder builder = uriInfo.getAbsolutePathBuilder();
        builder.path(Integer.toString(pedido.getId()));
        return Response.created(builder.build()).build();

    }

    @GET
    @Path("{id}")
    @Produces("application/xml")
    public Response consultaPedido(@PathParam("id") int id, @Context UriInfo uriInfo) {
        Pedido pedido = DB_PEDIDOS.get(id);
        if (pedido == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        Response.ResponseBuilder builder = Response.ok(pedido);
        if (!pedido.isCancelado()) {
            agregaHeaderCancelado(uriInfo, builder);
        }
        return builder.build();
    }

    protected void agregaHeaderCancelado(UriInfo uriInfo, Response.ResponseBuilder builder) {
        UriBuilder absoluto = uriInfo.getAbsolutePathBuilder();
        URI cancelUrl = absoluto.clone().path("cancelado").build();
        builder.links(Link.fromUri(cancelUrl).rel("cancelado").build());
    }

    @POST
    @Path("{id}/cancelado")
    public void cancelaPedido(@PathParam("id") int id) {
        Pedido pedido = DB_PEDIDOS.get(id);
        if (pedido == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        pedido.setCancelado(true);
    }

    @PUT
    @Path("{id}")
    public void cancelaPedidoPUT(@PathParam("id") int id, Pedido pedidoCancelado, @Context UriInfo uriInfo) {
        Pedido pedido = DB_PEDIDOS.get(id);
        if (pedido == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        pedido.setCancelado(pedidoCancelado.isCancelado());
    }

    @HEAD
    @Path("{id}")
    @Produces("application/xml")
    public Response consultaHeadersPedido(@PathParam("id") int id, @Context UriInfo uriInfo) {
        Pedido pedido = DB_PEDIDOS.get(id);
        if (pedido == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        Response.ResponseBuilder builder = Response.ok();
        builder.type("application/xml");
        if (!pedido.isCancelado()) {
            agregaHeaderCancelado(uriInfo, builder);
        }
        return builder.build();
    }

    @GET
    @Produces("application/xml")
    public Response consultaPedidos(@QueryParam("indiceInicial") int indiceInicial,
            @QueryParam("tamanhio") @DefaultValue("2") int tamanhio,
            @Context UriInfo uriInfo) {
        UriBuilder builder = uriInfo.getAbsolutePathBuilder();
        builder.queryParam("indiceInicial", "{indiceInicial}");
        builder.queryParam("tamanhio", "{tamanhio}");

        ArrayList<Pedido> list = new ArrayList<>();
        ArrayList<Link> links = new ArrayList<>();
        synchronized (DB_PEDIDOS) {
            int i = 0;
            for (Pedido order : DB_PEDIDOS.values()) {
                if (i >= indiceInicial && i < indiceInicial + tamanhio) {
                    list.add(order);
                }
                i++;
            }
            // next link
            if (indiceInicial + tamanhio < DB_PEDIDOS.size()) {
                int next = indiceInicial + tamanhio;
                URI nextUri = builder.clone().build(next, tamanhio);
                Link nextLink = Link.fromUri(nextUri).rel("next").type("application/xml").build();
                links.add(nextLink);
            }
            // previous link
            if (indiceInicial > 0) {
                int previous = indiceInicial - tamanhio;
                if (previous < 0) {
                    previous = 0;
                }
                URI previousUri = builder.clone().build(previous, tamanhio);
                Link previousLink = Link.fromUri(previousUri).rel("previous").type("application/xml").build();
                links.add(previousLink);
            }
        }
        Pedidos pedidos = new Pedidos();
        pedidos.setPedidos(list);
        pedidos.setLinks(links);
        Response.ResponseBuilder responseBuilder = Response.ok(pedidos);
        agregaHeaderLinkPurga(uriInfo, responseBuilder);
        return responseBuilder.build();
    }

    protected void agregaHeaderLinkPurga(UriInfo uriInfo, Response.ResponseBuilder builder) {
        UriBuilder absolute = uriInfo.getAbsolutePathBuilder();
        URI purgeUri = absolute.clone().path("purga").build();
        builder.links(Link.fromUri(purgeUri).rel("purga").build());
    }

    @POST
    @Path("purgar")
    public void purgarPedidos() {
        synchronized (DB_PEDIDOS) {
            List<Pedido> pedidos = new ArrayList<>();
            pedidos.addAll(DB_PEDIDOS.values());
            pedidos.stream().filter((pedido) -> (pedido.isCancelado())).forEachOrdered((pedido) -> {
                DB_PEDIDOS.remove(pedido.getId());
            });
        }
    }

    @HEAD
    @Produces("application/xml")
    public Response consultaHeadersPedidos(@QueryParam("indiceInicial") int indiceInicial,
            @QueryParam("tamanhio") @DefaultValue("2") int tamanhio,
            @Context UriInfo uriInfo) {
        Response.ResponseBuilder builder = Response.ok();
        builder.type("application/xml");
        agregaHeaderLinkPurga(uriInfo, builder);
        return builder.build();
    }

    private void cargaPedidosPrueba(int cantidadPedidos) {
        for (int i = 0; i < cantidadPedidos; i++) {
            Pedido p = this.creaPedido();
            DB_PEDIDOS.put(p.getId(), p);
        }
    }

    //
    private Pedido creaPedido() {
        Pedido pSalida = new Pedido();
        pSalida.setId(IDENTIFICADOR.incrementAndGet());
        pSalida.setCliente(creaCliente());
        pSalida.setCancelado(false);
        pSalida.setArticulos(creaArticulos(2));
        pSalida.setFecha(new Date());
        pSalida.setTotal(calculaCostoTotal(pSalida.getArticulos()));
        return pSalida;
    }

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

    private List<Articulo> creaArticulos(int cuantosArticulos) {
        List<Articulo> articulos = new ArrayList<>();
        for (int i = 0; i < cuantosArticulos; i++) {
            Articulo a = new Articulo();
            a.setProducto("Producto " + i);
            a.setCosto(100.0 + 1);
            articulos.add(a);
        }
        return articulos;
    }

    private double calculaCostoTotal(List<Articulo> articulos) {
        double total = 0.0;
        total = articulos.stream().map((a) -> a.getCosto()).reduce(total, (accumulator, _item) -> accumulator + _item);
        return total;
    }

}
