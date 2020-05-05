package be.hogent.view;

import be.hogent.model.Beverage;
import be.hogent.model.Cafe;
import be.hogent.model.dao.DAOException;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class MainApp extends Application {

    private final Cafe model;
    private Stage primaryStage;
    private BorderPane rootLayout;
    private ObservableList<Beverage> beverages;
    private MainController mainController;
    private LoginController loginController;

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
        showMainView();
    }

    @Override
    public void stop(){
        model.serializeTables();
    }

    public Stage getPrimaryStage () {
        return primaryStage;
    }

    private void initRootLayout () {
        try {
            FXMLLoader loader = new FXMLLoader ();
            loader.setLocation (MainApp.class.getResource ("/cafe/RootLayout.fxml"));
            rootLayout = loader.load ();
            mainController = loader.getController();

            // Show the scene containing the root layout.
            Scene scene = new Scene (rootLayout);
            primaryStage.setScene (scene);
            primaryStage.show ();
        } catch (IOException e) {
            e.printStackTrace ();
        }
    }


    public void showMainView(){
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/cafe/Main.fxml"));
            AnchorPane main = loader.load();

            rootLayout.setCenter(main);
            mainController = loader.getController();
            mainController.setModel(model);
            mainController.setCafeView(this);
        }catch (IOException | DAOException e){
            e.printStackTrace();
        }
    }

    public boolean showLogin(){
        try{
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/cafe/Login.fxml"));
            AnchorPane login = loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Login");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(login);
            dialogStage.setScene(scene);

            loginController = loader.getController();
            loginController.setDialogStage(dialogStage);
            loginController.setModel(model);

            dialogStage.showAndWait();
            return loginController.getLoginSuccess();

        }catch (IOException e){
            e.printStackTrace();
            return false;
        }
    }

    public void closePrimary(){
        if(!loginController.showingDialog().isShowing() && !loginController.getLoginSuccess()){
            primaryStage.close();
        }
    }
}
