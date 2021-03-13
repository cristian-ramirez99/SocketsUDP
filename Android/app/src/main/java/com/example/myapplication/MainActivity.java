package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class MainActivity extends AppCompatActivity {

    private EditText editTextNombre;
    private EditText editTextMensaje;
    private Button btnEnviar;
    private Button btnRefresh;
    private TextView textViewChat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextNombre = (EditText) findViewById(R.id.txtNombre);
        editTextMensaje = (EditText) findViewById(R.id.txtMensaje);
        btnEnviar = (Button) findViewById(R.id.btnEnviar);
        btnRefresh = (Button) findViewById(R.id.btnRefresh);
        textViewChat = (TextView) findViewById(R.id.tvChat);

        btnEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enviar(v);
            }
        });

        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refrescar(v);
            }
        });

    }


    public void enviar(View view) {

        String s1 = editTextNombre.getText().toString();
        String s2 = editTextMensaje.getText().toString();

        if (!s2.isEmpty() && !s1.isEmpty()) {
            String msg = s1 + "&" + s2;
            client(msg);
        }

    }

    public void refrescar(View view) {
        client("refresh");
    }

    public void client(String msg) {
        Toast toast1;
        try {
            DatagramSocket socketUDP = new DatagramSocket();
            byte[] mensaje = msg.getBytes();
            InetAddress hostServidor = InetAddress.getByName("192.168.113.26");
            int puertoServidor = 5555;

            // Construimos un datagrama para enviar el mensaje al servidor
            DatagramPacket peticion =
                    new DatagramPacket(mensaje, mensaje.length, hostServidor,
                            puertoServidor);


            // Enviamos el datagrama
            socketUDP.send(peticion);

            // Construimos el DatagramPacket que contendrá la respuesta
            byte[] bufer = new byte[1000];
            DatagramPacket respuesta =
                    new DatagramPacket(bufer, bufer.length);
            socketUDP.receive(respuesta);

            // Enviamos la respuesta del servidor a la salida estandar
            //System.out.println("Respuesta: " + new String(respuesta.getData()));
            textViewChat.setText(new String(respuesta.getData()));

            // Cerramos el socket
            socketUDP.close();

        } catch (SocketException e) {
            //System.out.println("Socket: " + e.getMessage());
            toast1 =
                    Toast.makeText(getApplicationContext(),
                            "Socket: " + e.getMessage(), Toast.LENGTH_SHORT);

            toast1.show();


        } catch (IOException e) {
            //System.out.println("IO: " + e.getMessage());
            toast1 =
                    Toast.makeText(getApplicationContext(),
                            "IO: " + e.getMessage(), Toast.LENGTH_SHORT);

            toast1.show();

        }

    }

}