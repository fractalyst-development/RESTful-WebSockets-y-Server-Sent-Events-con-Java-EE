package curso.rest.dominio.dao;

import curso.rest.dominio.Cliente;
import curso.rest.dominio.Direccion;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 * @author usuario
 */
public class DataSourceHandler {

    private static final Map<Integer, Cliente> DB_CLIENTE = new ConcurrentHashMap<Integer, Cliente>();
    private static AtomicInteger IDENTIFICADOR = new AtomicInteger();

    private static DataSourceHandler singleton = new DataSourceHandler();

    public static DataSourceHandler getSingleton() {
        return singleton;
    }

    private DataSourceHandler() {
        final int cuantos = 10;
        cargaClientesPrueba(cuantos);
    }

    private void cargaClientesPrueba(int cantidadClientes) {
        for (int i = 0; i < cantidadClientes; i++) {
            Cliente c = this.creaCliente();
            DB_CLIENTE.put(c.getId(), c);
        }
    }

    //
    private Cliente creaCliente() {
        Cliente clienteSalida = new Cliente();
        clienteSalida.setId(IDENTIFICADOR.incrementAndGet());
        clienteSalida.setNombre("Romeo" + clienteSalida.getId());
        clienteSalida.setApellidos("Montesco" + ThreadLocalRandom.current().nextInt());
        Direccion direccion = new Direccion();
        direccion.setCalle("Vía Capello, 23");
        direccion.setCiudad("Verona");
        direccion.setEstado("Véneto");
        direccion.setCodigoPostal(37100);
        clienteSalida.setDireccion(direccion);
        return clienteSalida;
    }

    //
    public Cliente altaCliente(final Cliente c) {
        c.setId(IDENTIFICADOR.incrementAndGet());
        DB_CLIENTE.put(c.getId(), c);
        return c;
    }

    //
    public void bajaCliente(final int id) {
        DB_CLIENTE.remove(id);
    }

    //
    public Cliente cambiosCliente(final Cliente c) {
        Cliente cActual = consultaCliente(c.getId());
        if (cActual != null) {
            DB_CLIENTE.put(c.getId(), c);
        } else {
            cActual = null;
        }
        return c;
    }

    //
    public Cliente consultaCliente(final int id) {
        return DB_CLIENTE.values().stream().filter(c -> c.getId() == id).findFirst().orElse(null);
    }

    public List<Cliente> consultaClientes() {
        return new ArrayList<>(DB_CLIENTE.values());
    }
}
