package com.example.videoplayer;

import com.example.videoplayer.Controller.MainPanelController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class VideoPlayerApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(VideoPlayerApplication.class.getResource("video-player.fxml"));
        Parent root = fxmlLoader.load();
        MainPanelController controller = fxmlLoader.getController();
        stage.setTitle("Video Player!");
        stage.setScene(new Scene(root,1000,700));
        stage.show();
        controller.init();
    }

    public static void main(String[] args) {
        launch();
    }
}