/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sis258.practicas.practica2;

/**
 *
 * @author X13
 */
import java.rmi.Naming;
import java.util.List;

public class JuezCliente {
    public static void main(String[] args) {
        try {
            // Conectamos con el Gateway (Servidor de Justicia) por RMI
            IJusticia justicia = (IJusticia) Naming.lookup("rmi://172.20.10.3:1099/Justicia");
            
            // Datos de prueba especificados en la práctica
            String ci = "11021654";
            String nombres = "Juan";
            String apellidos = "Perez Segovia";

            System.out.println("--- JUEZ INICIANDO INVESTIGACIÓN ---");
            RespuestaCuenta respuesta = justicia.consultarCuentas(ci, nombres, apellidos);
            
            System.out.println(respuesta.getMensaje());
            List<Cuenta> cuentas = respuesta.getCuentas();
            
            if (cuentas.isEmpty()) {
                System.out.println("El acusado no tiene cuentas bancarias.");
            } else {
                for (Cuenta c : cuentas) {
                    System.out.println("Encontrada: " + c.toString());
                    
                    // Instruir congelamiento por un monto, por ejemplo 1000 bs.
                    System.out.println(">>> Emitiendo orden de congelamiento de 1000.0 en " + c.getBanco());
                    boolean exito = justicia.congelarMonto(c.getNumeroCuenta(), 1000.0, c.getBanco());
                    System.out.println("Resultado congelamiento: " + (exito ? "EXITOSO" : "FALLIDO"));
                }
            }
            
            System.out.println("--- INVESTIGACIÓN FINALIZADA ---");
            
        } catch (Exception e) {
            System.out.println("Error en el cliente del Juez: " + e.getMessage());
            e.printStackTrace();
        }
    }
}