/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sis258.parcial;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
/**
 *
 * @author PC
 */
public class Antifraude {
    public static void main(String[] args){
        int port = 6789;
        try{
            DatagramSocket socketUDP = new DatagramSocket(port);
            byte[] bufer = new byte[1000];
            while(true){
                DatagramPacket peticion = new DatagramPacket(bufer, bufer.length);
                socketUDP.receive(peticion);
                System.out.println("Datagrama recibido del host: " + peticion.getAddress());
                System.out.println("Desde en el puerto remoto: " + peticion.getPort());
                String cadena = new String (peticion.getData());
                String response="hola"+cadena;
                byte[] mensaje = response.getBytes();
                DatagramPacket respuesta = new DatagramPacket(mensaje, response.length(), peticion.getAddress(), peticion.getPort());
                socketUDP.send(respuesta);
            }
        }catch(SocketException e){
            System.out.print("Socket: "+ e.getMessage());
        }catch(IOException e){
            System.out.print("IO: "+ e.getMessage());
        }
    }
}
