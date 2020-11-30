package curso.rest.subrecurso.servicios;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.URI;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import curso.rest.subrecurso.dominio.Cliente;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class RecursoClienteOtros implements RecursoClienteRegion {

    private final static Map<Integer, Cliente> DB_CLIENTE = new ConcurrentHashMap<>();
    private final static AtomicInteger IDENTIFICADOR = new AtomicInteger();

    @Override
    public Response altaCliente(InputStream is) {
        Cliente cliente = leeCliente(is);
        cliente.setId(IDENTIFICADOR.incrementAndGet());
        DB_CLIENTE.put(cliente.getId(), cliente);
        System.out.println("Cliente creado con Id: " + cliente.getId());
        return Response.created(URI.create("/clientes/" + cliente.getId())).build();
    }

    @Override
    public StreamingOutput obtenCliente(int id) {
        final Cliente cliente = DB_CLIENTE.get(id);
        if (cliente == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        return (OutputStream outputStream) -> {
            escribeClienteComoXML(outputStream, cliente);
        };
//        return new StreamingOutput() {
//            @Override
//            public void write(OutputStream outputStream)
//                    throws IOException, WebApplicationException {
//                escribeClienteComoXML(outputStream, cliente);
//            }
//        };
    }

    @Override
    public void cambioCliente(int id, InputStream is) {
        Cliente clienteNuevosDatos = leeCliente(is);
        Cliente clienteDatosActuales = DB_CLIENTE.get(id);
        if (clienteDatosActuales == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        clienteDatosActuales.setNombre(clienteNuevosDatos.getNombre());
        clienteDatosActuales.setApellidos(clienteNuevosDatos.getApellidos());
        clienteDatosActuales.setCalle(clienteNuevosDatos.getCalle());
        clienteDatosActuales.setEstado(clienteNuevosDatos.getEstado());
        clienteDatosActuales.setCodigoPostal(clienteNuevosDatos.getCodigoPostal());
        clienteDatosActuales.setPais(clienteNuevosDatos.getPais());
    }

    private void escribeClienteComoXML(OutputStream os, Cliente cliente) throws IOException {
        StringBuilder xmlCliente = new StringBuilder();
        xmlCliente.append("<cliente id=\"").append(cliente.getId()).append("\">");
        xmlCliente.append("<nombre>").append(cliente.getNombre()).append("</nombre>");
        xmlCliente.append("<apellidos>").append(cliente.getApellidos()).append("</apellidos>");
        xmlCliente.append("<calle>").append(cliente.getCalle()).append("</calle>");
        xmlCliente.append("<ciudad>").append(cliente.getCiudad()).append("</ciudad>");
        xmlCliente.append("<estado>").append(cliente.getEstado()).append("</estado>");
        xmlCliente.append("<codigoPostal>").append(cliente.getCodigoPostal()).append("</codigoPostal>");
        xmlCliente.append("<pais>").append(cliente.getPais()).append("</pais>");
        xmlCliente.append("</cliente>");
        //
        PrintStream writer = new PrintStream(os);
        writer.println(xmlCliente.toString());
    }

    private Cliente leeCliente(InputStream is) {
        try {
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document doc = builder.parse(is);
            Element root = doc.getDocumentElement();
            Cliente cliente = new Cliente();
            if (root.getAttribute("id") != null && !root.getAttribute("id").trim().equals("")) {
                cliente.setId(Integer.valueOf(root.getAttribute("id")));
            }
            NodeList nodes = root.getChildNodes();
            for (int i = 0; i < nodes.getLength(); i++) {
                if (nodes.item(i).getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) nodes.item(i);
                    switch (element.getTagName()) {
                        case "nombre":
                            cliente.setNombre(element.getTextContent());
                            break;
                        case "apellidos":
                            cliente.setApellidos(element.getTextContent());
                            break;
                        case "calle":
                            cliente.setCalle(element.getTextContent());
                            break;
                        case "ciudad":
                            cliente.setCiudad(element.getTextContent());
                            break;
                        case "estado":
                            cliente.setEstado(element.getTextContent());
                            break;
                        case "codigoPostal":
                            cliente.setCodigoPostal(Integer.parseInt(element.getTextContent()));
                            break;
                        case "pais":
                            cliente.setPais(element.getTextContent());
                            break;
                        default:
                            break;
                    }
                }
            }
            return cliente;
        } catch (IOException | NumberFormatException | ParserConfigurationException | DOMException | SAXException e) {
            System.out.println("e:" + e.getLocalizedMessage());
            throw new WebApplicationException(e, Response.Status.BAD_REQUEST);
        }
    }

}
