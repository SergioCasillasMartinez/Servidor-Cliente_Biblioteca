package bibliotecapsp;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class Cliente implements Runnable {

    private DataInputStream in;
    private DataOutputStream out;
    private Socket skCliente;
    private String contenidoRegistro;
    private Scanner scan = new Scanner(System.in);

    public Cliente() {

    }

    public Cliente(Socket skCliente) {
        this.skCliente = skCliente;
    }

    private void conectarCliente() {
        try {
            in = new DataInputStream(skCliente.getInputStream());
            out = new DataOutputStream(skCliente.getOutputStream());

            //Server: Escribe nombre
            String pregunta = in.readUTF(); //Recoge el mensaje enviado por el servidor
            System.out.print(pregunta); //Imprime dicho mensaje

            //Cliente:Manda respuesta
            String idCliente = scan.nextLine(); //Escribe la respuesta
            out.writeUTF(idCliente); //Manda dicha respuesta
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    @Override
    public void run() {
        conectarCliente();
        
        try {
            String opt = "", mensaje;

            while (!opt.equals("4")) {
                System.out.println("REGISTROS ALEJANDRÍA ONLINE: "
                        + "\n1- Escribir en el registro"
                        + "\n2- Leer el registro"
                        + "\n3- Imprimir registro"
                        + "\n4- Salir del programa");

                System.out.print("Opción > ");
                opt = scan.nextLine();
                out.writeUTF(opt);

                switch (opt) {
                    case "1": //MANDAR MENSAJE A GUARDAR
                        System.out.print("Anadir contenido en el fichero:\n>");
                        contenidoRegistro = scan.nextLine();
                        out.writeUTF(contenidoRegistro); //Mandamos el mensaje
                        //System.out.println("Mensaje guardado");
                        break;

                    case "2": //MOSTRAR CONTENIDO DEL REGISTRO, PROCESO
                        System.out.println("CONTENIDO DEL REGISTRO");
                        contenidoRegistro = in.readUTF();
                        System.out.println(contenidoRegistro);
                        break;

                    case "3": //GENERAR REGISTRO
                        mensaje = in.readUTF();
                        System.out.println(mensaje);
                        break;

                    case "4": //SALIR DEL PROGRAMA
                        mensaje = in.readUTF();
                        System.out.println(mensaje);
                        break;

                    default:
                        mensaje = in.readUTF();
                        System.out.println(mensaje);
                }
            }
        } catch (IOException ex) {
            System.out.println(ex);
        }
    }

    public static void main(String[] args) {
        try {
            Socket socket = new Socket("localhost", Servidor.PUERTO); //IP y Puerto

            Cliente cliente = new Cliente(socket);
            Thread hilo = new Thread(cliente);
            hilo.start();

        } catch (IOException ex) {
            System.out.println(ex);
        }
    }

}
