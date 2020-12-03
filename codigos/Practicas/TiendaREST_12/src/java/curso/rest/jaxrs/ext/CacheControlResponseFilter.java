package curso.rest.jaxrs.ext;

import java.io.IOException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.CacheControl;
import javax.ws.rs.ext.Provider;

@Provider
public class CacheControlResponseFilter implements ContainerResponseFilter {

    @Override
    public void filter(ContainerRequestContext req, ContainerResponseContext res) throws IOException {
        if (req.getMethod().equalsIgnoreCase("GET")) {
            CacheControl cc = new CacheControl();
            cc.setMaxAge(100);
            res.getHeaders().add("Cache-Control", cc.toString());
            res.getHeaders().add("TEST-NAME", "TEST-VALUE");
        }
    }
}
