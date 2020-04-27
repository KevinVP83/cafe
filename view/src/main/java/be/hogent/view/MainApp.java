package be.hogent.view;

import be.hogent.model.Beverage;
import be.hogent.model.Cafe;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;

public class MainApp extends Application {

    private final Cafe model;
    private Stage primaryStage;
    private BorderPane rootLayout;
    private ObservableList<Beverage> beverages;

    public MainApp () {
        model = new Cafe();
    }

    public static void main(String[] args) { launch(args); }

    public ObservableList<Beverage> getBeverages () {
        return beverages;
    }

    @Override
    public void start(Stage primaryStage) {
        beverages = FXCollections.observableArrayList(model.getBeverages());
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle ("Café Java");

        initRootLayout ();
        showBeverageOverview ();
    }

    public Stage getPrimaryStage () {
        return primaryStage;
    }

    private void initRootLayout () {
        try {
            FXMLLoader loader = new FXMLLoader ();
            loader.setLocation (MainApp.class.getResource ("/cafe/RootLayout.fxml"));

            rootLayout = loader.load ();

            // Show the scene containing the root layout.
            Scene scene = new Scene (rootLayout);
            primaryStage.setScene (scene);
            primaryStage.show ();
        } catch (IOException e) {
            e.printStackTrace ();
        }
    }

    private void showBeverageOverview () {
        try {
            // Load beverage overview.
            FXMLLoader loader = new FXMLLoader ();
            loader.setLocation (getClass ().getResource ("/cafe/Main.fxml"));
            AnchorPane beverageOverview = loader.load ();

            // Set beverage overview into the center of root layout.
            rootLayout.setCenter (beverageOverview);

            // Give the controller access to the main app.
            be.hogent.view.MainController controller = loader.getController ();
            controller.setModel (model);

            controller.setCafeView (this);

        } catch (IOException e) {
            e.printStackTrace ();
        }
    }
}
