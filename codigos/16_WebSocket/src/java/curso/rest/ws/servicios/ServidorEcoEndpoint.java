package curso.rest.ws.servicios;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

/**
 *
 * @author usuario
 */
@ServerEndpoint("/servidor-eco")
public class ServidorEcoEndpoint {

    @OnOpen
    public void onOpen(Session _sesion) {
        System.out.println("Nueva conexión con el cliente: " + _sesion.getId());
    }

    @OnMessage
    public String onMessage(String _msj, Session _sesion) {
        System.out.println("Nuevo mensaje del cliente[{" + _sesion.getId() + "}]: {" + _msj + "}");
        return "Respuesta desde el servidor: [" + _msj + "]";
    }

    @OnClose
    public void onClose(Session _sesion) {
        System.out.println("Cerrada la conexión para el cliente: {" + _sesion.getId() + "}");
    }

    @OnError
    public void onError(Throwable _ex, Session _sesion) {
        System.out.println("Error del cliente: {" + _sesion.getId() + "}");
    }

}
