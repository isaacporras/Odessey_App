package SocketClient;

import java.io.*;
import javazoom.jl.player.Player;

class MP3 {
    private String filename;
    private Player player;
    private byte[] bytesArray;

    public MP3(byte[] bytesArray){

        this.bytesArray = bytesArray;
        this.filename = filename;

    }

    public void close() {
        if (player != null) player.close();
    }

    public boolean isfinish() {
        if(player.isComplete())
            return true;
        else
            return false;
    }

    public void play() {
        try {
            ByteArrayInputStream data = new ByteArrayInputStream(bytesArray);
            BufferedInputStream bis = new BufferedInputStream(data);
            player = new Player(bis);
        } catch (Exception e) {
            System.out.println("Problem playing file " + filename);
            System.out.println(e);
        }

        new Thread() {
            public void run() {
                try {
                    player.play();
                } catch (Exception e) {
                    System.out.println(e);
                }
            }
        }.start();

    }
}