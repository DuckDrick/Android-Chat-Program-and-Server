package com.mygdx.game.input;

import com.mygdx.game.Character;
import com.mygdx.game.Hud;
import com.mygdx.game.MyGame;
import com.mygdx.game.PlayScreen;

import com.udojava.evalex.Expression;
import javafx.util.Pair;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class Atpazink {

    private List<String> commands;
    private static Character player;
    private static PlayScreen ps;
    private List<String> another;
    private List<Pair<String, Pair<String, String>>> kintamieji;

    private int si, ei;
    private static MyGame game;
    private static Hud hud;
    public static boolean reset = false;
    public Atpazink(List<String> commands, List<Pair<String,Pair<String,String>>> kintamieji) throws ClassNotFoundException, InterruptedException {
        this.commands = commands;
        this.kintamieji = kintamieji;
        for(String s: commands){
            //System.out.println(s);
        }
        Pradek();

    }

    private void Pradek() throws InterruptedException, ClassNotFoundException {
        String s;
        boolean needelse = false;
        for(int i = 0; i < commands.size(); i++){
            s = commands.get(i);
           // //System.out.println(s);
            Thread.sleep(150);
            if(!ps.moving) {
                s = s.replaceFirst("(\\(.*\\))", "");
                s = s.replaceFirst(" .*", "");

                //System.out.println("Komanda:  " + s + " dydis: " + s.length());
                switch (s) {
                    case "while":
                        s=commands.get(i);
                        s=s.substring(s.indexOf('(')+1, s.lastIndexOf(')'));
                        Expression exp = new Expression(s);
                        for (Pair<String, Pair<String, String>> pz : kintamieji) {
                            BigDecimal bd = new BigDecimal(pz.getValue().getValue());
                            exp.with(pz.getValue().getKey(), bd);
                        }
                        int kintamujukiekis=kintamieji.size();
                        makeNew(i+1);
                        while(exp.eval().toString().equals("1")){
                            new Atpazink(another, kintamieji);
                            while(kintamieji.size()>kintamujukiekis){
                                kintamieji.remove(kintamieji.size()-1);
                            }

                                exp = new Expression(s);
                            //System.out.println("EXP:"+exp);

                                for(Pair<String, Pair<String, String>> pz: kintamieji){
                                    BigDecimal bd = new BigDecimal(pz.getValue().getValue());
                                    exp.with(pz.getValue().getKey(), bd);
                                }

                        }

                        i=ei;

                        break;
                    case "for":
                        s=commands.get(i);
                        int siz=kintamieji.size();
                        s=s.substring(s.indexOf('(')+1, s.lastIndexOf(')'));
                        List <String> foras = new ArrayList<String>();
                        foras.add(s.substring(0, s.indexOf(';')));
                        new Atpazink(foras, kintamieji);
                        s=s.substring(s.indexOf(';')+1);
                        int kitas=0;
                        Expression foro = new Expression(s.substring(0, s.indexOf(';')));
                        if(s.substring(0,s.indexOf(';')).length()>0) {
                            for (Pair<String, Pair<String, String>> pz : kintamieji) {
                                BigDecimal bd = new BigDecimal(pz.getValue().getValue());
                                foro.with(pz.getValue().getKey(), bd);
                            }
                        kitas=1;
                        }else{
                            foro= new Expression("1");
                        }

                        kintamujukiekis = kintamieji.size();
                        makeNew(i+1);
                        another.add(s.substring(s.lastIndexOf(';')+1));
                        while(foro.eval().toString().equals("1")){
                            //System.out.println("CIKLAS:"+foro);
                            new Atpazink(another, kintamieji);
                            while(kintamieji.size()>kintamujukiekis){
                                kintamieji.remove(kintamieji.size()-1);
                            }
                            if(kitas==1){
                            foro = new Expression(s.substring(0, s.indexOf(';')));

                            for(Pair<String, Pair<String, String>> pz: kintamieji){
                                BigDecimal bd = new BigDecimal(pz.getValue().getValue());
                                foro.with(pz.getValue().getKey(), bd);
                            }}

                        }

                        i=ei;
                        while(kintamieji.size()>siz){
                            kintamieji.remove(kintamieji.size()-1);
                        }

                        break;
                    case "else":
                        if(needelse){
                            if(commands.get(i+1).equals("{")) {
                                makeNew(i + 1);

                                //System.out.println(s);

                                int sizeofkintamieji = kintamieji.size();
                                //System.out.println("S:  " + s);
                                //System.out.println(i);
                                i = ei;
                                //System.out.println(i);
                                if (s.equals("1")) {
                                    new Atpazink(another, kintamieji);
                                    while (kintamieji.size() > sizeofkintamieji) {
                                        kintamieji.remove(kintamieji.size() - 1);
                                    }
                                }
                                needelse=false;
                            }
                        }else{
                            makeNew(i+1);
                            i=ei;
                        }
                    break;

                    case "if":
                        //System.out.println("IF" + i);
                        s = commands.get(i);
                        s = s.substring(s.indexOf('(')+1, s.lastIndexOf(')'));
                        Expression e = new Expression(s);
                        //System.out.println(e);

                        for(Pair<String, Pair<String, String>> p: kintamieji){
                            BigDecimal bd = new BigDecimal(p.getValue().getValue());
                            e.with(p.getValue().getKey(), bd);
                        }
                        s = e.eval().toString();
                        //System.out.println(s);
                        if(!commands.get(i+1).equals("{")){


                        }else {
                            makeNew(i+1);

                            //System.out.println(s);

                            int sizeofkintamieji = kintamieji.size();
                            //System.out.println("S:  " + s);
                            //System.out.println(i);
                            i = ei;
                            //System.out.println(i);
                            if(s.equals("1")) {
                                new Atpazink(another, kintamieji);
                                while (kintamieji.size() > sizeofkintamieji) {
                                    kintamieji.remove(kintamieji.size() - 1);
                                }
                               needelse = false;

                            }else{
                                needelse = true;
                            }
                        }
                        break;
                    case "int": case "byte": case "short": case "long": case "float": case "double": case "boolean": case "char":
                        kintamasis(s, i);
                        needelse = false;
                        break;
                    case "{":
                        needelse = false;
                        makeNew(i);
                        //System.out.println(another);
                        //System.out.println(kintamieji);
                        int sizeofkintamieji = kintamieji.size();
                            new Atpazink(another, kintamieji);
                        while(kintamieji.size()>sizeofkintamieji){
                            kintamieji.remove(kintamieji.size()-1);
                        }
                        i=ei;
                        break;
                    case "isspresk":
                        needelse=false;
                        s = commands.get(i);
                        s = s.replaceAll("[A-Za-z()]*", "");
                        s = s.replaceAll(" ", "");
                        String[] com;
                        if(s.length()>0) {
                            if (Integer.parseInt(s) == 1) {

                                com = new String[]{"desinen", "virsun", "desinen", "desinen", "apacion", "kairen", "kairen", "kairen", "desinen", "desinen", "desinen", "apacion", "apacion", "apacion", "kairen", "apacion", "desinen", "kairen", "kairen", "apacion", "kairen", "kairen", "virsun", "virsun", "apacion", "apacion", "desinen", "desinen", "virsun", "desinen", "virsun", "desinen", "virsun", "virsun", "virsun", "kairen", "kairen", "apacion", "desinen", "apacion", "desinen", "apacion", "apacion", "kairen", "kairen", "apacion", "kairen", "kairen", "virsun", "desinen"};
                                List<String> n = new ArrayList<String>(Arrays.asList(com));

                                new Atpazink(n, kintamieji);
                            } else if (Integer.parseInt(s) == 2) {
                                com = new String[]{"kairen", "kairen", "kairen", "virsun", "virsun", "virsun", "kairen", "kairen", "apacion", "kairen", "apacion", "kairen", "kairen", "apacion", "kairen", "kairen", "virsun", "desinen", "virsun", "virsun", "virsun", "virsun", "desinen", "virsun", "virsun", "kairen", "kairen", "kairen", "apacion", "apacion", "virsun", "virsun", "desinen", "desinen", "desinen", "apacion", "apacion", "kairen", "kairen", "kairen", "kairen", "kairen", "kairen", "apacion", "apacion", "kairen", "kairen", "virsun", "virsun", "desinen", "virsun", "desinen", "apacion", "desinen", "desinen", "desinen", "virsun", "virsun", "desinen", "desinen", "apacion", "desinen", "desinen", "desinen", "desinen", "apacion", "desinen", "desinen", "virsun", "kairen", "kairen", "kairen", "kairen", "kairen", "apacion", "kairen", "kairen", "kairen", "kairen", "kairen", "kairen", "apacion", "apacion", "desinen", "desinen", "desinen", "virsun", "apacion", "apacion", "kairen", "kairen", "kairen", "kairen", "kairen", "kairen", "virsun", "kairen", "apacion", "virsun", "virsun", "virsun", "desinen", "desinen", "desinen", "virsun", "desinen", "apacion", "desinen", "desinen", "desinen", "desinen", "desinen", "apacion", "desinen", "desinen", "virsun", "kairen", "kairen", "kairen", "kairen", "kairen", "kairen", "kairen", "apacion", "apacion", "desinen", "apacion", "kairen", "kairen", "kairen", "virsun", "virsun", "virsun", "desinen", "virsun", "desinen", "apacion", "desinen", "desinen", "desinen", "desinen", "desinen", "desinen", "desinen", "desinen", "virsun", "kairen", "desinen", "desinen", "desinen", "apacion", "kairen", "kairen", "kairen", "kairen", "kairen", "kairen", "kairen", "kairen", "kairen", "kairen", "apacion", "apacion", "kairen", "kairen", "virsun", "virsun", "desinen", "virsun", "desinen", "apacion", "apacion", "virsun", "desinen", "desinen", "desinen", "virsun", "virsun", "desinen", "desinen", "desinen", "apacion", "desinen", "apacion", "kairen", "kairen", "kairen", "kairen", "kairen", "kairen", "desinen", "desinen", "apacion", "apacion", "kairen", "kairen", "kairen", "kairen", "kairen", "apacion", "kairen", "virsun", "desinen", "virsun", "kairen", "virsun", "desinen", "desinen", "virsun", "desinen", "apacion", "apacion", "virsun", "desinen", "desinen", "desinen", "desinen", "desinen", "desinen", "desinen", "desinen", "desinen", "desinen", "desinen", "apacion", "apacion", "kairen", "kairen", "apacion", "kairen", "apacion", "kairen", "kairen", "apacion", "kairen", "virsun", "virsun", "virsun", "virsun", "desinen", "virsun", "kairen", "kairen", "kairen", "kairen", "kairen", "desinen", "desinen", "apacion", "apacion", "kairen", "kairen", "kairen", "kairen", "kairen", "apacion", "desinen", "desinen", "desinen", "kairen", "kairen", "kairen", "kairen", "virsun", "desinen", "virsun", "virsun", "desinen", "virsun", "desinen", "apacion", "desinen", "desinen", "desinen", "desinen", "desinen", "desinen", "desinen", "desinen", "desinen", "desinen", "desinen", "apacion", "apacion", "apacion", "kairen", "apacion", "apacion", "desinen", "desinen", "desinen", "desinen", "desinen", "desinen", "virsun", "virsun", "desinen", "virsun", "virsun", "kairen", "apacion", "kairen", "kairen", "virsun", "virsun", "kairen", "kairen", "kairen", "kairen", "kairen", "kairen", "kairen", "kairen", "kairen", "kairen", "kairen", "kairen", "kairen", "kairen", "apacion", "apacion", "desinen", "apacion", "kairen", "kairen", "kairen", "virsun", "virsun", "virsun", "desinen", "virsun", "desinen", "apacion", "desinen", "desinen", "desinen", "virsun", "virsun", "desinen", "desinen", "apacion", "desinen", "apacion", "kairen", "kairen", "kairen", "kairen", "kairen", "kairen", "apacion", "apacion", "kairen", "kairen", "virsun", "virsun", "desinen", "virsun", "desinen", "apacion", "apacion", "virsun", "desinen", "desinen", "desinen", "desinen", "desinen", "desinen", "desinen", "desinen", "desinen", "desinen", "desinen", "desinen", "desinen", "virsun", "kairen", "virsun", "virsun", "desinen", "desinen", "apacion", "kairen", "virsun", "kairen", "apacion", "apacion", "desinen", "apacion", "kairen", "kairen", "kairen", "kairen", "kairen", "kairen", "kairen", "kairen", "kairen", "kairen", "kairen", "kairen", "desinen", "desinen", "apacion", "apacion", "kairen", "kairen", "kairen", "kairen", "kairen", "kairen", "apacion", "virsun", "desinen", "virsun", "virsun", "desinen", "virsun", "desinen", "apacion", "apacion", "virsun", "desinen", "desinen", "desinen", "desinen", "desinen", "virsun", "desinen", "desinen", "desinen", "desinen", "desinen", "virsun", "desinen", "apacion", "desinen", "apacion", "kairen", "kairen", "kairen", "kairen", "kairen", "kairen", "kairen", "kairen", "kairen", "kairen", "kairen", "desinen", "desinen", "apacion", "apacion", "kairen", "kairen", "kairen", "desinen", "apacion", "kairen", "kairen", "kairen", "virsun", "virsun", "virsun", "desinen", "virsun", "desinen", "apacion", "desinen", "desinen", "desinen", "desinen", "desinen", "virsun", "desinen", "desinen", "desinen", "desinen", "desinen", "desinen", "desinen", "desinen", "virsun", "virsun", "kairen", "apacion", "apacion", "desinen", "apacion", "kairen", "kairen", "kairen", "kairen", "kairen", "kairen", "kairen", "kairen", "kairen", "kairen", "kairen", "kairen", "kairen", "apacion", "apacion", "kairen", "kairen", "virsun", "virsun", "desinen", "virsun", "desinen", "apacion", "apacion", "virsun", "desinen", "desinen", "desinen", "desinen", "desinen", "desinen", "desinen", "desinen", "desinen", "desinen", "desinen", "apacion", "apacion", "apacion", "kairen", "apacion", "apacion", "desinen", "virsun", "virsun", "virsun", "virsun", "virsun", "desinen", "virsun", "kairen", "kairen", "kairen", "kairen", "kairen", "kairen", "desinen", "desinen", "desinen", "desinen", "virsun", "desinen", "desinen", "desinen", "desinen", "desinen", "apacion", "desinen", "desinen", "virsun", "kairen", "kairen", "kairen", "kairen", "virsun", "kairen", "apacion", "apacion", "desinen", "apacion", "kairen", "kairen", "kairen", "kairen", "kairen", "kairen", "kairen", "kairen", "kairen", "kairen", "kairen", "kairen", "desinen", "desinen", "apacion", "apacion", "kairen", "kairen", "apacion", "kairen", "kairen", "virsun", "kairen", "virsun", "virsun", "desinen", "virsun", "desinen", "apacion", "desinen", "desinen", "desinen", "apacion", "apacion", "apacion", "kairen", "kairen", "kairen", "desinen", "virsun", "kairen", "kairen", "desinen", "desinen", "desinen", "desinen", "virsun", "virsun", "desinen", "desinen", "desinen", "desinen", "desinen", "desinen", "desinen", "desinen", "desinen", "desinen", "desinen", "apacion", "apacion", "desinen", "desinen", "virsun", "virsun", "virsun", "desinen", "virsun", "kairen", "kairen", "kairen", "kairen", "virsun", "kairen", "apacion", "apacion", "desinen", "apacion", "kairen", "kairen", "kairen", "kairen", "kairen", "kairen", "kairen", "kairen", "kairen", "kairen", "kairen", "kairen", "kairen", "apacion", "virsun", "kairen", "kairen", "kairen", "desinen", "desinen", "desinen", "desinen", "desinen", "desinen", "virsun", "virsun", "desinen", "desinen", "apacion", "desinen", "apacion", "kairen", "kairen", "kairen", "kairen", "kairen", "kairen", "kairen", "kairen", "desinen", "desinen", "desinen", "desinen", "desinen", "desinen", "desinen", "apacion", "apacion", "apacion", "apacion", "desinen", "desinen", "desinen", "virsun", "desinen", "desinen", "apacion", "desinen", "virsun", "virsun", "virsun", "apacion", "apacion", "apacion", "apacion", "desinen", "desinen", "desinen", "desinen", "desinen", "virsun", "virsun", "virsun", "virsun", "virsun", "virsun", "desinen", "virsun", "kairen", "kairen", "kairen", "kairen", "virsun", "kairen", "apacion", "desinen", "apacion", "kairen", "kairen", "kairen", "kairen", "kairen", "kairen", "kairen", "desinen", "desinen", "desinen", "desinen", "desinen", "desinen", "apacion", "kairen", "kairen", "kairen", "kairen", "kairen", "kairen", "kairen", "kairen", "kairen", "kairen", "kairen", "kairen", "kairen", "desinen", "desinen", "desinen", "desinen", "virsun", "virsun", "desinen", "desinen", "apacion", "desinen", "apacion", "kairen", "kairen", "kairen", "kairen", "kairen", "kairen", "desinen", "desinen", "desinen", "apacion", "apacion", "kairen", "kairen", "kairen"};
                                List<String> n = new ArrayList<String>(Arrays.asList(com));

                                new Atpazink(n, kintamieji);

                            } else if (Integer.parseInt(s) == 3) {
                                com = new String[]{"virsun", "kairen", "kairen", "kairen", "apacion", "desinen", "apacion", "apacion", "kairen", "virsun", "kairen", "kairen", "kairen", "kairen", "kairen", "desinen", "apacion", "apacion", "kairen", "virsun", "kairen", "kairen", "apacion", "desinen", "apacion", "apacion", "apacion", "kairen", "apacion", "apacion", "desinen", "virsun", "desinen", "desinen", "desinen", "desinen", "apacion", "desinen", "desinen", "apacion", "desinen", "desinen", "virsun", "apacion", "kairen", "kairen", "virsun", "desinen", "virsun", "desinen", "desinen", "desinen", "desinen", "desinen", "apacion", "desinen", "virsun", "virsun", "virsun", "virsun", "virsun", "virsun", "virsun", "apacion", "apacion", "apacion", "apacion", "apacion", "apacion", "kairen", "kairen", "kairen", "kairen", "kairen", "kairen", "apacion", "apacion", "desinen", "virsun", "kairen", "virsun", "desinen", "desinen", "desinen", "desinen", "desinen", "apacion", "desinen", "virsun", "virsun", "virsun", "virsun", "virsun", "virsun", "kairen", "virsun", "virsun", "desinen", "apacion", "kairen", "apacion", "desinen", "apacion", "apacion", "apacion", "apacion", "apacion", "kairen", "kairen", "kairen", "kairen", "kairen", "apacion", "kairen", "kairen", "kairen", "kairen", "virsun", "virsun", "desinen", "apacion", "kairen", "kairen", "kairen", "kairen", "kairen", "apacion", "kairen", "apacion", "apacion", "desinen", "virsun", "virsun", "kairen", "virsun", "desinen", "desinen", "desinen", "desinen", "desinen", "apacion", "desinen", "desinen", "desinen", "apacion", "desinen", "virsun", "kairen", "kairen", "kairen", "kairen", "virsun", "virsun", "desinen", "apacion", "kairen", "apacion", "desinen", "desinen", "desinen", "virsun", "desinen", "desinen", "desinen", "desinen", "desinen", "apacion", "desinen", "virsun", "virsun", "virsun", "virsun", "virsun", "virsun", "desinen", "kairen", "apacion", "apacion", "apacion", "apacion", "apacion", "kairen", "kairen", "kairen", "kairen", "kairen", "kairen", "apacion", "apacion", "desinen", "virsun", "kairen", "virsun", "desinen", "desinen", "desinen", "desinen", "desinen", "apacion", "desinen", "virsun", "virsun", "virsun", "virsun", "virsun", "apacion", "apacion", "apacion", "apacion", "kairen", "kairen", "kairen", "kairen", "kairen", "apacion", "kairen", "kairen", "kairen", "virsun", "kairen", "kairen", "kairen", "kairen", "kairen", "apacion", "apacion", "apacion", "desinen", "desinen", "virsun", "virsun", "kairen", "virsun", "desinen", "desinen", "desinen", "virsun", "desinen", "apacion", "kairen", "apacion", "desinen", "desinen", "desinen", "apacion", "desinen", "virsun", "kairen", "virsun", "desinen", "desinen", "desinen", "desinen", "desinen", "apacion", "desinen", "virsun", "virsun", "virsun", "virsun", "kairen", "kairen", "kairen", "virsun", "virsun", "virsun", "desinen", "desinen", "desinen", "desinen", "apacion", "kairen", "apacion", "virsun", "virsun", "kairen", "kairen", "kairen", "apacion", "apacion", "apacion", "desinen", "desinen", "desinen", "apacion", "apacion", "apacion", "kairen", "kairen", "kairen", "kairen", "kairen", "apacion", "kairen", "kairen", "kairen", "virsun", "kairen", "kairen", "kairen", "apacion", "apacion", "apacion", "kairen", "virsun", "kairen", "virsun", "kairen", "virsun", "desinen", "desinen", "desinen", "desinen", "desinen", "virsun", "desinen", "apacion", "kairen", "kairen", "kairen", "apacion", "apacion", "kairen", "virsun", "kairen", "virsun", "desinen", "desinen", "desinen", "desinen", "apacion", "desinen", "desinen", "desinen", "apacion", "desinen", "virsun", "kairen", "kairen", "kairen", "kairen", "virsun", "virsun", "desinen", "apacion", "kairen", "apacion", "desinen", "desinen", "desinen", "virsun", "desinen", "desinen", "desinen", "desinen", "desinen", "apacion", "desinen", "virsun", "virsun", "virsun", "virsun", "virsun", "virsun", "virsun", "virsun", "apacion", "apacion", "apacion", "apacion", "apacion", "apacion", "apacion", "kairen", "kairen", "kairen", "kairen", "kairen", "kairen", "apacion", "apacion", "desinen", "virsun", "kairen", "virsun", "desinen", "desinen", "desinen", "desinen", "desinen", "apacion", "desinen", "virsun", "virsun", "virsun", "virsun", "virsun", "virsun", "virsun", "apacion", "apacion", "apacion", "apacion", "apacion", "apacion", "kairen", "kairen", "kairen", "kairen", "kairen", "apacion", "kairen", "kairen", "kairen", "kairen", "apacion", "desinen", "apacion", "apacion", "kairen", "virsun", "virsun", "virsun", "desinen", "virsun", "kairen", "kairen", "kairen", "apacion", "kairen", "kairen", "virsun", "desinen", "desinen", "desinen", "desinen", "virsun", "desinen", "apacion", "kairen", "apacion", "desinen", "desinen", "desinen", "apacion", "kairen", "desinen", "desinen", "virsun", "kairen", "kairen", "kairen", "kairen", "apacion", "apacion", "apacion", "desinen", "desinen", "virsun", "kairen", "virsun", "kairen", "virsun", "desinen", "desinen", "desinen", "virsun", "desinen", "desinen", "desinen", "desinen", "desinen", "apacion", "desinen", "virsun", "virsun", "virsun", "virsun", "virsun", "virsun", "desinen", "virsun", "kairen", "kairen", "virsun", "virsun", "desinen", "desinen", "kairen", "apacion", "kairen", "apacion", "desinen", "apacion", "apacion", "apacion", "apacion", "apacion", "apacion", "kairen", "kairen", "kairen", "kairen", "kairen", "kairen", "apacion", "apacion", "desinen", "virsun", "kairen", "virsun", "desinen", "desinen", "desinen", "desinen", "desinen", "apacion", "desinen", "virsun", "virsun", "virsun", "virsun", "virsun", "virsun", "virsun", "virsun", "desinen", "apacion", "kairen", "apacion", "apacion", "apacion", "apacion", "apacion", "apacion", "kairen", "kairen", "kairen", "kairen", "kairen", "apacion", "apacion", "apacion", "kairen", "kairen", "apacion", "kairen", "virsun", "virsun", "desinen", "desinen", "apacion", "desinen", "virsun", "virsun", "apacion", "kairen", "kairen", "kairen", "kairen", "virsun", "desinen", "desinen", "desinen", "virsun", "desinen", "desinen", "desinen", "desinen", "desinen", "apacion", "desinen", "virsun", "virsun", "virsun", "virsun", "virsun", "virsun", "virsun", "kairen", "virsun", "virsun", "desinen", "apacion", "kairen", "apacion", "desinen", "apacion", "apacion", "apacion", "apacion", "apacion", "apacion", "kairen", "kairen", "kairen", "kairen", "kairen", "kairen", "apacion", "apacion", "desinen", "virsun", "kairen", "virsun", "desinen", "desinen", "desinen", "desinen", "desinen", "apacion", "desinen", "virsun", "virsun", "virsun", "virsun", "virsun", "virsun", "virsun", "virsun", "virsun", "virsun", "apacion", "apacion", "apacion", "apacion", "apacion", "apacion", "apacion", "apacion", "apacion", "kairen", "kairen", "kairen", "kairen", "kairen", "kairen", "virsun", "virsun", "kairen", "virsun", "virsun", "kairen", "virsun", "virsun", "kairen", "kairen", "kairen", "apacion", "kairen", "kairen", "apacion", "apacion", "apacion", "apacion", "kairen", "apacion", "desinen", "desinen", "desinen", "desinen", "desinen", "virsun", "desinen", "apacion", "kairen", "apacion", "desinen", "desinen", "desinen", "apacion", "desinen", "virsun", "kairen", "virsun", "desinen", "desinen", "desinen", "desinen", "desinen", "apacion", "desinen", "virsun", "virsun", "virsun", "virsun", "virsun", "virsun", "virsun", "virsun", "virsun", "kairen", "virsun", "desinen", "desinen", "kairen", "kairen", "virsun", "desinen", "desinen", "apacion", "kairen", "apacion", "apacion", "apacion", "apacion", "apacion", "apacion", "apacion", "apacion", "apacion", "kairen", "kairen", "kairen", "kairen", "kairen", "apacion", "kairen", "kairen", "kairen", "virsun", "kairen", "kairen", "kairen", "kairen", "kairen", "virsun", "virsun", "virsun", "virsun", "virsun", "kairen", "virsun", "virsun", "desinen", "apacion", "apacion", "apacion", "apacion", "apacion", "apacion", "kairen", "apacion", "desinen", "desinen", "desinen", "desinen", "desinen", "virsun", "desinen", "apacion", "kairen", "apacion", "desinen", "desinen", "desinen", "apacion", "desinen", "virsun", "kairen", "virsun", "desinen", "desinen", "desinen", "desinen", "desinen", "apacion", "desinen", "virsun", "virsun", "virsun", "virsun", "virsun", "virsun", "virsun", "virsun", "virsun", "apacion", "apacion", "apacion", "apacion", "apacion", "apacion", "apacion", "apacion", "kairen", "kairen", "kairen", "kairen", "kairen", "kairen", "virsun", "virsun", "kairen", "virsun", "virsun", "kairen", "virsun", "virsun", "kairen", "kairen", "kairen", "kairen", "apacion", "kairen", "kairen", "virsun", "virsun", "desinen", "virsun", "desinen", "apacion", "kairen", "apacion", "apacion", "apacion", "apacion", "apacion", "apacion", "kairen", "apacion", "desinen", "desinen", "desinen", "desinen", "desinen", "virsun", "desinen", "apacion", "kairen", "apacion", "desinen", "desinen", "desinen", "apacion", "desinen", "virsun", "kairen", "virsun", "desinen", "desinen", "desinen", "desinen", "desinen", "apacion", "desinen", "virsun", "virsun", "virsun", "virsun", "virsun", "virsun", "virsun", "virsun", "desinen", "virsun", "virsun", "apacion", "kairen", "apacion", "apacion", "apacion", "apacion", "apacion", "apacion", "apacion", "apacion", "kairen", "kairen", "kairen", "kairen", "kairen", "kairen", "virsun", "virsun", "kairen", "virsun", "virsun", "kairen", "virsun", "virsun", "kairen", "kairen", "kairen", "kairen", "virsun", "virsun", "desinen", "apacion", "kairen", "kairen", "apacion", "apacion", "apacion", "apacion", "apacion", "apacion", "kairen", "apacion", "desinen", "desinen", "desinen", "desinen", "desinen", "virsun", "desinen", "apacion", "kairen", "apacion", "desinen", "desinen", "desinen", "apacion", "desinen", "virsun", "kairen", "virsun", "desinen", "desinen", "desinen", "desinen", "desinen", "apacion", "desinen", "virsun", "virsun", "virsun", "virsun", "virsun", "virsun", "virsun", "kairen", "virsun", "virsun", "virsun", "desinen", "apacion", "desinen", "apacion", "desinen", "desinen", "desinen", "kairen", "kairen", "kairen", "kairen", "apacion", "apacion", "apacion", "apacion", "apacion", "apacion", "apacion", "kairen", "kairen", "kairen", "kairen", "kairen", "kairen", "virsun", "virsun", "kairen", "virsun", "virsun", "kairen", "virsun", "virsun", "kairen", "kairen", "kairen", "kairen", "virsun", "kairen", "apacion", "apacion", "apacion", "apacion", "apacion", "apacion", "kairen", "apacion", "desinen", "desinen", "desinen", "desinen", "desinen", "virsun", "desinen", "apacion", "kairen", "apacion", "desinen", "desinen", "desinen", "apacion", "desinen", "virsun", "kairen", "virsun", "desinen", "desinen", "desinen", "desinen", "desinen", "apacion", "desinen", "virsun", "virsun", "virsun", "virsun", "virsun", "virsun", "virsun", "virsun", "apacion", "kairen", "virsun", "virsun", "desinen", "apacion", "apacion", "apacion", "apacion", "apacion", "apacion", "apacion", "apacion", "kairen", "kairen", "kairen", "kairen", "kairen", "kairen", "virsun", "virsun", "kairen", "virsun", "virsun", "virsun", "desinen", "desinen", "apacion", "kairen", "virsun", "virsun", "kairen", "kairen", "kairen", "kairen", "kairen", "kairen", "virsun", "kairen", "apacion", "apacion", "apacion", "apacion", "apacion", "apacion", "kairen", "apacion", "desinen", "desinen", "desinen", "desinen", "desinen", "virsun", "desinen", "apacion", "kairen", "apacion", "desinen", "desinen", "desinen", "apacion", "desinen", "virsun", "kairen", "virsun", "desinen", "desinen", "desinen", "desinen", "desinen", "apacion", "desinen", "virsun", "virsun", "virsun", "virsun", "virsun", "virsun", "virsun", "apacion", "apacion", "apacion", "desinen", "desinen", "desinen", "desinen", "apacion", "desinen", "virsun", "virsun", "kairen", "virsun", "virsun", "virsun", "kairen", "kairen", "kairen", "kairen", "apacion", "kairen", "virsun", "apacion", "desinen", "apacion", "apacion", "apacion", "apacion", "apacion", "apacion", "kairen", "kairen", "kairen", "kairen", "kairen", "kairen", "virsun", "virsun", "kairen", "virsun", "virsun", "virsun", "desinen", "virsun", "kairen", "kairen", "kairen", "kairen", "kairen", "kairen", "virsun", "kairen", "apacion", "apacion", "apacion", "apacion", "apacion", "apacion", "kairen", "apacion", "desinen", "desinen", "desinen", "desinen", "desinen", "virsun", "virsun", "kairen", "kairen", "kairen", "desinen", "desinen", "desinen", "apacion", "desinen", "apacion", "kairen", "apacion", "desinen", "desinen", "desinen", "apacion", "desinen", "virsun", "kairen", "virsun", "desinen", "desinen", "desinen", "desinen", "desinen", "apacion", "desinen", "virsun", "virsun", "virsun", "virsun", "virsun", "virsun", "virsun", "apacion", "apacion", "apacion", "desinen", "desinen", "desinen", "desinen", "virsun", "virsun", "virsun", "virsun", "kairen", "kairen", "kairen", "kairen", "desinen", "desinen", "desinen", "desinen", "apacion", "desinen", "desinen", "virsun", "kairen", "kairen", "kairen", "kairen", "desinen", "desinen", "apacion", "apacion", "apacion", "desinen", "virsun", "virsun", "desinen", "virsun", "kairen", "kairen", "kairen", "desinen", "apacion", "apacion", "apacion", "apacion", "kairen", "kairen", "kairen", "kairen", "apacion", "apacion", "apacion", "kairen", "kairen", "kairen", "kairen", "kairen", "kairen", "virsun", "virsun", "kairen", "virsun", "virsun", "virsun", "virsun", "desinen", "virsun", "virsun", "desinen", "desinen", "apacion", "kairen", "virsun", "kairen", "apacion", "virsun", "kairen", "apacion", "virsun", "kairen", "apacion", "desinen", "apacion", "kairen", "kairen", "kairen", "kairen", "kairen", "apacion", "kairen", "apacion", "apacion", "apacion", "apacion", "kairen", "apacion", "desinen", "desinen", "desinen", "desinen", "desinen", "virsun", "desinen", "apacion", "kairen", "apacion", "desinen", "desinen", "desinen", "apacion", "desinen", "virsun", "kairen", "virsun", "desinen", "desinen", "desinen", "desinen", "desinen", "apacion", "desinen", "virsun", "virsun", "virsun", "virsun", "virsun", "virsun", "virsun", "virsun", "virsun", "virsun", "virsun", "apacion", "apacion", "apacion", "apacion", "apacion", "apacion", "apacion", "apacion", "apacion", "apacion", "kairen", "kairen", "kairen", "kairen", "kairen", "kairen", "virsun", "virsun", "kairen", "virsun", "virsun", "desinen", "virsun", "desinen", "virsun", "kairen", "kairen", "kairen", "kairen", "kairen", "kairen", "apacion", "apacion", "kairen", "virsun", "kairen", "apacion", "apacion", "apacion", "apacion", "kairen", "apacion", "desinen", "desinen", "desinen", "desinen", "desinen", "virsun", "desinen", "apacion", "kairen", "apacion", "desinen", "desinen", "desinen", "apacion", "desinen", "virsun", "kairen", "virsun", "desinen", "desinen", "desinen", "desinen", "desinen", "apacion", "desinen", "virsun", "virsun", "virsun", "virsun", "virsun", "virsun", "virsun", "virsun", "virsun", "virsun", "apacion", "apacion", "apacion", "apacion", "apacion", "apacion", "apacion", "apacion", "apacion", "kairen", "kairen", "kairen", "kairen", "kairen", "kairen", "virsun", "virsun", "kairen", "virsun", "virsun", "virsun", "kairen", "virsun", "kairen", "kairen", "kairen", "virsun", "kairen", "apacion", "virsun", "kairen", "apacion", "apacion", "apacion", "apacion", "apacion", "apacion", "kairen", "apacion", "desinen", "desinen", "desinen", "desinen", "desinen", "virsun", "desinen", "apacion", "kairen", "apacion", "desinen", "desinen", "desinen", "apacion", "desinen", "virsun", "kairen", "virsun", "desinen", "desinen", "desinen", "desinen", "desinen", "apacion", "desinen", "virsun", "virsun", "virsun", "virsun", "virsun", "virsun", "virsun", "virsun", "virsun", "apacion", "apacion", "apacion", "apacion", "apacion", "apacion", "apacion", "apacion", "kairen", "kairen", "kairen", "kairen", "kairen", "kairen", "virsun", "virsun", "kairen", "virsun", "virsun", "virsun", "desinen", "virsun", "kairen", "kairen", "kairen", "kairen", "kairen", "kairen", "virsun", "kairen", "apacion", "apacion", "apacion", "apacion", "apacion", "apacion", "kairen", "apacion", "desinen", "desinen", "desinen", "desinen", "desinen", "virsun", "desinen", "apacion", "kairen", "apacion", "desinen", "desinen", "desinen", "apacion", "desinen", "virsun", "kairen", "virsun", "desinen", "desinen", "desinen", "desinen", "desinen", "apacion", "desinen", "virsun", "virsun", "virsun", "virsun", "virsun", "virsun", "virsun", "virsun", "apacion", "apacion", "apacion", "apacion", "apacion", "apacion", "apacion", "kairen", "kairen", "kairen", "kairen", "kairen", "kairen", "virsun", "virsun", "kairen", "kairen", "kairen", "kairen", "kairen", "kairen", "desinen", "desinen", "desinen", "desinen", "desinen", "virsun", "virsun", "virsun", "virsun", "virsun", "virsun", "desinen", "desinen", "desinen", "apacion", "kairen", "kairen", "virsun", "kairen", "apacion", "kairen", "apacion", "kairen", "kairen", "kairen", "apacion", "kairen", "apacion", "kairen", "apacion", "apacion", "apacion", "kairen", "apacion", "desinen", "desinen", "desinen", "desinen", "desinen", "virsun", "desinen", "apacion", "kairen", "apacion", "desinen", "desinen", "desinen", "apacion", "desinen", "virsun", "kairen", "virsun", "desinen", "desinen", "desinen", "desinen", "desinen", "apacion", "desinen", "virsun", "virsun", "virsun", "virsun", "virsun", "virsun", "virsun", "apacion", "apacion", "apacion", "apacion", "apacion", "apacion", "kairen", "kairen", "kairen", "kairen", "kairen", "kairen", "virsun", "virsun", "kairen", "virsun", "virsun", "virsun", "kairen", "virsun", "kairen", "kairen", "kairen", "kairen", "kairen", "apacion", "apacion", "apacion", "apacion", "apacion", "kairen", "apacion", "desinen", "desinen", "desinen", "desinen", "desinen", "virsun", "desinen", "apacion", "kairen", "apacion", "desinen", "desinen", "desinen", "apacion", "desinen", "virsun", "kairen", "virsun", "desinen", "desinen", "desinen", "desinen", "desinen", "apacion", "desinen", "virsun", "virsun", "virsun", "virsun", "virsun", "virsun", "apacion", "apacion", "apacion", "apacion", "apacion", "kairen", "kairen", "kairen", "kairen", "kairen", "kairen", "virsun", "virsun", "kairen", "virsun", "virsun", "virsun", "desinen", "virsun", "kairen", "kairen", "kairen", "kairen", "kairen", "kairen", "virsun", "kairen", "apacion", "apacion", "apacion", "apacion", "apacion", "apacion", "kairen", "apacion", "desinen", "desinen", "desinen", "desinen", "desinen", "virsun", "desinen", "apacion", "kairen", "apacion", "desinen", "desinen", "desinen", "apacion", "desinen", "virsun", "kairen", "virsun", "desinen", "desinen", "desinen", "desinen", "desinen", "apacion", "desinen", "virsun", "virsun", "virsun", "virsun", "virsun", "apacion", "kairen", "kairen", "kairen", "virsun", "virsun", "virsun", "desinen"};
                                List<String> n = new ArrayList<String>(Arrays.asList(com));

                                new Atpazink(n, kintamieji);
                            }
                        }
                        break;
                    case "lygis":

                        needelse=false;
                        s = commands.get(i);
                        s = s.replaceAll("[A-Za-z() ]*", "");
                        if(Integer.parseInt(s) >= 0 && Integer.parseInt(s) <=4){
                            Hud.level = Integer.parseInt(s);
                            hud.setDeaths(-1);
                            reset = true;
                        }
                        break;
                    case "isnaujo":
                        needelse=false;
                        reset = true;
                        break;
                    case "kairen":
                        needelse=false;
                        new judek().VYK(player, ps, -1, 0);
                        break;
                    case "desinen":
                        needelse=false;
                        new judek().VYK(player, ps, 1, 0);
                        break;
                    case "virsun":
                        needelse=false;
                        new judek().VYK(player, ps, 0, 1);
                        break;
                    case "apacion": needelse=false;
                        new judek().VYK(player, ps, 0, -1);
                        break;
                    case "kartok": needelse=false;
                        //System.out.println("KARTOK");
                        if(!commands.get(i+1).equals("{")){

                        }else {
                            makeNew(i+1);
                            s = commands.get(i);
                            s=s.substring(s.indexOf('(')+1, s.lastIndexOf(')'));

                            //System.out.println(s);

                            sizeofkintamieji = kintamieji.size();

                            e = new Expression(s);
                            //System.out.println(e);

                            for(Pair<String, Pair<String, String>> p: kintamieji){
                                BigDecimal bd = new BigDecimal(p.getValue().getValue());
                                e.with(p.getValue().getKey(), bd);
                            }

                            //System.out.println("EVAL:"+e);
                            s = e.eval().toString();
                            BigDecimal bd = new BigDecimal(s);
                            for (int z = 0; z < bd.intValue(); z++) {
                                new Atpazink(another, kintamieji);
                                while(kintamieji.size()>sizeofkintamieji){
                                    kintamieji.remove(kintamieji.size()-1);
                                }
                            }
                            //System.out.println("JUMP");
                            i = ei;
//
                        }
                        break;
                    default:
                        if(commands.get(i).indexOf('=')!=-1){
                            s=commands.get(i);
                            s=commands.get(i).substring(0,s.indexOf('='));
                            s=s.replaceAll(" *", "");
                           // //System.out.println("DEFAULT: " + commands.get(i) + "  "+ s);
                            int ind=0;
                            for(Pair<String, Pair<String, String>> p: kintamieji){
                                if(p.getValue().getKey().equals(s)){
                                    s=commands.get(i);
                                    s=s.substring(s.indexOf('=')+1);
                                    e = new Expression(s);
                                  //  //System.out.println(e);
                                    for(Pair<String, Pair<String, String>> pz: kintamieji){
                                        BigDecimal bd = new BigDecimal(pz.getValue().getValue());
                                        e.with(pz.getValue().getKey(), bd);
                                    }
                                    s=e.eval().toString();
                                    kintamieji.set(ind,new Pair(p.getKey(), new Pair(p.getValue().getKey(), s)));
                                 //   //System.out.println("KEISTAS: " + kintamieji.get(ind));
                                    break;
                                }
                                ind++;
                            }
                        }
                        break;
                }
            }else{
                i--;
            }


        }

    }

    private void makeNew(int i){
        another = new ArrayList<>();
        si=i+1;

            int kartok = 0;
            for(int a = si; a<commands.size(); a++){
                if(kartok == 0 && commands.get(a) == "}"){
                    ei=a;
                }else if(commands.get(a).equals("{")){
                    kartok++;
                }else if(commands.get(a).equals("}")){
                    kartok--;
                }
            }
            for(int a = si; a<ei; a++){
                another.add(commands.get(a));
              //  //System.out.println(a);
            }
           // //System.out.println("STRING");
          //  for(String s: another){
           //     //System.out.println(s);
           // }
          //  //System.out.println("END");


    }

    private void kintamasis(String tipas, int i){
        String s = commands.get(i);
        s=s.substring(s.indexOf(' '));
        String value = new String("0");
        if (s.indexOf('=') != -1) {
            value = s.substring(s.indexOf('=') + 1);
            s = s.substring(0, s.indexOf('='));
        }

        if(value.length()<1){
            //System.out.println("No value");
            return;
        }

        s = s.replaceAll(" *","");
        value = value.replaceAll(" *", "");
        //System.out.println("KINTAMASIS");
        //System.out.println(s + "   " + s.length());

        for(Pair<String, Pair<String, String>> p: kintamieji){
            if(p.getValue().getKey().equals(s)){
                //System.out.println("Already exists");
                return;
            }
        }

        Expression e = new Expression(value);
        //System.out.println(e);

        for(Pair<String, Pair<String, String>> p: kintamieji){
            BigDecimal bd = new BigDecimal(p.getValue().getValue());
            e.with(p.getValue().getKey(), bd);
        }

        value = e.eval().toString();
        //System.out.println(value);
        try {
            if (tipas.equals("int")) {
                value = Integer.toString(Integer.parseInt(value));
            } else if (tipas.equals("byte")) {
                value = Byte.toString(Byte.parseByte(value));
            } else if (tipas.equals("short") || tipas.equals("char")) {
                value = Short.toString(Short.parseShort(value));
            } else if (tipas.equals("boolean")) {
                if (!value.equals("1") && !value.equals("0")) {
                    throw new NumberFormatException();
                    //value = "0";
                  //  //System.out.println("BAD BOOLEAN");
                }
            } else if (tipas.equals("long")) {
                value = Long.toString(Long.parseLong(value));
            } else if (tipas.equals("float")) {
                value = Float.toString(Float.parseFloat(value));
            } else if (tipas.equals("double")) {
                value = Double.toString(Double.parseDouble(value));
            }
        }catch (NumberFormatException exc){
            exc.printStackTrace();
        }


        kintamieji.add(new Pair(tipas, new Pair(s, value)));
//        for(Pair<String, Pair<String, String>> p: kintamieji){
//            //System.out.println(p.getKey() + " " + p.getValue().getKey() + " " + p.getValue().getValue());
//        }

    }
    public static void player(Character pl, PlayScreen pS, MyGame gam, Hud hu){
        player = pl;
        ps = pS;
        game = gam;
        hud = hu;
    }


}
