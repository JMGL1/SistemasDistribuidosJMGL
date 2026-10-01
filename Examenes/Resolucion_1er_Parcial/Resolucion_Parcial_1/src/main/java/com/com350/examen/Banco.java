/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.com350.examen;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;

/**
 *
 * @author Dell
 */
public class Banco extends UnicastRemoteObject
        implements IBanco {

    public Banco() throws RemoteException {
        super();
    }

    // Simulación de una pequeña base de datos de saldo
    private double saldoCliente = 1500.0;

    @Override
    public Pago Debitar(String pasaporte, double montoUSD) throws RemoteException {

        // Llamar al servidor antifraude (UDP) enviando el pasaporte y el monto
        String respuesta = ConsultarAntifraude(pasaporte, montoUSD);
        
        if (respuesta.equals("bajo")) {
            // "El BANCO aprueba solo si el riesgo es bajo y el saldo del pasaporte alcanza"
            if (saldoCliente >= montoUSD) {
                saldoCliente -= montoUSD; // Descontar saldo
                return new Pago(true, "AUTH-123", "");
            } else {
                // Rechazo por saldo insuficiente tal como pide el examen
                return new Pago(false, "", "Saldo insuficiente");
            }
        }
        
        // Rechazo por riesgo alto tal como pide el examen
        return new Pago(false, "", "Riesgo alto");
    }

    public String ConsultarAntifraude(String pasaporte, double montoUSD) {
        int puerto = 6789;
        try {
            String ip = "localhost";
            DatagramSocket socketUDP = new DatagramSocket();
            
            // Armamos la petición de acuerdo al requisito: "riesgo:pasaporte-monto"
            String trama = "riesgo:" + pasaporte + "-" + montoUSD;
            byte[] mensaje = trama.getBytes();
            
            InetAddress hostServidor = InetAddress.getByName(ip);

            // Construimos un datagrama para enviar el mensaje al servidor
            DatagramPacket peticion
                    = new DatagramPacket(mensaje, mensaje.length, hostServidor, puerto);

            // Enviamos el datagrama
            socketUDP.send(peticion);

            // Construimos el DatagramPacket que contendrá la respuesta
            byte[] bufer = new byte[1000];
            DatagramPacket respuesta = new DatagramPacket(bufer, bufer.length);
            socketUDP.receive(respuesta);

            // MUY IMPORTANTE: Se debe usar getLength() para no convertir en string los nulls residuales (\0) del arreglo de 1000 bytes.
            String cadena = new String(respuesta.getData(), 0, respuesta.getLength()).trim();

            // Cerramos el socket
            socketUDP.close();
            return cadena;
            
        } catch (SocketException e) {
            System.out.println("Socket: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("IO: " + e.getMessage());
        }
        return "alto"; // Si falla la comunicación, asumimos riesgo alto
    }

}
