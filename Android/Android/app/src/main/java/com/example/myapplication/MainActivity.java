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

    private EditText editText1;
    private EditText editTextmsg;
    private Button botonSend;
    private TextView chat;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText1=(EditText)findViewById(R.id.editText1);
        editTextmsg=(EditText)findViewById(R.id.editText2);
        botonSend=(Button)findViewById(R.id.button);
        chat= (TextView)findViewById(R.id.tVChat);

        botonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enviar(v);
            }
        });
    }


    public void enviar(View view){

        String s1=editText1.getText().toString();
        String s2=editTextmsg.getText().toString();
        if(!s2.isEmpty() && !s1.isEmpty()){
            String msg=s1+"&"+s2;
            client(msg);
        }

    }

    public void client(String msg){
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
            chat.setText(new String(respuesta.getData()));
            toast1 =
                    Toast.makeText(getApplicationContext(),
                            "Respuesta: " + new String(respuesta.getData()) , Toast.LENGTH_SHORT);

            toast1.show();

            // Cerramos el socket
            socketUDP.close();

        } catch (SocketException e) {
            //System.out.println("Socket: " + e.getMessage());
            toast1 =
                    Toast.makeText(getApplicationContext(),
                            "Socket: "+e.getMessage() , Toast.LENGTH_SHORT);

            toast1.show();


        } catch (IOException e) {
            //System.out.println("IO: " + e.getMessage());
            toast1 =
                    Toast.makeText(getApplicationContext(),
                            "IO: "+e.getMessage() , Toast.LENGTH_SHORT);

            toast1.show();

        }

    }

}