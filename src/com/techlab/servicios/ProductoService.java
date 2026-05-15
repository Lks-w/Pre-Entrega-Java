package com.techlab.servicios;

import com.techlab.productos.Producto;
import java.util.ArrayList;

public class ProductoService {

    private ArrayList<Producto> productos = new ArrayList<>();

    public void agregarProducto(String nombre, double precio, int stock) {
        productos.add(new Producto(nombre, precio, stock));
        System.out.println("Producto agregado correctamente.");
    }

    public void listarProductos() {
        if (productos.isEmpty()) {
            System.out.println("No hay productos registrados.");
            return;
        }
        System.out.println("--- Lista de productos ---");
        for (Producto p : productos) {
            System.out.println(p);
        }
    }

    public Producto buscarPorId(int id) {
        for (Producto p : productos) {
            if (p.getId() == id) return p;
        }
        return null;
    }

    public Producto buscarPorNombre(String nombre) {
        for (Producto p : productos) {
            if (p.getNombre().equalsIgnoreCase(nombre)) return p;
        }
        return null;
    }

    public boolean eliminarPorId(int id) {
        return productos.removeIf(p -> p.getId() == id);
    }

    public void actualizarProducto(int id, double nuevoPrecio, int nuevoStock) {
        Producto p = buscarPorId(id);
        if (p != null) {
            p.setPrecio(nuevoPrecio);
            p.setStock(nuevoStock);
            System.out.println("Producto actualizado: " + p);
        } else {
            System.out.println("No se encontró un producto con ID " + id + ".");
        }
    }
}
