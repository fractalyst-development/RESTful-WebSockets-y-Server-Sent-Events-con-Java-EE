package curso.rest.dominio;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="respuesta")
@XmlAccessorType(XmlAccessType.FIELD)
public class RespuestaCliente {
    @XmlAttribute
    private int id;
    @XmlElement
    private Cliente data;
    @XmlElement
    private String error;

    public RespuestaCliente() {
    }

    public RespuestaCliente(int id, Cliente data, String error) {
        this.id = id;
        this.data = data;
        this.error = error;
    }
    public RespuestaCliente(int id, String error) {
        this.id = id;
        this.error = error;
    }

    public RespuestaCliente(String mi_mensajito_de_error) {
        this.error = mi_mensajito_de_error;
    }

    
    
    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return the data
     */
    public Cliente getData() {
        return data;
    }

    /**
     * @param data the data to set
     */
    public void setData(Cliente data) {
        this.data = data;
    }

    /**
     * @return the error
     */
    public String getError() {
        return error;
    }

    /**
     * @param error the error to set
     */
    public void setError(String error) {
        this.error = error;
    }
    
}
