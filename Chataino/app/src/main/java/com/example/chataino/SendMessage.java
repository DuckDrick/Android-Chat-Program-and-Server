package com.example.chataino;

import android.os.AsyncTask;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class SendMessage extends AsyncTask<String, Void, Void> {
    DataOutputStream dos;
    DataInputStream dis;
    public SendMessage(DataOutputStream d, DataInputStream di){
        this.dos = d;
        this.dis =di;
    }
    @Override
    protected Void doInBackground(String... params){
        try {
            dos.writeUTF(params[0]);
            Thread.sleep(100);
            Login.setResponse(dis.readUTF());
            return null;
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        return null;
    }
}