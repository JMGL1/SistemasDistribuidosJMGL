/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bo.edu.usfx.jgroups.remate;

/**
 *
 * @author X13
 */
import java.io.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import org.jgroups.Address;
import org.jgroups.JChannel;
import org.jgroups.Message;
import org.jgroups.ObjectMessage;
import org.jgroups.Receiver;
import org.jgroups.View;
import org.jgroups.util.Util;

public class RemateUSFX implements Receiver {

    private JChannel canal;
    private String miNombre;
    private final ConcurrentHashMap<String, Subasta> subastas = new ConcurrentHashMap<>();
    private final Map<String, Timer> timersActivos = new HashMap<>();

    public void iniciar(String nombre) throws Exception {
        this.miNombre = nombre;
        canal = new JChannel(System.getProperty("config", "udp.xml"));
        canal.name(miNombre);
        canal.setReceiver(this);
        canal.connect("RemateSIS258");
        canal.getState(null, 10000); 
        leerTeclado();
        canal.close();
    }

    private void leerTeclado() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Comandos: /crear <art> <precio> <seg> | /subastas | /pujar <art> <monto> | /estado <art> | /quien | /salir");
        
        while (true) {
            try {
                String linea = sc.nextLine().trim();
                if (linea.equals("/salir")) break;

                String[] partes = linea.split(" ");
                String comando = partes[0].toLowerCase();

                switch (comando) {
                    case "/crear":
                        if (partes.length == 4) {
                            String art = partes[1].toLowerCase();
                            double precio = Double.parseDouble(partes[2]);
                            int seg = Integer.parseInt(partes[3]);
                            
                            if (subastas.containsKey(art)) {
                                System.out.println("Error: El articulo ya esta en subasta.");
                            } else {
                                Subasta s = new Subasta(art, precio, seg * 1000L);
                                MensajeRemate msg = new MensajeRemate(MensajeRemate.Tipo.NUEVA_SUBASTA);
                                msg.setSubasta(s);
                                canal.send(new ObjectMessage(null, msg)); 
                            }
                        }
                        break;
                        
                    case "/subastas":
                        subastas.values().stream().filter(Subasta::isActiva).forEach(System.out::println);
                        break;
                        
                    case "/pujar":
                        if (partes.length == 3) {
                            String art = partes[1].toLowerCase();
                            double monto = Double.parseDouble(partes[2]);
                            Address coordinador = canal.getView().getCoord();
                            MensajeRemate msg = new MensajeRemate(MensajeRemate.Tipo.PROPONER_PUJA);
                            msg.setArticulo(art);
                            msg.setMonto(monto);
                            msg.setRemitente(miNombre);
                            canal.send(new ObjectMessage(coordinador, msg));
                        }
                        break;
                        
                    case "/estado":
                        if (partes.length == 2) {
                            Subasta s = subastas.get(partes[1].toLowerCase());
                            if (s != null) {
                                s.getHistorialPujas().forEach(System.out::println);
                            }
                        }
                        break;
                        
                    case "/quien":
                        System.out.println("Conectados: " + canal.getView().getMembers());
                        System.out.println("Coordinador actual: " + canal.getView().getCoord());
                        break;
                }
            } catch (Exception e) {
                System.out.println("Error procesando comando.");
            }
        }
    }

    @Override
    public void receive(Message rawMsg) {

        MensajeRemate msg = (MensajeRemate) rawMsg.getObject();
        
        switch (msg.getTipo()) {
            case NUEVA_SUBASTA:
                Subasta s = msg.getSubasta();
                subastas.put(s.getArticulo(), s);
                System.out.println("\n[NUEVA SUBASTA] " + s.getArticulo() + " por " + s.getPrecioActual());
                programarCierreSiSoyCoordinador(s);
                break;
                
            case PROPONER_PUJA:
                Subasta sub = subastas.get(msg.getArticulo());
                if (sub != null && sub.isActiva()) {
                    if (msg.getMonto() > sub.getPrecioActual()) {
                        MensajeRemate aceptada = new MensajeRemate(MensajeRemate.Tipo.PUJA_ACEPTADA);
                        aceptada.setArticulo(msg.getArticulo());
                        aceptada.setMonto(msg.getMonto());
                        aceptada.setRemitente(msg.getRemitente());
                        try { canal.send(new ObjectMessage(null, aceptada)); } catch (Exception e){}
                    } else {
                        if(msg.getRemitente().equals(miNombre)) System.out.println("\n[!] Puja rechazada.");
                    }
                } else if(msg.getRemitente().equals(miNombre)){
                    System.out.println("\n[!] Subasta cerrada o no existe.");
                }
                break;
                
            case PUJA_ACEPTADA:
                Subasta acept = subastas.get(msg.getArticulo());
                if (acept != null) {
                    acept.setPrecioActual(msg.getMonto());
                    acept.setMejorPostor(msg.getRemitente());
                    acept.getHistorialPujas().add(msg.getRemitente() + " pujo " + msg.getMonto());
                    System.out.println("\n[$] " + msg.getRemitente() + " lidera " + msg.getArticulo() + " con " + msg.getMonto());
                }
                break;
                
            case CIERRE:
                Subasta subCierre = subastas.get(msg.getArticulo());
                if (subCierre != null && subCierre.isActiva()) {
                    subCierre.setActiva(false);
                    System.out.println("\n[X] CERRADA: " + subCierre.getArticulo() + " | Ganador: " + subCierre.getMejorPostor());
                }
                break;
        }
    }

    @Override
    public void viewAccepted(View view) {
        if (view.getMembers().get(0).equals(canal.getAddress())) {
            for (Subasta s : subastas.values()) {
                programarCierreSiSoyCoordinador(s);
            }
        }
    }

    private void programarCierreSiSoyCoordinador(Subasta s) {
        if (!canal.getView().getMembers().get(0).equals(canal.getAddress())) return;
        
        if (s.isActiva() && !timersActivos.containsKey(s.getArticulo())) {
            long tiempoRestante = s.getTiempoFinAbsoluto() - System.currentTimeMillis();
            if (tiempoRestante <= 0) {
                enviarCierreMulticast(s.getArticulo());
            } else {
                Timer timer = new Timer();
                timersActivos.put(s.getArticulo(), timer);
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        enviarCierreMulticast(s.getArticulo());
                    }
                }, tiempoRestante);
            }
        }
    }

    private void enviarCierreMulticast(String articulo) {
        try {
            timersActivos.remove(articulo);
            MensajeRemate msgCierre = new MensajeRemate(MensajeRemate.Tipo.CIERRE);
            msgCierre.setArticulo(articulo);
            canal.send(new ObjectMessage(null, msgCierre));
        } catch (Exception e) {}
    }

    @Override
    public void getState(OutputStream output) throws Exception {
        Util.objectToStream(subastas, new DataOutputStream(output));
    }

    @Override
    @SuppressWarnings("unchecked")
    public void setState(InputStream input) throws Exception {
        ConcurrentHashMap<String, Subasta> estadoRecibido = 
            (ConcurrentHashMap<String, Subasta>) Util.objectFromStream(new DataInputStream(input));
        subastas.clear();
        subastas.putAll(estadoRecibido);
    }

    public static void main(String[] args) throws Exception {
        String nombre = args.length > 0 ? args[0] : "postor-" + (int)(Math.random()*100);
        new RemateUSFX().iniciar(nombre);
    }
}