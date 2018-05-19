package Interface;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import SocketClient.OdesseyClient;

import java.io.IOException;

public class Main extends Application {
    public static OdesseyClient cliente;
    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("LogIn.fxml"));
        primaryStage.setTitle("Odessey");
        primaryStage.setScene(new Scene(root, 405, 354));
        primaryStage.show();


    }


    public static void main(String[] args)throws IOException {
        cliente = OdesseyClient.getInstance();
        Thread thread = new Thread(cliente);
        thread.start();
        launch(args);


    }
}
