/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package battleship;

/**
 *
 * @author Deian
 */
import java.io.*;
import java.net.*;

class ClientHandling{
    private PrintWriter pW[];
    public ClientHandling(){
        pW = new PrintWriter[0];
    }
    public synchronized void addC(PrintWriter p){
        PrintWriter buf[]=new PrintWriter[pW.length+1];
        System.arraycopy(pW,0,buf,0,pW.length);
        buf[buf.length-1]=p;
        pW=buf;
    }
    public synchronized void rmvC(PrintWriter p){
        for(int i=0;i<pW.length;i++){
            if(pW[i]==p){
                pW[i]=pW[pW.length-1];
                break;
            }
        }
        PrintWriter buf[]=new PrintWriter[pW.length-1];
        System.arraycopy(pW,0,buf,0,pW.length-1);
        pW=buf;
    }
    public synchronized void sendC(String s){
        for(int i=0;i<pW.length;i++){
            pW[i].println(s);
        }
        
    }        
}