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


    exports pl.wartego.messageChat.database;
    opens pl.wartego.messageChat.database to javafx.fxml;
    exports pl.wartego.messageChat.login;
    opens pl.wartego.messageChat.login to javafx.fxml;
    exports pl.wartego.messageChat.registration;
    opens pl.wartego.messageChat.registration to javafx.fxml;
    exports pl.wartego.messageChat.utils;
    opens pl.wartego.messageChat.utils to javafx.fxml;
    exports pl.wartego.messageChat.chat;
    opens pl.wartego.messageChat.chat to javafx.fxml;
    exports pl.wartego.messageChat.emails;
    opens pl.wartego.messageChat.emails to javafx.fxml;
    exports pl.wartego.messageChat.main;
    opens pl.wartego.messageChat.main to javafx.fxml;
    exports pl.wartego.messageChat.scene;
    opens pl.wartego.messageChat.scene to javafx.fxml;

}