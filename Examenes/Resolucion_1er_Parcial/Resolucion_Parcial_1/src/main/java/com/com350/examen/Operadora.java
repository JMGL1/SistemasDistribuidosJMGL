/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.com350.examen;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

/**
 *
 * @author Dell
 */
public class Operadora extends UnicastRemoteObject
        implements IOperadora {

    public Operadora() throws RemoteException {
        super();
    }

    private int contadorVentas = 1;

    @Override
    public Voucher ComprarTour(String pasaporte, String codigoTour, int personas) {

        // Llamar a migracion (enviando solo el número de pasaporte)
        String migracion = consultarMgiracion(pasaporte);
        double precio = 0f;
        
        // Respuesta de migración viene como "valido:PAIS" o "invalido"
        String[] respuesta = migracion.split(":");
        
        // Verificamos si la posición 0 es "valido"
        if (respuesta[0].equalsIgnoreCase("valido")) {
            
            // Verificamos si la posición 1 existe y es "BOLIVIA" para el descuento
            if (respuesta.length > 1 && respuesta[1].equalsIgnoreCase("BOLIVIA")) {
                precio = personas * 180 * 0.5f;
            } else {
                precio = personas * 180;
            }
            
            // Llamar a Banco
            try {
                Registry reg = LocateRegistry.getRegistry(1099);
                IBanco banco = (IBanco) reg.lookup("Banco");
                
                Pago respuestaPago = banco.Debitar(pasaporte, precio);
                
                if (respuestaPago.isAprobado()) {
                    // Generación del código de compra autoincremental
                    String codigoCompra = String.format("C-%04d", contadorVentas++);
                    return new Voucher(true, codigoCompra, "");
                } else {
                    // El rechazo viene del banco (Riesgo alto o Saldo insuficiente)
                    return new Voucher(false, "", respuestaPago.getMotivo());
                }
            } catch (RemoteException | NotBoundException ex) {
                System.getLogger(Operadora.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
            }
        }
        
        // Si no es válido por migración, se rechaza por este motivo exacto
        return new Voucher(false, "", "Pasaporte inválido");
    }

    public String consultarMgiracion(String pasaporte) {
        try {
            int port = 5002;
            Socket client = new Socket("localhost", port);
            PrintStream toServer = new PrintStream(client.getOutputStream());
            BufferedReader fromServer = new BufferedReader(
                    new InputStreamReader(client.getInputStream()));
            
            // Aquí ya se arma la cadena "pasaporte:numero"
            toServer.println("pasaporte:" + pasaporte);
            
            String result = fromServer.readLine();
            client.close(); // Cerramos el socket
            return result;

        } catch (IOException ex) {
            System.out.print(ex.getMessage());
        }
        return "invalido";
    }

}
