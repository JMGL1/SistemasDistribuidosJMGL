/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sis258.practicas.practica2;

/**
 *
 * @author X13
 */
import java.io.*;
import java.net.*;

public class BancoMercantilTCP {
    public static void main(String[] args) {
        int puerto = 5000;
        // Datos quemados para la práctica
        String ciPrueba = "11021654";
        String cuentaPrueba = "MER-111";
        double saldoPrueba = 5100.0;

        try (ServerSocket serverSocket = new ServerSocket(puerto)) {
            System.out.println("Banco Mercantil (TCP) escuchando en el puerto " + puerto + "...");

            while (true) {
                Socket clienteSocket = serverSocket.accept();
                BufferedReader entrada = new BufferedReader(new InputStreamReader(clienteSocket.getInputStream()));
                PrintWriter salida = new PrintWriter(clienteSocket.getOutputStream(), true);

                String mensajeRecibido = entrada.readLine();
                System.out.println("Mercantil recibió orden: " + mensajeRecibido);

                // Protocolo TCP: "operacion,ci" o "operacion,cuenta,monto"
                String[] partes = mensajeRecibido.split(",");
                String operacion = partes[0];

                if (operacion.equals("buscar") && partes[1].equals(ciPrueba)) {
                    // Formato exigido: cuenta1-saldo1:cuenta2-saldo2...
                    salida.println(cuentaPrueba + "-" + saldoPrueba);
                } 
                else if (operacion.equals("congelar") && partes[1].equals(cuentaPrueba)) {
                    double montoACongelar = Double.parseDouble(partes[2]);
                    saldoPrueba -= montoACongelar; // Simulamos el congelamiento restando
                    salida.println("Congelamiento exitoso. Nuevo saldo disponible: " + saldoPrueba);
                } 
                else {
                    salida.println("no_encontrado");
                }
                clienteSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}