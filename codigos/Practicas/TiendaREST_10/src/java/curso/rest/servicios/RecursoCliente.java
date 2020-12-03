package curso.rest.servicios;

import curso.rest.dominio.Cliente;
import curso.rest.dominio.Clientes;
import curso.rest.dominio.MiLinkHandler;
import curso.rest.dominio.RespuestaCliente;
import curso.rest.dominio.dao.ClienteDAO;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Link;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

@Path("/clientes")
public class RecursoCliente {

    private ClienteDAO dao;

    public RecursoCliente() {
        dao = new ClienteDAO();
    }

    @POST
    @Consumes({"application/xml", "application/json"})
    @Produces({"application/xml", "application/json"})
    public Response altaCliente(Cliente c) {
        Cliente nuevoCliente = dao.altaCliente(c);
        return Response.created(URI.create("/clientes/" + nuevoCliente.getId())).build();
    }

    @DELETE
    @Path("{id}")
    @Produces("application/xml")
    public void bajaCliente(@PathParam("id") int id) {
        dao.bajaCliente(id);

    }

    @PUT
    @Consumes({"application/xml", "application/json"})
    @Produces({"application/xml", "application/json"})
    public Response cambiosCliente(Cliente c) {
        Cliente nuevoCliente = dao.cambiosCliente(c);
        return Response.created(URI.create("/clientes/" + nuevoCliente.getId())).build();
    }

    @GET
    @Path("{id}")
    @Produces({"application/xml", "application/json"})
    public RespuestaCliente obtenCliente(@PathParam("id") int id) {
        RespuestaCliente res = new RespuestaCliente();
        Cliente c = dao.consultaCliente(id);
        if (c != null) {
            res.setData(c);
        } else {
            throw new NotFoundException("MENSAJE");
        }
        return res;
    }

    final String TAMANHIO = "5";

    @GET
    @Produces({"application/xml", "application/json"})
    public List<Cliente> consultaClientes(@DefaultValue("id") @QueryParam("ordenarPor") List<String> ordenarPor,
            @DefaultValue(TAMANHIO) @QueryParam("tamanhio") int tamanhio,
            @DefaultValue("1") @QueryParam("pagina") int pagina) {

        return dao.consultaClientes(tamanhio, pagina, ordenarPor);
    }

    @GET
    @Path("hateoas")
    @Produces({"application/xml", "application/json"})
    public Response consultaClientesHateoas(@DefaultValue("id") @QueryParam("ordenarPor") List<String> ordenarPor,
            @DefaultValue(TAMANHIO) @QueryParam("tamanhio") int tamanhio,
            @DefaultValue("1") @QueryParam("pagina") int pagina,
            @Context UriInfo uriInfo) {

        UriBuilder builder = uriInfo.getAbsolutePathBuilder();
        builder.queryParam("pagina", "{pagina}");
        builder.queryParam("tamanio", "{tamanhio}");

        List<Cliente> clientes = new ArrayList<>();
        List<Link> links = new ArrayList<>();
        Clientes clientesWrapper = new Clientes();

        List<Cliente> clientesSin = dao.consultaClientes(tamanhio, pagina, ordenarPor);
        clientesWrapper.setClientes(clientesSin);

        // next link
        if (dao.consultaNumeroDeClientes() / tamanhio >= pagina) {
            int pagSig = pagina + 1;
            URI nextUri = builder.clone().build(pagSig, tamanhio);
            Link nextLink = Link.fromUri(nextUri).rel("next").build();
            links.add(nextLink);
        }
        // previous link
        if (pagina > 1) {
            int pagAnterior = pagina - 1;
            URI nextUri = builder.clone().build(pagAnterior, tamanhio);
            Link nextLink = Link.fromUri(nextUri).rel("previous").build();
            links.add(nextLink);
        }

        clientesWrapper.setLinks(MiLinkHandler.toMiLink(links));
        Response respuesta = Response.ok(clientesWrapper)
                .links(links.toArray(new Link[links.size()])).build();

        return respuesta;
    }
    @GET
    @Path("hateoas-detalle")
    @Produces({"application/xml", "application/json"})
    public Response consultaClientesHateoasSinDetalle(@DefaultValue("id") @QueryParam("ordenarPor") List<String> ordenarPor,
            @DefaultValue(TAMANHIO) @QueryParam("tamanhio") int tamanhio,
            @DefaultValue("1") @QueryParam("pagina") int pagina,
            @DefaultValue("direccion") @QueryParam("expandir") List<String> expandir,
            @Context UriInfo uriInfo) {

        UriBuilder builder = uriInfo.getAbsolutePathBuilder();
        builder.queryParam("pagina", "{pagina}");
        builder.queryParam("tamanio", "{tamanhio}");

        List<Cliente> clientes = new ArrayList<>();
        List<Link> links = new ArrayList<>();
        Clientes clientesWrapper = new Clientes();

        List<Cliente> clientesSin = dao.consultaClientes(tamanhio, pagina, ordenarPor);
        clientesWrapper.setClientes(clientesSin);

        // next link
        if (dao.consultaNumeroDeClientes() / tamanhio >= pagina) {
            int pagSig = pagina + 1;
            URI nextUri = builder.clone().build(pagSig, tamanhio);
            Link nextLink = Link.fromUri(nextUri).rel("next").build();
            links.add(nextLink);
        }
        // previous link
        if (pagina > 1) {
            int pagAnterior = pagina - 1;
            URI nextUri = builder.clone().build(pagAnterior, tamanhio);
            Link nextLink = Link.fromUri(nextUri).rel("previous").build();
            links.add(nextLink);
        }

        clientesWrapper.setLinks(MiLinkHandler.toMiLink(links));
        Response respuesta = Response.ok(clientesWrapper)
                .links(links.toArray(new Link[links.size()])).build();

        return respuesta;
    }
}
