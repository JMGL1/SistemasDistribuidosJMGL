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
          
            ServerSocket server = new ServerSocket(port);
            System.out.println("Se inicio el servidor interactivo con exito");
            
            while (true) {
                Socket client = server.accept();
                BufferedReader fromClient = new BufferedReader(new InputStreamReader(client.getInputStream()));
                PrintStream toClient = new PrintStream(client.getOutputStream());
                System.out.println("Cliente se conecto");
                
                String recibido = fromClient.readLine();
                int numero1 = Integer.parseInt(recibido);
                
                toClient.println("introduzca el segundo numero");
                String recibido2 = fromClient.readLine();
                int numero2 = Integer.parseInt(recibido2);
                
                toClient.println("1.suma 2.resta 3.multiplicacion 4.division. introduzca la operacion");
                String recibido3 = fromClient.readLine();
                
                int resultado = 0;

                switch (recibido3) {
                    case "suma": case "1": resultado = numero1 + numero2; break;
                    case "resta": case "2": resultado = numero1 - numero2; break;
                    case "multiplicacion": case "3": resultado = numero1 * numero2; break;
                    case "division": case "4": resultado = numero1 / numero2; break;
                }
                
                toClient.println(String.valueOf(resultado));

                client.close();
            }
        } catch (IOException ex) {
            System.out.print(ex.getMessage());
        }
    }
}