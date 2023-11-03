package com.example.videoplayer.Controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class AboutController {
    @FXML
    Button button;
    @FXML
    private void aboutClose(){
        Stage stage = (Stage) button.getScene().getWindow();
        stage.close();
    }
}
