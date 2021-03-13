package servidorsockets;

import java.io.IOException;
import java.net.DatagramPacket;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;

public class ServerUDP {

    private int contador = 1;
    public String chat = "";
    public final int TAM_BUF = 1024; 
    private boolean continuar = true;
    private DatagramSocket socket;

    public static void main(String[] args) {
        ServerUDP se1 = new ServerUDP();
        try {
            se1.init(5555);
            se1.runServer();
        } catch (SocketException e) {
            System.out.println("Problemas creando Socket.");
        } catch (IOException e) {
            System.out.println("Problemas de comunicaciones.");
        } finally {
            se1.close();
        }
    }

    public void init(int port) throws SocketException {
        socket = new DatagramSocket(port);
    }

    public void runServer() throws IOException {
        byte[] receivingData = new byte[TAM_BUF];
        byte[] sendingData;
        InetAddress clientIP;
        int clientPort;

        //el servidor atén el port indefinidament
        while (continuar) {
            //creació del paquet per rebre les dades
            DatagramPacket packet = new DatagramPacket(receivingData,
                    receivingData.length);
            //espera de les dades
            socket.receive(packet);

            //processament de les dades rebudes i obtenció de la resposta
            sendingData = processData(packet.getData(), packet.getLength());

            //obtenció de l’adreça del client
            clientIP = packet.getAddress();
            //obtenció del port del client
            clientPort = packet.getPort();
            //creació del paquet per enviar la resposta
            packet = new DatagramPacket(sendingData, sendingData.length,
                    clientIP, clientPort);
            //enviament de la resposta

            socket.send(packet);
            //Si finaliza server devuelve true
            continuar = isFiServer();
        }
    }

    public void close() {
        if (socket != null && !socket.isClosed()) {
            socket.close();
        }
    }

    private byte[] processData(byte[] data, int length) {
        // byte[] to string
        String strData = (new String(data, StandardCharsets.UTF_8)).substring(0, length);

        if (!strData.equals("refresh")) {
            String[] strSplittedData = strData.split("&");
            String nombre = contador + "-" + strSplittedData[0] + ": ";
            String mensaje = strSplittedData[1];
            String strSalida = nombre + mensaje + "\n";
            contador++;

            chat += strSalida;
        }
        byte[] mSalida = (chat.getBytes());
        return mSalida;
    }

    private boolean isFiServer() {
        return true;
    }
}
