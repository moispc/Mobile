package com.example.food_front.models;

import java.util.ArrayList;

public class Carrito {
    private static Carrito instance;
    private ArrayList<Producto> productos;

    // Constructor privado para evitar instanciación
    private Carrito() {
        productos = new ArrayList<>();
    }

    // Método para obtener la instancia del carrito (Singleton)
    public static Carrito getInstance() {
        if (instance == null) {
            instance = new Carrito();
        }
        return instance;
    }

    // Método para agregar productos al carrito
    public void agregarProducto(Producto producto) {
        productos.add(producto);
    }

    // Método para agregar una lista de productos al carrito
    public void agregarProductos(ArrayList<Producto> productos) {
        this.productos.addAll(productos);
    }

    // Método para eliminar un producto del carrito
    public void eliminarProducto(Producto producto) {
        productos.remove(producto);
    }

    // Método para obtener la lista de productos del carrito
    public ArrayList<Producto> obtenerProductos() {
        return productos;
    }

    // Método para limpiar el carrito
    public void limpiarCarrito() {
        productos.clear();
    }
}
