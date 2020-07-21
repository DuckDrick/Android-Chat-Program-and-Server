package com.mygdx.game.server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server extends Thread {
    private final int serverPort;

    private static ArrayList<ServerWorker> workerList = new ArrayList<>();

    public Server(int port) {
        this.serverPort = port;
    }

    public static List<ServerWorker> getWorkerList(){
        return workerList;
    }

    @Override
    public void run() {
        try {
            ServerSocket serverSocket = new ServerSocket(serverPort);//,1,InetAddress.getByName("213.103.63.32")
            while(true){
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connection accepted from " + clientSocket);
                ServerWorker worker = new ServerWorker(this, clientSocket);
                System.out.println("Worker created");
                workerList.add(worker);
                System.out.println("Worker added to the list");
                worker.start();
                System.out.println("Worker thread started");
            }
        }catch (IOException e) {
            e.printStackTrace();
        }
    }
        public void removeWorker(ServerWorker serverWorker) {
            workerList.remove(serverWorker);
        }
    }
