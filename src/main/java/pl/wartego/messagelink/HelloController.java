package pl.wartego.messagelink;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;


import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.util.ResourceBundle;

public class HelloController implements Initializable {
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






//    @FXML
//    protected void onHelloButtonClick() {
//        progressIndicator.setProgress(1);
//    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        //insert Main Logo
        circleLogo.setStroke(Color.WHITE);
        Image imageLogo = new Image(getClass().getResourceAsStream("/messagelinkLogo.png"));

        circleLogo.setFill(new ImagePattern(imageLogo));
        circleLogo.setEffect(new DropShadow(+25d,0d,+2d,Color.WHITESMOKE));

      //insert Logo into Button Exit

        Image imageExit = new Image(getClass().getResourceAsStream("/closeIcon.png"));
        ImageView imageViewExit = new ImageView(imageExit);
        imageViewExit.setFitHeight(30);
        imageViewExit.setFitWidth(30);
        circleExit.setFill(new ImagePattern(imageExit));
        circleExit.setStroke(Color.TRANSPARENT);
        circleExit.setEffect(new DropShadow(20,Color.WHITE));
        try {
            Connection connection = DatabaseConnection.getConnection();
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
    protected void login() throws IOException {
       EmailService service = new EmailService();
       service.getEmailConfigResources();
    }
}
