module pl.wartego.messagelink{
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires javafx.base;
    requires org.kordamp.ikonli.fontawesome5;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.ikonli.core;
    requires java.desktop;
    requires java.sql;
    requires mysql.connector.j;
    requires jakarta.mail;
    requires spring.security.crypto;
    requires slf4j.api;
    requires javafx.media;


    exports pl.messagechat.messageChat.database;
    opens pl.messagechat.messageChat.database to javafx.fxml;
    exports pl.messagechat.messageChat.login;
    opens pl.messagechat.messageChat.login to javafx.fxml;
    exports pl.messagechat.messageChat.registration;
    opens pl.messagechat.messageChat.registration to javafx.fxml;
    exports pl.messagechat.messageChat.utils;
    opens pl.messagechat.messageChat.utils to javafx.fxml;
    exports pl.messagechat.messageChat.chat;
    opens pl.messagechat.messageChat.chat to javafx.fxml;
    exports pl.messagechat.messageChat.emails;
    opens pl.messagechat.messageChat.emails to javafx.fxml;
    exports pl.messagechat.messageChat.main;
    opens pl.messagechat.messageChat.main to javafx.fxml;
    exports pl.messagechat.messageChat.scene;
    opens pl.messagechat.messageChat.scene to javafx.fxml;
    exports pl.messagechat.messageChat.traynotifications.notification;
    opens pl.messagechat.messageChat.traynotifications.notification to javafx.fxml;

}