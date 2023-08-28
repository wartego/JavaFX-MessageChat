package pl.wartego.messagelink;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;

public class HelloController {
    @FXML
    private Label welcomeText;
    @FXML
    private ProgressIndicator progressIndicator;
    @FXML
    private Button buttonProgres;


    @FXML
    protected void onHelloButtonClick() {
        progressIndicator.setProgress(1);
    }

    }
