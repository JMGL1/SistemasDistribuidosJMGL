/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bo.edu.usfx.sockets;

/**
 *
 * @author X13
 */

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class ServidorChat {
    
    // JUSTIFICACIÓN CONCURRENCIA: ConcurrentHashMap permite que varios hilos 
    // busquen o agreguen salas/usuarios al mismo tiempo sin bloquear todo el mapa.
    public static final ConcurrentMap<String, Sala> salas = new ConcurrentHashMap<>();
    public static final ConcurrentMap<String, ManejadorUsuario> usuariosConectados = new ConcurrentHashMap<>();
    
    // JUSTIFICACIÓN CONCURRENCIA: AtomicInteger garantiza que el incremento sea una 
    // operación atómica a nivel de hardware. Si 10 hilos intentan sumar 1 al mismo 
    // tiempo, ninguno se perderá (evita condiciones de carrera).
    public static final AtomicInteger contadorHistorico = new AtomicInteger(0);

    public static void main(String[] args) {
        // Requisito 2: Tamaño del pool configurable por argumentos (por defecto 10)
        int puerto = args.length > 0 ? Integer.parseInt(args[0]) : 5000;
        int tamanoPool = args.length > 1 ? Integer.parseInt(args[1]) : 10;

        salas.put("general", new Sala("general"));

        // Requisito 6: try-with-resources para cerrar el ServerSocket siempre
        try (ServerSocket servidor = new ServerSocket(puerto)) {
            ExecutorService pool = Executors.newFixedThreadPool(tamanoPool);
            System.out.println("Servidor de Chat iniciado en puerto " + puerto + " con pool de " + tamanoPool);

            // Requisito 1: Bucle principal SOLO acepta y delega.
            while (true) {
                Socket cliente = servidor.accept();
                contadorHistorico.incrementAndGet();
                pool.execute(new ManejadorUsuario(cliente));
            }
        } catch (IOException e) {
            System.err.println("Error en el servidor: " + e.getMessage());
        }
    }
}
