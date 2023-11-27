package pl.messagechat.messageChat.registration;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.kordamp.ikonli.javafx.FontIcon;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.messagechat.messageChat.login.LoginController;
import pl.messagechat.messageChat.messages.Message;
import pl.messagechat.messageChat.messages.UserNew;
import pl.messagechat.messageChat.scene.SceneController;
import pl.messagechat.messageChat.utils.SocketController;

import java.io.*;
import java.net.Socket;
import java.net.URL;

import java.util.Random;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterController implements Initializable {
    private boolean userExist;
    private String randomCodeValue;
    private File fileToSend;

    @FXML private Button cancelButton;
    @FXML private Button submitButton;
    @FXML private Button registerButton;
    @FXML private Button exitButton;
    @FXML private Button chooseImageButton;
    @FXML private Circle circleExit;
    @FXML private ImageView selectedProfileImage;

    @FXML private TextField loginTextField;
    @FXML private PasswordField passwordTextField;
    @FXML private TextField emailTextField;
    @FXML private TextField firstNameTextField;
    @FXML private TextField lastNameTextField;
    @FXML private TextField verificationCodeTextField;
    @FXML private Label verificationCodeLabel;
    @FXML private Label infoVerificationSendLabel;
    @FXML private FontIcon verificationCodeIcon;

    //labels
    @FXML private Label loginLabelVerify;
    @FXML private Label passwordLabelVerify;
    @FXML private Label emailLabelVerify;
    @FXML private Label firstNameLabelVerify;
    @FXML private Label lastNameLabelVerify;

    private ObjectOutputStream oos ;
    private InputStream is;
    private ObjectInputStream input;
    private OutputStream outputStream;
    private Socket socket ;
    Logger logger = LoggerFactory.getLogger(RegisterController.class);


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
        //register button and verification label  at begining is not visiable
        registerButton.setVisible(false);
        infoVerificationSendLabel.setVisible(false);
    }

    @FXML
    protected void submitButtonAction(ActionEvent event) throws IOException  {
        //generate 8 digits CODE for verification
        randomCodeValue = getRandomCode();
        //get connection
        boolean socketConnectionSuccess = SocketController.createSocketConnection();
        if(socketConnectionSuccess){
            socket = SocketController.getSocket();
            oos = SocketController.getOos();
            is = SocketController.getIs();
            input = SocketController.getInput();
            outputStream = SocketController.getOutputStream();
            logger.info("Connection accepted " + socket.getInetAddress() + ":" + socket.getPort());
        } else{
            logger.error("Could not Connect IO Exception");
            //show error dialog
            LoginController.getInstance().showErrorDialog("Warning"
                    ,"Could not connect to server"
                    , Alert.AlertType.WARNING
                    ,"Please check for firewall issues and check if the server is running."
            );
        }

        UserNew userNew = UserNew.builder()
            .login(loginTextField.getText())
            .password(passwordTextField.getText())
            .email(emailTextField.getText())
            .firstName(firstNameTextField.getText())
            .lastName(lastNameTextField.getText())
            .verificationCode(randomCodeValue)
            .build();

        //Send registration form to Server
        oos.writeObject(userNew);
        oos.flush();

        logger.info("sended registration form to server for user: " + loginTextField.getText());
        //Respond from server
        try{
            Message message = (Message) input.readObject();
            if(message.getMessageBody().contains("Success")){
                verificationCodeTextField.setDisable(false);
                verificationCodeLabel.setDisable(false);
                infoVerificationSendLabel.setVisible(true);
                submitButton.setVisible(false);
                registerButton.setVisible(true);

                // here should be added writeObject and send confifmation to server to add user to SQL
                sendImagetoServer();
            } else {
                logger.info(message.getMessageBody());
            }
        } catch (ClassNotFoundException e) {
            logger.info("Class not found exception!");
            throw new RuntimeException(e);
        }
    }

    @FXML
    protected void userRegistry(ActionEvent event) throws IOException {
                //switch to Registration Success Page
                SceneController.switchToSceneRegistrationSuccessWindow(event);
    }

//    @FXML
//    protected boolean checkIfUserExist(){
//
//        if (queryResultInt == 1){
//            loginLabelVerify.setText("User already exist!!");
//            loginLabelVerify.setStyle("-fx-text-fill: #ff0117");
//            userExist = true;
//            return true;
//        } else {
//            loginLabelVerify.setText("User is available!!");
//            loginLabelVerify.setStyle("-fx-text-fill: #60ff00;-fx-font-size: 20px");
//            userExist = false;
//            return false;
//        }
//    }

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
    protected void emailVerficationAction(){
        if(emailVerifiactionPattern(emailTextField.getText()) && !emailTextField.getText().isEmpty()){
            emailLabelVerify.setText("Verification ok");
            emailLabelVerify.setStyle("-fx-text-fill: #0fec0f ; -fx-font-size: 10px");
        } else{
            emailLabelVerify.setText("Wrong email");
            emailLabelVerify.setStyle("-fx-text-fill: rgba(255,0,0,0.84) ; -fx-font-size: 8px ; -fx-font-weight: bold" );
        }
    }
    protected boolean emailVerifiactionPattern(String emailFromInput){
        Pattern pattern = Pattern.compile("^([_a-zA-Z0-9-]+(\\.[_a-zA-Z0-9-]+)*@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*(\\.[a-zA-Z]{1,6}))?$");
        Matcher matcher = pattern.matcher(emailFromInput);
        return matcher.matches();
    }
    @FXML
    protected void inputDataValidationIfNotNull(){

        if(!passwordTextField.getText().isEmpty()) passwordLabelVerify.setText("OK");
        if(!firstNameTextField.getText().isEmpty()) firstNameLabelVerify.setText("OK");
        if(!lastNameTextField.getText().isEmpty()) lastNameLabelVerify.setText("OK");
    }

    protected String getRandomCode(){
        Random random = new Random();
        int i = random.nextInt(999999-100000+1)+100000;
        return String.valueOf(i);
    }
    @FXML
    protected boolean codeVerification(ActionEvent event) throws IOException {
        if(randomCodeValue.equals(verificationCodeTextField.getText())){
            infoVerificationSendLabel.setText("Verification code match, registration success");
            userRegistry(event);
            return true;
        } else{
            infoVerificationSendLabel.setText("Verification code incorrect");
            return false;
        }
    }
    @FXML
    protected void setChooseImageButtonAction() throws IOException {

        FileChooser fileChooser = new FileChooser();
        fileToSend = fileChooser.showOpenDialog( (Stage) cancelButton.getScene().getWindow());
        if(fileToSend != null){
            System.out.println(fileToSend.getAbsolutePath());
            logger.info("choosen Image path: " + fileToSend.getCanonicalPath());
        }
    }


    protected void sendImagetoServer(){
        if(fileToSend != null){
            try(FileInputStream fileInputStream = new FileInputStream(fileToSend.getAbsolutePath())){

                DataOutputStream dataOutputStream = new DataOutputStream(outputStream);
                String fileName = fileToSend.getName();
                byte[] fileNameBytes = fileName.getBytes();

                byte[] fileContentBytes = new byte[(int) fileToSend.length()];
                fileInputStream.read(fileContentBytes);

                dataOutputStream.writeInt(fileNameBytes.length);
                dataOutputStream.write(fileNameBytes);

                dataOutputStream.writeInt(fileContentBytes.length);
                dataOutputStream.write(fileContentBytes);
                //dataOutputStream.close();
            } catch (IOException e){
                logger.error("Something goes wrong during sending file to server!");
            }
        } else {
            logger.error("file is null");
        }


    }


}
