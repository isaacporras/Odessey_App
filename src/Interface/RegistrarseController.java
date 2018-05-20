package Interface;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.event.ActionEvent;
import javafx.stage.Stage;
import SocketClient.OdesseyClient;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;

import java.io.IOException;

public class RegistrarseController {
    @FXML
    private RadioButton Reggae_RadioButton;

    @FXML
    private PasswordField Password_TextFiel;

    @FXML
    private TextField Username_TextField;

    @FXML
    private RadioButton Rock_RadioButton;

    @FXML
    private TextField Name_TextFiel;

    @FXML
    private TextField Age_TextFiel;

    @FXML
    private Button BackButton;

    @FXML
    private RadioButton Pop_RadioButton;

    @FXML
    private Button CreateAccount_Button;

    @FXML
    void CreateAccount_Button_Action(ActionEvent event) {
        OdesseyClient.Registrar_Usario(Username_TextField.getText(),Name_TextFiel.getText(),Age_TextFiel.getText(),Password_TextFiel.getText()
        ,Rock_RadioButton.isSelected(),Pop_RadioButton.isSelected(),Reggae_RadioButton.isSelected());
    }

    @FXML
    void BackButton(ActionEvent event) throws IOException {
        System.out.println("SE CLICKEO BACK");
        Parent LogIn_page = FXMLLoader.load(getClass().getResource("LogIn.fxml"));
        Scene LogIn_scene = new Scene(LogIn_page);
        Stage app_stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        app_stage.hide();
        app_stage.setScene(LogIn_scene);
        app_stage.show();
    }
}
