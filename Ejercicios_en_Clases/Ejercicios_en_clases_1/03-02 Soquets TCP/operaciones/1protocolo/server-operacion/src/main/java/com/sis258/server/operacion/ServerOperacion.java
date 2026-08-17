/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.sis258.server.operacion;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerOperacion {
    public static void main(String[] args) {
        int port = 5002;
        try {
            // CORRECCION: ServerSocket fuera del while
            ServerSocket server = new ServerSocket(port);
            System.out.println("Se inicio el servidor de protocolo con exito");
            
            while (true) {
                Socket client = server.accept();
                BufferedReader fromClient = new BufferedReader(new InputStreamReader(client.getInputStream()));
                PrintStream toClient = new PrintStream(client.getOutputStream());
                
                String recibido = fromClient.readLine();
                System.out.println("Solicitud recibida: " + recibido);
                
                String respuesta = procesarSolicitud(recibido);
                toClient.println(respuesta);
                
                client.close(); // Cerrar cliente
            }
        } catch (IOException ex) {
            System.out.print(ex.getMessage());
        }
    }

    // CORRECCION: Logica sencilla del protocolo
    public static String procesarSolicitud(String cadena) {
        try {
            // Cortamos el mensaje donde haya "dos puntos" (ej: "suma:10:5")
            String[] partes = cadena.split(":"); 
            String operacion = partes[0];
            int num1 = Integer.parseInt(partes[1]);
            int num2 = Integer.parseInt(partes[2]);
            int resultado = 0;
            
            if (operacion.equals("suma")) resultado = num1 + num2;
            if (operacion.equals("resta")) resultado = num1 - num2;
            if (operacion.equals("multiplicacion")) resultado = num1 * num2;
            if (operacion.equals("division")) resultado = num1 / num2;
            
            return String.valueOf(resultado);
        } catch (Exception e) {
            return "Error de protocolo. Formato correcto -> operacion:numero1:numero2";
        }
    }
}