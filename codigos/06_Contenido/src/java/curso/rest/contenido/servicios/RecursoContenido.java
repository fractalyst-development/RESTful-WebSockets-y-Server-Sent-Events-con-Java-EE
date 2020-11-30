/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package curso.rest.contenido.servicios;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.LineNumberReader;
import java.io.OutputStream;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.URI;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import curso.rest.contenido.dominio.Cliente;
import curso.rest.contenido.dominio.Direccion;

@Path("/contenido")

public class RecursoContenido {

    @GET
    @Path("/StreamingOutput/{mensaje}")
    public StreamingOutput testStreamingOutput(@PathParam("mensaje") String mensaje) {
        return new StreamingOutput() {
            @Override
            public void write(OutputStream output) throws IOException, WebApplicationException {
                output.write(mensaje.getBytes());
            }
        };
    }

    @GET
    @Path("/StreamingOutput/lambda/{mensaje}")
    public StreamingOutput testStreamingOutputLambda(@PathParam("mensaje") String mensaje) {
        return (OutputStream output) -> {
            output.write(mensaje.getBytes());
        };
    }

    @POST
    @Path("/InputStream")
    public void testInputStream(InputStream is) {
        try {
            byte[] bytes = readFromStream(is);
            System.out.println(new String(bytes));
        } catch (IOException ex) {
            Logger.getLogger(RecursoContenido.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private byte[] readFromStream(InputStream stream) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        byte[] buffer = new byte[1000];
        int leido = 0;
        do {
            leido = stream.read(buffer);
            if (leido > 0) {
                baos.write(buffer, 0, leido);
            }
        } while (leido > -1);
        return baos.toByteArray();
    }

    @POST
    @Path("/Reader")
    public void testReader(Reader reader) {
        LineNumberReader lr = new LineNumberReader(reader);
        String renglon = null;
        do {
            try {
                renglon = lr.readLine();
                if (renglon != null) {
                    System.out.println(renglon);
                }
            } catch (IOException ex) {
                Logger.getLogger(RecursoContenido.class.getName()).log(Level.SEVERE, null, ex);
            }
        } while (renglon != null);
    }

    @POST
    @Path("/File")
    public void testFile(File file) {
        try {
            FileInputStream fis = new FileInputStream(file);
            byte[] bytes = readFromStream(fis);
            System.out.println(new String(bytes));
        } catch (IOException ex) {
            Logger.getLogger(RecursoContenido.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @GET
    @Path("/InputStreamRetorno/{filepath: .*}")
    @Produces("text/x-java-source")
    public InputStream testInputStreamRetorno(@PathParam("filepath") String path) {
        final String basePath = "/home/usuario/NetBeansProjects/06_Contenido/src/java/mx/com/fractalyst/rest/contenido/servicios/";
        FileInputStream is = null;
        try {
            is = new FileInputStream(basePath + path);
            return is;
        } catch (FileNotFoundException ex) {
            Logger.getLogger(RecursoContenido.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
            } catch (IOException ex) {
                Logger.getLogger(RecursoContenido.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return null;
    }

    @GET
    @Path("/FileRetorno/{filepath: .*}")
    @Produces("text/x-java-source")
    public File testFileRetorno(@PathParam("filepath") String path) {
        final String basePath = "/home/usuario/NetBeansProjects/06_Contenido/src/java/mx/com/fractalyst/rest/contenido/servicios/";
        return new File(basePath + path);
    }

    @POST
    @Path("/MultivaluedMap")
    @Consumes("application/x-www-form-urlencoded")
    @Produces("application/x-www-form-urlencoded")
    public MultivaluedMap<String, String> testMultivaluedMap(MultivaluedMap<String, String> formulario) {
        formulario.keySet().forEach((nombre) -> {
            System.out.println(formulario.get(nombre));
        });
        return formulario;
    }

    @POST
    @Path("/Source")
    @Consumes("application/xml")
    public String testSource(Source origenXML) {
        final String rutaBase = "/home/usuario/NetBeansProjects/06_Contenido/resources/";
        final String archivoXSL = rutaBase + "simple.xsl";
        String resultado = "<vacio/>";
        try {
            TransformerFactory tFactory = TransformerFactory.newInstance();
            Transformer transformer = tFactory.newTransformer(new StreamSource(archivoXSL));

            StringWriter escritor = new StringWriter();
            transformer.transform(origenXML, new StreamResult(escritor));
            resultado = escritor.toString();
        } catch (TransformerException ex) {
            Logger.getLogger(RecursoContenido.class.getName()).log(Level.SEVERE, null, ex);
        }
        return resultado;
    }

    @POST
    @Path("/JAXB")
    @Produces("application/xml")
    public Cliente testJaxb(Cliente clienteEntrada) {
        Cliente clienteSalida = new Cliente();
        clienteSalida.setId(clienteEntrada.getId());
        clienteSalida.setNombre(clienteEntrada.getNombre());
        clienteSalida.setApellidos(clienteEntrada.getApellidos());
        Direccion direccion = new Direccion();
        direccion.setCalle(clienteEntrada.getDireccion().getCalle());
        direccion.setCiudad(clienteEntrada.getDireccion().getCiudad());
        direccion.setEstado(clienteEntrada.getDireccion().getEstado());
        direccion.setCodigoPostal(clienteEntrada.getDireccion().getCodigoPostal());
        clienteSalida.setDireccion(direccion);
        return clienteSalida;
    }

    @POST
    @Path("/JAXB/JSON")
    @Consumes("application/xml")
    @Produces("application/json")
    public Cliente testJaxbJson(Cliente clienteEntrada) {
        Cliente clienteSalida = new Cliente();
        clienteSalida.setId(clienteEntrada.getId());
        clienteSalida.setNombre(clienteEntrada.getNombre());
        clienteSalida.setApellidos(clienteEntrada.getApellidos());
        Direccion direccion = new Direccion();
        direccion.setCalle(clienteEntrada.getDireccion().getCalle());
        direccion.setCiudad(clienteEntrada.getDireccion().getCiudad());
        direccion.setEstado(clienteEntrada.getDireccion().getEstado());
        direccion.setCodigoPostal(clienteEntrada.getDireccion().getCodigoPostal());
        clienteSalida.setDireccion(direccion);
        return clienteSalida;
    }


    @GET
    @Path("/JAXB/manual")
    @Produces("application/xml")
    public String testJaxbManual() {
        String resultado = "";
        try {
            Cliente cliente = new Cliente();
            cliente.setId(876);
            cliente.setNombre("Romeo");
            cliente.setApellidos("Montesco");
            Direccion direccion = new Direccion();
            direccion.setCalle("Vía Capello, 23");
            direccion.setCiudad("Verona");
            direccion.setEstado("Véneto");
            direccion.setCodigoPostal(37100);
            cliente.setDireccion(direccion);

            JAXBContext ctx = JAXBContext.newInstance(Cliente.class);
            StringWriter writer = new StringWriter();
            ctx.createMarshaller().marshal(cliente, writer);
            resultado = writer.toString();
            //cliente = (Cliente) ctx.createUnmarshaller().unmarshal(new StringReader(custString));
        } catch (JAXBException ex) {
            Logger.getLogger(RecursoContenido.class.getName()).log(Level.SEVERE, null, ex);
        }
        return resultado;
    }

    @POST
    @Path("/handler")
    @Consumes("application/x-java-serialized-object")
    @Produces("application/example-java")
    public Response testHandler(Cliente cliente) {

        cliente.setId(876);
        System.out.println("Cliente creado id:" + cliente.getId());

        return Response.created(URI.create("/clientes/" + cliente.getId())).build();
    }
    @POST
    @Path("/MBW")
    @Consumes("application/xml")
    @Produces("application/xml")
    public Cliente testMBW(Cliente clienteEntrada) {
        Cliente clienteSalida = new Cliente();
        clienteSalida.setId(clienteEntrada.getId());
        clienteSalida.setNombre(clienteEntrada.getNombre());
        clienteSalida.setApellidos(clienteEntrada.getApellidos());
        Direccion direccion = new Direccion();
        direccion.setCalle(clienteEntrada.getDireccion().getCalle());
        direccion.setCiudad(clienteEntrada.getDireccion().getCiudad());
        direccion.setEstado(clienteEntrada.getDireccion().getEstado());
        direccion.setCodigoPostal(clienteEntrada.getDireccion().getCodigoPostal());
        clienteSalida.setDireccion(direccion);
        return clienteSalida;
    }
}
