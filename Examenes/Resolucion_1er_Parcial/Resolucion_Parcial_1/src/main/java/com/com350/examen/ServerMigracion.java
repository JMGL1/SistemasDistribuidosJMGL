/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.com350.examen;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author Dell
 */
public class ServerMigracion {
    public static void main(String[] args) {
        int port = 5002;
        ServerSocket server = null;
        try {
            server = new ServerSocket(port);
            System.out.println("Se inicio el servidor de Migración con éxito");
        } catch (IOException ex) {
            System.out.println("Error al iniciar el servidor: " + ex.getMessage());
            return; // Termina si el puerto está ocupado
        }

        while (true) {
            try {
                // conexion entre cliente y servidor para comunicacion bidireccional
                Socket client = server.accept(); 
                BufferedReader fromClient = new BufferedReader(new InputStreamReader(client.getInputStream())); 
                
                System.out.println("Cliente se conecto");
                String recibido = fromClient.readLine();
                System.out.println("El cliente envio el mensaje: " + recibido);
                
                String respuesta = procesarSolicitud(recibido);
                
                PrintStream toClient = new PrintStream(client.getOutputStream());
                toClient.println(respuesta);
                
                client.close(); // Buena práctica, cerramos el socket al terminar
            } catch (IOException ex) {
                System.out.println("Error en conexión: " + ex.getMessage());
            }
        }
    }
    
    public static String procesarSolicitud(String cadena) {
        if (cadena == null || !cadena.contains(":")) {
            return "invalido";
        }
        String[] comando = cadena.split(":");
        // Ejemplo de cadena: "pasaporte:123"
        if (comando.length > 1 && "123".equals(comando[1])) {
            return "valido:BOLIVIA";
        } else {
            return "invalido";
        }
    }
    
}
