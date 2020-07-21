package com.example.chataino;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.text.method.ScrollingMovementMethod;
import android.text.style.ImageSpan;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    EditText textOut;
    private ListView listView;
    private ArrayAdapter<String> arrayAdapter;
    private ArrayList<String> list_of_users = new ArrayList<>();
    private DataOutputStream dataOutputStream;
    private DataInputStream dataInputStream;
    private String username;
    private Cnect c;
    private TextView tv;
    private Button bt;
    private Button send;
    private Button buttonSend;
    public static Game game;
    public static boolean changed=false;
    static String kodas = new String("");
    int height;
    int width;
    public final static int PICK_PHOTO_CODE = 1046;
    Context con;
    Intent i;
    SpannableStringBuilder ssb = new SpannableStringBuilder();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        height = displayMetrics.heightPixels;
        width = displayMetrics.widthPixels;

        username = getIntent().getExtras().get("user_name").toString();
        c = new Cnect();
        setContext(this);
        dataInputStream = c.getDataInputStream();
        dataOutputStream = c.getDataOutputStream();


        textOut = (EditText)findViewById(R.id.textout);
        buttonSend = (Button)findViewById(R.id.send);
        send = (Button)findViewById(R.id.button4);
        listView = (ListView) findViewById(R.id.listv);
        tv = (TextView) findViewById(R.id.textView);
        tv.setMovementMethod(new ScrollingMovementMethod());
        bt = (Button) findViewById(R.id.button);




        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list_of_users);
        listView.setAdapter(arrayAdapter);
        new readFromServer().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        new sendToServer(dataOutputStream).execute("sendOnlineUsers");




        send.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(intent, PICK_PHOTO_CODE);
                }
            }
        });

        bt.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                new sendToServer(dataOutputStream).execute("msg " + username + ": " + textOut.getText().toString());
                textOut.setText("");
            }
        });

        buttonSend.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View v) {
                i = new Intent(v.getContext(), Game.class);
                i.putExtra("Cnect", c);
                i.putExtra("kodas", kodas);
                startActivity(i);

                // new sendToServer().execute("game " + textOut.getText().toString());
                //new sendToServer().execute("msg " + "I zaidima: " + textOut.getText().toString());
              //  textOut.setText("");
            }
        });



    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            Uri photoUri = data.getData();
            Bitmap selectedImage = null;
            try {
                selectedImage = MediaStore.Images.Media.getBitmap(this.getContentResolver(), photoUri);
            } catch (IOException e) {
                e.printStackTrace();
            }

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            selectedImage.compress(Bitmap.CompressFormat.JPEG,50 , stream);
            byte[] byteArray = stream.toByteArray();
            new sendToServerBytes().execute(byteArray);

            ssb.append("\n" + username + ": ");
            Bitmap kitas = Bitmap.createScaledBitmap(selectedImage, width, (int)((float)selectedImage.getHeight()/((float)selectedImage.getWidth()/(float)(width))), true);
            ssb.setSpan(new ImageSpan(con, kitas), ssb.length()-1, ssb.length(),0);
            tv.setText(ssb);

            final int scrollAmount = tv.getLayout().getLineTop(tv.getLineCount()) - tv.getHeight();
            if (scrollAmount > 0)
                tv.scrollTo(0, scrollAmount);
            else
                tv.scrollTo(0, 0);
        }
    }



    private class sendToServerBytes extends AsyncTask<byte[], Void, Void>{
        @Override
        protected Void doInBackground(byte[]... params) {
            try {
                dataOutputStream.writeUTF("PHOTO " + params[0].length);
                dataOutputStream.write(params[0]);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }
    }

    static class sendToServer extends AsyncTask<String, Void, Void>{
        private DataOutput dataOutputStream;

        public sendToServer(DataOutputStream dos){
            this.dataOutputStream = dos;
        }
        @Override
        protected Void doInBackground(String... params) {
            String message = params[0];
            try {
                dataOutputStream.writeUTF(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    private class readFromServer extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            try {
                String com;
                String[] tok;
                while((com = dataInputStream.readUTF()) != null){
                    System.out.println(com);
                    tok = com.split(" ", 2);
                    final String msg;
                    if(tok.length>1){
                    msg = tok[1];
                    if(tok[0].equals("$add")){
                        runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                arrayAdapter.add(msg);
                            }
                        });
                    }else if(tok[0].equals("alert")){
                        if(game != null && i != null){
                            final String[] finalTok1 = tok;
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(game);
                                    builder.setMessage(finalTok1[1]);
                                    builder.setPositiveButton("Valio", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.cancel();
                                        }
                                    });

                                    builder.show();
                                }
                            });
}
                    }else if(tok[0].equals("$msg")){
                        final String[] finalTok = tok;
                        runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                ssb.append("\n" + finalTok[1]);
                                tv.setText(ssb);

                                final int scrollAmount = tv.getLayout().getLineTop(tv.getLineCount()) - tv.getHeight();
                                if (scrollAmount > 0)
                                    tv.scrollTo(0, scrollAmount);
                                else
                                    tv.scrollTo(0, 0);
                            }
                        });
                    }else if(tok[0].equals("code")){
                        if(tok[1].length()>0){
                            kodas = tok[1];
                            //Game.setText(tok[1]);
                        }else{
                            kodas = "";
                            //Game.setText("");
                        }
                        changed=true;
                    }else if(tok[0].equals("$remove")){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                arrayAdapter.remove(msg);
                            }
                        });
                    }else if(tok[0].equals("PHOTO")){
                        int i = Integer.parseInt(tok[1]);
                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                        byte[] b;
                        final String login = dataInputStream.readUTF();
                        for(int a = 0; a< i; a++){
                            byteArrayOutputStream.write(dataInputStream.read());
                        }
                        b=byteArrayOutputStream.toByteArray();
                        final Bitmap image = BitmapFactory.decodeByteArray(b, 0, b.length);
                        System.out.println("Photo saved");
                        runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                ssb.append("\n" + login + ": ");
                                Bitmap kitas = Bitmap.createScaledBitmap(image, width, (int)((float)image.getHeight()/((float)image.getWidth()/(float)(width))) , true);
                                ssb.setSpan(new ImageSpan(con, kitas),ssb.length()-1, ssb.length(),0);
                                tv.setText(ssb);

                                final int scrollAmount = tv.getLayout().getLineTop(tv.getLineCount()) - tv.getHeight();
                                if (scrollAmount > 0)
                                    tv.scrollTo(0, scrollAmount);
                                else
                                    tv.scrollTo(0, 0);
                            }
                        });
                    }}else{
                          if(tok[0].equals("TOSB")){
                            if(i!=null){
                                //Game.press(false);
                                Game.clickable=false;
                                Game.sent = true;
                            }
                        }else if(tok[0].equals("ESB")){
                            if(i!=null)
                                Game.clickable = true;
                            //Game.press(true);
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
    public void setContext(Context c){
        this.con = c;
    }
}


