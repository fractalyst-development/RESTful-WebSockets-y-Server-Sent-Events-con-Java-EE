
package curso.rest.dominio;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "articulo")
public class Articulo
{
   protected String producto;
   protected double costo;

   public String getProducto()
   {
      return producto;
   }

   public void setProducto(String producto)
   {
      this.producto = producto;
   }

   public double getCosto()
   {
      return costo;
   }

   public void setCosto(double costo)
   {
      this.costo = costo;
   }
}