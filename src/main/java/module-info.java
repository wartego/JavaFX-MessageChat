module pl.wartego.messagelink{
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires javafx.base;
    requires org.kordamp.ikonli.fontawesome5;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.ikonli.core;



    opens pl.wartego.messagelink to javafx.fxml;
    exports pl.wartego.messagelink;
}