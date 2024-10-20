package com.example.food_front.models;


import java.util.ArrayList;
import java.util.List;

public class Carrito {
    private List<Producto> productos = new ArrayList<>();

    public void agregarProducto(Producto producto) {
        productos.add(producto);
    }

    public void eliminarProducto(Producto producto) {
        for (int i = 0; i < productos.size(); i++) {
            if (productos.get(i).getIdProducto() == producto.getIdProducto()) {
                productos.remove(i);
                break;
            }
        }
    }

    public int obtenerCantidadProductos() {
        return productos.size();
    }

    public List<Producto> obtenerProductos() {
        return productos;
    }

    public void limpiarCarrito() {
        productos.clear();
    }
}

