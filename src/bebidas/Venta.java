package bebidas;

public class Venta {
    private int id;
    private String cliente;
    private Producto idProducto;
    private int cantidad;
    private double totalProducto;

    public Venta() {
    }

    public Venta(int id, String cliente, Producto idProducto, int cantidad, double totalProducto) {
        this.id = id;
        this.cliente = cliente;
        this.idProducto = idProducto;
        this.cantidad = cantidad;
        this.totalProducto = totalProducto;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCliente() {
        return cliente;
    }

    public void setCliente(String cliente) {
        this.cliente = cliente;
    }

    public Producto getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(Producto idProducto) {
        this.idProducto = idProducto;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public double getTotalProducto() {
        return totalProducto;
    }

    public void setTotalProducto(double totalProducto) {
        this.totalProducto = totalProducto;
    }
}
