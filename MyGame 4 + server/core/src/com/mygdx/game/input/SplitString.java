package com.mygdx.game.input;

import com.mygdx.game.server.Server;
import com.mygdx.game.server.ServerWorker;
import javafx.util.Pair;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class SplitString extends Thread{
    public static boolean running = false;


    private String commands;
    private List<String> commandList;
    private Input in;
    private int groups=0;
    private List<Pair<Integer, Integer>> poros = new ArrayList<Pair<Integer,Integer>>();
    public void run(){
        //System.out.println("thread is running...");
        running=true;
        noNewLine();
        running=false;
    }

    public SplitString(String string){
        this.commands = string;
        commandList = new ArrayList<String>();
  //  this.in = in;
    }

    private void noNewLine(){
       commands = commands.replaceAll("\n{1,}"," ");
        commands = commands.replaceAll(" +"," ");
        //System.out.println(commands);
       parseCommands();
    }

    private void parseCommands(){
        while(commands.length()>0) {
            if (commands.indexOf(";") == -1 && commands.indexOf("{") == -1 && commands.indexOf("}") == -1) {
                //System.out.println("FAIL");
                break;
            } else {
                for(int i=0; i<commands.length(); i++){
                    //System.out.println(commands.charAt(i));
                    if(commands.startsWith(" ")){
                        commands=commands.replaceFirst(" ", "");
                    }
                    //System.out.println("comlist: " + commandList);
                    //System.out.println(commands.matches("for.*"));
                    if(commandList.size()>0){
                       // //System.out.println("check " + commandList.get(commandList.size()-1));
                    }
                    if(commandList.size() > 0 ? commandList.get(commandList.size()-1).equals("") : false){
                     //   commandList.remove(commandList.size()-1);
                    }

                    if(commands.charAt(i)==';'){
                        commandList.add(commands.substring(0, commands.indexOf(';')));
                        commands = commands.substring(commands.indexOf(';')+1);

                       // //System.out.println("CUT:" + commandList.get(commandList.size()-1));
                       // //System.out.println(commands);
                        break;
                    }else if(commands.charAt(i)=='{'){
                        if (0 != commands.indexOf('{')) {
                            commandList.add(commands.substring(0, commands.indexOf('{')));
                        }
                       // //System.out.println("CUT2:" + commandList.get(commandList.size()-1));

                        commandList.add("{");
                        commands = commands.substring(commands.indexOf('{')+1);
                       // //System.out.println("CUT2:" + commandList.get(commandList.size()-1));
                        break;
                    }else if(commands.charAt(i)=='}'){
                       // //System.out.println("CUT3:" + commandList.get(commandList.size()-1));
                        commandList.add("}");
                        commands = commands.substring(commands.indexOf('}')+1);
                       // //System.out.println("CUT3:" + commandList.get(commandList.size()-1));
                        break;
                    }else if(commands.matches("(else if).*")){
                        commandList.add(commands.substring(0, 4));
                        commands = commands.substring(5);
//                        commands.replaceFirst(" ", "");
                        break;
                    }else if(commands.matches("for\\(.*")){
                        //System.out.println("MATCHESSSSSSSSSSS");
                        commandList.add(commands.substring(0,commands.indexOf('{')));
                        //System.out.println(commands);
                        commands=commands.substring(commands.indexOf('{'));
                        //System.out.println(commands);
                        break;
                    }

                }
            }
        }

        if(enoughBrackets()) {
            try {
                //System.out.println("comlist: " + commandList);

                    new Atpazink(commandList, new ArrayList<Pair<String, Pair<String,String>>>());

            } catch (InterruptedException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }else{
            //System.out.println("NOT ENOUGH");
        }

        for(ServerWorker sw: Server.getWorkerList()){
            try {
                sw.dataOutputStream.writeUTF("ESB");
            }catch (IOException e){
                e.printStackTrace();
            }
        }

       // if(in!=null)
       // in.setButtonTouchable();
    }
    private boolean enoughBrackets(){
        int start = 0, finish = 0;
        int brack=0;
        int index=0;
        for(String s: commandList){
            if(s.equals("{")){
                if(brack==0){
                    if(start == index){
                        start=index;
                    }else{
                        finish = index;
                        groups++;
                        poros.add(new Pair(start, finish));
                        start=index;
                    }
                }
                brack++;
            }else if(s.equals("}")){
                brack--;
                if(brack<0){
                    return false;
                }
                if(brack==0){
                    finish=index;
                    groups++;
                    poros.add(new Pair(start, finish));
                    start=index;
                }
            }
            index++;
        }
        if(groups == 0){
            poros.add(new Pair(0, commandList.size()-1));
            groups++;
        }
        if(brack == 0)
            return true;
        else
            return false;
    }

}
