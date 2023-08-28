module pl.wartego.echolink {
    requires javafx.controls;
    requires javafx.fxml;


    opens pl.wartego.messagelink to javafx.fxml;
    exports pl.wartego.messagelink;
}