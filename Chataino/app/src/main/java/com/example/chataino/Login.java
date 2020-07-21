package com.example.chataino;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;

import androidx.appcompat.app.AppCompatActivity;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class Login extends AppCompatActivity {
    Button login;
    private Socket socket;
    private DataOutputStream dataOutputStream;
    private DataInputStream dataInputStream;
    private EditText username;
    private PopupWindow badName;
    boolean connected = false;
    private static String response = new String("nothing");

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.join);

        badName = new PopupWindow(this);
        login = (Button)findViewById(R.id.button3);
        username = (EditText)findViewById(R.id.editText3);
        login.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("CLICK");
                if (username.getText().toString().length() > 0) {
                    if (!connected) {

                            new ConnectToIpTask().execute();

                    } else {
                        new SendMessage(dataOutputStream, dataInputStream).execute("login " + username.getText().toString());
                    }

                    while ((connected == false || response.equals("nothing"))) ;

                    if (response != null)
                        if (response.equals("PASS")) {
                            Intent i = new Intent(getObject(), MainActivity.class);
                            i.putExtra("user_name", username.getText().toString());
                            Cnect c = new Cnect(socket, dataInputStream, dataOutputStream);
                            i.putExtra("Cnect", c);
                            startActivity(i);
                        } else {
                            System.out.println("NOPASS");
                            AlertDialog.Builder builder = new AlertDialog.Builder(getObject());
                            builder.setMessage("Toks vartotojas jau egzistuoja");
                            builder.setPositiveButton("Gerai", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                            builder.show();
                        }
                    response = "nothing";
                }else{
                    System.out.println("NOPASS");
                    AlertDialog.Builder builder = new AlertDialog.Builder(getObject());
                    builder.setMessage("Iveskite vardą");
                    builder.setPositiveButton("Gerai", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    builder.show();
                }
            }
        });
    }

    private Login getObject() {
        return this;
    }

    private void setSocket(Socket s, DataOutputStream dos, DataInputStream dis) {
        this.socket = s;
        this.dataOutputStream = dos;
        this.dataInputStream = dis;
    }

    private class readFromServer extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {

            try {
                String msg;
                while ((msg = dataInputStream.readUTF()) != null) {
                    setResponse(msg);
                }
            } catch (UnknownHostException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    public static void setResponse(String r) {
        response = r;
    }
    private String getName(){
        return username.getText().toString();
    }

    private class ConnectToIpTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... params) {

            Socket socket = null;
            DataOutputStream dataOutputStream = null;
            DataInputStream dataInputStream = null;

            try {
                socket = new Socket("192.168.8.106", 7800);
                //socket = new Socket("213.103.63.32", 7800);
                if(socket.isConnected()) {


                    dataOutputStream = new DataOutputStream(socket.getOutputStream());
                    dataInputStream = new DataInputStream(socket.getInputStream());

                    dataOutputStream.writeUTF("login " + getName());
                    setSocket(socket, dataOutputStream, dataInputStream);
                    connected = true;
                    Thread.sleep(100);
                    setResponse(dataInputStream.readUTF());
                }else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(getObject());
                    builder.setMessage("Nepavyko prisijungti prie serverio");
                    builder.setPositiveButton("Gerai", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    builder.show();
                }

            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (Exception e){
                System.out.println("NONONON");
            }
            return true;
        }
    }
}