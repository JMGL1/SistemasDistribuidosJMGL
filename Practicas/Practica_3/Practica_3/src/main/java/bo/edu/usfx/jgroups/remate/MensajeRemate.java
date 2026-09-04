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

public class MensajeRemate implements Serializable {
    public enum Tipo { NUEVA_SUBASTA, PROPONER_PUJA, PUJA_ACEPTADA, CIERRE }
    
    private Tipo tipo;
    private Subasta subasta;
    private String articulo;
    private double monto;
    private String remitente;

    public MensajeRemate(Tipo tipo) { this.tipo = tipo; }

    public Tipo getTipo() { return tipo; }
    public Subasta getSubasta() { return subasta; }
    public void setSubasta(Subasta subasta) { this.subasta = subasta; }
    public String getArticulo() { return articulo; }
    public void setArticulo(String articulo) { this.articulo = articulo; }
    public double getMonto() { return monto; }
    public void setMonto(double monto) { this.monto = monto; }
    public String getRemitente() { return remitente; }
    public void setRemitente(String remitente) { this.remitente = remitente; }
}