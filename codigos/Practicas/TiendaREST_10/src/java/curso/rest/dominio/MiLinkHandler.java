package curso.rest.dominio;

import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.core.Link;

public class MiLinkHandler {

    public static MiLink toMiLink(Link link) {
        MiLink miLink = new MiLink();
        miLink.setHref(link.getUri().toString());
        miLink.setRel(link.getRel());

        return miLink;
    }
    
    public static List<MiLink> toMiLink(List<Link> links) {
        List<MiLink> miLinks = new ArrayList<>(links.size());
        links.forEach((l) -> {
            miLinks.add(toMiLink(l));
        });
        return miLinks;
    }
}
