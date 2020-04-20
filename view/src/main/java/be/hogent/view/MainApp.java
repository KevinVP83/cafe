package be.hogent.view;

import be.hogent.model.Cafe;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;

public class MainApp extends Application {

    private final Cafe model;
    private Stage primaryStage;
    private BorderPane rootLayout;

    public MainApp () {
        model = new Cafe();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle ("Café Java");

        initRootLayout ();
    }

    public static void main(String[] args) { launch(args);
    }

    public Stage getPrimaryStage () {
        return primaryStage;
    }

    private void initRootLayout () {
        try {
            FXMLLoader loader = new FXMLLoader ();
            loader.setLocation (MainApp.class.getResource ("/be.hogent.view/RootLayout.fxml"));

            rootLayout = loader.load ();

            // Show the scene containing the root layout.
            Scene scene = new Scene (rootLayout);
            primaryStage.setScene (scene);
            primaryStage.show ();
        } catch (IOException e) {
            e.printStackTrace ();
        }
    }
}
