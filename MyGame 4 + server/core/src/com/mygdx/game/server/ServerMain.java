package com.mygdx.game.server;

public class ServerMain extends Thread {

    public static String msg;

    public void run(){
        msg=new String("");
      //  Siusti s = new Siusti();
      //  s.start();
        int port = 7800;
        Server server = new Server(port);
        server.start();
    }

}

