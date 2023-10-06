package pl.messagechat.messageChat.login;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene. Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.messagechat.messageChat.chat.ChatController;
import pl.messagechat.messageChat.chat.Listener;
import pl.messagechat.messageChat.main.MessageLinkApplication;
import pl.messagechat.messageChat.messages.MessageType;
import pl.messagechat.messageChat.messages.UserOnlyLogin;
import pl.messagechat.messageChat.scene.SceneController;
import pl.messagechat.messageChat.database.DatabaseConnection;
import pl.messagechat.messageChat.util.ResizeHelper;
import pl.messagechat.messageChat.utils.PasswordValidation;


import java.io.*;
import java.net.Socket;
import java.net.URL;
import java.sql.*;
import java.util.Objects;
import java.util.ResourceBundle;

public class LoginController implements Initializable {
    private Connection  connection;
    private String hostname = "localhost";
    private int port = 5555;
    private String picture = "Dominic";
    private static LoginController instance;
    public static ChatController chatController;
    private String currentUser;
    private double xOffset;
    private double yOffset;
    private String loginChoose;
    private String passwordChoose;

    @FXML private Button loginButton;
    @FXML private Button cancelButton;
    @FXML private Button registerButton;
    @FXML private Circle circleLogo;
    @FXML private Circle circleExit;
    @FXML private TextField loginTextField;
    @FXML private PasswordField passwordTextField;
    @FXML private Label loginMessageLabel;
    @FXML private ImageView defaultUserImageView;
    @FXML private BorderPane borderPane;
    private static Image defaultUserImage;
    private Scene scene;
    static Logger logger = LoggerFactory.getLogger(LoginController.class);

    private ObjectOutputStream oos ;
    private InputStream is;
    private ObjectInputStream input;
    private OutputStream outputStream;
    private Socket socket ;






    public LoginController(){
        instance = this;
    }
    public static LoginController getInstance(){
        return instance;
    }

    public String getLoginChoose() {
        return loginChoose;
    }

    public void setLoginChoose(String loginChoose) {
        this.loginChoose = loginChoose;
    }

    public String getPasswordChoose() {
        return passwordChoose;
    }

