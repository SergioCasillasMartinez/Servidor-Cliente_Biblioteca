package bibliotecapsp;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.Semaphore;

public class Biblioteca {

    private static ArrayList<String> listaRegistro = new ArrayList<>();
    private static Semaphore semaforo = new Semaphore(5);

    public Biblioteca() {
    }

    public void imprimirRegistro(String idCliente) {
        String contenido = "Usuario " + idCliente + " crea registro:" + "\n" + leerRegistro();
        try {
            if (System.getProperty("os.name").contains("Linux")) {
                String[] cmd = {"/bin/bash", "-c", "echo '" + contenido + "' > registro.txt | gedit registro.txt"};
                Runtime.getRuntime().exec(cmd);
            }else if (System.getProperty("os.name").contains("Windows")){
                String[] cmd = {"cmd.exe", "/c", "echo '" + contenido + "' > registro.txt"};
                Runtime.getRuntime().exec(cmd);
            }
        } catch (IOException ex) {
            System.out.println(ex);
        }
    }

    //Leemos el fichero donde estan los registros
    public String leerRegistro() {
        String contenido = "";
        try {
            semaforo.acquire(1);
            for (String string : listaRegistro) {
                contenido += string;
            }
            semaforo.release(1);

        } catch (InterruptedException ex) {
            System.out.println(ex);
        }

        return contenido;
    }

    //Escribimos en el fichero que contiene los registros
    public void escribirRegistro(String idCliente, String mensaje) {
        try {
            semaforo.acquire(5);
            listaRegistro.add('[' + idCliente + "]> " + mensaje + "\n");
            semaforo.release(5);
        } catch (InterruptedException ex) {
            System.out.println(ex);
        }
    }

    public ArrayList<String> getListaRegistro() {
        return listaRegistro;
    }

    public void setListaRegistro(ArrayList<String> listaRegistro) {
        this.listaRegistro = listaRegistro;
    }

}
