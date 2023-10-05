package pl.messagechat.messageChat.main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class MessageLinkApplication extends Application {
    private static Stage primaryStageObj;
    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) throws IOException {
        primaryStageObj = stage;
        FXMLLoader fxmlLoader = new FXMLLoader(MessageLinkApplication.class.getResource("/pl/messagechat/messageChat/login/login-page.fxml"));
        Parent page = fxmlLoader.load();
        StackPane root = new StackPane(page);
        Scene scene = new Scene(root, 1000, 600);
        scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
        stage.getIcons().add(new Image(MessageLinkApplication.class.getResource("/pictures/AppLogo2.png").toString()));
        stage.setTitle("Message Chat: Client version 0.1");
        stage.setScene(scene);
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.show();
        stage.setResizable(false);
        stage.getScene().getRoot().getStyleClass().add("stage-rounded");




//        stage.initStyle(StageStyle.TRANSPARENT);
//        FXMLLoader fxmlLoader = new FXMLLoader(MessageLinkApplication.class.getResource("login-page.fxml"));
//        Parent page = fxmlLoader.load();
//
//        Rectangle roundedRectangle = new Rectangle(1000,600);
//        roundedRectangle.setArcWidth(30);
//        roundedRectangle.setArcHeight(30);
//        roundedRectangle.setFill(Color.TRANSPARENT);
//
//        StackPane root = new StackPane(page);
//        root.getChildren().add(roundedRectangle);
//
//        Scene scene = new Scene(root);
//        scene.setFill(Color.TRANSPARENT);
//        stage.setScene(scene);
//        stage.show();




        // zaokraglenie nie dziala
//        FXMLLoader fxmlLoader = new FXMLLoader(MessageLinkApplication.class.getResource("login-page.fxml"));
//        Parent page = fxmlLoader.load();
//        StackPane root = new StackPane(page);
//
//        Scene scene = new Scene(root, 1000, 600);
//        scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
////        scene.setFill(Color.TRANSPARENT);
//        stage.setTitle("Message Link Application");
//        stage.setScene(scene);
//        stage.show();


//        to dziala bez zaokroglenia
//         FXMLLoader fxmlLoader = new FXMLLoader(MessageLinkApplication.class.getResource("login-page.fxml"));
//                Parent root = fxmlLoader.load();
//                Scene scene = new Scene(root, 1000, 600);
//                scene.setFill(Color.TRANSPARENT);
//                stage.setTitle("Message Link Application");
//                stage.initStyle(StageStyle.TRANSPARENT);
//                stage.setScene(scene);
//                stage.show();

    }

    public static Stage getPrimaryStageObj(){
        return primaryStageObj;
    }
}