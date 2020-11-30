package curso.rest.subrecurso.servicios;

import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

@Path("/clientes")
public class RecursoCliente {

    @Path("{region}-db")
    public RecursoClienteRegion configuraDatosRegion(@PathParam("region") String db) {
        // localiza la instancia adecuada en función de la región de entrada
        RecursoClienteRegion recurso = localizaRecursoCliente(db);
        return recurso;
    }

    private RecursoClienteRegion localizaRecursoCliente(String region) {
        //
        RecursoClienteRegion rc;
        switch (region) {
            case "mx":
                rc = new RecursoClienteMX();
                break;
            default:
                rc = new RecursoClienteOtros();
                break;
        }
        return rc;
    }

}
