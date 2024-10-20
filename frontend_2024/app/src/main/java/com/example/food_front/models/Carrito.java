package com.example.food_front.models;

public class Carrito {
    private String producto;
    private int cantidad;
    private double precio;
    private String imagenUrl;


// Constructor
    public Carrito(String producto, int cantidad, double precio, String ImagenUrl){
        this.producto = producto;
        this.cantidad = cantidad;
        this.precio = precio;
        this.imagenUrl = (imagenUrl != null) ? imagenUrl : ""; //Asigna una imagen vacia si es null
    }

    public String getProducto() {
        return producto;
    }

    public int getCantidad() {
        return cantidad;
    }

    public CharSequence getPrecio() {
        return precio;
    }

    public String getImagenUrl() {
        return imagenUrl;
    }
}