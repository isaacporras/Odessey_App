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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.TransformerException;
import java.nio.file.Files;
 import java.nio.file.Paths;
 import java.nio.file.Path;
 import javazoom.jl.player.Player;

         import java.nio.ShortBuffer;
 import java.nio.charset.StandardCharsets;
 import java.util.Base64;
 import java.util.concurrent.TimeUnit;
 import javax.xml.transform.stream.StreamResult;
 import javax.xml.transform.Transformer;
 import javax.xml.transform.TransformerFactory;
 import javax.xml.transform.dom.DOMSource;
 import javax.xml.transform.OutputKeys;
 import javazoom.jl.player.Player;

/**
 * Clase que se encarga de la coneccion con servidor
 */
public class OdesseyClient  implements Runnable {
    public static int counter = 0;
    public static boolean paused = false;
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

//            String modifiedSentence = null;
//            boolean allPlaylistCharged = true;
//            while (true) {
//                BufferedReader inFromServer = new BufferedReader(new InputStreamReader(Clientsocket.getInputStream()));
//                modifiedSentence = inFromServer.readLine();
//                System.out.println("Playlist: " + modifiedSentence);
//                if (modifiedSentence.equals("finished")) {
//                    System.out.println("Ya no hay mas playlist");
//                    return;
//                } else {
////                    HomePage.addTreeItem(modifiedSentence);
//                }
//            }

//        **************//


        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void startClient() throws IOException {
        Clientsocket = new Socket("localhost", 1421);
    }

