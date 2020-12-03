package curso.rest.jaxrs.ext;

import curso.rest.dominio.Cliente;
import curso.rest.dominio.RespuestaCliente;
import javax.ws.rs.core.MediaType;
import javax.persistence.EntityNotFoundException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class NotFoundExceptionMapper implements ExceptionMapper<NotFoundException> {

    @Override
    public Response toResponse(NotFoundException exception) {

        RespuestaCliente res = new RespuestaCliente(exception.getMessage());
//        Cliente c = new Cliente();
//        c.setNombre("Romeo");
//        res.setData(c);
        return Response.status(Response.Status.BAD_REQUEST).type(MediaType.APPLICATION_JSON)
                .entity(res)
                .build();
    }

}
