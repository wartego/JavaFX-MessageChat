module pl.wartego.echolink {
    requires javafx.controls;
    requires javafx.fxml;


    opens pl.wartego.echolink to javafx.fxml;
    exports pl.wartego.echolink;
}