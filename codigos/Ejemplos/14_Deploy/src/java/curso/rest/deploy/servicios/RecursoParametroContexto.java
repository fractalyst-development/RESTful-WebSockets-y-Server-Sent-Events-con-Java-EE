package curso.rest.deploy.servicios;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/deploy")

public class RecursoParametroContexto {

    public RecursoParametroContexto() {

    }
    
    private int parametroContexto;
    

    @GET
    @Path("/ping")
    @Produces(MediaType.TEXT_PLAIN)
    public String testPing() {
        return " ...pong ";
    }

    @GET
    @Path("/param-contexto")
    @Produces({MediaType.WILDCARD})
    public String testParamContexto() {
        return getParametroContexto()+"";
    }



    /**
     * @return the parametroContexto
     */
    public int getParametroContexto() {
        return parametroContexto;
    }

    /**
     * @param parametroContexto the parametroContexto to set
     */
    public void setParametroContexto(int parametroContexto) {
        this.parametroContexto = parametroContexto;
    }

}
