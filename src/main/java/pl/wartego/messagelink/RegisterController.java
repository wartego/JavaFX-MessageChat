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
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterController implements Initializable {
    private boolean userExist;
    private String randomCodeValue;

    public Connection databaseConnection;

    @FXML
    private Button cancelButton;
    @FXML
    private Button submitButton;
    @FXML
    private Button registerButton;
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
    private Label infoVerificationSendLabel;
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
        Image imageExit = new Image(getClass().getResourceAsStream("/pictures/closeIcon.png"));
        ImageView imageViewExit = new ImageView(imageExit);
        imageViewExit.setFitHeight(30);
        imageViewExit.setFitWidth(30);
        circleExit.setFill(new ImagePattern(imageExit));
        circleExit.setStroke(Color.TRANSPARENT);
        circleExit.setEffect(new DropShadow(20,Color.WHITE));
        //set verification text field to disable
        verificationCodeTextField.setDisable(true);
        verificationCodeLabel.setDisable(true);

        //SQL DatabaseConnection
        try {
            databaseConnection = DatabaseConnection.getConnection();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        //generate 8 digits CODE for verifiaction
        randomCodeValue = getRandomCode();
        //register button and verification label  at begining is not visiable
        registerButton.setVisible(false);
        infoVerificationSendLabel.setVisible(false);
    }

    @FXML
    protected void userRegistry(ActionEvent event) {

        String insertNewUserQuery = ("INSERT INTO echoLink.users (login, password, FirstName, LastName, Role, email, createDate, verificationCode) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?);");
        try{
            PreparedStatement preparedStatement = databaseConnection.prepareStatement(insertNewUserQuery);

            preparedStatement.setString(1,loginTextField.getText());
            preparedStatement.setString(2,PasswordValidation.HashPasswordUnderRegistration(passwordTextField.getText()));
            preparedStatement.setString(3,firstNameTextField.getText());
            preparedStatement.setString(4, lastNameTextField.getText());
            preparedStatement.setString(5, "BASIC_USER");
            preparedStatement.setString(6, emailTextField.getText());
            preparedStatement.setString(7, getActualDateAndTime());
            preparedStatement.setString(8, randomCodeValue);

            //check If user exist already in database
            if(userExist){
                loginLabelVerify.setText("Please choose different login!");
            } else{
                preparedStatement.executeUpdate();
                //switch to login Page
                SceneController.switchToSceneHello(event);
            }
        } catch (SQLException e){
            e.printStackTrace();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    protected boolean checkIfUserExist(){
        int queryResultInt = 2;
        String countLogin = ("SELECT COUNT(*) AS 'LOGINEXIST' FROM users where login = ?");
        try {
            PreparedStatement statement = databaseConnection.prepareStatement(countLogin);
            statement.setString(1,loginTextField.getText());
            ResultSet queryResult = statement.executeQuery();
            while (queryResult.next()) {
                queryResultInt = queryResult.getInt("LOGINEXIST");
                System.out.println(queryResultInt);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        if (queryResultInt == 1){
            loginLabelVerify.setText("User already exist!!");
            loginLabelVerify.setStyle("-fx-text-fill: #ff0117");
            userExist = true;
            return true;
        } else {
            loginLabelVerify.setText("User is available!!");
            loginLabelVerify.setStyle("-fx-text-fill: #60ff00;-fx-font-size: 20px");
            userExist = false;
            return false;
        }
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
    protected void registerButtonAction(ActionEvent event) throws IOException {
        verificationCodeTextField.setDisable(false);
        verificationCodeLabel.setDisable(false);

        //email sending
        EmailBody.getFileFromResourceAsStream();
        String emailbody = EmailBody.replaceKeyWordInBodyEmail(firstNameTextField.getText(), randomCodeValue);
        EmailService service = new EmailService();
        service.getEmailConfigResources(emailbody);
        infoVerificationSendLabel.setVisible(true);
        submitButton.setVisible(false);
        registerButton.setVisible(true);

        //userRegistry(event);
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

        if(!passwordTextField.getText().isEmpty()) passwordLabelVerify.setText("OK");
        if(!firstNameTextField.getText().isEmpty()) firstNameLabelVerify.setText("OK");
        if(!lastNameTextField.getText().isEmpty()) lastNameLabelVerify.setText("OK");
    }

    protected String getActualDateAndTime(){
        LocalDateTime date = LocalDateTime.now();
        return date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

    }

    protected String getRandomCode(){
        Random random = new Random();
        int i = random.nextInt(999999-100000+1)+100000;
        return String.valueOf(i);
    }

}
