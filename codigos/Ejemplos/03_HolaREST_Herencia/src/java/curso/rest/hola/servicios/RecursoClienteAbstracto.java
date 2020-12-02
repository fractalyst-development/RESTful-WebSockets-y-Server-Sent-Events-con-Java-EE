package curso.rest.hola.servicios;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;
import curso.rest.hola.dominio.Cliente;


public abstract class RecursoClienteAbstracto {

    private Map<Integer, Cliente> clienteDB = new ConcurrentHashMap<Integer, Cliente>();
    private AtomicInteger contadorId = new AtomicInteger();

    @POST
    @Consumes("application/xml")
    public Response altaCliente(InputStream is) {
        Cliente cliente = leeCliente(is);
        cliente.setId(contadorId.incrementAndGet());
        clienteDB.put(cliente.getId(), cliente);
        System.out.println("Cliente creado con Id: " + cliente.getId());
        return Response.created(URI.create("/clientes/" + cliente.getId())).build();
    }

    @GET
    @Path("{id}")
    @Produces("application/xml")
    public StreamingOutput obtenCliente(@PathParam("id") int id) {
        final Cliente cliente = clienteDB.get(id);
        if (cliente == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        return new StreamingOutput() {
            @Override
            public void write(OutputStream outputStream)
                    throws IOException, WebApplicationException {
                escribeClienteComoXML(outputStream, cliente);
            }
        };
    }

    @PUT
    @Path("{id}")
    @Consumes("application/xml")
    public void cambioCliente(@PathParam("id") int id, InputStream is) {
        Cliente clienteNuevosDatos = leeCliente(is);
        Cliente clienteDatosActuales = clienteDB.get(id);
        if (clienteDatosActuales == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        clienteDatosActuales.setNombre(clienteNuevosDatos.getNombre());
        clienteDatosActuales.setApellidos(clienteNuevosDatos.getApellidos());
        clienteDatosActuales.setCalle(clienteNuevosDatos.getCalle());
        clienteDatosActuales.setEstado(clienteNuevosDatos.getEstado());
        clienteDatosActuales.setCodigoPostal(clienteNuevosDatos.getCodigoPostal());
        clienteDatosActuales.setPais(clienteNuevosDatos.getPais());
    }

    protected abstract void escribeClienteComoXML(OutputStream os, Cliente cliente) throws IOException;

    protected abstract Cliente leeCliente(InputStream is);

}
