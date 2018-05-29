package Interface;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.event.ActionEvent;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import SocketClient.OdesseyClient;
import java.io.IOException;


public class LogInController {

    public static String username_logged;


    @FXML
    private PasswordField PassWord_TextField;
    @FXML
    private TextField UserName_TextField;
    @FXML
    private Button LogInButton;

    @FXML
    private Button getStartedButton;


    @FXML
    private void initialize() {
        OdesseyClient.chargeUsers();
    }


    @FXML
    void LogInButton(ActionEvent event) throws IOException{
        System.out.println("SE CLICKEO");
        if(OdesseyClient.LogIn_Usuario(UserName_TextField.getText(),PassWord_TextField.getText())){


            username_logged = UserName_TextField.getText();
            Parent home_page = FXMLLoader.load(getClass().getResource("HomePage.fxml"));
            Scene home_page_scene = new Scene(home_page);
            Stage app_stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            app_stage.hide();
            app_stage.setScene(home_page_scene);
            app_stage.show();
        }
        else{
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error!");


            alert.setContentText("La contraseña o el usuario es incorrecto. Si no esta registrado puede hacerlo en Get Started");

            alert.showAndWait();
        }


    }
    @FXML
    void getStartedButton(ActionEvent event) throws IOException{

        System.out.println("GET STARTED");

        Parent GetStarted_page = FXMLLoader.load(getClass().getResource("Registrarse_Window.fxml"));
        Scene GetStarted_scene = new Scene(GetStarted_page);
        Stage app_stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        app_stage.hide();
        app_stage.setScene(GetStarted_scene);
        app_stage.show();
    }

}
