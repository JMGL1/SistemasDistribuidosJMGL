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
import java.util.Scanner;

/**
 *
 * @author Dell
 */
public class ServidorAntifraude {

    
  public static void main (String args[]) { 
    int port=6789;  
    try {
      
      DatagramSocket socketUDP = new DatagramSocket(port);
      byte[] bufer = new byte[1000];

      while (true) {
        // Construimos el DatagramPacket para recibir peticiones
        DatagramPacket peticion =
          new DatagramPacket(bufer, bufer.length);

        // Leemos una petición del DatagramSocket
        socketUDP.receive(peticion);

        System.out.print("Datagrama recibido del host: " + peticion.getAddress());
        System.out.println(" desde el puerto remoto: " + peticion.getPort());
        
        // CORRECCIÓN: usar getLength() para no agarrar los bytes nulos del final del buffer
        String cadena = new String(peticion.getData(), 0, peticion.getLength());
        System.out.println("Solicitud a procesar: " + cadena);
        
        String response = procesar(cadena);
       
        byte[] mensaje = response.getBytes();

        DatagramPacket respuesta =
          new DatagramPacket(mensaje, mensaje.length,
                             peticion.getAddress(), peticion.getPort());

        // Enviamos la respuesta
        socketUDP.send(respuesta);
      }

    } catch (SocketException e) {
      System.out.println("Socket: " + e.getMessage());
    } catch (IOException e) {
      System.out.println("IO: " + e.getMessage());
    }
  }
  
  public static String procesar(String cadena) {
      try {
          String[] comando = cadena.split(":"); // sepera "riesgo" de "pasaporte-monto"
          if (comando.length > 1) {
              String consulta = comando[1];
              String[] comando2 = consulta.split("-"); // separa pasaporte de monto
              
              if (comando2.length > 1) {
                  // Lo parseamos como double porque el parametro montoUSD era Double
                  double monto = Double.parseDouble(comando2[1]);
                  
                  if (monto > 1000) {
                      return "alto";
                  } else {
                      return "bajo";
                  }
              }
          }
      } catch (NumberFormatException e) {
          System.out.println("Error parseando el monto numérico: " + e.getMessage());
      } catch (Exception e) {
          System.out.println("Error procesando trama: " + e.getMessage());
      }
      return "alto"; // Por seguridad, si falla el parseo se asume riesgo alto
  }

}