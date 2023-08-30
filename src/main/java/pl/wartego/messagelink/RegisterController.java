package pl.wartego.messagelink;

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
import org.kordamp.ikonli.javafx.FontIcon;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterController implements Initializable {

    @FXML
    private Button cancelButton;
    @FXML
    private Button submitButton;
    @FXML
    private Button exitButton;
    @FXML
    private Circle circleExit;

    @FXML
    private TextField loginTextField;
    @FXML
    private PasswordField passwordTextField;
    @FXML
    private TextField emailTextField;
    @FXML
    private TextField firstNameTextField;
    @FXML
    private TextField lastNameTextField;
    @FXML
    private TextField verificationCodeTextField;
    @FXML
    private Label verificationCodeLabel;
    @FXML
    private FontIcon verificationCodeIcon;

    //labels
    @FXML
    private Label loginLabelVerify;
    @FXML
    private Label passwordLabelVerify;
    @FXML
    private Label emailLabelVerify;
    @FXML
    private Label firstNameLabelVerify;
    @FXML
    private Label lastNameLabelVerify;




    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Image imageExit = new Image(getClass().getResourceAsStream("/closeIcon.png"));
        ImageView imageViewExit = new ImageView(imageExit);
        imageViewExit.setFitHeight(30);
        imageViewExit.setFitWidth(30);
        circleExit.setFill(new ImagePattern(imageExit));
        circleExit.setStroke(Color.TRANSPARENT);
        circleExit.setEffect(new DropShadow(20,Color.WHITE));
        //set verification text field to disable
        verificationCodeTextField.setDisable(true);
        verificationCodeLabel.setDisable(true);

    }

    @FXML
    protected void closeStageButton(){
        Stage currentStage =  (Stage) cancelButton.getScene().getWindow();
        currentStage.close();
    }

    @FXML
    protected void cancelButton(ActionEvent event) throws IOException {
        SceneController.switchToSceneHello(event);
    }
    @FXML
    protected void registerButtonAction(ActionEvent event){
        verificationCodeTextField.setDisable(false);
        verificationCodeLabel.setDisable(false);
    }
    protected boolean emailVerifiaction(String emailFromInput){
        Pattern pattern = Pattern.compile("^([_a-zA-Z0-9-]+(\\.[_a-zA-Z0-9-]+)*@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*(\\.[a-zA-Z]{1,6}))?$");
        Matcher matcher = pattern.matcher(emailFromInput);
        return matcher.matches();
    }
    @FXML
    protected void emailVerficationAction(){
        if(emailVerifiaction(emailTextField.getText()) && !emailTextField.getText().isEmpty()){
            emailLabelVerify.setText("Verification ok");
            emailLabelVerify.setStyle("-fx-text-fill: #0fec0f ; -fx-font-size: 10px");
        } else{
            emailLabelVerify.setText("Wrong email");
            emailLabelVerify.setStyle("-fx-text-fill: rgba(255,0,0,0.84) ; -fx-font-size: 8px ; -fx-font-weight: bold" );
        }
    }
    @FXML
    protected void inputDataValidationIfNotNull(){
        if(!loginTextField.getText().isEmpty()) loginLabelVerify.setText("OK");
        if(!passwordTextField.getText().isEmpty()) passwordLabelVerify.setText("OK");
        if(!firstNameTextField.getText().isEmpty()) firstNameLabelVerify.setText("OK");
        if(!lastNameTextField.getText().isEmpty()) lastNameLabelVerify.setText("OK");
    }
}
