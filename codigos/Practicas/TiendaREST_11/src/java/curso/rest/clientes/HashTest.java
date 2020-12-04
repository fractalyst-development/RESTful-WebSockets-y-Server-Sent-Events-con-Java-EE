/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package curso.rest.clientes;

import java.util.Objects;

/**
 *
 * @author usuario
 */
public class HashTest {
    public static void main(String[] args) {
        String nombre = "nombre";
        System.out.println("1.nombre:"+Objects.hash(nombre));
        System.out.println("1.nombre:"+Objects.hash(nombre));
        System.out.println("2.nombre:"+nombre.hashCode());
        System.out.println("2.nombre:"+nombre.hashCode());
        Persona p = new Persona(nombre);
        Persona pp = new Persona(nombre);
        System.out.println("3.persona:"+p.hashCode());
        System.out.println("3.persona:"+pp.hashCode());
        System.out.println("4.persona:"+Objects.hash(p));
        System.out.println("4.persona:"+Objects.hash(pp));
        System.out.println("4.persona:"+Objects.hash(pp));
    }
}
class Persona {
    String nombre;

    public Persona(String nombre) {
        this.nombre = nombre;
    }
    public String getNombre(){
        return nombre;
    }
    public void setNombre(String n) {
        this.nombre = n;
    }
}