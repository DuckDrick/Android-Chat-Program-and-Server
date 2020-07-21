package com.mygdx.game.server;

import com.mygdx.game.input.SplitString;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class ServerWorker extends Thread {


    private final Socket clientSocket;
    private final Server server;
    private String login = null;
    private static int photocount = 0;
    FileOutputStream duomenys;
    public DataInputStream dataInputStream = null;
    public DataOutputStream dataOutputStream = null;


    public ServerWorker(Server server, Socket clientSocket) {
        this.server = server;
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {

        try {
            handleClientSocket();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleClientSocket() throws IOException{

        dataInputStream = new DataInputStream(clientSocket.getInputStream());
        dataOutputStream = new DataOutputStream(clientSocket.getOutputStream());
        System.out.println("ip: " + clientSocket.getInetAddress());
        String msg;

        duomenys = new FileOutputStream(new File("duom.txt"), true);
        String[] tokens;
        try {
            FileOutputStream fout = null;
            while ((msg = dataInputStream.readUTF()) != null) {
                System.out.println(msg);

                tokens = msg.split(" ", 2);
                if (tokens[0].equals("login")) {
                    if (!checkIfExists(tokens[1])) {
                        this.login = tokens[1];
                        dataOutputStream.writeUTF("PASS");
                        for(ServerWorker sw: server.getWorkerList()){
                            if(sw != this){
                            sw.dataOutputStream.writeUTF("$add " + getLogin());
                            sw.dataOutputStream.writeUTF("$END");
                            }
                        }
                    }else{
                        dataOutputStream.writeUTF("NO PASS");
                    }
                } else if(tokens[0].equals("any")){
                    dataOutputStream.writeUTF("$END");
                }else if(tokens[0].equals("code")){
                    for(ServerWorker sw : server.getWorkerList()){
                        if(sw!=this)
                            sw.dataOutputStream.writeUTF("code " + tokens[1]);

                    }
                }else if(tokens[0].equals("sendOnlineUsers")) {

                    for(ServerWorker sw: server.getWorkerList()){
                        if(sw!=this)
                        dataOutputStream.writeUTF("$add " + sw.getLogin());
                    }

                    dataOutputStream.writeUTF("$END");

                    runOnThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                FileReader r = new FileReader("duom.txt");
                                Scanner scanner = new Scanner(new File("duom.txt"));
                                while(scanner.hasNextLine()){
                                    String s = scanner.nextLine();
                                    String[] tokens = s.split(" ", 2);
                                    if(tokens[0].equals("$msg")){
                                        dataOutputStream.writeUTF(s);
                                    }else if(tokens[0].equals("PHOTO")){
                                        String[] ph = tokens[1].split(" ", 2);
                                        int nr = Integer.parseInt(ph[0]);

                                        File nuotrauka = new File("photo"+nr+".jpg");
                                        BufferedImage bi = ImageIO.read(nuotrauka);
                                        ByteArrayOutputStream byteArrayOutputStream1 = new ByteArrayOutputStream();
                                        ImageIO.write(bi, "jpg", byteArrayOutputStream1);
                                        byteArrayOutputStream1.flush();

                                        byte[] bytes = byteArrayOutputStream1.toByteArray();
                                        dataOutputStream.writeUTF("PHOTO "+ bytes.length);
                                        dataOutputStream.writeUTF(ph[1]);
                                        dataOutputStream.write(bytes);
                                    }
                                }
                                scanner.close();
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    });

                }else if(tokens[0].equals("PHOTO")){

                    fout  =  new FileOutputStream("photo" + photocount + ".jpg");
                    final ByteArrayOutputStream byteArrayOutputStream1 = new ByteArrayOutputStream();
                    final int i = Integer.parseInt(tokens[1]);
                    final byte[][] bytar = {new byte[i]};

                    final FileOutputStream finalFout = fout;
                    runOnThread(new Runnable() {
                        @Override
                        public void run() {
                            for(int a = 0; a< i; a++) {
                                try {
                                    byteArrayOutputStream1.write(dataInputStream.read());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                            bytar[0] = byteArrayOutputStream1.toByteArray();
                            for(ServerWorker sw:server.getWorkerList()){
                                if(sw != returnthis()){
                                    try {
                                        sw.dataOutputStream.writeUTF("PHOTO " + i);
                                        sw.dataOutputStream.writeUTF(login);
                                        sw.dataOutputStream.write(bytar[0]);
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                            try {
                                duomenys = new FileOutputStream(new File("duom.txt"), true);
                                duomenys.write(("PHOTO " + photocount + " " + login + "\n").getBytes());
                                duomenys.close();
                                photocount++;
                                finalFout.write(bytar[0]);
                                finalFout.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            System.out.println("Photo saved");
                        }
                    });

                }
                else if(tokens[0].equals("game")) {
                    if(!SplitString.running) {
                        SplitString ss = new SplitString(tokens[1]);
                        ss.start();
                        for(ServerWorker sw : Server.getWorkerList()){
                            sw.dataOutputStream.writeUTF("TOSB");
                        }
                    }
                    System.out.println("Command started");
                    dataOutputStream.writeUTF("$END");

                }else if(tokens[0].equals("msg")){
                    duomenys = new FileOutputStream(new File("duom.txt"), true);
                    duomenys.write(("$msg " + tokens[1].replaceAll("\\n", " ") + "\n").getBytes());
                    duomenys.close();
                    for(ServerWorker sw: server.getWorkerList()){
                        sw.dataOutputStream.writeUTF("$msg " + tokens[1]);
                        sw.dataOutputStream.writeUTF("$END");
                    }
                }else{
                    System.out.println(msg);
                    dataOutputStream.writeUTF("$END");
                }
            }
        }catch (EOFException e){
            for(ServerWorker sw : server.getWorkerList()){
                if(sw != this){
                    sw.dataOutputStream.writeUTF("$remove " + getLogin());
                }
            }
            System.out.println("REMOVING WORKER " + login);
            server.removeWorker(this);


        }
    }

    public final void runOnThread(Runnable action) {
        action.run();
    }

    private ServerWorker returnthis(){
        return this;
    }

    private boolean checkIfExists(String name) {
        for(ServerWorker sw: server.getWorkerList()){
            if(sw!=this && sw.getLogin()!=null)
            if(sw.getLogin().equals(name)){
                return true;
            }
        }
        return false;
    }

    public String getLogin(){
        return login;
    }
}
