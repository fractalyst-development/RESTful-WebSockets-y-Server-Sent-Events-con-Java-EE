package curso.rest.seguridad.cliente;

import com.sun.jersey.api.client.ClientResponse.Status;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.filter.ClientFilter;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class APICliente {

    // http://localhost:7001/15_Seguridad/servicios/seguridad/dd/clientes/cuantos
    private final static String URL_BASE = "http://localhost:7001/15_Seguridad/servicios/seguridad";

    public static void main(String[] args) {
        APICliente ref = new APICliente();
//        ref.invocaBasicLogin();
        ref.invocaBasicLoginCliente();

    }

    public void invocaBasicLogin() {

        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(URL_BASE);
        Response response = target.path("/dd/clientes/cuantos")
                .request(MediaType.TEXT_PLAIN)
                .get();
        System.out.println("Respuesta HTTP:" + response.getStatus());
        if (response.getStatus() == Status.FOUND.getStatusCode()) {
            System.out.println("Nuevo URI:" + response.getLocation());
            // NO PARA PRODUCCION
            // Brincando la validación del nombre del servidor en el certificado:
            HttpsURLConnection.setDefaultHostnameVerifier((hostname, session) -> true);
            //
            Client clientSeguro = ClientBuilder.newBuilder()
                    .sslContext(getSSLContext())
                    .build();
            WebTarget wt = clientSeguro.target(response.getLocation());

            String usuario = "miUsuario", pwd = "Password_1";
            String usuarioYpwd = usuario + ":" + pwd;
            String valorEncabezadoAuthorization = "Basic " + java.util.Base64.getEncoder().encodeToString(usuarioYpwd.getBytes());

            Response r = wt.request(MediaType.TEXT_PLAIN)
                    .header("Authorization", valorEncabezadoAuthorization)
                    .get();
            System.out.println("Respuesta HTTPs:" + r.getStatus());

            if (r.getStatus() == Status.OK.getStatusCode()) {
                System.out.println("Salida:" + r.readEntity(String.class));
            }
            r.close();
            clientSeguro.close();
        }
        response.close();
        client.close();
    }

    public void invocaBasicLoginCliente() {
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(URL_BASE);
        Response response = target.path("/dd/clientes/cuantos/cliente")
                .request(MediaType.TEXT_PLAIN)
                .get();
        System.out.println("Respuesta HTTP:" + response.getStatus());
        if (response.getStatus() == Status.FOUND.getStatusCode()) {
            System.out.println("Nuevo URI:" + response.getLocation());
            // NO PARA PRODUCCION
            // Brincando la validación del nombre del servidor en el certificado:
            HttpsURLConnection.setDefaultHostnameVerifier((hostname, session) -> true);
            //
            Client clientSeguro = ClientBuilder.newBuilder()
                    .sslContext(getSSLContext())
                    .build();
            WebTarget wt = clientSeguro.target(response.getLocation());

            String usuario = "miUsuario", pwd = "Password_1";
            String usuarioYpwd = usuario + ":" + pwd;
            String valorEncabezadoAuthorization = "Basic " + java.util.Base64.getEncoder().encodeToString(usuarioYpwd.getBytes());

            Response r = wt.request(MediaType.TEXT_PLAIN)
                    .header("Authorization", valorEncabezadoAuthorization)
                    .get();
            System.out.println("Respuesta HTTPs:" + r.getStatus());

            if (r.getStatus() == Status.OK.getStatusCode()) {
                System.out.println("Salida:" + r.readEntity(String.class));
            }
            r.close();
            clientSeguro.close();
        }
        response.close();
        client.close();
    }

    private SSLContext getSSLContext() {
        SSLContext ctx = null;
        try {
            KeyStore trustStore;
            trustStore = KeyStore.getInstance("JKS");
            trustStore.load(new FileInputStream("/home/usuario/Oracle/Middleware/Oracle_Home/user_projects/domains/wl_server/security/DemoIdentity.jks"), "DemoIdentityKeyStorePassPhrase".toCharArray());
            TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
            tmf.init(trustStore);
            ctx = SSLContext.getInstance("SSL");
            ctx.init(null, tmf.getTrustManagers(), null);
        } catch (NoSuchAlgorithmException | KeyStoreException | java.security.cert.CertificateException | IOException | KeyManagementException ex) {
            System.out.println("ERR: " + ex.getMessage());
        }
        return ctx;
    }


}
