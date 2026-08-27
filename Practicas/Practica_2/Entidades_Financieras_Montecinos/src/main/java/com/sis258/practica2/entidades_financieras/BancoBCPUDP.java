/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sis258.practicas.practica2;

/**
 *
 * @author X13
 */
import java.net.*;

public class BancoBCPUDP {
    public static void main(String[] args) {
        int puerto = 6000;
        // Datos quemados
        String ciPrueba = "11021654";
        String cuentaPrueba = "BCP-222";
        double saldoPrueba = 6500.0;

        try (DatagramSocket socket = new DatagramSocket(puerto)) {
            System.out.println("Banco BCP (UDP) escuchando en el puerto " + puerto + "...");
            byte[] buffer = new byte[1024];

            while (true) {
                DatagramPacket paqueteRecibido = new DatagramPacket(buffer, buffer.length);
                socket.receive(paqueteRecibido);
                
                String mensaje = new String(paqueteRecibido.getData(), 0, paqueteRecibido.getLength());
                System.out.println("BCP recibió orden: " + mensaje);

                // Protocolo UDP exigido: "Operación:ci" o "Operacion:cuenta:monto"
                String[] partes = mensaje.split(":");
                String operacion = partes[0];
                String respuesta = "no_encontrado";

                if (operacion.equals("buscar") && partes[1].equals(ciPrueba)) {
                    // Formato exigido: cuenta1-saldo1:cuenta2-saldo2...
                    respuesta = cuentaPrueba + "-" + saldoPrueba;
                }
                else if (operacion.equals("congelar") && partes[1].equals(cuentaPrueba)) {
                    double montoACongelar = Double.parseDouble(partes[2]);
                    saldoPrueba -= montoACongelar;
                    respuesta = "Congelamiento exitoso. Nuevo saldo: " + saldoPrueba;
                }

                byte[] bufferRespuesta = respuesta.getBytes();
                DatagramPacket paqueteRespuesta = new DatagramPacket(
                        bufferRespuesta, bufferRespuesta.length,
                        paqueteRecibido.getAddress(), paqueteRecibido.getPort());
                socket.send(paqueteRespuesta);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}