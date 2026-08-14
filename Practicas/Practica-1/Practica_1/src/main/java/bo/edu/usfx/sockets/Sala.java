/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bo.edu.usfx.sockets;

/**
 *
 * @author X13
 */
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CopyOnWriteArraySet;

public class Sala {
    private final String nombre;
    
    // JUSTIFICACIÓN CONCURRENCIA: CopyOnWriteArraySet es ideal porque en un chat
    // recorremos la lista de miembros constantemente para difundir mensajes.
    // Si alguien entra/sale mientras iteramos, esta colección evita el ConcurrentModificationException
    // creando una copia temporal en memoria.
    private final Set<ManejadorUsuario> miembros = new CopyOnWriteArraySet<>();
    
    // JUSTIFICACIÓN CONCURRENCIA: Cola segura para hilos. Evita que dos mensajes 
    // lleguen al mismo milisegundo y corrompan el historial.
    private final Queue<String> historial = new ConcurrentLinkedQueue<>();

    public Sala(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() { return nombre; }
    public int getCantidadUsuarios() { return miembros.size(); }
    public Set<ManejadorUsuario> getMiembros() { return miembros; }

    public void agregarUsuario(ManejadorUsuario usuario) {
        miembros.add(usuario);
    }

    public void removerUsuario(ManejadorUsuario usuario) {
        miembros.remove(usuario);
    }

    public void difundirMensaje(String mensaje, ManejadorUsuario remitente) {
        // Guardar en el historial (Bonus)
        if (historial.size() >= 10) {
            historial.poll(); // Borra el más antiguo
        }
        historial.offer(mensaje); // Agrega el nuevo

        // Enviar a todos excepto al que lo envió
        for (ManejadorUsuario miembro : miembros) {
            if (miembro != remitente) {
                miembro.enviarMensaje(mensaje);
            }
        }
    }

    public void enviarHistorial(ManejadorUsuario usuario) {
        usuario.enviarMensaje("--- Historial de la sala [" + nombre + "] ---");
        for (String msj : historial) {
            usuario.enviarMensaje(msj);
        }
        usuario.enviarMensaje("-----------------------------------");
    }
}