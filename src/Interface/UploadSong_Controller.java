package Interface;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.control.TextField;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.StringWriter;
import SocketClient.OdesseyClient;


public class UploadSong_Controller {


    @FXML
    private TextField Album_TextField;

    @FXML
    private TextField Artista_TextField;

    @FXML
    private TextArea Lyrics_TextField;

    @FXML
    private TextField Genero_TextField;

    @FXML
    private TextField Year_TextField;
    @FXML
    public TextField Path_TextField;

    @FXML
    private Button Upload_Song_Button;

    @FXML
    private AnchorPane UploadSong_AnchorPane;
    @FXML
    private Button Open_Song_Path_Button;

    @FXML
    void Open_Song_Path_Click(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Buscar Imagen");

//        // Agregar filtros para facilitar la busqueda
//        fileChooser.getExtensionFilters().addAll(
//                new FileChooser.ExtensionFilter("MP3", "*.mp3")
//
//        );
        Stage stage = (Stage) UploadSong_AnchorPane.getScene().getWindow();
        // Obtener la imagen seleccionada
        File sonfile = fileChooser.showOpenDialog(stage);
        String path = "no se ha seleccionado archivo";
        try {
            path = sonfile.getPath();
        } catch (RuntimeException e) {
            System.out.println("No se selecciono un archivo");
        }
        if(!path.equals("no se ha seleccionado archivo")) {
            Path_TextField.setEditable(true);
            Path_TextField.setText(path);
            Path_TextField.setEditable(false);
        }



        System.out.println("El path del archivo seleccionado es: " + path);
    }

    @FXML
    void Upload_Song_Click(ActionEvent event) {
        if(!Genero_TextField.getText().equals("")&& !Artista_TextField.getText().equals("")&& !Album_TextField.getText().equals("")&&
                !Year_TextField.getText().equals("")&& !Lyrics_TextField.getText().equals("")&& !Path_TextField.getText().equals("")){

            String xml = Generate_Song_XML();

            OdesseyClient.Send_Song_to_Server(xml);

        }
        else{
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error!");


            alert.setContentText("Por favor rellene todos los campos solicitados");

            alert.showAndWait();
        }
    }
    public String Generate_Song_XML(){
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
        //Le anade un atributo al operation code(3-> Song sending to server)//
        Attr attr = Registrarse_doc.createAttribute("ID");
        attr.setValue("3");
        operation.setAttributeNode(attr);

        //Anade la cancion en bytes al xml//

        //
//        String cancion =  converPathBytes(Path_TextField.getText());
        //
        Element SongBytes = Registrarse_doc.createElement("CancionBytes");
        operation.appendChild(SongBytes);
        //*************************** ACA SE DEBE METER LOS BYTES DE LA CANCION *******************************//
        SongBytes.appendChild(Registrarse_doc.createTextNode("ACA DEBE IR LA CANCION EN BASE 64"));
        //Anade el Generp al xml//
        Element Genero = Registrarse_doc.createElement("Genero");
        operation.appendChild(Genero);
        Genero.appendChild(Registrarse_doc.createTextNode(Genero_TextField.getText()));
        //Anade el  Artista //
        Element Artista = Registrarse_doc.createElement("Artista");
        operation.appendChild(Artista);
        Artista.appendChild(Registrarse_doc.createTextNode(Artista_TextField.getText()));
        //Anade el Album al password//
        Element Album = Registrarse_doc.createElement("Album");
        operation.appendChild(Album);
        Album.appendChild(Registrarse_doc.createTextNode(Album_TextField.getText()));
        //Anade el Year musical favorito //
        Element Year = Registrarse_doc.createElement("Year");
        operation.appendChild(Year);
        Year.appendChild(Registrarse_doc.createTextNode(Year_TextField.getText()));
        //Anade el Letra musical favorito //
        Element Letra = Registrarse_doc.createElement("Letra");
        operation.appendChild(Letra);
        Letra.appendChild(Registrarse_doc.createTextNode(Lyrics_TextField.getText()));

        //*********************** TERMINA EL DOCUMENTO ******************************************* //
        String xml = convertDocumentToString(Registrarse_doc);
        System.out.println("La metadata de la cancion es:" + convertDocumentToString(Registrarse_doc));
        return xml;
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
}
