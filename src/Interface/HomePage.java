package Interface;

import SocketClient.OdesseyClient;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.event.ActionEvent;

import javafx.event.ActionEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
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
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class HomePage {

    public static String nombre_set_in_UploadWindow = "";

    public static String playlist_selected = "";

    public static int restriccion;

    HomePage homePage = this;

    public String name = "";

    @FXML
    private TreeView<String> Non_Friend_Tree;

    @FXML
    private Label UserName_Area;

    @FXML
    private Label Name_Area;

    @FXML
    private TextField PlayList_Name_TextField;

    private ContextMenu contextMenu;
    @FXML
    private Button Add_Playlist_Button;
    @FXML
    private AnchorPane MainWindow_AnchorPane;

    @FXML
    private Button LogOut_Button;

    @FXML
    public TreeView<String> Playlist_TreeView;

    private TreeItem<String> root;

    private TreeItem<String> search_root;

    @FXML
    private TreeView<String> Friends_List;

    @FXML
    private TreeView<String> SearchListView;
    @FXML
    private TextField Search_TextField;

    private Slider Song_Slider;

    private Boolean pause = true;

    public TreeItem root3 ;
    public TreeItem root4;


    @FXML
    void SearchButton_Clicked(ActionEvent event) {
        if(!Search_TextField.getText().equals("")) {
            SearchListView.getRoot().getChildren().clear();
            List<String> results = OdesseyClient.search(Search_TextField.getText());

            for (int i=0;i < results.size();i++)
            {
                System.out.println("Un resultado es: "+results.get(i));

            }
            System.out.println("Se terminaron los resultados lol");

            for (int i=0;i < results.size();i++)
            {
                if(!results.get(i).equals("finished")){
                    TreeItem<String> item = new TreeItem<>(results.get(i));
                    SearchListView.getRoot().getChildren().addAll(item);
                }


            }
        }
        else{
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error!");


            alert.setContentText("Digite algo en el campo de buscar");

            alert.showAndWait();
        }
    }

    @FXML
    private void initialize() {
        root4 = new TreeItem<String> ("Root4");
        Friends_List.setRoot(root4);
        Friends_List.setShowRoot(false);
        root3 = new TreeItem<String> ("Root3");
        Non_Friend_Tree.setRoot(root3);
        Non_Friend_Tree.setShowRoot(false);
        root = new TreeItem<String> ("Root");
        search_root = new TreeItem<String>("SearchRoot");
        System.out.println("Root: "+ root);
        Playlist_TreeView.setRoot(root);
        SearchListView.setRoot(search_root);
        SearchListView.setShowRoot(false);
        Playlist_TreeView.setShowRoot(false);
        root.setExpanded(true);
        List<String> playlistNames = OdesseyClient.getPlaylist();
        List<String> SongNames = OdesseyClient.getSonglist();
        for (int i=0;i < playlistNames.size();i++)
        {
            TreeItem<String> item = new TreeItem<>(playlistNames.get(i));
            Playlist_TreeView.getRoot().getChildren().addAll(item);
        }
        if(!SongNames.isEmpty()){
            AddSongToTreeView(SongNames);
        }
        OdesseyClient.chargeSongsName();


//        Name_Area.setText(OdesseyClient.getUserInfo(LogInController.username_logged));

        UserName_Area.setText(LogInController.username_logged);

        List<String> UsersList = OdesseyClient.getUsersList();

        for (int i=0;i < UsersList.size();i++)
        {
            if(!UsersList.get(i).equals("finished")){
                TreeItem<String> item = new TreeItem<>(UsersList.get(i).substring(0,UsersList.get(i).length()-5));
                Non_Friend_Tree.getRoot().getChildren().addAll(item);
            }

        }


    }

    @FXML
    void Add_Playlist_Button_Clicked(ActionEvent event) {
        if(!PlayList_Name_TextField.getText().equals("")){
            TreeItem<String> item = new TreeItem<>(PlayList_Name_TextField.getText());
            Playlist_TreeView.getRoot().getChildren().addAll(item);
            OdesseyClient.AddPlaylist(PlayList_Name_TextField.getText());
        }
        else{
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error!");


            alert.setContentText("Digite un nombre para el Playlist");

            alert.showAndWait();
        }


    }
    @FXML
    void Non_Friend_Clicked(MouseEvent mouseEvent) {
        TreeItem<String> item = Non_Friend_Tree.getSelectionModel().getSelectedItem();

        if (item != null) {
            if(item.getParent() == root3) {
                ContextMenu rootContextMenu = ContextMenuBuilder.create().items(


                        MenuItemBuilder.create().text("Add Friend").onAction(new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent arg0) {
                                Non_Friend_Tree.getRoot().getChildren().remove(item);
                                Friends_List.getRoot().getChildren().addAll(item);


                            }
                        }).build()).build();
                Non_Friend_Tree.setContextMenu(rootContextMenu);
            }
        }
    }


    @FXML
    void TreeView_Item_Clicked(MouseEvent mouseEvent) {

        TreeItem<String> item = Playlist_TreeView.getSelectionModel().getSelectedItem();

        if (item != null) {
            if(item.getParent() == root) {
                ContextMenu rootContextMenu = ContextMenuBuilder.create().items(


                        MenuItemBuilder.create().text("Add Song").onAction(new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent arg0) {
                                displayUpload_Window(item);


                            }
                        }).build()).build();
                Playlist_TreeView.setContextMenu(rootContextMenu);
            }
            else if(item.getParent().getParent() == root){

                ContextMenu rootContextMenu = ContextMenuBuilder.create().items(

                        MenuItemBuilder.create().text("PlaySong").onAction(new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent arg0) {
                                displayReproduccionWindow(item);

                            }
                        }).build(),
                        MenuItemBuilder.create().text("Eliminar").onAction(new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent arg0) {
                                OdesseyClient.eliminar_cancion(item.getValue(),item.getParent().getValue());
                                item.getParent().getChildren().remove(item);

                            }
                        }).build(),
                        MenuItemBuilder.create().text("Modificar Data").onAction(new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent arg0) {
                                displayModifyMetadata(item);

                            }
                        }).build(),
                        MenuItemBuilder.create().text("Calificar").onAction(new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent arg0) {
                                displayStarsWindow(item);

                            }
                        }).build()).build();
                Playlist_TreeView.setContextMenu(rootContextMenu);
            }
        }
    }
    public void displayStarsWindow(TreeItem<String> item){
        Stage StarsWindow = new Stage();
        StarsWindow.initModality(Modality.APPLICATION_MODAL);

        StarsWindow.initModality(Modality.APPLICATION_MODAL);
        StarsWindow.setTitle("Setting Stars");

        RadioButton OneStar = new RadioButton("1");

        OneStar.setLayoutX(50);
        OneStar.setLayoutY(50);

        OneStar.setOnAction(e->{

        });

        RadioButton TwoStar = new RadioButton("2");

        TwoStar.setLayoutX(100);
        TwoStar.setLayoutY(50);

        RadioButton ThreeStar = new RadioButton("3");

        ThreeStar.setLayoutX(150);
        ThreeStar.setLayoutY(50);

        RadioButton FourStar = new RadioButton("4");

        FourStar.setLayoutX(200);
        FourStar.setLayoutY(50);

        RadioButton FiveStar = new RadioButton("5");

        FiveStar.setLayoutX(250);
        FiveStar.setLayoutY(50);

        OneStar.setOnAction(e->{
            TwoStar.setSelected(false);
            ThreeStar.setSelected(false);
            FourStar.setSelected(false);
            FiveStar.setSelected(false);

        });
        TwoStar.setOnAction(e->{
            OneStar.setSelected(false);
            ThreeStar.setSelected(false);
            FourStar.setSelected(false);
            FiveStar.setSelected(false);

        });
        ThreeStar.setOnAction(e->{
            OneStar.setSelected(false);
            TwoStar.setSelected(false);
            FourStar.setSelected(false);
            FiveStar.setSelected(false);

        });
        FourStar.setOnAction(e->{
            OneStar.setSelected(false);
            TwoStar.setSelected(false);
            ThreeStar.setSelected(false);
            FiveStar.setSelected(false);

        });
        FiveStar.setOnAction(e->{
            OneStar.setSelected(false);
            TwoStar.setSelected(false);
            FourStar.setSelected(false);
            ThreeStar.setSelected(false);

        });

        Button ok = new Button("Ok");
        ok.setLayoutX(300);
        ok.setLayoutY(80);

        ok.setOnAction(e->{
            if(OneStar.isSelected() || TwoStar.isSelected() || ThreeStar.isSelected() || FourStar.isSelected() || FiveStar.isSelected()){
                if(OneStar.isSelected()){
                    OdesseyClient.AddStars_cancion(item.getValue(),"1");
                    StarsWindow.close();
                }
                else if(TwoStar.isSelected()){
                    OdesseyClient.AddStars_cancion(item.getValue(),"2");
                    StarsWindow.close();
                }
                else if(ThreeStar.isSelected()){
                    OdesseyClient.AddStars_cancion(item.getValue(),"3");
                    StarsWindow.close();
                }
                else if(FourStar.isSelected()){
                    OdesseyClient.AddStars_cancion(item.getValue(),"4");
                    StarsWindow.close();
                }
                else if(FiveStar.isSelected()){
                    OdesseyClient.AddStars_cancion(item.getValue(),"5");
                    StarsWindow.close();
                }

            }

        });

        AnchorPane StarsAnchorPane = new AnchorPane();
        StarsAnchorPane.getChildren().addAll(OneStar,TwoStar, ThreeStar, FourStar,FiveStar, ok);

        Scene UploadSong_Scene = new Scene(StarsAnchorPane, 350, 130);
        StarsWindow.setScene(UploadSong_Scene);
        StarsWindow.showAndWait();



    }
    public void displayModifyMetadata(TreeItem<String> item){


        Stage ventanaUpload = new Stage();
        ventanaUpload.initModality(Modality.APPLICATION_MODAL);

        ventanaUpload.initModality(Modality.APPLICATION_MODAL);
        ventanaUpload.setTitle("Modificando Data");

        Label SongInfo_Label = new Label("Song Information:");
        SongInfo_Label.setLayoutX(14);
        SongInfo_Label.setLayoutY(14);
        SongInfo_Label.setFont(new Font(26));

        Label Nombre_Label = new Label(item.getValue());
        Nombre_Label.setLayoutX(100);
        Nombre_Label.setLayoutY(122);
        Nombre_Label.setFont(new Font(26));


        Label Genero_Label = new Label("Genero:");
        Genero_Label.setLayoutX(16);
        Genero_Label.setLayoutY(162);

        TextField Genero_TextField = new TextField();
        Genero_TextField.setLayoutX(93);
        Genero_TextField.setLayoutY(157);

        Label Artista_Label = new Label("Artista:");
        Artista_Label.setLayoutX(16);
        Artista_Label.setLayoutY(207);

        TextField Artista_TextField = new TextField();
        Artista_TextField.setLayoutX(93);
        Artista_TextField.setLayoutY(202);

        Label Album_Label = new Label("Album:");
        Album_Label.setLayoutX(16);
        Album_Label.setLayoutY(256);

        TextField Album_TextField = new TextField();
        Album_TextField.setLayoutX(93);
        Album_TextField.setLayoutY(251);

        Label Year_Label = new Label("Año:");
        Year_Label.setLayoutX(16);
        Year_Label.setLayoutY(305);

        TextField Year_TextField = new TextField();
        Year_TextField.setLayoutX(93);
        Year_TextField.setLayoutY(301);

        Label Letra_Label = new Label("Letra:");
        Letra_Label.setLayoutX(16);
        Letra_Label.setLayoutY(345);

        TextArea Letra_TextField = new TextArea();
        Letra_TextField.setLayoutX(77);
        Letra_TextField.setLayoutY(362);
        Letra_TextField.setPrefSize(348, 200);

        Button Modify_Button = new Button("Modify Metadata");
        Modify_Button.setLayoutX(328);
        Modify_Button.setLayoutY(577);

        Modify_Button.setOnAction(i -> {

            if (!Genero_TextField.getText().equals("") && !Artista_TextField.getText().equals("") && !Album_TextField.getText().equals("") &&
                    !Year_TextField.getText().equals("") && !Letra_TextField.getText().equals("") ) {
                Modify_Button.setDisable(true);

                String xml = Generate_Song_XML("", item.getValue(), Genero_TextField.getText(),
                        Artista_TextField.getText(), Album_TextField.getText(), Year_TextField.getText(), Letra_TextField.getText(), item.getParent().getValue(),"20");


                OdesseyClient.Send_Song_to_Server(xml);

                ventanaUpload.close();





            } else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Error!");


                alert.setContentText("Por favor rellene todos los campos solicitados");

                alert.showAndWait();
            }
        });

        AnchorPane UploadAnchorPane = new AnchorPane();

        UploadAnchorPane.getChildren().addAll(SongInfo_Label, Nombre_Label,
                Genero_Label, Genero_TextField, Artista_Label, Artista_TextField, Album_Label, Album_TextField, Year_Label, Year_TextField,
                Letra_Label, Letra_TextField, Modify_Button);
        Scene UploadSong_Scene = new Scene(UploadAnchorPane, 441, 628);
        ventanaUpload.setScene(UploadSong_Scene);
        ventanaUpload.showAndWait();

    }
    public void displayReproduccionWindow(TreeItem<String> item){

        List<String> metadata = OdesseyClient.getMetadataList(item.getValue());




        Stage Ventana_Reproduccion = new Stage();
        Ventana_Reproduccion.initModality(Modality.APPLICATION_MODAL);

        Ventana_Reproduccion.initModality(Modality.APPLICATION_MODAL);
        Ventana_Reproduccion.setTitle("Reproduciendo Cancion");


        Label Nombre_Label = new Label("Nombre:");
        Nombre_Label.setLayoutX(14);
        Nombre_Label.setLayoutY(22);
        Nombre_Label.setFont(new Font(31));



        Label Nombre_Label_text = new Label(metadata.get(0));
        Nombre_Label_text.setLayoutX(150);
        Nombre_Label_text.setLayoutY(22);
        Nombre_Label_text.setFont(new Font(31));



        Label Genero_Label = new Label("Genero:");
        Genero_Label.setLayoutX(14);
        Genero_Label.setLayoutY(69);

        Label Genero_Label_text = new Label(metadata.get(1));
        Genero_Label_text.setLayoutX(78);
        Genero_Label_text.setLayoutY(69);

        Label Artista_Label = new Label("Artista:");
        Artista_Label.setLayoutX(14);
        Artista_Label.setLayoutY(100);

        Label Artista_Label_text = new Label(metadata.get(2));
        Artista_Label_text.setLayoutX(78);
        Artista_Label_text.setLayoutY(100);

        Label Album_Label = new Label("Album:");
        Album_Label.setLayoutX(14);
        Album_Label.setLayoutY(129);

        Label Album_Label_text = new Label(metadata.get(3));
        Album_Label_text.setLayoutX(78);
        Album_Label_text.setLayoutY(129);

        Label Year_Label = new Label("Año:");
        Year_Label.setLayoutX(14);
        Year_Label.setLayoutY(156);

        Label Year_Label_text = new Label(metadata.get(4));
        Year_Label_text.setLayoutX(78);
        Year_Label_text.setLayoutY(156);

        Label Letra_Label = new Label("Letra:");
        Letra_Label.setLayoutX(14);
        Letra_Label.setLayoutY(182);

        TextArea Letra_TextField = new TextArea();
        Letra_TextField.setLayoutX(37);
        Letra_TextField.setLayoutY(214);
        Letra_TextField.setPrefSize(246, 175);

        Letra_TextField.setText(metadata.get(5));

        Label Stars_Label = new Label("Stars:");
        Stars_Label.setLayoutX(300);
        Stars_Label.setLayoutY(55);
        Stars_Label.setFont(new Font(31));

        Label Stars_Label_text = new Label();
        Stars_Label_text.setLayoutX(390);
        Stars_Label_text.setLayoutY(90);
        Stars_Label_text.setFont(new Font(15));
        Stars_Label_text.setText(metadata.get(6));

        Letra_TextField.setEditable(false);

        Button PlayButton = new Button("Play");
        PlayButton.setLayoutX(367);
        PlayButton.setLayoutY(302);

        Song_Slider = new Slider();

        PlayButton.setOnAction(e->{
            OdesseyClient.paused = false;
            pause = false;
//            String xml = makeXML_for_Reproduction(item.getParent().getValue(),item.getValue(), "0");
            if(restriccion==0) {
                //Song_Slider.setValue(Song_Slider.getValue()+10);
                OdesseyClient.Play_Song(item.getParent().getValue(), item.getValue(), "0");
                restriccion++;
            }else{
                OdesseyClient.paused = false;
                pause = false;
            }
        });

        Button PauseButton = new Button("Pause");
        PauseButton.setLayoutX(438);
        PauseButton.setLayoutY(302);

        PauseButton.setOnAction(e->{
            //System.out.println("SE ESTA DANDO PAUSA PERO NO HACE NADA MAS QUE ESCRIBIR");
            OdesseyClient.paused = true;
            pause = true;
            //OdesseyClient.Pause_Song(item.getParent().getValue(), item.getValue(),String.valueOf(OdesseyClient.counter));
        });

        /*new Thread() {
            public void run() {
                try {
                    while(pause == true){
                        Thread.sleep(1000);
                        //System.out.println("Se detuvo el thread por al menos 8 segundos");
                    }
                    Song_Slider.setValue(Song_Slider.getValue()+10);
                    TimeUnit.SECONDS.sleep(1);
                    run();
                } catch (Exception e) {
                    System.out.println(e);
                }
            }
        }.start();*/


        //Slider Song_Slider = new Slider();
        Song_Slider.setLayoutX(301);
        Song_Slider.setLayoutY(353);
        Song_Slider.setPrefSize(274,16);
        /*Song_Slider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
            double value = Song_Slider.getValue();
            System.out.println("El valor del Slider es = " + value);
            }
        });*/

        int totalSeconds = 0;

        try {
            TimeUnit.SECONDS.sleep(1);
            String xml = OdesseyClient.makeXML_for_Reproduction(item.getParent().getValue(), item.getValue(), "t");
            DataOutputStream outToServer = new DataOutputStream(OdesseyClient.Clientsocket.getOutputStream());
            outToServer.writeBytes(xml + '\n');

            String time;
            BufferedReader inFromServer = new BufferedReader(new InputStreamReader(OdesseyClient.Clientsocket.getInputStream()));
            time = inFromServer.readLine();
            String minutes = time.substring(0,1);
            String seconds = time.substring(1);
            System.out.println("Tiempo de la canción: " + minutes + " minutos con " + seconds + " segundos.");
            totalSeconds = Integer.parseInt(seconds) + Integer.parseInt(minutes)*60;
        }
        catch (Exception e){
            System.out.println(e);
        }

        double timeRatio = 100.00 / (double) totalSeconds;

        new Thread() {
            public void run() {
                try {
                    while(pause == true){
                        Thread.sleep(1000);
                        //System.out.println("Se detuvo el thread por al menos 8 segundos");
                    }

                    Song_Slider.setValue(Song_Slider.getValue()+timeRatio);
                    TimeUnit.SECONDS.sleep(1);
                    run();
                } catch (Exception e) {
                    System.out.println(e);
                }
            }
        }.start();

        Pane Reproduccion_AnchorPane = new Pane();

        Reproduccion_AnchorPane.getChildren().addAll(Nombre_Label,Nombre_Label_text,
                Genero_Label,Genero_Label_text,  Artista_Label,Artista_Label_text, Album_Label,Album_Label_text,
                Year_Label,Year_Label_text,Stars_Label,Stars_Label_text,
                Letra_Label, Letra_TextField, PauseButton, PlayButton, Song_Slider);
        Scene Reproduccion_Scene = new Scene(Reproduccion_AnchorPane, 575, 406);
        Ventana_Reproduccion.setScene(Reproduccion_Scene);
        Ventana_Reproduccion.showAndWait();
        if (Ventana_Reproduccion.isShowing()!= true){
            restriccion = 0;
        }


    }

    public TreeItem<String> search_PlayList_in_TreeView(String nombre){

        System.out.println("El buscado es:" + nombre);
        TreeIterator<String> iterator = new TreeIterator<>(root);
        int nItems = 0;
        while (iterator.hasNext()) {
            nItems++;
            iterator.next();
        }
        int contador = 0;
        System.out.println("nItems:" + nItems);

        while(contador != nItems-1){
            if(Playlist_TreeView.getTreeItem(contador).getValue().equals(nombre)){
                return Playlist_TreeView.getTreeItem(contador);
            }
            else {
                System.out.println("El que no cumplio es: "+Playlist_TreeView.getTreeItem(contador).getValue());

                contador = contador + 1 ;
            }
        }
        TreeItem<String> defaultItem = new TreeItem<String>();
        defaultItem.setValue("No se encontro");
        return defaultItem;



    }

    public void AddSongToTreeView(List<String> response){

        System.out.println("EN HOME PAGE");
        int  i = 0;
        while(i < response.size()){
            System.out.println("for loop");
            if(response.get(i).equals("PlayList Start") && response.get(i + 2).equals("PlayList End")){
                i = i + 1;
            }
            else if(response.get(i).equals("PlayList Start") && !response.get(i + 2).equals("PlayList End")) {

                TreeItem<String> treeItem = search_PlayList_in_TreeView(response.get(i + 1));
                System.out.println("Encontro canciones: " + treeItem.getValue());
                i = i + 2;
                while(!response.get(i).equals("PlayList End")){
                    System.out.println("Inserta el item el el TreeView");
                    TreeItem<String> itemToInsert = new TreeItem<String>();
                    itemToInsert.setValue(response.get(i));
                    treeItem.getChildren().addAll(itemToInsert);
                    i = i + 1;
                }


            }
            i = i +1 ;
        }
    }



    @FXML
    void LogOut(ActionEvent event) {
        Parent LogIn_page = null;
        try {
            LogIn_page = FXMLLoader.load(getClass().getResource("LogIn.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Scene LogIn_scene = new Scene(LogIn_page);
        Stage app_stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        app_stage.hide();
        app_stage.setScene(LogIn_scene);
        app_stage.show();
    }

    public void displayUpload_Window(TreeItem<String> item) {


        Stage ventanaUpload = new Stage();
        ventanaUpload.initModality(Modality.APPLICATION_MODAL);

        ventanaUpload.initModality(Modality.APPLICATION_MODAL);
        ventanaUpload.setTitle("Uploading Song");

        Label SongInfo_Label = new Label("Song Information:");
        SongInfo_Label.setLayoutX(14);
        SongInfo_Label.setLayoutY(14);
        SongInfo_Label.setFont(new Font(26));

        Label Nombre_Label = new Label("Nombre:");
        Nombre_Label.setLayoutX(16);
        Nombre_Label.setLayoutY(122);

        TextField Path_TextField = new TextField();
        Path_TextField.setLayoutX(23);
        Path_TextField.setLayoutY(77);
        Path_TextField.setDisable(true);
        Path_TextField.setPrefHeight(27);
        Path_TextField.setPrefWidth(348);

        Button Open_Button = new Button("Open");
        Open_Button.setLayoutX(376);
        Open_Button.setLayoutY(77);

        Open_Button.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Buscar Cancion");


            // Agregar filtros para facilitar la busqueda
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("MP3", "*.mp3")

            );

            // Obtener la imagen seleccionada
            File sonfile = fileChooser.showOpenDialog(ventanaUpload);
            String path = "no se ha seleccionado archivo";
            try {
                path = sonfile.getPath();
            } catch (RuntimeException x) {
                System.out.println("No se selecciono un archivo");
            }
            if (!path.equals("no se ha seleccionado archivo")) {
                Path_TextField.setEditable(true);
                Path_TextField.setText(path);
                Path_TextField.setEditable(false);
            }


            System.out.println("El path del archivo seleccionado es: " + path);
        });

        Label Cancion_Label = new Label("Cancion:");
        Cancion_Label.setLayoutX(23);
        Cancion_Label.setLayoutY(52);

        TextField Nombre_TextField = new TextField();
        Nombre_TextField.setLayoutX(93);
        Nombre_TextField.setLayoutY(117);

        Label Genero_Label = new Label("Genero:");
        Genero_Label.setLayoutX(16);
        Genero_Label.setLayoutY(162);

        TextField Genero_TextField = new TextField();
        Genero_TextField.setLayoutX(93);
        Genero_TextField.setLayoutY(157);

        Label Artista_Label = new Label("Artista:");
        Artista_Label.setLayoutX(16);
        Artista_Label.setLayoutY(207);

        TextField Artista_TextField = new TextField();
        Artista_TextField.setLayoutX(93);
        Artista_TextField.setLayoutY(202);

        Label Album_Label = new Label("Album:");
        Album_Label.setLayoutX(16);
        Album_Label.setLayoutY(256);

        TextField Album_TextField = new TextField();
        Album_TextField.setLayoutX(93);
        Album_TextField.setLayoutY(251);

        Label Year_Label = new Label("Año:");
        Year_Label.setLayoutX(16);
        Year_Label.setLayoutY(305);

        TextField Year_TextField = new TextField();
        Year_TextField.setLayoutX(93);
        Year_TextField.setLayoutY(301);

        Label Letra_Label = new Label("Letra:");
        Letra_Label.setLayoutX(16);
        Letra_Label.setLayoutY(345);

        TextArea Letra_TextField = new TextArea();
        Letra_TextField.setLayoutX(77);
        Letra_TextField.setLayoutY(362);
        Letra_TextField.setPrefSize(348, 200);

        Button UploadSong_Button = new Button("Upload Song");
        UploadSong_Button.setLayoutX(328);
        UploadSong_Button.setLayoutY(577);

        UploadSong_Button.setOnAction(i -> {

            if (!Genero_TextField.getText().equals("") && !Artista_TextField.getText().equals("") && !Album_TextField.getText().equals("") &&
                    !Year_TextField.getText().equals("") && !Letra_TextField.getText().equals("") && !Path_TextField.getText().equals("")) {
                UploadSong_Button.setDisable(true);

                String xml = Generate_Song_XML(Path_TextField.getText(), Nombre_TextField.getText(), Genero_TextField.getText(),
                        Artista_TextField.getText(), Album_TextField.getText(), Year_TextField.getText(), Letra_TextField.getText(), item.getValue(),"3");


                OdesseyClient.Send_Song_to_Server(xml);

                TreeItem<String> NewSongItem = new TreeItem<String>();
                NewSongItem.setValue(Nombre_TextField.getText() + ".mp3");
                item.getChildren().addAll(NewSongItem);
                ventanaUpload.close();





            } else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Error!");


                alert.setContentText("Por favor rellene todos los campos solicitados");

                alert.showAndWait();
            }
        });

        AnchorPane UploadAnchorPane = new AnchorPane();

        UploadAnchorPane.getChildren().addAll(SongInfo_Label, Cancion_Label, Path_TextField, Open_Button, Nombre_Label, Nombre_TextField,
                Genero_Label, Genero_TextField, Artista_Label, Artista_TextField, Album_Label, Album_TextField, Year_Label, Year_TextField,
                Letra_Label, Letra_TextField, UploadSong_Button);
        Scene UploadSong_Scene = new Scene(UploadAnchorPane, 441, 628);
        ventanaUpload.setScene(UploadSong_Scene);
        ventanaUpload.showAndWait();

    }


    public String Songtobase64(String ruta){


        Path path = Paths.get(ruta);
        byte[] data = new byte[0];
        try {
            data = Files.readAllBytes(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String song = Base64.getEncoder().encodeToString(data);
        return song;
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
    public String Generate_Song_XML(String path,String nombre,String genero,String artista, String album,String year,
                                    String lyrics,String playlist,String OperationCode){
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
        attr.setValue(OperationCode);
        operation.setAttributeNode(attr);

        //Anade la cancion en bytes al xml//

        //
//        String cancion =  converPathBytes(Path_TextField.getText());
        //
        Element SongBytes = Registrarse_doc.createElement("CancionBytes");
        operation.appendChild(SongBytes);
        //*************************** ACA SE DEBE METER LOS BYTES DE LA CANCION *******************************//
        if(OperationCode.equals("3")){
            SongBytes.appendChild(Registrarse_doc.createTextNode(Songtobase64(path)));
        }

        //Anade el Nombre de la cancion al xml//
        Element Nombre = Registrarse_doc.createElement("Nombre");
        operation.appendChild(Nombre);
        Nombre.appendChild(Registrarse_doc.createTextNode(nombre));
        //Anade el Generp al xml//
        Element Genero = Registrarse_doc.createElement("Genero");
        operation.appendChild(Genero);
        Genero.appendChild(Registrarse_doc.createTextNode(genero));
        //Anade el  Artista //
        Element Artista = Registrarse_doc.createElement("Artista");
        operation.appendChild(Artista);
        Artista.appendChild(Registrarse_doc.createTextNode(artista));
        //Anade el Album al password//
        Element Album = Registrarse_doc.createElement("Album");
        operation.appendChild(Album);
        Album.appendChild(Registrarse_doc.createTextNode(album));
        //Anade el Year musical favorito //
        Element Year = Registrarse_doc.createElement("Year");
        operation.appendChild(Year);
        Year.appendChild(Registrarse_doc.createTextNode(year));


        Element PlaylistElement = Registrarse_doc.createElement("Playlist");
        operation.appendChild(PlaylistElement);
        PlaylistElement.appendChild(Registrarse_doc.createTextNode(playlist));
        System.out.println("El playlist que busco es este:" + playlist);

        //Anade el Letra musical favorito //
        Element Letra = Registrarse_doc.createElement("Letra");
        operation.appendChild(Letra);
        Letra.appendChild(Registrarse_doc.createTextNode(lyrics));
        //Anade el playlist al cual anadir la cancion //

        if(Letra.getFirstChild().getNodeValue().equals("")){
            Letra.appendChild(Registrarse_doc.createTextNode("La letra contiene caracteres ilegibles"));
        }


        //*********************** TERMINA EL DOCUMENTO ******************************************* //
        String xml = convertDocumentToString(Registrarse_doc);

        return xml;
    }
    class TreeIterator<T> implements Iterator<TreeItem<T>> {
        private Stack<TreeItem<T>> stack = new Stack<>();

        public TreeIterator(TreeItem<T> root) {
            stack.push(root);
        }

        @Override
        public boolean hasNext() {
            return !stack.isEmpty();
        }

        @Override
        public TreeItem<T> next() {
            TreeItem<T> nextItem = stack.pop();
            nextItem.getChildren().forEach(stack::push);

            return nextItem;
        }
    }
}