    public void setPasswordChoose(String passwordChoose) {
        this.passwordChoose = passwordChoose;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {


        //insert Main Logo
        circleLogo.setStroke(Color.WHITE);
        Image imageLogo = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/pictures/messagelinkLogo.png")));
        circleLogo.setFill(new ImagePattern(imageLogo));
        circleLogo.setEffect(new DropShadow(+25d,0d,+2d,Color.WHITESMOKE));

      //insert Logo into Button Exit

        Image imageExit = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/pictures/closeIcon.png")));
        ImageView imageViewExit = new ImageView(imageExit);
        imageViewExit.setFitHeight(30);
        imageViewExit.setFitWidth(30);
        circleExit.setFill(new ImagePattern(imageExit));
        circleExit.setStroke(Color.TRANSPARENT);
        circleExit.setEffect(new DropShadow(20,Color.WHITE));

        // Set User Logo (default always the same)
         defaultUserImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/pictures/sarah.png")));
        defaultUserImageView.setImage(defaultUserImage);
        defaultUserImageView.setFitHeight(60);
        defaultUserImageView.setFitWidth(60);
        defaultUserImageView.setEffect(new DropShadow(20,Color.WHITE));

        /* Drag and Drop */
        borderPane.setOnMousePressed(event -> {
            xOffset = MessageLinkApplication.getPrimaryStageObj().getX() - event.getScreenX();
            yOffset = MessageLinkApplication.getPrimaryStageObj().getY() - event.getScreenY();
            borderPane.setCursor(Cursor.CLOSED_HAND);
        });

        borderPane.setOnMouseDragged(event -> {
            MessageLinkApplication.getPrimaryStageObj().setX(event.getScreenX() + xOffset);
            MessageLinkApplication.getPrimaryStageObj().setY(event.getScreenY() + yOffset);

        });

        borderPane.setOnMouseReleased(event -> {
            borderPane.setCursor(Cursor.DEFAULT);
        });

        //DateBaseConnection
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
    protected void loginButtonOnClick(ActionEvent event) throws IOException {

        System.out.println("Login start");
        if (!loginTextField.getText().isBlank() && !passwordTextField.getText().isBlank()) {
            loginMessageLabel.setText("You try to login");
            String userName = loginTextField.getText();
            setLoginChoose(loginTextField.getText());
            setPasswordChoose(passwordTextField.getText());
            try{
                if(socket == null){
                    socket = new Socket("localhost",5555);
                }
                if(outputStream == null){
                    outputStream = socket.getOutputStream();
                }
                if(oos == null){
                    oos = new ObjectOutputStream(outputStream);
                }
                if(is == null){
                    is = socket.getInputStream();
                }
                if(input == null){
                    input = new ObjectInputStream(is);
                }

            } catch (IOException e) {
                logger.error("Could not Connect IO Exception");
            }
            logger.info("Connection accepted " + socket.getInetAddress() + ":" + socket.getPort());

            try {
                UserOnlyLogin userOnlyLogin = new UserOnlyLogin();
                userOnlyLogin.setPassword(passwordChoose);
                userOnlyLogin.setLogin(loginChoose);
                userOnlyLogin.setMessageType(MessageType.FIRSTLOGIN);
                oos.writeObject(userOnlyLogin);
                oos.flush();
                logger.info("Sended login to verification");
                    ///tutaj program czeka na
                    UserOnlyLogin o = (UserOnlyLogin) input.readObject();
                    if(o != null){
                        if(o.getLogin().equals("NO")){
                            loginMessageLabel.setText("WRONG LOGIN/PASSWORD!");
                            logger.info("Password is incorrect, please try again");
                            System.out.println("Password is incorrect, please try again");
                        } else {
                            logger.info("password match");
                            System.out.println("password match");

                            openChatPageAfterSuccessfulLogin(userName);
                        }
                    }
            } catch (Exception e){
                e.printStackTrace();
                logger.error("Something goes wrong during login verification");
            }
        } else {
            loginMessageLabel.setText("Please input login and password first!");
            setUserAndPasswordFieldBlank();
            //todo
        }
    }

    private void openChatPageAfterSuccessfulLogin(String userName) throws IOException {
        FXMLLoader fmxlLoader = new FXMLLoader(getClass().getResource("/pl/messagechat/messageChat/chat/chat-page.fxml"));
        Parent window = (Pane) fmxlLoader.load();
        chatController = fmxlLoader.<ChatController>getController();
        Listener listener = new Listener(hostname, port, userName, picture, chatController);
        this.scene = new Scene(window);
        Thread x = new Thread(listener);
        x.start();
    }

    private void setUserAndPasswordFieldBlank() {
        loginTextField.setText("");
        passwordTextField.setText("");
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

    public static Image getDefaultUserImage() {
        return defaultUserImage;
    }

    public void showScene() {
        Platform.runLater(()-> {
            Stage stage = (Stage) loginButton.getScene().getWindow();
            stage.setResizable(true);
            stage.setWidth(1000);
            stage.setHeight(600);

            stage.setOnCloseRequest((WindowEvent e) -> {
                Platform.exit();
                System.exit(0);
            });
            stage.setScene(this.scene);
            ResizeHelper.addResizeListener(stage);
            stage.centerOnScreen();
            chatController.setUserNameLabel(loginTextField.getText());
            try {
                chatController.setImageLabel();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public void closeSystem(){
        Platform.exit();
        System.exit(0);
    }
    public void minimizeWindow(){
        MessageLinkApplication.getPrimaryStageObj().setIconified(true);
    }

    public void showErrorDialog(String message) {
        Platform.runLater(()-> {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning!");
            alert.setHeaderText(message);
            alert.setContentText("Please check for firewall issues and check if the server is running.");
            alert.showAndWait();
        });
    }
}
