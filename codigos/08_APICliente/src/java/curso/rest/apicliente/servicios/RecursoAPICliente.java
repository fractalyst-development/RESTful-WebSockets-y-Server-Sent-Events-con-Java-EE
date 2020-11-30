package curso.rest.apicliente.servicios;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;
import curso.rest.apicliente.dominio.Cliente;
import curso.rest.apicliente.dominio.Direccion;
import curso.rest.apicliente.dominio.Reporte;

@Path("/apicliente")

public class RecursoAPICliente {

    private Map<Integer, Cliente> DB_CLIENTE = new ConcurrentHashMap<Integer, Cliente>();
    private AtomicInteger IDENTIFICADOR = new AtomicInteger();

    @POST
    @Path("/altaCliente")
    @Consumes({"application/xml", MediaType.APPLICATION_JSON})
    @Produces({"application/xml", MediaType.APPLICATION_JSON})
    public Cliente altaCliente(Cliente clienteEntrada) {
        Cliente clienteSalida = new Cliente();
        clienteSalida.setId(IDENTIFICADOR.incrementAndGet());
        clienteSalida.setNombre(clienteEntrada.getNombre());
        clienteSalida.setApellidos(clienteEntrada.getApellidos());
        Direccion direccion = new Direccion();
        direccion.setCalle(clienteEntrada.getDireccion().getCalle());
        direccion.setCiudad(clienteEntrada.getDireccion().getCiudad());
        direccion.setEstado(clienteEntrada.getDireccion().getEstado());
        direccion.setCodigoPostal(clienteEntrada.getDireccion().getCodigoPostal());
        clienteSalida.setDireccion(direccion);
        DB_CLIENTE.put(clienteSalida.getId(), clienteSalida);
        return clienteSalida;
    }

    @GET
    @Path("/consultaCliente/{id}")
    @Consumes({"application/xml", MediaType.APPLICATION_JSON})
    @Produces({"application/xml", MediaType.APPLICATION_JSON})
    public Cliente consultaCliente(@PathParam("id") int id) {
        final Cliente cliente = DB_CLIENTE.get(id);
        if (cliente == null) {
            throw new NotFoundException();
        }
        return cliente;
    }

    @POST
    @Path("/Entity/json/")
    @Consumes({"application/xml", MediaType.APPLICATION_JSON})
    @Produces({"application/xml", MediaType.APPLICATION_JSON})
    public Cliente testEntityJson(Cliente cliente) {
        //final Cliente cliente = DB_CLIENTE.get(id);
        if (cliente == null) {
            throw new NotFoundException();
        }
        return cliente;
    }

    @POST
    @Path("/Entity/form/")
    @Produces({"application/xml", MediaType.APPLICATION_JSON})
    public Cliente testEntityForm(@FormParam("nombre") String nombre,
            @FormParam("apellidos") String apellidos) {
        //final Cliente cliente = DB_CLIENTE.get(id);
        Cliente cliente = new Cliente();
        cliente.setId(876);
        cliente.setNombre(nombre);
        cliente.setApellidos(apellidos);
        DB_CLIENTE.put(cliente.getId(), cliente);
        return cliente;
    }

    @GET
    @Path("/Invoke/")
    @Produces({"application/xml", MediaType.APPLICATION_JSON})
    public Reporte testInvoke(@QueryParam("inicio") String inicio,
            @QueryParam("fin") String fin) {

        Reporte reporte = new Reporte(inicio, fin);

        return reporte;
    }
}
