package bibliotecapsp;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

public class Servidor implements Runnable {

    public static final int PUERTO = 5000;
    private Socket skCliente;
    private DataInputStream in;
    private DataOutputStream out;
    private Biblioteca biblioteca;
    private String idCliente;
    private String registro;

    public Servidor() {
    }

    public Servidor(Socket skCliente, Biblioteca biblioteca) {
        this.skCliente = skCliente;
        this.biblioteca = biblioteca;
    }

    private void conectarServidor() {
        String mensaje;
        try {
            in = new DataInputStream(skCliente.getInputStream());
            out = new DataOutputStream(skCliente.getOutputStream());

            mensaje = "Nombre de usuario: ";
            out.writeUTF(mensaje);

            idCliente = in.readUTF();
            System.out.println("Cliente " + idCliente + " conectado");
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    @Override
    public void run() {
        conectarServidor();

        try {
            String opt = "", mensaje;

            while (!opt.equals("4")) {
                opt = in.readUTF(); //Escuchamos la opcion seleccionada del cliente
                System.out.println("Cliente " + idCliente + " opción escuchada: " + opt);

                switch (opt) {
                    case "1": //AÑADIR CADENA AL REGISTRO: ID_USER: STRING
                        registro = in.readUTF();
                        biblioteca.escribirRegistro(idCliente, registro);
                        break;

                    case "2": //MOSTRAR CONTENIDO DEL REGISTRO, PROCESO
                        registro = biblioteca.leerRegistro();
                        out.writeUTF(registro);
                        break;

                    case "3": //GENERAR REGISTRO
                        biblioteca.imprimirRegistro(idCliente);
                        mensaje = "Registro imprimido";
                        out.writeUTF(mensaje);
                        break;

                    case "4": //SALIR DEL PROGRAMA
                        System.out.println("Cliente " + idCliente + " desconectado");

                        mensaje = "Saliendo del servidor";
                        out.writeUTF(mensaje);
                        break;

                    default:
                        mensaje = "Opcion no valida";
                        out.writeUTF(mensaje);
                }
            }
        } catch (SocketException ex) {
            System.out.println(ex);
        } catch (IOException ex) {
            System.out.println(ex);
        }
    }

    public static void main(String[] args) {
        try {
            ServerSocket server = new ServerSocket(Servidor.PUERTO);
            System.out.println("SERVIDOR ALEJANDRÍA ABIERTO...");
            Biblioteca b = new Biblioteca();

            while (true) {
                Socket socket = server.accept();//Se conecta el cliente
                
                Servidor servidor = new Servidor(socket, b);
                Thread hilo = new Thread(servidor);
                hilo.start();
            }
        } catch (SocketException ex) {
            System.out.println("Error socket: " + ex.getLocalizedMessage());
            System.out.println(ex.getCause());

        } catch (IOException ex) {
            System.out.println(ex);
        }
    }
}
