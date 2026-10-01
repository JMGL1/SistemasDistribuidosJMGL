/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.com350.examen;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 *
 * @author Dell
 */
public class Turista {

    public static void main(String[] a) throws Exception {

        // La Operadora se está publicando en el puerto 1098 según ServidorOperadora.java
        Registry reg = LocateRegistry.getRegistry(1098);
        
        // El nombre registrado en ServidorOperadora es "Operadora" (con O mayúscula)
        IOperadora operadora = (IOperadora) reg.lookup("Operadora");

        System.out.println(operadora.ComprarTour("123", "T1", 3));

    }
}
