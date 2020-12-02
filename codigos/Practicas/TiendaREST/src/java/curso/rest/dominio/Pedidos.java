package curso.rest.dominio;

import java.net.URI;
import java.util.List;
import javax.ws.rs.core.Link;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@XmlRootElement(name = "pedidos")
public class Pedidos {

    protected List<Pedido> pedidos;
    protected List<Link> links;

    @XmlElementRef
    public List<Pedido> getPedidos() {
        return pedidos;
    }

    public void setPedidos(List<Pedido> pedidos) {
        this.pedidos = pedidos;
    }

    //@XmlJavaTypeAdapter(Link.JaxbAdapter.class)
    @XmlElement(name = "link")
    public List<Link> getLinks() {
        return links;
    }

    public void setLinks(List<Link> links) {
        this.links = links;
    }

    @XmlTransient
    public URI getNext() {
        if (links == null) {
            return null;
        }
        for (Link link : links) {
            if ("next".equals(link.getRel())) {
                return link.getUri();
            }
        }
        return null;
    }

    @XmlTransient
    public URI getPrevious() {
        if (links == null) {
            return null;
        }
        for (Link link : links) {
            if ("previous".equals(link.getRel())) {
                return link.getUri();
            }
        }
        return null;
    }

}