    /**
     * Crea un nuevo playlist (carpeta en el servidor)
     *
     * @param playlistName nombre del playlist
     */
    public static void AddPlaylist(String playlistName) {

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
     *
     * @param username        Nombre de usuario
     * @param name            Nombre
     * @param age             Edad
     * @param password        contraseña
     * @param rock_selected   Genero favorito rock
     * @param pop_selected    Genero favorito pop
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
        if (rock_selected) {
            GeneroFavXML.appendChild(Registrarse_doc.createTextNode("Rock"));
        } else if (pop_selected) {
            GeneroFavXML.appendChild(Registrarse_doc.createTextNode("Pop"));
        } else if (reggae_selected) {
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

            if (se_encontro.equals("true")) {
                System.out.println("No puede continuar");

                return false;
            } else if (se_encontro.equals("false")) {
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

    public static void Play_Song(String playlist, String song, String chunk){
        //Manda el XML con el nombre de la cancion a reproducir al servidor //

        try {
            /**
             * Hay que definir una estructura XML con un ID de operación '13' para la reproducción de MP3.
             * También debe de contener un parámetro como contador, para ir "seleccionando" los chunks de bytes
             * necesarios.
             */
            String xml = makeXML_for_Reproduction(playlist,song,chunk);
            DataOutputStream outToServer = new DataOutputStream(Clientsocket.getOutputStream());
            outToServer.writeBytes(xml + '\n');
        }
        catch (Exception e){
            System.out.println(e);
        }

        new Thread() {

            int counter = 0;

            public void run() {
                try {

                    while(paused == true){
                        Thread.sleep(1000);
                        //System.out.println("Se detuvo el thread por al menos 8 segundos");
                    }

                    /**
                     * Hay que definir la lectura del XML para la reproducción de los bytes. Finalmente, los chunks
                     * de bytes serán almacenados como string en 'modifiedSentence'.
                     */

                    String modifiedSentence;

                    BufferedReader inFromServer = new BufferedReader(new InputStreamReader(Clientsocket.getInputStream()));
                    modifiedSentence = inFromServer.readLine();
                    System.out.println("Chunk del MP3 de C-Server: " + modifiedSentence);

                    byte[] bytes =  Base64.getDecoder().decode(modifiedSentence.getBytes(StandardCharsets.UTF_8));
                    System.out.println(bytes);
                    MP3 mp3 = new MP3(bytes);
                    mp3.play();
                    TimeUnit.SECONDS.sleep(6);

                    counter = counter + 1 ;

                    /**
                     * La siguiente instrucción representa el nuevo XML a enviar con el contador aumentado en 1, que
                     * hará que el servidor envíe el siguiente chunk del MP3, y la llamada a run() hará que esto se
                     * ejecute hasta terminar la canción.
                     *
                     * RECORDAR!!!: Añadir excepción cuando toda la canción se ha reproducido.
                     */
                    String newXml = makeXML_for_Reproduction(playlist,song,String.valueOf(counter));
                    DataOutputStream outToServer = new DataOutputStream(Clientsocket.getOutputStream());
                    outToServer.writeBytes(newXml + '\n');

                    run();

                } catch (Exception e) {
                    System.out.println(e);
                }
            }
        }.start();

    }
    public static String makeXML_for_Reproduction(String playlist, String song, String chunk){

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
        Document Reproducir_doc = dBuilder.newDocument();
        //
        //Crea el elemento principal del XML
        Element operation = Reproducir_doc.createElement("OperationCode");
        Reproducir_doc.appendChild(operation);
        //
        //Le anade un atributo al operation code(3-> Song sending to server)//
        Attr attr = Reproducir_doc.createAttribute("ID");
        attr.setValue("13");
        operation.setAttributeNode(attr);
        //Anade el Playlist donde se contiene la cancion que se quiere reproducir//
        Element Playlist = Reproducir_doc.createElement("Playlist");
        operation.appendChild(Playlist);
        Playlist.appendChild(Reproducir_doc.createTextNode(playlist));

        //Anade el nombre la cancion que se quiere reproducir//
        Element cancion = Reproducir_doc.createElement("Cancion");
        operation.appendChild(cancion);
        cancion.appendChild(Reproducir_doc.createTextNode(song));

        Element Chunk = Reproducir_doc.createElement("Chunk");
        operation.appendChild(Chunk);
        Chunk.appendChild(Reproducir_doc.createTextNode(chunk));

        System.out.println("La cancion que se quiere reproducir va en:" + convertDocumentToString(Reproducir_doc));
        return convertDocumentToString(Reproducir_doc);
    }

    public static void Send_Song_to_Server(String XML) {
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
        try {
            builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new InputSource(new StringReader(xmlStr)));
            return doc;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<String> getPlaylist() {

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
        Document PLaylisXML_DOC = dBuilder.newDocument();
        //

        //Crea el elemento principal del XML
        Element operation = PLaylisXML_DOC.createElement("OperationCode");
        PLaylisXML_DOC.appendChild(operation);
        //
        //Le anade un atributo al operation code(1-> registrarse)//
        Attr attr = PLaylisXML_DOC.createAttribute("ID");
        attr.setValue("5");
        operation.setAttributeNode(attr);
        //Manda el XML con la informacion de registro al servidor //
        try {
            DataOutputStream outToServer = new DataOutputStream(Clientsocket.getOutputStream());
            outToServer.writeBytes(convertDocumentToString(PLaylisXML_DOC) + '\n');
        } catch (IOException e) {
            e.printStackTrace();
        }
        //Termina de mandarlo al servidor

        //Espera la respuesta de las playlist //


        List<String> playlistNames = new ArrayList<String>();


        boolean allPlaylistCharged = true;

        while (allPlaylistCharged) {

            try {
                //Inicia el socket //

                //Espera la respuesta del servidor a ver si el usuario ya esta registrado //

                String modifiedSentence = null;
                allPlaylistCharged = true;
                while (true) {
                    BufferedReader inFromServer = new BufferedReader(new InputStreamReader(Clientsocket.getInputStream()));
                    modifiedSentence = inFromServer.readLine();
                    System.out.println("Playlist: " + modifiedSentence);
                    if (!modifiedSentence.equals("finished") && !modifiedSentence.equals("..")) {
                        playlistNames.add(modifiedSentence);


                    } else if (modifiedSentence.equals("finished")) {
                        System.out.println("Ya no hay mas playlist");
                        return playlistNames;
                    }
//                    else{
////                    HomePage.addTreeItem(modifiedSentence);
//                    }
                }

//        **************//


            } catch (IOException e) {
                e.printStackTrace();
            }
//            if (modifiedSentence.equals("finished")) {
//                System.out.println("Ya no hay mas playlist");
//                return playlistNames;
//
//            }
//            else {
////                if(!modifiedSentence.equals( ".."))
//
//                playlistNames.add(modifiedSentence);
//                System.out.println("inserto en el array");
//
//            }
        }

//        ******************************** //
        return playlistNames;

    }
    public static List<String> getMetadataList(String songname) {

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
        Document Metadata_XML_DOC = dBuilder.newDocument();
        //

        //Crea el elemento principal del XML
        Element operation = Metadata_XML_DOC.createElement("OperationCode");
        Metadata_XML_DOC.appendChild(operation);
        //
        //Le anade un atributo al operation code(6-> get songs names)//
        Attr attr = Metadata_XML_DOC.createAttribute("ID");
        attr.setValue("9");
        operation.setAttributeNode(attr);
        //Anade el nombre de la cancion//
        Element Nombre = Metadata_XML_DOC.createElement("Nombre");
        operation.appendChild(Nombre);
        System.out.println("The metadata song is :"+ songname);
        Nombre.appendChild(Metadata_XML_DOC.createTextNode(songname));
        //Manda el XML con la informacion de registro al servidor //
        try {
            DataOutputStream outToServer = new DataOutputStream(Clientsocket.getOutputStream());
            outToServer.writeBytes(convertDocumentToString(Metadata_XML_DOC) + '\n');
        } catch (IOException e) {
            e.printStackTrace();
        }

        List<String>  Metadata = new ArrayList<String>();
        boolean finished = true;
        System.out.println("SE EMPIEZA LLOS RESULTADOS DE LA BUSQUEDA EN PROFUNDIDAD");
        boolean allPlaylistCharged = true;

        while (allPlaylistCharged) {

            try {
                //Inicia el socket //

                //Espera la respuesta del servidor a ver si el usuario ya esta registrado //

                String modifiedSentence = null;
                allPlaylistCharged = true;
                while (true) {
                    BufferedReader inFromServer = new BufferedReader(new InputStreamReader(Clientsocket.getInputStream()));
                    modifiedSentence = inFromServer.readLine();
                    System.out.println("Metadata: " + modifiedSentence);
                    if (!modifiedSentence.equals("finished") && !modifiedSentence.equals("..")) {
                        Metadata.add(modifiedSentence);


                    } else if (modifiedSentence.equals("finished")) {
                        System.out.println("Ya no hay mas metadata");
                        return Metadata;
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return Metadata;
    }
    public static List<String> getSonglist() {

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
        Document SonglisXML_DOC = dBuilder.newDocument();
        //

        //Crea el elemento principal del XML
        Element operation = SonglisXML_DOC.createElement("OperationCode");
        SonglisXML_DOC.appendChild(operation);
        //
        //Le anade un atributo al operation code(6-> get songs names)//
        Attr attr = SonglisXML_DOC.createAttribute("ID");
        attr.setValue("6");
        operation.setAttributeNode(attr);
        //Manda el XML con la informacion de registro al servidor //
        try {
            DataOutputStream outToServer = new DataOutputStream(Clientsocket.getOutputStream());
            outToServer.writeBytes(convertDocumentToString(SonglisXML_DOC) + '\n');
        } catch (IOException e) {
            e.printStackTrace();
        }

        List<String> SongNames = new ArrayList<String>();
        boolean finished = true;
        System.out.println("SE EMPIEZA LLOS RESULTADOS DE LA BUSQUEDA EN PROFUNDIDAD");
        boolean allPlaylistCharged = true;

        while (allPlaylistCharged) {

            try {
                //Inicia el socket //

                //Espera la respuesta del servidor a ver si el usuario ya esta registrado //

                String modifiedSentence = null;
                allPlaylistCharged = true;
                while (true) {
                    BufferedReader inFromServer = new BufferedReader(new InputStreamReader(Clientsocket.getInputStream()));
                    modifiedSentence = inFromServer.readLine();
                    System.out.println("TreeView Structure: " + modifiedSentence);
                    if (!modifiedSentence.equals("finished") && !modifiedSentence.equals("..")) {
                        SongNames.add(modifiedSentence);


                    } else if (modifiedSentence.equals("finished")) {
                        System.out.println("Ya no hay mas playlist");
                        return SongNames;
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return SongNames;
    }
    public static void chargeUsers(){
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
        Document Charge_Users_Doc = dBuilder.newDocument();
        //
        //Crea el elemento principal del XML
        Element operation = Charge_Users_Doc.createElement("OperationCode");
        Charge_Users_Doc.appendChild(operation);
        //
        //Le anade un atributo al operation code(3-> Song sending to server)//
        Attr attr = Charge_Users_Doc.createAttribute("ID");
        attr.setValue("7");
        operation.setAttributeNode(attr);

        //Manda el XML con la informacion de registro al servidor //
        try {
            DataOutputStream outToServer = new DataOutputStream(Clientsocket.getOutputStream());
            outToServer.writeBytes(convertDocumentToString(Charge_Users_Doc) + '\n');
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public static void chargeSongsName(){
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
        Document Charge_Users_Doc = dBuilder.newDocument();
        //
        //Crea el elemento principal del XML
        Element operation = Charge_Users_Doc.createElement("OperationCode");
        Charge_Users_Doc.appendChild(operation);
        //
        //Le anade un atributo al operation code(3-> Song sending to server)//
        Attr attr = Charge_Users_Doc.createAttribute("ID");
        attr.setValue("8");
        operation.setAttributeNode(attr);

        //Manda el XML con la informacion de registro al servidor //
        try {
            DataOutputStream outToServer = new DataOutputStream(Clientsocket.getOutputStream());
            outToServer.writeBytes(convertDocumentToString(Charge_Users_Doc) + '\n');
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}