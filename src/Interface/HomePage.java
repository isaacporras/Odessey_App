package Interface;

import SocketClient.OdesseyClient;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;


import javafx.event.ActionEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

public class HomePage {
    public static String playlist_selected = "";
    public static String playlist_To_Charge = "";

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

    private TreeItem<String> root = new TreeItem<String> ("Root");

    @FXML
    private void initialize() {
        System.out.println("Root: "+ root);
        Playlist_TreeView.setRoot(root);
        Playlist_TreeView.setShowRoot(false);
        root.setExpanded(true);

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
    void TreeView_Item_Clicked(MouseEvent mouseEvent) {

        TreeItem<String> item = Playlist_TreeView.getSelectionModel().getSelectedItem();

        if (item != null) {
            if(item.getParent() == root) {
                ContextMenu rootContextMenu = ContextMenuBuilder.create().items(


                        MenuItemBuilder.create().text("Add Song").onAction(new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent arg0) {

                                Parent GetStarted_page = null;
                                try {
                                    GetStarted_page = FXMLLoader.load(getClass().getResource("Upload_Song.fxml"));
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                Scene GetStarted_scene = new Scene(GetStarted_page);
                                Stage stage = new Stage();
                                stage.initModality(Modality.APPLICATION_MODAL);
                                stage.setScene(GetStarted_scene);
                                stage.show();
                                playlist_selected = item.getValue();


                            }
                        }).build()).build();
                Playlist_TreeView.setContextMenu(rootContextMenu);
            }
            else if(item.getParent().getParent() == root){
                ContextMenu rootContextMenu = ContextMenuBuilder.create().items(

                        MenuItemBuilder.create().text("PlaySong").onAction(new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent arg0) {


                            }
                        }).build()).build();
                Playlist_TreeView.setContextMenu(rootContextMenu);
            }
        }


    }
    public static void setPlaylist_To_Charge(String playlistname){
        playlist_To_Charge = playlistname;

    }
    public void addTreeItem(){
//        Playlist_TreeView.;
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
}
