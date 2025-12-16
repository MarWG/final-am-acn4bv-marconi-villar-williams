package com.example.eternal_games.model;

import java.util.Date;
import java.util.List;

public class Compra {
    public String id;
    public Date fecha;
    public boolean leido;
    public String ubicacionRetiro;
    public int cantidadTotal;
    public double totalGeneral;
    public List<Producto> productos;

    public boolean isLeido() {
        return leido;
    }

    public void setLeido(boolean leido) {
        this.leido = leido;
    }

}
