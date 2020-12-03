package curso.rest.dominio.dao;

import curso.rest.dominio.Cliente;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class ClienteDAO {

//    private static ClienteDAO singleton = new ClienteDAO();
    DataSourceHandler ds = DataSourceHandler.getSingleton();

    public ClienteDAO() {
    }

    //
//    public static ClienteDAO getSingleton() {
//        return singleton;
//    }
    //
    public Cliente altaCliente(final Cliente c) {
        return ds.altaCliente(c);
    }

    //
    public void bajaCliente(final int id) {
        ds.bajaCliente(id);
    }
    //

    public Cliente cambiosCliente(final Cliente c) {
        return ds.cambiosCliente(c);
    }

    //
    public Cliente consultaCliente(final int id) {
        return ds.consultaCliente(id);
    }

    public List<Cliente> consultaClientes() {
        return ds.consultaClientes();
    }

    public List<Cliente> consultaClientes(int tamanhio) {
        return ds.consultaClientes();
    }
    public int consultaNumeroDeClientes() {
        return ds.consultaClientes().size();
    }

    public List<Cliente> consultaClientes(int tamanhio, int pagina, List<String> ordenarPor) {
        List<Cliente> clientes;
        Comparator<Cliente> comparator = Comparator.comparing(Cliente::getId);
        if (ordenarPor == null) {
            clientes = consultaClientes();
        } else {
            clientes = consultaClientes().stream().collect(Collectors.toList());
            switch (ordenarPor.size()) {
                case 1:
                    switch (ordenarPor.get(0).toUpperCase()) {
                        case "ID":
                            comparator = Comparator.comparing(Cliente::getId);
                            break;
                        case "NOMBRE":
                            comparator = Comparator.comparing(Cliente::getNombre);
                            break;
                        case "APELLIDOS":
                            comparator = Comparator.comparing(Cliente::getApellidos);
                            break;
                    }
                    break;
                case 2:
                    switch (ordenarPor.get(0).toUpperCase() + "-" + ordenarPor.get(1).toUpperCase()) {
                        case "ID-NOMBRE":
                            comparator = Comparator.comparing(Cliente::getId).thenComparing(Cliente::getNombre);
                            break;
                        case "ID-APELLIDOS":
                            comparator = Comparator.comparing(Cliente::getId).thenComparing(Cliente::getApellidos);
                            break;
                        case "NOMBRE-ID":
                            comparator = Comparator.comparing(Cliente::getNombre).thenComparing(Cliente::getId);
                            break;
                        case "NOMBRE-APELLIDOS":
                            comparator = Comparator.comparing(Cliente::getNombre).thenComparing(Cliente::getApellidos);
                            break;
                        case "APELLIDOS-ID":
                            comparator = Comparator.comparing(Cliente::getApellidos).thenComparing(Cliente::getId);
                            break;
                        case "APELLIDOS-NOMBRE":
                            comparator = Comparator.comparing(Cliente::getApellidos).thenComparing(Cliente::getNombre);
                            break;
                    }
                    break;
                default:
                    comparator = Comparator.comparing(Cliente::getId);
                    break;
            }

            Collections.sort(clientes, comparator);
        }
        // qué página
        int indiceInicial = tamanhio * pagina - tamanhio;
        int indiceFinal = tamanhio * pagina;
        //
        List<Cliente> clientesSalida = new ArrayList<>(tamanhio);

        if (indiceInicial <= clientes.size()) {
            for (int i = indiceInicial; i < indiceFinal; i++) {
                if (i == clientes.size()) {
                    break;
                }
                clientesSalida.add(clientes.get(i));
            }
        }

        return clientesSalida;
    }

}
