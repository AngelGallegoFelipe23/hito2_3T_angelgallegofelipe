package com.empresa.hito_angelgallegofelipe;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Fruta {
    private final StringProperty id;
    private final StringProperty nombre;
    private final DoubleProperty precio;
    private final DoubleProperty pesoPorUnidad;

    public Fruta(String id, String nombre, Double precio, Double pesoPorUnidad) {
        this.id = new SimpleStringProperty(id);
        this.nombre = new SimpleStringProperty(nombre);
        this.precio = new SimpleDoubleProperty(precio);
        this.pesoPorUnidad = new SimpleDoubleProperty(pesoPorUnidad);
    }

    public String getId() {
        return id.get();
    }

    public StringProperty idProperty() {
        return id;
    }

    public String getNombre() {
        return nombre.get();
    }

    public StringProperty nombreProperty() {
        return nombre;
    }

    public double getPrecio() {
        return precio.get();
    }

    public DoubleProperty precioProperty() {
        return precio;
    }

    public double getPesoPorUnidad() {
        return pesoPorUnidad.get();
    }

    public DoubleProperty pesoPorUnidadProperty() {
        return pesoPorUnidad;
    }
}
