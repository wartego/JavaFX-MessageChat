package pl.messagechat.messageChat.chat;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.media.Media;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

import javafx.util.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.messagechat.messageChat.main.MessageLinkApplication;
import pl.messagechat.messageChat.messages.Message;
import pl.messagechat.messageChat.messages.MessageType;
import pl.messagechat.messageChat.messages.Status;
import pl.messagechat.messageChat.messages.User;
import pl.messagechat.messageChat.messages.bubble.BubbleSpec;
import pl.messagechat.messageChat.messages.bubble.BubbledLabel;
import pl.messagechat.messageChat.scene.SceneController;
import pl.messagechat.messageChat.traynotifications.animations.AnimationType;
import pl.messagechat.messageChat.traynotifications.notification.TrayNotification;
import pl.messagechat.messageChat.util.VoicePlayback;
import javafx.scene.media.MediaPlayer;
import pl.messagechat.messageChat.util.VoiceRecorder;
import pl.messagechat.messageChat.util.VoiceUtil;


import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class ChatController implements Initializable {
    @FXML private Button recordBtn;
    @FXML private ComboBox statusComboBox;

    @FXML private Label userNameLabel;
    @FXML private ImageView userImage;
    @FXML private Label onlineCountLabel;
    @FXML private Image image;
    @FXML private TextArea inputMessageBox;
    @FXML private Button submitButton;
    @FXML private ListView listMessagesView;
    @FXML private ListView usersListView;
    @FXML private ImageView microphoneImageView;
    @FXML private BorderPane borderPane;
    private Logger logger = LoggerFactory.getLogger(ChatController.class);
    private double xOffset;
    private double yOffset;
    private Image microphoneActiveImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/pictures/microphone-active.png")));
    private Image microphoneInactiveImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/pictures/microphone.png")));

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        statusComboBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                try {
                    Listener.sendStatusUpdate(Status.valueOf(newValue.toUpperCase()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        /* Added to prevent the enter from adding a new line to inputMessageBox */
        inputMessageBox.addEventFilter(KeyEvent.KEY_PRESSED, ke -> {
            if (ke.getCode().equals(KeyCode.ENTER)) {
                try {
                    sendButtonAction();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                ke.consume();
            }
        });

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

    }

    public void setUserNameLabel(String username){
        this.userNameLabel.setText(username);
    }
    public void setImageLabel() throws IOException{
        this.userImage.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/pictures/dominic.png"))));
//        image = LoginController.getDefaultUserImage();
//        userImage.setImage(image);
//        userImage.setFitWidth(60);
    }
    public void setOnlineLabel(String usercount) {
        Platform.runLater(() -> onlineCountLabel.setText(usercount));
    }
    public void setUserList(Message msg) {
        logger.info("setUserList() method Enter");
        Platform.runLater(() -> {
            ObservableList<User> users = FXCollections.observableList(msg.getUsers());
            usersListView.setItems(users);
            usersListView.setCellFactory(new CellRenderer());
            setOnlineLabel(String.valueOf(msg.getUserlist().size()));
        });
        logger.info("setUserList() method Exit");
    }
    public void sendMethod(KeyEvent event) throws IOException {
        if (event.getCode() == KeyCode.ENTER) {
            sendButtonAction();
        }
    }
    @FXML
    private void sendButtonAction() throws IOException {
        String msg = inputMessageBox.getText();
        if (!inputMessageBox.getText().isEmpty()) {
            Listener.send(msg);
            inputMessageBox.clear();
        }
    }
    /* Method to display server messages */
    public synchronized void addAsServer(Message msg) {
        Task<HBox> task = new Task<HBox>() {
            @Override
            public HBox call() throws Exception {
                BubbledLabel bl6 = new BubbledLabel();
                bl6.setText(msg.getMsg());
                bl6.setBackground(new Background(new BackgroundFill(Color.ALICEBLUE,
                        null, null)));
                HBox x = new HBox();
                bl6.setBubbleSpec(BubbleSpec.FACE_BOTTOM);
                x.setAlignment(Pos.CENTER);
                x.getChildren().addAll(bl6);
                return x;
            }
        };
        task.setOnSucceeded(event -> {
            listMessagesView.getItems().add(task.getValue());
        });

        Thread t = new Thread(task);
        t.setDaemon(true);
        t.start();
    }

    @FXML
    public void logoutScene(ActionEvent event) {

        Platform.runLater(() -> {
            try {
                SceneController.switchToSceneHello(event);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public synchronized void addToChat(Message msg) {
        Task<HBox> othersMessages = new Task<HBox>() {
            @Override
            public HBox call() throws Exception {
                Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/pictures/" + msg.getPicture().toLowerCase() + ".png")));
                ImageView profileImage = new ImageView(image);
                profileImage.setFitHeight(32);
                profileImage.setFitWidth(32);
                BubbledLabel bl6 = new BubbledLabel();
                if (msg.getType() == MessageType.VOICE){
                    ImageView imageview = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/pictures/sound.png"))));
                    bl6.setGraphic(imageview);
                    bl6.setText("Sent a voice message!");
                    VoicePlayback.playAudio(msg.getVoiceMsg());
                }else {
                    bl6.setText(msg.getName() + ": " + msg.getMsg());
                }
                bl6.setBackground(new Background(new BackgroundFill(Color.WHITE,null, null)));
                HBox x = new HBox();
                bl6.setBubbleSpec(BubbleSpec.FACE_LEFT_CENTER);
                x.getChildren().addAll(profileImage, bl6);
                logger.debug("ONLINE USERS: " + (msg.getUserlist().size()));
                setOnlineLabel(Integer.toString(msg.getOnlineCount()));
                return x;
            }
        };

        othersMessages.setOnSucceeded(event -> {
            listMessagesView.getItems().add(othersMessages.getValue());
        });

        Task<HBox> yourMessages = new Task<HBox>() {
            @Override
            public HBox call() {
                Image image = userImage.getImage();
                ImageView profileImage = new ImageView(image);
                profileImage.setFitHeight(32);
                profileImage.setFitWidth(32);

                BubbledLabel bl6 = new BubbledLabel();
                if (msg.getType() == MessageType.VOICE){
                    bl6.setGraphic(new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/pictures/sound.png")))));
                    bl6.setText("Sent a voice message!");
                    VoicePlayback.playAudio(msg.getVoiceMsg());
                }else {
                    bl6.setText(msg.getMsg());
                }
                bl6.setBackground(new Background(new BackgroundFill(Color.LIGHTGREEN,
                        null, null)));
                HBox x = new HBox();
                x.setMaxWidth(listMessagesView.getWidth() - 20);
                x.setAlignment(Pos.TOP_RIGHT);
                bl6.setBubbleSpec(BubbleSpec.FACE_RIGHT_CENTER);
                x.getChildren().addAll(bl6, profileImage);

                setOnlineLabel(Integer.toString(msg.getOnlineCount()));
                return x;
            }
        };
        yourMessages.setOnSucceeded(event -> listMessagesView.getItems().add(yourMessages.getValue()));

        if (msg.getName().equals(userNameLabel.getText())) {
            Thread t2 = new Thread(yourMessages);
            t2.setDaemon(true);
            t2.start();
        } else {
            Thread t = new Thread(othersMessages);
            t.setDaemon(true);
            t.start();
        }
    }

    /* Displays Notification when a user joins */
    public void newUserNotification(Message msg) {
        Platform.runLater(() -> {
            Image profileImg = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/pictures/" + msg.getPicture().toLowerCase() + ".png")),50,50,false,false);
            TrayNotification tray = new TrayNotification();
            tray.setTitle("A new user has joined!");
            tray.setMessage(msg.getName() + " has joined the JavaFX Chatroom!");
            tray.setRectangleFill(Paint.valueOf("#2C3E50"));
            tray.setAnimationType(AnimationType.POPUP);
            tray.setImage(profileImg);
            tray.showAndDismiss(Duration.seconds(5));
            try {
                Media hit = new Media(getClass().getResource("/sounds/Messenger_Facebook.wav").toExternalForm());
                MediaPlayer mediaPlayer = new MediaPlayer(hit);
                mediaPlayer.play();
            } catch (NullPointerException e) {
                e.printStackTrace();
            }

        });
    }
    public void recordVoiceMessage() {
        if (VoiceUtil.isRecording()) {
            Platform.runLater(() -> microphoneImageView.setImage(microphoneInactiveImage)
            );
            VoiceUtil.setRecording(false);
        } else {
            Platform.runLater(() -> microphoneImageView.setImage(microphoneActiveImage)
            );
            VoiceRecorder.captureAudio();
        }
    }
    @FXML
    public void closeApplication() {
        Platform.exit();
        System.exit(0);
    }
}
