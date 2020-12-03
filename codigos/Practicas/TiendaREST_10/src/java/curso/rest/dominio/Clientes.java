package curso.rest.dominio;

import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "clientes")
@XmlAccessorType(XmlAccessType.FIELD)
public class Clientes {

    @XmlElementRef
    protected List<Cliente> clientes;
    @XmlElementRef
    protected List<MiLink> links;

    public List<Cliente> getClientes() {
        return clientes;
    }

    public void setClientes(List<Cliente> clientes) {
        this.clientes = clientes;
    }

    public List<MiLink> getLink() {
        return links;
    }

    public void setLinks(List<MiLink> links) {
        this.links = links;
    }

}
