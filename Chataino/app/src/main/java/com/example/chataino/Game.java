package com.example.chataino;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.ScrollingMovementMethod;
import android.text.style.ImageSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.concurrent.Executor;

public class Game extends AppCompatActivity {

    Button atgal;
    TextView text;
    static EditText kodas;
    DataOutputStream dos;
    DataInputStream dis;
    String txt;
    static Button send;
    int position=0;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MainActivity.game = null;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game);
        MainActivity.game = this;
        dos = ((Cnect) getIntent().getExtras().get("Cnect")).getDataOutputStream();
        dis = ((Cnect) getIntent().getExtras().get("Cnect")).getDataInputStream();
        txt = (getIntent().getExtras().get("kodas")).toString();
        atgal = (Button) findViewById(R.id.atgal);
        kodas = (EditText)findViewById(R.id.gameCode);
        send = (Button) findViewById(R.id.SendToGame);

        kodas.setText(txt);

        kodas.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!changedfromserver)
                new MainActivity.sendToServer(dos).execute("code " + s);
                changedfromserver=false;
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        send.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                changedfromserver=true;
                new MainActivity.sendToServer(dos).execute("game " + kodas.getText().toString());
                kodas.setHint(kodas.getText().toString());
               // kodas.setText("");
            }
        });


        atgal.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                baigti();
            }
        });


        new laukkolkeisis().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);


    }
static boolean clickable = true;
//    public static void press(boolean t){
//        send.setClickable(t);
//    }
static boolean changedfromserver = false;
    static boolean sent = false;





    private class laukkolkeisis extends AsyncTask<Void,Void,Void>{

        @Override
        protected Void doInBackground(Void... voids) {
            Looper.prepare();
            while(true){
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        send.setClickable(clickable);
                    }
                });
                if(MainActivity.changed) {

                    changedfromserver = true;
                    final int[] position = new int[1];
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            position[0] = kodas.getSelectionStart();
                            kodas.setText(MainActivity.kodas);
                            kodas.setSelection(position[0] > MainActivity.kodas.length() ? MainActivity.kodas.length() : position[0]);
                        }
                    });

                    //text.setText(text.getText().toString() + "\n" + MainActivity.kodas);
                    MainActivity.changed = false;

                }else if(sent){
                    System.out.println("SDASKNJDAKSJDBKJABdskjbs");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            changedfromserver=true;
                            kodas.setHint(kodas.getText().toString());
                            kodas.setText("");
                            sent = false;
                        }
                    });

                }
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    private void baigti(){
        this.finish();
    }
}
