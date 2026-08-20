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
import java.net.SocketTimeoutException;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;


public class Nodo1 {

    private static final int PUERTO_NODO1 = 7001;

    private static final String HOST_NODO2 = "172.20.10.13";
    private static final int PUERTO_NODO2 = 7002;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        try (DatagramSocket socket = new DatagramSocket(PUERTO_NODO1)) {
            
            socket.setSoTimeout(10000);

            System.out.print("Ingrese una palabra o frase: ");
            String texto = sc.nextLine();
            int cantidadCaracteres = texto.length();

            String mensajeSalida = texto + "|" + cantidadCaracteres;
            enviarMensaje(socket, mensajeSalida, HOST_NODO2, PUERTO_NODO2);
            System.out.println("Enviado al Nodo 2 -> " + mensajeSalida);

            System.out.println("Esperando resultado final del Nodo 3...");
            byte[] buffer = new byte[4096];
            DatagramPacket paqueteRespuesta = new DatagramPacket(buffer, buffer.length);
            socket.receive(paqueteRespuesta);

            String resultadoFinal = new String(
                    paqueteRespuesta.getData(), 0, paqueteRespuesta.getLength(), StandardCharsets.UTF_8);

            System.out.println();
            System.out.println(resultadoFinal);

        } catch (SocketTimeoutException e) {
            System.out.println("No llego respuesta a tiempo (posible paquete UDP perdido "
                    + "o el Nodo 2/Nodo 3 no esta corriendo). Verifica y vuelve a intentar.");
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void enviarMensaje(DatagramSocket socket, String mensaje, String host, int puerto)
            throws IOException {
        byte[] datos = mensaje.getBytes(StandardCharsets.UTF_8);
        InetAddress direccion = InetAddress.getByName(host);
        DatagramPacket paquete = new DatagramPacket(datos, datos.length, direccion, puerto);
        socket.send(paquete);
    }
}

