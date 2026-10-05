package bo.edu.usfx.jgroups.aforo;

import java.io.*;
import java.util.Scanner;
import org.jgroups.Address;
import org.jgroups.JChannel;
import org.jgroups.Message;
import org.jgroups.ObjectMessage;
import org.jgroups.Receiver;
import org.jgroups.View;
import org.jgroups.util.Util;
import bo.edu.usfx.jgroups.aforo.MensajeAforo.Tipo;

public class NodoPuerta implements Receiver {
    private JChannel canal;
    private final String nombre;
    private final int aforoMaximo;
    
    // Estado Replicado
    private int ocupacion = 0;
    
    // Para ver cambios de membresía
    private View vistaAnterior;

    public NodoPuerta(String nombre, int aforoMaximo) {
        this.nombre = nombre;
        this.aforoMaximo = aforoMaximo;
    }

    @Override
    public void viewAccepted(View vista) {
        if (vistaAnterior != null) {
            Address[][] cambios = View.diff(vistaAnterior, vista);
            for (Address a : cambios[0]) System.out.println("** PUERTA ENTRO: " + a);
            for (Address a : cambios[1]) System.out.println("** PUERTA SALIO: " + a);
        }
        vistaAnterior = vista;
        System.out.println("** Vista " + vista.getViewId().getId()
                + " | Coordinador actual: " + vista.getCoord());
    }

    @Override
    public void receive(Message msg) {
        Object obj = msg.getObject();
        if (obj instanceof MensajeAforo) {
            MensajeAforo m = (MensajeAforo) obj;
            
            switch (m.getTipo()) {
                case SOLICITUD:
                    // Sólo procesado por el coordinador
                    if (canal.getAddress().equals(vistaAnterior.getCoord())) {
                        synchronized (this) {
                            if (ocupacion + m.getPersonas() <= aforoMaximo) {
                                // Aceptado: difundir a todos (multicast)
                                MensajeAforo aceptado = new MensajeAforo(Tipo.ACEPTADO, m.getPuerta(), m.getPersonas());
                                try {
                                    canal.send(new ObjectMessage(null, aceptado));
                                } catch (Exception e) { e.printStackTrace(); }
                            } else {
                                // Rechazado: enviar unicast de vuelta al solicitante
                                MensajeAforo rechazado = new MensajeAforo(Tipo.RECHAZADO, m.getPuerta(), m.getPersonas());
                                try {
                                    canal.send(new ObjectMessage(msg.getSrc(), rechazado));
                                } catch (Exception e) { e.printStackTrace(); }
                            }
                        }
                    }
                    break;
                    
                case ACEPTADO:
                    synchronized (this) {
                        ocupacion += m.getPersonas();
                    }
                    System.out.println("-> [ACEPTADO] " + m.getPersonas() + " personas entraron por " + m.getPuerta());
                    verificarAforoCompleto();
                    break;
                    
                case RECHAZADO:
                    System.out.println("-> [RECHAZADO] Tu solicitud para " + m.getPersonas() + " personas ha sido denegada (Supera el aforo máximo de " + aforoMaximo + ").");
                    break;
                    
                case SALIDA:
                    synchronized (this) {
                        ocupacion -= m.getPersonas();
                        if (ocupacion < 0) ocupacion = 0; // Prevenir negativos por seguridad
                    }
                    System.out.println("-> [SALIDA] " + m.getPersonas() + " personas salieron por " + m.getPuerta());
                    break;
            }
        }
    }

    @Override
    public void getState(OutputStream salida) throws Exception {
        // Envia el estado actual al nuevo miembro
        synchronized (this) {
            Util.objectToStream(ocupacion, new DataOutputStream(salida));
        }
        System.out.println("** Se envió el estado a un nuevo nodo (Ocupación: " + ocupacion + ")");
    }

    @Override
    public void setState(InputStream entrada) throws Exception {
        // Recibe el estado al unirse
        Integer ocupacionRecibida = Util.objectFromStream(new DataInputStream(entrada));
        synchronized (this) {
            this.ocupacion = ocupacionRecibida;
        }
        System.out.println("** Estado recibido correctamente. Ocupación actual sincronizada a: " + ocupacion);
    }

    public void iniciar() throws Exception {
        canal = new JChannel(System.getProperty("config", "udp.xml"));
        canal.name(nombre);
        canal.setReceiver(this);
        canal.connect("AforoSIS258"); // Nombre del grupo especificado
        canal.getState(null, 10000);  // Sincronizar estado (ocupación)
        
        leerComandos();
        
        canal.close();
    }

    private void leerComandos() throws Exception {
        Scanner teclado = new Scanner(System.in);
        System.out.println("=== SISTEMA DE AFORO ===");
        System.out.println("Comandos: /entrar n | /salir n | /estado | /desconectar");
        
        while (teclado.hasNextLine()) {
            String linea = teclado.nextLine().trim();
            if (linea.equals("/desconectar")) break;
            
            try {
                if (linea.startsWith("/entrar ")) {
                    int n = Integer.parseInt(linea.substring(8).trim());
                    MensajeAforo msg = new MensajeAforo(Tipo.SOLICITUD, nombre, n);
                    // Unicast al coordinador
                    canal.send(new ObjectMessage(vistaAnterior.getCoord(), msg));
                    
                } else if (linea.startsWith("/salir ")) {
                    int n = Integer.parseInt(linea.substring(7).trim());
                    MensajeAforo msg = new MensajeAforo(Tipo.SALIDA, nombre, n);
                    // Multicast a todos
                    canal.send(new ObjectMessage(null, msg));
                    
                } else if (linea.equals("/estado")) {
                    synchronized (this) {
                        System.out.println("-> ESTADO: Ocupación: " + ocupacion + "/" + aforoMaximo);
                        verificarAforoCompleto();
                    }
                    
                } else {
                    System.out.println("Comando desconocido. Use: /entrar n, /salir n, /estado");
                }
            } catch (NumberFormatException e) {
                System.out.println("Error: Debes ingresar un número válido (ej. /entrar 5)");
            } catch (Exception e) {
                System.out.println("Error enviando mensaje: " + e.getMessage());
            }
        }
    }
    
    private void verificarAforoCompleto() {
        synchronized (this) {
            if (ocupacion >= aforoMaximo) {
                System.out.println("*** AFORO COMPLETO ***");
            }
        }
    }

    public static void main(String[] args) throws Exception {
        if (args.length < 2) {
            System.out.println("Uso: java bo.edu.usfx.jgroups.aforo.NodoPuerta <nombre> <aforoMaximo>");
            return;
        }
        
        String nombre = args[0];
        int aforoMaximo = Integer.parseInt(args[1]);
        
        System.out.println("Iniciando Puerta: " + nombre + " con aforo máximo: " + aforoMaximo);
        new NodoPuerta(nombre, aforoMaximo).iniciar();
    }
}
