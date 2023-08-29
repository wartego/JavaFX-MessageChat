package pl.wartego.messagelink;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class HelloController implements Initializable {
@FXML
private Button exitButton;

@FXML
private Circle circleLogo;

@FXML
private Circle circleExit;




//    @FXML
//    protected void onHelloButtonClick() {
//        progressIndicator.setProgress(1);
//    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        //insert Main Logo
        circleLogo.setStroke(Color.WHITE);
        Image image = new Image("file:src/main/java/pl/wartego/messagelink/images/messagelinkLogo.png",false);
         //Image img = new Image(getClass().getResourceAsStream("barrel_icon.png"));
        circleLogo.setFill(new ImagePattern(image));
        circleLogo.setEffect(new DropShadow(+25d,0d,+2d,Color.WHITESMOKE));

      //insert Logo into Button Exit

        Image imageExit = new Image("file:src/main/java/pl/wartego/messagelink/images/closeIcon.png",false);
        ImageView imageViewExit = new ImageView(imageExit);
        imageViewExit.setFitHeight(30);
        imageViewExit.setFitWidth(30);
        circleExit.setFill(new ImagePattern(imageExit));
        circleExit.setStroke(Color.TRANSPARENT);
        circleExit.setEffect(new DropShadow(20,Color.WHITE));
    }

    @FXML
    protected void closeStage(){
        Stage currentStage =  (Stage) circleLogo.getScene().getWindow();
        currentStage.close();
    }
}
