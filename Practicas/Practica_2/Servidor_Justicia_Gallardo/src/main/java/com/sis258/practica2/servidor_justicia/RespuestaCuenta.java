/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sis258.practicas.practica2;

/**
 *
 * @author X13
 */
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

// Objeto que agrupa las cuentas encontradas para enviarlas al Juez
public class RespuestaCuenta implements Serializable {
    private String mensaje;
    private List<Cuenta> cuentas;

    public RespuestaCuenta(String mensaje) {
        this.mensaje = mensaje;
        this.cuentas = new ArrayList<>();
    }

    public void agregarCuenta(Cuenta cuenta) {
        this.cuentas.add(cuenta);
    }

    public List<Cuenta> getCuentas() { return cuentas; }
    public String getMensaje() { return mensaje; }
}