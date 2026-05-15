package com.techlab;

import com.techlab.excepciones.StockInsuficienteException;
import com.techlab.pedidos.LineaPedido;
import com.techlab.pedidos.Pedido;
import com.techlab.productos.Producto;
import com.techlab.servicios.ProductoService;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    private static final ProductoService productoService = new ProductoService();
    private static final List<Pedido> pedidos = new ArrayList<>();
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        int opcion = 0;
        do {
            mostrarMenu();
            opcion = leerEntero("Ingrese una opcion: ");
            switch (opcion) {
                case 1 -> agregarProducto();
                case 2 -> productoService.listarProductos();
                case 3 -> buscarActualizar();
                case 4 -> eliminarProducto();
                case 5 -> crearPedido();
                case 6 -> listarPedidos();
                case 7 -> System.out.println("Hasta luego!");
                default -> System.out.println("Opcion invalida. Intente de nuevo.");
            }
        } while (opcion != 7);

        scanner.close();
    }

    private static void mostrarMenu() {
        System.out.println("\n========== TechLab ==========");
        System.out.println("1) Agregar producto");
        System.out.println("2) Listar productos");
        System.out.println("3) Buscar / Actualizar producto");
        System.out.println("4) Eliminar producto");
        System.out.println("5) Crear pedido");
        System.out.println("6) Listar pedidos");
        System.out.println("7) Salir");
        System.out.println("==============================");
    }

    // ── Opción 1 ──────────────────────────────────────────────────────────────
    private static void agregarProducto() {
        System.out.print("Nombre: ");
        String nombre = scanner.nextLine().trim();
        double precio = leerDouble("Precio: ");
        int stock = leerEntero("Stock: ");
        productoService.agregarProducto(nombre, precio, stock);
    }

    // ── Opción 3 ──────────────────────────────────────────────────────────────
    private static void buscarActualizar() {
        System.out.println("Buscar por: 1) ID  2) Nombre");
        int modo = leerEntero("Opcion: ");

        Producto encontrado = null;
        if (modo == 1) {
            int id = leerEntero("ID: ");
            encontrado = productoService.buscarPorId(id);
        } else if (modo == 2) {
            System.out.print("Nombre: ");
            String nombre = scanner.nextLine().trim();
            encontrado = productoService.buscarPorNombre(nombre);
        } else {
            System.out.println("Opcion invalida.");
            return;
        }

        if (encontrado == null) {
            System.out.println("Producto no encontrado.");
            return;
        }

        System.out.println("Encontrado: " + encontrado);
        System.out.print("Actualizar? (s/n): ");
        String resp = scanner.nextLine().trim();
        if (resp.equalsIgnoreCase("s")) {
            double nuevoPrecio = leerDouble("Nuevo precio: ");
            int nuevoStock = leerEntero("Nuevo stock: ");
            productoService.actualizarProducto(encontrado.getId(), nuevoPrecio, nuevoStock);
        }
    }

    // ── Opción 4 ──────────────────────────────────────────────────────────────
    private static void eliminarProducto() {
        int id = leerEntero("ID del producto a eliminar: ");
        if (productoService.eliminarPorId(id)) {
            System.out.println("Producto eliminado.");
        } else {
            System.out.println("No se encontro un producto con ese ID.");
        }
    }

    // ── Opción 5 ──────────────────────────────────────────────────────────────
    private static void crearPedido() {
        Pedido pedido = new Pedido();
        boolean agregando = true;

        while (agregando) {
            productoService.listarProductos();
            int id = leerEntero("ID del producto a agregar al pedido (0 para terminar): ");
            if (id == 0)
                break;

            Producto producto = productoService.buscarPorId(id);
            if (producto == null) {
                System.out.println("Producto no encontrado.");
                continue;
            }

            int cantidad = leerEntero("Cantidad: ");
            try {
                if (cantidad > producto.getStock()) {
                    throw new StockInsuficienteException(
                            String.format("Stock insuficiente para '%s'. Disponible: %d, solicitado: %d.",
                                    producto.getNombre(), producto.getStock(), cantidad));
                }
                pedido.agregarLinea(new LineaPedido(producto, cantidad));
                System.out.println("Linea agregada.");
            } catch (StockInsuficienteException e) {
                System.out.println("ERROR: " + e.getMessage());
            }
        }

        if (pedido.getLineas().isEmpty()) {
            System.out.println("El pedido esta vacio. No se registro.");
            return;
        }

        System.out.println("\nResumen del pedido:");
        System.out.println(pedido);
        System.out.print("Confirmar pedido? (s/n): ");
        String confirmacion = scanner.nextLine().trim();

        if (confirmacion.equalsIgnoreCase("s")) {
            for (LineaPedido linea : pedido.getLineas()) {
                Producto p = linea.getProducto();
                p.setStock(p.getStock() - linea.getCantidad());
            }
            pedidos.add(pedido);
            System.out.println("Pedido confirmado y stock actualizado.");
        } else {
            System.out.println("Pedido cancelado.");
        }
    }

    // ── Opción 6 ──────────────────────────────────────────────────────────────
    private static void listarPedidos() {
        if (pedidos.isEmpty()) {
            System.out.println("No hay pedidos registrados.");
            return;
        }
        for (Pedido p : pedidos) {
            System.out.println(p);
        }
    }

    // ── Helpers de lectura segura ──────────────────────────────────────────────
    private static int leerEntero(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            try {
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Valor invalido. Ingrese un numero entero.");
            }
        }
    }

    private static double leerDouble(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            try {
                return Double.parseDouble(scanner.nextLine().trim().replace(",", "."));
            } catch (NumberFormatException e) {
                System.out.println("Valor invalido. Ingrese un numero decimal.");
            }
        }
    }
}
