package SocketClient;

import java.io.*;
import java.net.*;

import Interface.HomePage;
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
            //Espera la respuesta del servidor a ver si el usuario ya esta registrado //

            String modifiedSentence = null;
            boolean allPlaylistCharged = true;
            while(true){
                BufferedReader inFromServer = new BufferedReader(new InputStreamReader(Clientsocket.getInputStream()));
                modifiedSentence = inFromServer.readLine();
                System.out.println("Playlist: " + modifiedSentence);
                if(modifiedSentence.equals("No_more_Playlist")){
                    System.out.println("Ya no hay mas playlist");
                    return;
                }
                else{
//                    HomePage.addTreeItem(modifiedSentence);
                }
            }

//        **************//


        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public void startClient() throws IOException {
        Clientsocket = new Socket("localhost", 1396);
    }

    /**
     * Crea un nuevo playlist (carpeta en el servidor)
     * @param playlistName nombre del playlist
     */
    public static void AddPlaylist(String playlistName){
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = null;

        try {
            dBuilder = dbFactory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            System.out.println("NO SE CREO EL DOCUMENTO");
            e.printStackTrace();
        }
        //Instancia el documento
        Document NewPlaylist = dBuilder.newDocument();
        //
        //Crea el elemento principal del XML
        Element operation = NewPlaylist.createElement("OperationCode");
        NewPlaylist.appendChild(operation);
        //
        //Le anade un atributo al operation code(1-> registrarse)//
        Attr attr = NewPlaylist.createAttribute("ID");
        attr.setValue("4");
        operation.setAttributeNode(attr);
        //Anade el playlistname al xml//
        Element playlistnameElement = NewPlaylist.createElement("PlayList");
        playlistnameElement.appendChild(NewPlaylist.createTextNode(playlistName));
        operation.appendChild(playlistnameElement);

        //Manda el XML con la informacion de registro al servidor //
        try {
            DataOutputStream outToServer = new DataOutputStream(Clientsocket.getOutputStream());
            outToServer.writeBytes(convertDocumentToString(NewPlaylist) + '\n');
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Termina de mandarlo al servidor


    }

    public static boolean LogIn_Usuario(String username, String password) {

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
        Document Log_In_doc = dBuilder.newDocument();
        //
        //Crea el elemento principal del XML
        Element operation = Log_In_doc.createElement("OperationCode");
        Log_In_doc.appendChild(operation);
        //
        //Le anade un atributo al operation code(1-> registrarse)//
        Attr attr = Log_In_doc.createAttribute("ID");
        attr.setValue("2");
        operation.setAttributeNode(attr);
        //Anade el username al xml//
        Element usernameXML = Log_In_doc.createElement("UserName");
        operation.appendChild(usernameXML);
        usernameXML.appendChild(Log_In_doc.createTextNode(username));
        //Anade el username al password//
        Element passwordXML = Log_In_doc.createElement("Password");
        operation.appendChild(passwordXML);
        passwordXML.appendChild(Log_In_doc.createTextNode(password));

        //Manda el XML con la informacion de registro al servidor //
        try {
            DataOutputStream outToServer = new DataOutputStream(Clientsocket.getOutputStream());
            outToServer.writeBytes(convertDocumentToString(Log_In_doc) + '\n');
        } catch (IOException e) {
            e.printStackTrace();
        }
        //Termina de mandarlo al servidor


        //Espera la respuesta del servidor a ver si el usuario ya esta registrado //




        try {
            BufferedReader inFromServer = new BufferedReader(new InputStreamReader(Clientsocket.getInputStream()));

            String modifiedSentence = inFromServer.readLine();
            System.out.println("Respuesta de si esta logeado: " + modifiedSentence);

            String se_encontro = modifiedSentence;

            //Si se encontro se sale y no cambia a la pantalla main//

            if (se_encontro.equals("true")) {
                System.out.println("Puede continuar");

                return true;
            } else if (se_encontro.equals("false")) {
                System.out.println("No puede continuar");

                return false;
            }
            return false;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
    /**
     * Registra el usuario en el servidor y regresa un true o false si el usario esta o no registrado.
     * @param username Nombre de usuario
     * @param name Nombre
     * @param age Edad
     * @param password contraseña
     * @param rock_selected Genero favorito rock
     * @param pop_selected Genero favorito pop
     * @param reggae_selected Genero favorito reggae
     * @return Regresa true si puede continuar y false si no puede.
     */
    public static boolean Registrar_Usario(String username, String name, String age, String password, boolean rock_selected, boolean pop_selected, boolean reggae_selected) {
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






        //Espera la respuesta del servidor a ver si el usuario ya esta registrado //

        String modifiedSentence = null;


        try {
            BufferedReader inFromServer = new BufferedReader(new InputStreamReader(Clientsocket.getInputStream()));

            modifiedSentence = inFromServer.readLine();
            System.out.println("Respuesta de buscado: " + modifiedSentence);


            String se_encontro = modifiedSentence;

            //Si se encontro se sale y no cambia a la pantalla main//

            if(se_encontro.equals("true")){
                System.out.println("No puede continuar");

                return false;
            }
            else if(se_encontro.equals("false")){
                System.out.println("Puede continuar");
                return true;
            }
            return false;



        } catch (IOException e) {
            e.printStackTrace();
        }


//        **************//



        return false;
    }
    public static void Send_Song_to_Server(String XML){
        //Manda el XML con la informacion de la cancion al servidor //
        try {
            DataOutputStream outToServer = new DataOutputStream(Clientsocket.getOutputStream());
            outToServer.writeBytes(XML + '\n');
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
