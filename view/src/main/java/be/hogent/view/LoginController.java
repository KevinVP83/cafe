package be.hogent.view;

import be.hogent.model.Cafe;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginController {
    private MainApp cafeView;
    private Cafe model;
    private Stage dialogStage;
    private boolean loginSuccess = false;
    @FXML
    private PasswordField passwordField;
    @FXML
    private TextField loginName;
    @FXML
    private Button bt_login;

    public void setDialogStage(Stage dialogStage){
        this.dialogStage = dialogStage;
    }

    public void setModel(Cafe model){
        this.model = model;
    }

    public boolean getLoginSuccess(){ return loginSuccess;}

    public Stage showingDialog(){
        return dialogStage;
    }

    @FXML
    public void login() throws Cafe.AlreadyLoggedOnException, Cafe.WrongCredentialsException {
        if (isInputValid()) {
            try {
                model.login(loginName.getText(), passwordField.getText());
                loginSuccess = true;
                dialogStage.close();
            } catch (Cafe.WrongCredentialsException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.initOwner(dialogStage);
                alert.setTitle("login failed");
                alert.setHeaderText("give correct name and password");
                alert.setContentText(" check name and password");
                alert.showAndWait();
            } catch (Cafe.AlreadyLoggedOnException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.initOwner(dialogStage);
                alert.setTitle("login failed");
                alert.setHeaderText("Already other waiter logged on");
                alert.setContentText(" Current waiter should log off first");
                alert.showAndWait();
            }
        }
    }

    private boolean isInputValid(){
        StringBuilder sb = new StringBuilder();
        if (loginName.getText().length() == 0 || loginName.getText() == null){
            sb.append("Please enter name \n");
        }
        if (passwordField.getText().length() == 0 || passwordField.getText() == null){
            sb.append("Please enter password \n");
        }

        if (sb.toString().length() ==0){
            return true;
        }
        else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initOwner(dialogStage);
            alert.setTitle("Incorrect fields");

            alert.setContentText(sb.toString());

            alert.showAndWait();
            return false;
        }
    }
}