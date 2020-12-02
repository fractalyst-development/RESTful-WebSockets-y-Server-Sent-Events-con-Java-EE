package curso.rest.ws.servicios;

import curso.rest.ws.dominio.GeneradorDatos;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.websocket.EndpointConfig;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;


@ServerEndpoint("/data-feed")
public class DataFeedEndpoint {

  @OnMessage
  public void onMessage(Session s, String message) {
    System.out.println("onMessage");
    try {
      s.getBasicRemote().sendText(GeneradorDatos.aJSON());
    } catch (IOException ex) {
      Logger.getLogger(DataFeedEndpoint.class.getName()).log(Level.SEVERE, null, ex);
    }
  }

  @OnOpen
  public void onOpen(Session s, EndpointConfig ec) {
    System.out.println("Abriendo/Configurando ServerEndpoint");
  }

  @OnClose
  public void onClose(Session s, EndpointConfig ec) {
    System.out.println("Cerrando ServerEndpoint");
  }
}
