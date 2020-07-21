package com.mygdx.game.server;

import java.io.DataOutputStream;
import java.io.IOException;

public class Siusti extends Thread {

    public void run(){
        while(true){
            if(ServerMain.msg.length()>0){
                System.out.println("MSG " + ServerMain.msg);
                for(ServerWorker sw : Server.getWorkerList()){
                    try {
                        sw.dataOutputStream.writeUTF("code " + ServerMain.msg);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                ServerMain.msg = "";
            }
        }
    }
}
