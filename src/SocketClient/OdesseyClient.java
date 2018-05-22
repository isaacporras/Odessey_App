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
import javax.xml.transform.TransformerException;


public class OdesseyClient  implements Runnable {
    public static Socket Clientsocket;
    static DataInput dataInput;
    static DataOutputStream dataOutputStream;
    public static OdesseyClient instance = null;

    public static OdesseyClient getInstance() {
        if (instance == null) {
            instance = new OdesseyClient();

            return instance;
        } else {
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

    public void startClient() throws IOException {
        Clientsocket = new Socket("localhost", 1829);
    }


    public void StartListening() throws IOException {
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
            outToServer.writeBytes(sentence + '\n');


            BufferedReader inFromServer = new BufferedReader(new InputStreamReader(Clientsocket.getInputStream()));
            modifiedSentence = inFromServer.readLine();
            System.out.println("FROM SERVER: " + modifiedSentence);

        }
    }

    public static void Registrar_Usario(String username, String name, String age, String password, boolean rock_selected, boolean pop_selected, boolean reggae_selected) {
        //Crea el XML para registrar con el operation code respectivo (1)//

        //Crea el documento XML//
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = null;
        //
        try {
            dBuilder = dbFactory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            System.out.println("NO SE CREO EL DOCUMENTO");
            e.printStackTrace();
        }
        //Instancia el documento
        Document Registrarse_doc = dBuilder.newDocument();
        //
        //Crea el elemento principal del XML
        Element operation = Registrarse_doc.createElement("OperationCode");
        Registrarse_doc.appendChild(operation);
        //
        //Le anade un atributo al operation code(1-> registrarse)//
        Attr attr = Registrarse_doc.createAttribute("ID");
        attr.setValue("1");
        operation.setAttributeNode(attr);
        //Anade el username al xml//
        Element usernameXML = Registrarse_doc.createElement("UserName");
        operation.appendChild(usernameXML);
        usernameXML.appendChild(Registrarse_doc.createTextNode(username));
        //Anade el name al xml//
        Element nameXML = Registrarse_doc.createElement("Name");
        operation.appendChild(nameXML);
        nameXML.appendChild(Registrarse_doc.createTextNode(name));
        //Anade el username al age//
        Element AgeXML = Registrarse_doc.createElement("Age");
        operation.appendChild(AgeXML);
        AgeXML.appendChild(Registrarse_doc.createTextNode(age));
        //Anade el username al password//
        Element passwordXML = Registrarse_doc.createElement("Password");
        operation.appendChild(passwordXML);
        passwordXML.appendChild(Registrarse_doc.createTextNode(password));
        //Anade el genero musical favorito
        Element GeneroFavXML = Registrarse_doc.createElement("GeneroFavorito");
        operation.appendChild(GeneroFavXML);
        if(rock_selected){
            GeneroFavXML.appendChild(Registrarse_doc.createTextNode("Rock"));
        }
        else if(pop_selected){
            GeneroFavXML.appendChild(Registrarse_doc.createTextNode("Pop"));
        }
        else if(reggae_selected){
            GeneroFavXML.appendChild(Registrarse_doc.createTextNode("Reggae"));
        }






        //Manda el XML con la informacion de registro al servidor //
        try {
            DataOutputStream outToServer = new DataOutputStream(Clientsocket.getOutputStream());
            outToServer.writeBytes(convertDocumentToString(Registrarse_doc) + '\n');
        } catch (IOException e) {
            e.printStackTrace();
        }
        //Termina de mandarlo al servidor



    }
    private static String convertDocumentToString(Document doc) {
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer;
        try {
            transformer = tf.newTransformer();
            // below code to remove XML declaration
            // transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            StringWriter writer = new StringWriter();
            transformer.transform(new DOMSource(doc), new StreamResult(writer));
            String output = writer.getBuffer().toString();
            return output;
        } catch (TransformerException e) {
            e.printStackTrace();
        }

        return null;
    }
    private static Document convertStringToDocument(String xmlStr) {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        try
        {
            builder = factory.newDocumentBuilder();
            Document doc = builder.parse( new InputSource( new StringReader( xmlStr ) ) );
            return doc;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}