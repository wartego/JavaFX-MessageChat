package pl.wartego.messageChat.registration;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import pl.wartego.messageChat.SceneController;


import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class RegistrySuccessController implements Initializable {
    @FXML
    private ImageView logoApp;

    @FXML
    private Button sumbitButton;
    @FXML
    private Button exitButton;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

//        Image imageLogo = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/pictures/JavaFXLogoChat3.png")));
//        logoApp.setImage(imageLogo);
        logoApp.setEffect(new DropShadow(+25d,0d,+2d,Color.WHITESMOKE));
    }

    @FXML
    protected void closeStage(){
        Stage currentStage =  (Stage) logoApp.getScene().getWindow();
        currentStage.close();
    }

    @FXML
    protected void setSumbitButton(ActionEvent event) throws IOException {
        SceneController.switchToSceneHello(event);
    }

}
