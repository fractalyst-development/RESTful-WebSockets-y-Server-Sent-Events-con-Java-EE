package curso.rest.deploy.dominio;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "reporte")
@XmlAccessorType(XmlAccessType.FIELD)
public class Reporte {

    @XmlElement
    private String inicio;
    @XmlElement
    private String fin;

    public Reporte() {
    }

    public Reporte(String inicio, String fin) {
        this.inicio = inicio;
        this.fin = fin;
    }


    /**
     * @return the inicio
     */
    public String getInicio() {
        return inicio;
    }

    /**
     * @param inicio the inicio to set
     */
    public void setInicio(String inicio) {
        this.inicio = inicio;
    }

    /**
     * @return the fin
     */
    public String getFin() {
        return fin;
    }

    /**
     * @param fin the fin to set
     */
    public void setFin(String fin) {
        this.fin = fin;
    }
    
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("<reporte: ").append("\n");
        sb.append("\tinicio:").append(inicio).append("\n");
        sb.append("\tfin:").append(fin).append("\n");
        sb.append("\t/>");
        return sb.toString();
    }

}
