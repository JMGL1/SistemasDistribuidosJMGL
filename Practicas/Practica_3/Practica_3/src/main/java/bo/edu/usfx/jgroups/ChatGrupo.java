/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bo.edu.usfx.jgroups;

/**
 *
 * @author X13
 */
import java.io.BufferedReader;
import java.io.InputStreamReader;
import org.jgroups.JChannel;
import org.jgroups.Message;
import org.jgroups.ObjectMessage;
import org.jgroups.Receiver;
import org.jgroups.View;

public class ChatGrupo implements Receiver {

    private JChannel canal;
    private final String nombre;

    public ChatGrupo(String nombre) {
        this.nombre = nombre;
    }

    // ---- Callbacks: los invoca un hilo de JGroups, NO el hilo principal ----
    @Override
    public void viewAccepted(View vista) {
        System.out.println("** Miembros (" + vista.size() + "): "
                + vista.getMembers());
    }

    @Override
    public void receive(Message msg) {
        System.out.println(msg.getSrc() + "> " + msg.getObject());
    }

    // ---- Ciclo de vida ----
    public void iniciar() throws Exception {
        canal = new JChannel();
        canal.name(nombre);
        canal.setReceiver(this);
        canal.connect("ChatSIS258");
        leerTeclado();
        canal.close();
    }

    private void leerTeclado() throws Exception {
        BufferedReader teclado = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Escriba mensajes. /salir para terminar.");
        String linea;
        while ((linea = teclado.readLine()) != null) {
            if (linea.equals("/salir")) {
                break;
            }
            canal.send(new ObjectMessage(null, linea)); // null = todo el grupo
        }
    }

    public static void main(String[] args) throws Exception {
        // Al ejecutar con "Run File" en NetBeans usará "anonimo" 
        // a menos que cambies los argumentos del proyecto
        String nombre = args.length > 0 ? args[0] : "anonimo-" + (int)(Math.random()*100);
        new ChatGrupo(nombre).iniciar();
    }
}
