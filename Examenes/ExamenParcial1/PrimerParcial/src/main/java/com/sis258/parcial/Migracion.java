/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sis258.parcial;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author PC
 */
public class Migracion {
    public static void main(String[] args){
        int port = 5002;
        ServerSocket server;
        try{
            server = new ServerSocket(port);
            System.out.println("Se inicio con exito");
            while(true){
                Socket client;
                PrintStream toClient;
                client = server.accept();
                BufferedReader fromClient = new BufferedReader(new InputStreamReader(client.getInputStream()));
                System.out.println("cliente se conecto");
                String recibido = fromClient.readLine();
                toClient = new PrintStream(client.getOutputStream());
                
            }
        }catch(IOException ex){
            System.out.print(ex.getMessage());
        }
    }
}
