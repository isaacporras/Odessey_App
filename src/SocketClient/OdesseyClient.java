package SocketClient;

import java.io.*;
import java.net.*;

import Interface.Main;
import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;
import org.w3c.dom.*;
import org.xml.sax.*;
import javax.xml.parsers.*;
import javax.xml.xpath.*;
import java.io.*;
import java.util.Scanner;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.OutputKeys;


public class OdesseyClient  implements Runnable{
    public static Socket Clientsocket;
    static DataInput dataInput;
    static DataOutputStream dataOutputStream;
    public static OdesseyClient instance = null;

    public static OdesseyClient getInstance() {
        if (instance == null) {
            instance = new OdesseyClient();

            return instance;
        }
        else{
            return instance;
        }
    }
    @Override
    public void run() {
        try {
        //Inicia el socket //
            startClient();
            //Empieza a escuchar //
            StartListening();
        } catch (IOException e) {
            e.printStackTrace();
        }

//        int  i = 0 ;
//        while(true){
//            System.out.println( i );
//            i =  i + 1 ;
//        }
        // must run socket client stuff here //

    }
    public void startClient() throws IOException{
        Clientsocket = new Socket("localhost",7373);
    }
    public void StartListening()throws IOException {
        String sentence;
        String modifiedSentence;
        DataOutputStream outToServer;
        BufferedReader inFromUser;
        while (true) {

            System.out.println("Nombre de la Cancion:");


            inFromUser = new BufferedReader(new InputStreamReader(System.in));
            outToServer = new DataOutputStream(Clientsocket.getOutputStream());

            if (inFromUser.toString().equals("Close")) {
                Clientsocket.close();
                return;
            }

            sentence = inFromUser.readLine();

            System.out.println("SENDING... " + sentence);
            outToServer.writeBytes(sentence + '\n');


            BufferedReader inFromServer = new BufferedReader(new InputStreamReader(Clientsocket.getInputStream()));
            modifiedSentence = inFromServer.readLine();
            System.out.println("FROM SERVER: " + modifiedSentence);

        }
    }
}



