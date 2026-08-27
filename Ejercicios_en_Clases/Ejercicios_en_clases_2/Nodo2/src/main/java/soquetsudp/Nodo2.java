/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package soquetsudp;

/**
 *
 * @author X13
 */

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;


public class Nodo2 {

    private static final int PUERTO_NODO2 = 7002;

    private static final String HOST_NODO3 = "172.20.10.12";
    private static final int PUERTO_NODO3 = 7003;

    public static void main(String[] args) {
        try (DatagramSocket socket = new DatagramSocket(PUERTO_NODO2)) {
            System.out.println("Nodo 2 escuchando en el puerto " + PUERTO_NODO2 + "...");

            byte[] buffer = new byte[4096];

            while (true) {
                DatagramPacket paqueteRecibido = new DatagramPacket(buffer, buffer.length);
                socket.receive(paqueteRecibido);

                try {
                    String mensaje = new String(
                            paqueteRecibido.getData(), 0, paqueteRecibido.getLength(), StandardCharsets.UTF_8);
                    System.out.println("Recibido del Nodo 1 -> " + mensaje);

                    String[] partes = mensaje.split("\\|", -1);
                    String texto = partes[0];
                    String cantidadCaracteres = partes[1];

                    int cantidadPalabras = contarPalabras(texto);
                    boolean esPar = Integer.parseInt(cantidadCaracteres) % 2 == 0;
                    String paridad = esPar ? "par" : "impar";

                    String mensajeSalida = texto + "|" + cantidadCaracteres + "|" + cantidadPalabras + "|" + paridad;

                    byte[] datos = mensajeSalida.getBytes(StandardCharsets.UTF_8);
                    InetAddress direccionNodo3 = InetAddress.getByName(HOST_NODO3);
                    DatagramPacket paqueteSalida = new DatagramPacket(datos, datos.length, direccionNodo3, PUERTO_NODO3);
                    socket.send(paqueteSalida);

                    System.out.println("Enviado al Nodo 3 -> " + mensajeSalida);
                } catch (RuntimeException e) {
                    
                    System.out.println("Mensaje descartado, no se pudo procesar: " + e.getMessage());
                }
            }

        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static int contarPalabras(String texto) {
        String textoLimpio = texto.trim();
        if (textoLimpio.isEmpty()) {
            return 0;
        }
        return textoLimpio.split("\\s+").length;
    }
}

