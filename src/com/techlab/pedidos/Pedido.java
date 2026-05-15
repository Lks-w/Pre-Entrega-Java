package com.techlab.pedidos;

import java.util.ArrayList;
import java.util.List;

public class Pedido {

    private static int contadorId = 1;

    private int id;
    private List<LineaPedido> lineas;

    public Pedido() {
        this.id = contadorId++;
        this.lineas = new ArrayList<>();
    }

    public int getId() { return id; }
    public List<LineaPedido> getLineas() { return lineas; }

    public void agregarLinea(LineaPedido linea) {
        lineas.add(linea);
    }

    public double calcularTotal() {
        double total = 0;
        for (LineaPedido linea : lineas) {
            total += linea.getSubtotal();
        }
        return total;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("=== Pedido #%d ===\n", id));
        for (LineaPedido linea : lineas) {
            sb.append(linea.toString()).append("\n");
        }
        sb.append(String.format("  TOTAL: $%.2f", calcularTotal()));
        return sb.toString();
    }
}
