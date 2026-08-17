package com.sis258.server.operacion;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author X13
 */

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class ClienteInteractivo {
    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        // Pon la IP de tu amigo aqui cuando prueben juntos. Por ahora "localhost"
        Socket socket = new Socket("localhost", 5002); 
        BufferedReader fromServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintStream toServer = new PrintStream(socket.getOutputStream());

        System.out.print("Ingrese el primer numero: ");
        toServer.println(scanner.nextLine());
        
        System.out.println("Servidor: " + fromServer.readLine()); // Lee: "introduzca el segundo numero"
        toServer.println(scanner.nextLine());
        
        System.out.println("Servidor: " + fromServer.readLine()); // Lee: "1.suma 2.resta..."
        toServer.println(scanner.nextLine());
        
        System.out.println("Respuesta del servidor: " + fromServer.readLine());
        socket.close();
    }
}