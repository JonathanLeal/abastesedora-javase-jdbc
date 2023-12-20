package bebidas;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import javax.swing.JOptionPane;

public class Bebidas {

    public static void main(String[] args) throws Exception{
        int opc = 0;
        do {            
            opc = menu();
            switch(opc){
                case 1:
                    verProductos();
                    break;
                case 2:
                    comprarProductos();
                    break;
                case 3:
                    JOptionPane.showMessageDialog(null, "Saliendo");
                    break;
                default:
                    JOptionPane.showMessageDialog(null, "Opcion no valida");
                    break;
            }
        } while (opc != 3);
    }
    
    private static int menu(){
        int opc = 0;
        StringBuilder sb = new StringBuilder();
        sb.append("1. Ver productos\n");
        sb.append("2. Comprar productos\n");
        sb.append("3. Salir...\n");
        opc = Integer.parseInt(JOptionPane.showInputDialog(sb));
        return opc;
    }
    
    private static boolean obtenerProducto(int id) throws Exception{
        Conexion conexion = new Conexion();
        boolean existe = false;
        PreparedStatement ide = conexion.con.prepareStatement("select * from productos where id=?");
        try {
            ide.setInt(1, id);
            ResultSet rs = ide.executeQuery();
            existe = rs.first();
            return existe;
        } catch (Exception e) {
            System.out.println("error al verificar el producto: "+e.getMessage());
        }
        return existe;
    }
    
    private static int obtenerExistenciaProducto(int id) throws Exception{
        Conexion conexion = new Conexion();
        PreparedStatement pst = conexion.con.prepareStatement("select existencia from productos where id=?");
        int existencia = 0;
        try {
            pst.setInt(1, id);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                existencia = rs.getInt("existencia");
                System.out.println(existencia);
                return existencia;
            }
        } catch (Exception e) {
            System.out.println("error al obtener la existencia: "+e.getMessage());
        }
        return existencia;
    }
    
    private static double obtenerPrecioProducto(int id) throws Exception{
        Conexion conexion = new Conexion();
        PreparedStatement pst = conexion.con.prepareStatement("select precio from productos where id=?");
        double precio = 0.00;
        try {
            pst.setInt(1, id);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                precio = rs.getDouble("precio");
                return precio;
            }
        } catch (Exception e) {
            System.out.println("error al obtener la existencia: "+e.getMessage());
        }
        return precio;
    }
    
    private static void comprarProductos() throws Exception{
        Conexion conexion = new Conexion();
        PreparedStatement insertVenta = conexion.con.prepareStatement("insert into venta(cliente,idProducto,cantidad,totalProducto) values(?,?,?,?)");
        PreparedStatement updateProducto = conexion.con.prepareStatement("update productos set existencia=? where id=?");
        int opcl = 0;
        try {
            do {
                String cliente = JOptionPane.showInputDialog("ingrese su nombre");
                
                int idProducto = Integer.parseInt(JOptionPane.showInputDialog("Ingresa el id del producto"));
                boolean productoId = obtenerProducto(idProducto);
                if (!productoId) {
                    JOptionPane.showMessageDialog(null, "Producto no encontrado");
                    return;
                }
                
                int cantidadComprada = Integer.parseInt(JOptionPane.showInputDialog("Ingres la cantidad que deseas comprar"));
                int existenciaActual = obtenerExistenciaProducto(idProducto);
                if (cantidadComprada > existenciaActual) {
                    JOptionPane.showMessageDialog(null, "No puede comprar mas que la existencia actual");
                    return;
                }
                
                double precioProducto = obtenerPrecioProducto(idProducto);
                double totalProducto = cantidadComprada * precioProducto;
                int nuevaExistencia = existenciaActual - cantidadComprada;
          
                insertVenta.setString(1, cliente);
                insertVenta.setInt(2, idProducto);
                insertVenta.setInt(3, cantidadComprada);
                insertVenta.setDouble(4, totalProducto);
                insertVenta.executeUpdate();
                
                updateProducto.setInt(1, nuevaExistencia);
                updateProducto.setInt(2, idProducto);
                updateProducto.executeUpdate();
                
                JOptionPane.showMessageDialog(null, "Compra realizada con exito");
                
                opcl = Integer.parseInt(JOptionPane.showInputDialog("1. Salir\n"+ "0. Para seguir comprando"));
                
            } while (opcl != 1);
        } catch (Exception e) {
            System.out.println("error al hacer la venta: "+e.getMessage());
        }
    }
    
    private static void verProductos() throws Exception{
        Conexion conexion = new Conexion();
        ArrayList<Producto> lista = new ArrayList();
        StringBuilder sb = new StringBuilder();
        PreparedStatement productos = conexion.con.prepareStatement("select p.id,p.nombre,p.precio,p.existencia,c.nombreCat from productos AS p inner join categoria AS c on p.idCategoria=c.id");
        try {
            ResultSet rs = productos.executeQuery();
            while (rs.next()) {                
                Producto p = new Producto();
                Categoria cat = new Categoria();
                p.setId(rs.getInt("p.id"));
                p.setNombre(rs.getString("p.nombre"));
                p.setPrecio(rs.getDouble("p.precio"));
                p.setExistencia(rs.getInt("p.existencia"));
                cat.setNombreCat(rs.getString("c.nombreCat"));
                p.setIdCategoria(cat);
                lista.add(p);
            }
            
            if (lista.size() == 0) {
                JOptionPane.showMessageDialog(null, "No hay productos");
                return;
            }
            
            sb.append("ID \t producto \t precio \t existencia \t categoria\n");
            for (Producto pro : lista) {
               sb.append(pro.getId() + " \t " + pro.getNombre() + " \t " + pro.getPrecio() + " \t " + pro.getExistencia() + " \t " + pro.getIdCategoria().getNombreCat() + "\n");
            }
            JOptionPane.showMessageDialog(null, sb);
            
        } catch (Exception e) {
            System.out.println("Error al ver los productos: "+e.getMessage());
        }
    }
}
