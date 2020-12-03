package curso.rest.servicios;

import curso.rest.dominio.Cliente;
import curso.rest.dominio.dao.ClienteDAO;
import java.net.URI;
import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

@Path("/clientes")
public class RecursoCliente {

    private ClienteDAO dao;

    public RecursoCliente() {
        dao = new ClienteDAO();
    }

    @GET
    @Path("{id}")
    @Produces({"application/xml", "application/json"})
    public Response obtenCliente(@PathParam("id") int id, @Context Request request) {

        Cliente c = dao.consultaCliente(id);

        if (c != null) {
            ResponseBuilder rb = Response.ok(c);
            return rb.build();
        } else {
            throw new NotFoundException("Cliente no localizado");
        }

    }

    @GET
    @Path("/future/{id}")
    @Produces({"application/xml", "application/json"})
    public Response obtenClienteFuture(@PathParam("id") int id, @Context Request request) {
        Cliente c = dao.consultaCliente(id);
        if (c != null) {
            ResponseBuilder rb = Response.ok(c);
            return rb.build();
        } else {
            throw new NotFoundException("Cliente no localizado");
        }

    }

    @POST
    @Consumes({"application/xml", "application/json"})
    @Produces({"application/xml", "application/json"})
    public Response altaCliente(Cliente c) {
        Cliente nuevoCliente = dao.altaCliente(c);
        return Response.created(URI.create("/clientes/" + nuevoCliente.getId())).build();
    }

    @POST
    @Path("/async-response")
    @Consumes({"application/xml", "application/json"})
    //@Produces({"application/xml", "application/json"})
    public void altaClienteAsyncResponse(Cliente c,
            final @Suspended AsyncResponse asyncResponse) {

        new Thread() {
            CyclicBarrier barrier = new CyclicBarrier(2);

            @Override
            public void run() {
                Cliente nuevoCliente = dao.altaCliente(c);
                nuevoCliente.setNombre("En SERVER");

                try {
                    //Thread.sleep(1);
                    barrier.await();
                } catch (InterruptedException | BrokenBarrierException ex) {
                    Logger.getLogger(RecursoCliente.class.getName()).log(Level.SEVERE, null, ex);
                }

                Response res = Response.ok(nuevoCliente).build();
                asyncResponse.resume(res);
            }

        }.start();

    }
    Executor executor = Executors.newSingleThreadExecutor();

    @POST
    @Path("/async-response-priorizacion")
    @Consumes({"application/xml", "application/json"})
    public void altaClienteAsyncResponsePriorizacion(Cliente c,
            final @Suspended AsyncResponse asyncResponse) {

        executor.execute(new Runnable() {
            @Override
            public void run() {
                Cliente nuevoCliente = dao.altaCliente(c);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(RecursoCliente.class.getName()).log(Level.SEVERE, null, ex);
                }

                Response res = Response.ok(nuevoCliente).build();
                asyncResponse.resume(res);
            }
        });

    }

    final String TAMANHIO = "5";

    @GET
    @Produces({"application/xml", "application/json"})
    public List<Cliente> consultaClientes(@DefaultValue("id") @QueryParam("ordenarPor") List<String> ordenarPor,
            @DefaultValue(TAMANHIO) @QueryParam("tamanhio") int tamanhio,
            @DefaultValue("1") @QueryParam("pagina") int pagina) {
        return dao.consultaClientes(tamanhio, pagina, ordenarPor);
    }
}
