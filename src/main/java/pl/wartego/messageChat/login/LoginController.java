package pl.wartego.messageChat.login;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import pl.wartego.messageChat.scene.SceneController;
import pl.wartego.messageChat.database.DatabaseConnection;
import pl.wartego.messageChat.utils.PasswordValidation;


import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class LoginController implements Initializable {
    private Connection  connection;
    private String currentUser;
    @FXML
    private Button loginButton;
    @FXML
    private Button cancelButton;
    @FXML
    private Button registerButton;
    @FXML
    private Circle circleLogo;
    @FXML
    private Circle circleExit;
    @FXML
    private TextField loginTextField;
    @FXML
    private PasswordField passwordTextField;
    @FXML
    private Label loginMessageLabel;

//    @FXML
//    protected void onHelloButtonClick() {
//        progressIndicator.setProgress(1);
//    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        //insert Main Logo
        circleLogo.setStroke(Color.WHITE);
        Image imageLogo = new Image(getClass().getResourceAsStream("/pictures/messagelinkLogo.png"));
        circleLogo.setFill(new ImagePattern(imageLogo));
        circleLogo.setEffect(new DropShadow(+25d,0d,+2d,Color.WHITESMOKE));

      //insert Logo into Button Exit

        Image imageExit = new Image(getClass().getResourceAsStream("/pictures/closeIcon.png"));
        ImageView imageViewExit = new ImageView(imageExit);
        imageViewExit.setFitHeight(30);
        imageViewExit.setFitWidth(30);
        circleExit.setFill(new ImagePattern(imageExit));
        circleExit.setStroke(Color.TRANSPARENT);
        circleExit.setEffect(new DropShadow(20,Color.WHITE));
        try {
           connection = DatabaseConnection.getConnection();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    protected void closeStage(){
        Stage currentStage =  (Stage) circleLogo.getScene().getWindow();
        currentStage.close();
    }
    @FXML
    protected void loginButtonOnClick(ActionEvent event) throws SQLException {

        if (!loginTextField.getText().isBlank() && !passwordTextField.getText().isBlank()) {
            loginMessageLabel.setText("You try to login");
            validateLogin(event); // call Method
        } else {
            loginMessageLabel.setText("Please input login and password first!");
            setUserAndPasswordFieldBlank();
        }
    }

    private void setUserAndPasswordFieldBlank() {
        loginTextField.setText("");
        passwordTextField.setText("");
    }
    @FXML
    protected void validateLogin(ActionEvent event) throws SQLException{

        PreparedStatement preparedStatement = connection.prepareStatement("SELECT password FROM users WHERE login = ? ");
        preparedStatement.setString(1,loginTextField.getText());
        try{
            ResultSet quaryResult = preparedStatement.executeQuery();
            while (quaryResult.next()){
                if(PasswordValidation.ifHashMatchToPassword(quaryResult.getString(1), passwordTextField.getText())){
                    currentUser = loginTextField.getText();
                    loginMessageLabel.setText("LOGIN MATCH");
                    //SceneController.switchToSceneChatWindow(event);
                    ClientSocket.newClient();
                } else {
                    loginMessageLabel.setText("Incorrect login or password, please try again!");
                }
            }
        } catch (SQLException e){
            e.printStackTrace();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }
    @FXML
    protected void registrationSwitch(ActionEvent event) throws IOException {
       SceneController.switchToSceneRegistration(event);
    }

    @FXML
    protected void sendEmail(){
//        EmailService service = new EmailService();
//        service.getEmailConfigResources();
    }


}
