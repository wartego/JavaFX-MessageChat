package pl.messagechat.messageChat.scene;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class SceneController {
    private static Stage stage;
    private static Scene scene;
    public static void switchToSceneHello(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(SceneController.class.getResource("/pl/messagechat/messageChat/login/login-page.fxml")));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

    }
    public static void switchToSceneRegistration(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(SceneController.class.getResource("/pl/messagechat/messageChat/registration/registration-page.fxml")));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public static void switchToSceneChatWindow(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(SceneController.class.getResource("/pl/messagechat/messageChat/chat/chat-page.fxml")));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    public static void switchToSceneRegistrationSuccessWindow(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(SceneController.class.getResource("/pl/messagechat/messageChat/registration/registry-success.fxml")));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    public static void switchToSceneChatWithUserController(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(SceneController.class.getResource("/pl/messagechat/messageChat/registration/chatWithUser.fxml")));
        stage = (Stage)((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
