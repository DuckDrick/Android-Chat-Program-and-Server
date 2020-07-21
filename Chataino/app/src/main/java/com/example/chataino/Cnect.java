package com.example.chataino;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.Serializable;
import java.net.Socket;

@SuppressWarnings("serial")
public class Cnect implements Serializable {
    private static Socket socket;
    private static DataInputStream dataInputStream;
    private static DataOutputStream dataOutputStream;

    public Cnect(){};
    public Cnect(Socket s, DataInputStream dis, DataOutputStream dos){
        socket = s;
        dataInputStream = dis;
        dataOutputStream = dos;
    }

    public Socket getSocket() {
        return socket;
    }

    public DataInputStream getDataInputStream() {
        return dataInputStream;
    }

    public DataOutputStream getDataOutputStream() {
        return dataOutputStream;
    }
}
