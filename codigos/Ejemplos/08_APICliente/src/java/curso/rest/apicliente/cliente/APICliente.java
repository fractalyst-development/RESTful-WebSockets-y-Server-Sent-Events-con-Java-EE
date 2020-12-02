package curso.rest.apicliente.cliente;

import curso.rest.apicliente.dominio.Reporte;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import curso.rest.apicliente.dominio.Cliente;
import curso.rest.apicliente.dominio.Direccion;

/**
 *
 * @author usuario
 */
public class APICliente {

    private final static String URL_BASE = "http://localhost:7001/08_APICliente/servicios/apicliente";

    public static void main(String[] args) {
        APICliente ref = new APICliente();
//        ref.introduccion();
//        ref.invocacion();
//        ref.invocacionBufferEntity();
//        ref.testEntityJson();
//        ref.testEntityForm();
        ref.testInvoke();
    }

    public void introduccion() {

        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(URL_BASE);
        Response response = target.path("/altaCliente")
                .request(MediaType.APPLICATION_XML)
                .post(Entity.xml(creaCliente()));
        System.out.println("Código de Rspuesta HTTP:" + response.getStatus());
        response.close();
        //
        Cliente cliente = target.path("/consultaCliente/" + 1)
                .request(MediaType.APPLICATION_JSON)
                .get(Cliente.class);
        System.out.println(cliente.toString());

        client.close();
    }

    public void invocacion() {

        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(URL_BASE);
        Response response = target.path("/altaCliente")
                .request(MediaType.APPLICATION_XML)
                .post(Entity.xml(creaCliente()));
        System.out.println("Código de Rspuesta HTTP:" + response.getStatus());
        response.close();
        //
        Cliente cliente = target.path("/consultaCliente/" + 1)
                .request(MediaType.APPLICATION_JSON)
                .get(Cliente.class);
        System.out.println(cliente.toString());
        //
        Response respuesta = target.path("/consultaCliente/" + 1)
                .request(MediaType.APPLICATION_JSON)
                .get();
        if (respuesta.getStatus() == 200) {
            Cliente cliente2 = respuesta.readEntity(Cliente.class);
            System.out.println(respuesta.getStatusInfo() + ": " + cliente2.toString());
        }
        response.close();
        client.close();
    }

    public void invocacionBufferEntity() {

        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(URL_BASE);
        Response response = target.path("/altaCliente")
                .request(MediaType.APPLICATION_XML)
                .post(Entity.xml(creaCliente()));
        System.out.println("Código de Rspuesta HTTP:" + response.getStatus());
        response.close();
        //
        Response respuesta = target.path("/consultaCliente/" + 1)
                .request(MediaType.APPLICATION_JSON)
                .get();
        if (respuesta.getStatus() == 200) {
            respuesta.bufferEntity();
            Cliente cliente = respuesta.readEntity(Cliente.class);
            String strCliente = respuesta.readEntity(String.class);
            strCliente = respuesta.readEntity(String.class);
            String strCliente3 = respuesta.readEntity(String.class);
            System.out.println(respuesta.getStatusInfo() + ": " + cliente.toString());
            System.out.println(respuesta.getStatusInfo() + ": " + strCliente3);
        }
        response.close();
        client.close();
    }

    //
    private Cliente creaCliente() {
        Cliente clienteSalida = new Cliente();
        clienteSalida.setId(876);
        clienteSalida.setNombre("Romeo");
        clienteSalida.setApellidos("Montesco");
        Direccion direccion = new Direccion();
        direccion.setCalle("Vía Capello, 23");
        direccion.setCiudad("Verona");
        direccion.setEstado("Véneto");
        direccion.setCodigoPostal(37100);
        clienteSalida.setDireccion(direccion);
        return clienteSalida;
    }

    public void testEntityJson() {

        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(URL_BASE);
        Response response = target.path("/Entity/json")
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.json(creaCliente()));
        System.out.println("Código de Rspuesta HTTP:" + response.getStatus());

        response.close();
        client.close();
    }

    public void testEntityForm() {

        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(URL_BASE);
        Form formulario = new Form().param("nombre", "Julieta")
                .param("apellidos", "Capuleto");
        Response response = target.path("/Entity/form")
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.form(formulario));
        System.out.println("Código de Rspuesta HTTP:" + response.getStatus());

        response.close();
        client.close();
    }

    public void testInvoke() {
        Invocation generaReporte = ClientBuilder.newClient().target(URL_BASE + "/Invoke")
                .queryParam("inicio", "2019-05-15")
                .queryParam("fin", "2020-05-15")
                .request()
                .accept(MediaType.APPLICATION_JSON)
                .buildGet();
        //
        for (int i = 1; i <= 5; i++) {
            Reporte reporte = generaReporte.invoke(Reporte.class);
            System.out.println("Reporte[" + i + "]:" + reporte);
        }
    }

}
