/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bo.edu.usfx.jgroups.remate;

/**
 *
 * @author X13
 */

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Subasta implements Serializable {
    private String articulo;
    private double precioActual;
    private String mejorPostor;
    private long tiempoFinAbsoluto;
    private boolean activa;
    private List<String> historialPujas;

    public Subasta(String articulo, double precioBase, long milisegundosDuracion) {
        this.articulo = articulo.toLowerCase();
        this.precioActual = precioBase;
        this.mejorPostor = "Nadie";
        this.tiempoFinAbsoluto = System.currentTimeMillis() + milisegundosDuracion;
        this.activa = true;
        this.historialPujas = new ArrayList<>();
    }

    public String getArticulo() { return articulo; }
    public double getPrecioActual() { return precioActual; }
    public void setPrecioActual(double precioActual) { this.precioActual = precioActual; }
    public String getMejorPostor() { return mejorPostor; }
    public void setMejorPostor(String mejorPostor) { this.mejorPostor = mejorPostor; }
    public long getTiempoFinAbsoluto() { return tiempoFinAbsoluto; }
    public boolean isActiva() { return activa; }
    public void setActiva(boolean activa) { this.activa = activa; }
    public List<String> getHistorialPujas() { return historialPujas; }

    @Override
    public String toString() {
        long faltan = (tiempoFinAbsoluto - System.currentTimeMillis()) / 1000;
        if (faltan < 0) faltan = 0;
        String estado = activa ? (faltan + "s restantes") : "CERRADA";
        return String.format("Articulo: %s | Mejor puja: %.2f (%s) | Estado: %s", 
                articulo, precioActual, mejorPostor, estado);
    }
}