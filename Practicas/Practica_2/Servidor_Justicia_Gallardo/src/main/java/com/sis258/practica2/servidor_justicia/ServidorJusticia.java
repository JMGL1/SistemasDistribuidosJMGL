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
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;

public class ServidorJusticia extends UnicastRemoteObject implements IJusticia {

    protected ServidorJusticia() throws RemoteException {
        super();
    }

    @Override
    public RespuestaCuenta consultarCuentas(String ci, String nombres, String apellidos) throws RemoteException {
        System.out.println("Juez consultando a: " + nombres + " " + apellidos + " (CI: " + ci + ")");
        RespuestaCuenta respuesta = new RespuestaCuenta("Búsqueda finalizada para CI: " + ci);

        // 1. Consultar a Banco Mercantil (TCP)
        try (Socket socket = new Socket("172.20.10.4", 5000);
             PrintWriter salida = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
             
             salida.println("buscar," + ci);
             String respMercantil = entrada.readLine();
             if (!respMercantil.equals("no_encontrado")) {
                 String[] partes = respMercantil.split("-");
                 respuesta.agregarCuenta(new Cuenta("Mercantil", partes[0], Double.parseDouble(partes[1])));
             }
        } catch (IOException e) {
            System.out.println("Error conectando con TCP Mercantil");
        }

        // 2. Consultar a Banco BCP (UDP)
        try (DatagramSocket socketUDP = new DatagramSocket()) {
            String orden = "buscar:" + ci; // Formato exigido en la práctica
            byte[] enviarInfo = orden.getBytes();
            InetAddress ip = InetAddress.getByName("172.20.10.4");
            
            DatagramPacket enviarPaquete = new DatagramPacket(enviarInfo, enviarInfo.length, ip, 6000);
            socketUDP.send(enviarPaquete);

            byte[] recibirInfo = new byte[1024];
            DatagramPacket recibirPaquete = new DatagramPacket(recibirInfo, recibirInfo.length);
            socketUDP.receive(recibirPaquete);

            String respBCP = new String(recibirPaquete.getData(), 0, recibirPaquete.getLength());
            if (!respBCP.equals("no_encontrado")) {
                String[] partes = respBCP.split("-");
                respuesta.agregarCuenta(new Cuenta("BCP", partes[0], Double.parseDouble(partes[1])));
            }
        } catch (IOException e) {
            System.out.println("Error conectando con UDP BCP");
        }

        return respuesta;
    }

    @Override
    public boolean congelarMonto(String numeroCuenta, double monto, String banco) throws RemoteException {
        System.out.println("Orden de juez: Congelar " + monto + " en cuenta " + numeroCuenta + " (" + banco + ")");
        
        if (banco.equalsIgnoreCase("Mercantil")) {
            try (Socket socket = new Socket("172.20.10.4", 5000);
                 PrintWriter salida = new PrintWriter(socket.getOutputStream(), true);
                 BufferedReader entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
                 
                 salida.println("congelar," + numeroCuenta + "," + monto);
                 System.out.println("Respuesta Mercantil: " + entrada.readLine());
                 return true;
            } catch (IOException e) { return false; }
        } 
        else if (banco.equalsIgnoreCase("BCP")) {
            try (DatagramSocket socketUDP = new DatagramSocket()) {
                String orden = "congelar:" + numeroCuenta + ":" + monto;
                byte[] enviarInfo = orden.getBytes();
                InetAddress ip = InetAddress.getByName("172.20.10.4");
                
                DatagramPacket enviar = new DatagramPacket(enviarInfo, enviarInfo.length, ip, 6000);
                socketUDP.send(enviar);
                
                byte[] buffer = new byte[1024];
                DatagramPacket recibir = new DatagramPacket(buffer, buffer.length);
                socketUDP.receive(recibir);
                System.out.println("Respuesta BCP: " + new String(recibir.getData(), 0, recibir.getLength()));
                return true;
            } catch (IOException e) { return false; }
        }
        return false;
    }

    // Método main para encender el servidor RMI
    public static void main(String[] args) {
    try {
        // TRUCO VITAL: Dile a Java cuál es la IP de ESTA laptop (Laptop 2)
        System.setProperty("java.rmi.server.hostname", "172.20.10.3"); 
        
        LocateRegistry.createRegistry(1099);
        ServidorJusticia servidor = new ServidorJusticia();
        // Usar localhost aquí está bien porque está registrándose a sí mismo
        java.rmi.Naming.rebind("rmi://localhost:1099/Justicia", servidor);
        System.out.println("Servidor Justicia encendido...");
    } catch (Exception e) {
        e.printStackTrace();
    }
}
}