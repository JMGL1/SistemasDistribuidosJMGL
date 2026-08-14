/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bo.edu.usfx.sockets;

/**
 *
 * @author X13
 */

import java.io.*;
import java.net.Socket;

public class ManejadorUsuario implements Runnable {
    private final Socket socket;
    private PrintWriter out;
    private String apodo;
    private Sala salaActual;

    public ManejadorUsuario(Socket socket) {
        this.socket = socket;
    }

    public String getApodo() { return apodo; }

    // JUSTIFICACIÓN CONCURRENCIA: Usamos 'synchronized' para evitar que dos hilos 
    // escriban al mismo PrintWriter a la vez (ej: si dos personas le envían un /privado 
    // exactamente al mismo milisegundo, los textos no se mezclarán).
    public synchronized void enviarMensaje(String mensaje) {
        if (out != null) {
            out.println(mensaje);
        }
    }

    @Override
    public void run() {
        // Requisito 6: try-with-resources garantiza el cierre de streams.
        try (
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true)
        ) {
            this.out = writer;
            this.apodo = "User" + ServidorChat.contadorHistorico.get();
            ServidorChat.usuariosConectados.put(this.apodo, this);
            
            unirseASala("general");
            enviarMensaje("Bienvenido! Tu apodo es " + this.apodo + ". Usa /nick <nuevo> para cambiarlo.");

            String linea;
            // El ciclo se rompe si el cliente se desconecta abruptamente (Ctrl+C)
            while ((linea = in.readLine()) != null) {
                if (linea.startsWith("/")) {
                    procesarComando(linea);
                } else {
                    salaActual.difundirMensaje("[" + this.apodo + "]: " + linea, this);
                }
            }
        } catch (IOException e) {
            // Requisito 5: Desconexión abrupta manejada sin tumbar el servidor
            System.err.println("Desconexión abrupta de " + apodo);
        } finally {
            limpiarDesconexion();
        }
    }

    private void procesarComando(String linea) {
        String[] partes = linea.split(" ", 3); // Divide máximo en 3 partes
        String comando = partes[0].toLowerCase();

        switch (comando) {
            case "/nick":
                if (partes.length < 2) { enviarMensaje("Uso: /nick <apodo>"); break; }
                cambiarNick(partes[1]);
                break;
            case "/salas":
                enviarMensaje("Salas disponibles:");
                for (Sala s : ServidorChat.salas.values()) {
                    enviarMensaje("- " + s.getNombre() + " (" + s.getCantidadUsuarios() + " usuarios)");
                }
                break;
            case "/crear":
                if (partes.length < 2) { enviarMensaje("Uso: /crear <sala>"); break; }
                crearSala(partes[1]);
                break;
            case "/unirse":
                if (partes.length < 2) { enviarMensaje("Uso: /unirse <sala>"); break; }
                unirseASala(partes[1]);
                break;
            case "/quien":
                enviarMensaje("Usuarios en " + salaActual.getNombre() + ":");
                for (ManejadorUsuario m : salaActual.getMiembros()) {
                    enviarMensaje("- " + m.getApodo());
                }
                break;
            case "/privado":
                if (partes.length < 3) { enviarMensaje("Uso: /privado <apodo> <texto>"); break; }
                enviarPrivado(partes[1], partes[2]);
                break;
            case "/estado":
                enviarMensaje("--- ESTADO DEL SERVIDOR ---");
                enviarMensaje("Conectados ahora: " + ServidorChat.usuariosConectados.size());
                enviarMensaje("Conexiones históricas: " + ServidorChat.contadorHistorico.get());
                enviarMensaje("Total de salas: " + ServidorChat.salas.size());
                break;
            case "/historial":
                salaActual.enviarHistorial(this);
                break;
            case "/salir":
                try { socket.close(); } catch (IOException ignored) {}
                break;
            default:
                enviarMensaje("Comando desconocido.");
        }
    }

    private void cambiarNick(String nuevoNick) {
        if (ServidorChat.usuariosConectados.containsKey(nuevoNick)) {
            enviarMensaje("Error: El apodo ya está en uso.");
            return;
        }
        ServidorChat.usuariosConectados.remove(this.apodo);
        this.apodo = nuevoNick;
        ServidorChat.usuariosConectados.put(this.apodo, this);
        enviarMensaje("Apodo cambiado a " + nuevoNick);
    }

    private void crearSala(String nombreSala) {
        if (ServidorChat.salas.containsKey(nombreSala)) {
            enviarMensaje("Error: La sala ya existe.");
        } else {
            ServidorChat.salas.put(nombreSala, new Sala(nombreSala));
            enviarMensaje("Sala " + nombreSala + " creada.");
            unirseASala(nombreSala);
        }
    }

    private void unirseASala(String nombreSala) {
        Sala nuevaSala = ServidorChat.salas.get(nombreSala);
        if (nuevaSala == null) {
            enviarMensaje("Error: La sala no existe.");
            return;
        }
        if (salaActual != null) {
            salaActual.removerUsuario(this);
            salaActual.difundirMensaje("El usuario " + this.apodo + " ha salido de la sala.", this);
        }
        salaActual = nuevaSala;
        salaActual.agregarUsuario(this);
        salaActual.difundirMensaje("El usuario " + this.apodo + " se ha unido a la sala.", this);
        enviarMensaje("Te has unido a la sala: " + nombreSala);
    }

    private void enviarPrivado(String destinatario, String mensaje) {
        ManejadorUsuario dest = ServidorChat.usuariosConectados.get(destinatario);
        if (dest != null) {
            dest.enviarMensaje("[Privado de " + this.apodo + "]: " + mensaje);
            enviarMensaje("[Privado para " + destinatario + "]: " + mensaje);
        } else {
            enviarMensaje("Error: Usuario no encontrado.");
        }
    }

    private void limpiarDesconexion() {
        ServidorChat.usuariosConectados.remove(this.apodo);
        if (salaActual != null) {
            salaActual.removerUsuario(this);
            salaActual.difundirMensaje("El usuario " + this.apodo + " se ha desconectado.", this);
        }
    }
}
