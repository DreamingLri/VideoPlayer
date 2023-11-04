package com.example.videoplayer.Controller;

import com.example.videoplayer.VideoPlayerApplication;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class MainPanelController {
    @FXML
    public MediaView mv;
    public MediaPlayer mp;

    @FXML
    private Slider timeSlider;

    @FXML
    private Slider voiceSlider;

    @FXML
    private Button playBtn;

    @FXML
    private Button voiceBtn;

    @FXML
    private Button fullBtn;

    @FXML
    private Button stopBtn;

    @FXML
    private Button forwardBtn;

    @FXML
    private Button backwardBtn;

    @FXML
    private Label timeLabel;

    @FXML
    private Label nameLabel;

    @FXML
    private BorderPane borderPane;

    @FXML
    private MenuBar menu;

    @FXML
    private ImageView imageView;

    @FXML
    private Pane backGroundPane;

    private Scene scene;

    private Stage stage;

    @FXML
    private HBox hboxTop;

    @FXML
    private HBox hboxButtom;

    @FXML
    private HBox hboxCenter;

    @FXML
    private AnchorPane mainPane;


    private boolean isFullWindow = false;
    private boolean isButtonVisible = true;
    private boolean isPlaying = false;
    private boolean isMute = false;
    private boolean isStop = false;

    private double currentWidth;
    private double currentHeight;
    private final double iconWidth = 40;
    private final double iconHeight = 40;
    private int totalTime;

    private String endIcon;
    private String fullScreenIcon;
    private String playIcon;
    private String pauseIcon;
    private String voiceOnIcon;
    private String voiceOffIcon;
    private String backGround;
    private String backwardIcon;
    private String forwardIcon;

    public MainPanelController() {
    }

    public void init(){

        playIcon = getClass().getResource("/icon/play.png").toString();
        pauseIcon = getClass().getResource("/icon/pause.png").toString();
        endIcon = getClass().getResource("/icon/end.png").toString();
        voiceOnIcon = getClass().getResource("/icon/voice-on.png").toString();
        voiceOffIcon = getClass().getResource("/icon/voice-off.png").toString();
        backGround = getClass().getResource("/icon/main.png").toString();
        fullScreenIcon = getClass().getResource("/icon/full-screen.png").toString();
        backwardIcon = getClass().getResource("/icon/backward.png").toString();
        forwardIcon = getClass().getResource("/icon/forward.png").toString();

        setIcon(playBtn,playIcon);
        setIcon(voiceBtn,voiceOnIcon);
        setIcon(fullBtn,fullScreenIcon);
        setIcon(stopBtn,endIcon);
        setIcon(backwardBtn,backwardIcon);
        setIcon(forwardBtn,forwardIcon);

        scene = mainPane.getScene();
        stage = (Stage) mainPane.getScene().getWindow();
        scene.widthProperty().addListener((observable, oldValue, newValue) -> {
            currentWidth = newValue.intValue();
            adjustSize();
        });
        scene.heightProperty().addListener((observable, oldValue, newValue) -> {
            currentHeight = newValue.intValue();
            adjustSize();
        });
        adjustSize();
        currentWidth = mainPane.getWidth();
        currentHeight = mainPane.getHeight();

        Image bg = new Image(backGround);
        imageView.setImage(bg);
        imageView.setFitHeight(bg.getHeight());
        imageView.setFitWidth(bg.getWidth());
        adjustSize();
    }

    private void adjustSize() {
        mainPane.setPrefSize(currentWidth, currentHeight);
        AnchorPane.setTopAnchor(menu,0.0);
        AnchorPane.setTopAnchor(mv,menu.getHeight());
        AnchorPane.setBottomAnchor(borderPane,0.0);
        mv.setFitHeight(currentHeight - menu.getHeight());
        mv.setFitWidth(currentWidth);
        menu.setPrefWidth(currentWidth);
        borderPane.setPrefWidth(currentWidth);
        AnchorPane.setTopAnchor(imageView, currentHeight / 2 - imageView.getFitHeight() / 2);
        AnchorPane.setLeftAnchor(imageView, currentWidth / 2 - imageView.getFitWidth() / 2);
        backGroundPane.setPrefWidth(currentWidth);
        backGroundPane.setPrefHeight(currentHeight);
    }

    private void setIcon(Button button, String path) {
        Image icon = new Image(path);
        ImageView imageView = new ImageView(icon);
        imageView.setFitWidth(iconWidth);
        imageView.setFitHeight(iconHeight);
        button.setGraphic(imageView);
    }
    @FXML
    public void selectVideo(){
        FileChooser fc = new FileChooser();
        fc.setTitle("Please choose a mp4 file");
        fc.setSelectedExtensionFilter(new FileChooser.ExtensionFilter("mp4 file","*.mp4"));
        File file = fc.showOpenDialog(null);
        if(!file.toString().endsWith(".mp4")){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Warning");
            alert.setHeaderText("You seems not open a mp4 file");
            alert.show();
        } else {
            if(file != null){
//            System.out.println(file.getAbsolutePath());
                Media media = null;
                try {
                    media = new Media(file.toURI().toURL().toString());
                } catch (MalformedURLException e) {
                    throw new RuntimeException(e);
                }
                if(mp != null){
                    mp.stop();
                }
                mp = new MediaPlayer(media);
                isPlaying = false;
                setIcon(playBtn,playIcon);

                mp.setOnReady(new Runnable() {
                    @Override
                    public void run() {
                        Duration duration = mp.getMedia().getDuration();
                        timeSlider.setMax(duration.toSeconds());
                        totalTime = (int) duration.toSeconds();
                        timeLabel.setText("00:00/" + timeFormat(totalTime));
                        nameLabel.setText(file.getName());

                        int height = mp.getMedia().getHeight();
                        int width = mp.getMedia().getWidth();
//                    System.out.println(width);
                        mv.setFitHeight(height*0.5);
                        mv.setFitWidth(width*0.5);
                        stage.setWidth(width*0.5);
                        voiceSlider.setValue(50.0);
                    }
                });
                mp.currentTimeProperty().addListener(new InvalidationListener() {
                    @Override
                    public void invalidated(Observable observable) {
                        timeSlider.setValue(mp.getCurrentTime().toSeconds());
                        timeLabel.setText(timeFormat((int) mp.getCurrentTime().toSeconds()) + "/" + timeFormat(totalTime));
                    }
                });
                mv.setMediaPlayer(mp);
            }
        }
    }

    @FXML
    public void play(){
        if(mp == null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Warning");
            alert.setHeaderText("No Video has been loaded");
            alert.show();
        } else {
            if(!isPlaying){
                mp.play();
                isPlaying = true;
//                playBtn.setText("Pause");
                setIcon(playBtn,pauseIcon);
            } else {
                mp.pause();
                isPlaying = false;
//                playBtn.setText("Play");
                setIcon(playBtn,playIcon);
            }
        }
    }

    @FXML
    private void forward(){
        if(mp != null){
            double value = mp.getCurrentTime().toSeconds();
            if(value + 5 <= mp.getStopTime().toSeconds()){
                mp.seek(Duration.seconds(value+5));
            }
        }
    }

    @FXML
    private void backward(){
        if(mp != null){
            double value = mp.getCurrentTime().toSeconds();
            if(value - 5 >= mp.getStartTime().toSeconds()){
                mp.seek(Duration.seconds(value-5));
            }
        }
    }

    @FXML
    private void initTime(){
        if(mp != null){
            timeSlider.valueProperty().addListener(new ChangeListener<Number>() {
                @Override
                public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                    double value = t1.doubleValue();
                    if(value == mp.getCurrentTime().toSeconds()){
                        return;
                    }
                    mp.seek(Duration.seconds(value));
                }
            });
        }
    }

    @FXML
    private void initVoice(){
        if(mp != null){
            voiceSlider.valueProperty().addListener(new ChangeListener<Number>() {
                @Override
                public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                    mp.setVolume(t1.doubleValue() / 100);
                }
            });
        }
    }

    @FXML
    private void changeMute(){
        if(mp != null){
            if(!isMute){
                isMute = true;
                setIcon(voiceBtn,voiceOffIcon);
                mp.setMute(true);
//                voiceBtn.setText("voice-off");
            } else {
                isMute = false;
                setIcon(voiceBtn,voiceOnIcon);
                mp.setMute(false);
//                voiceBtn.setText("voice-on");
            }
        }
    }


    private String timeFormat(int second){
        int minute = second / 60;
        second %= 60;
        String minStr = "";
        String secStr = "";
        if(minute < 10){
            minStr = "0" + minute;
        } else {
            minStr = minute + "";
        }
        if(second < 10){
            secStr = "0" + second;
        } else {
            secStr = second + "";
        }
        return minStr+":"+secStr;
    }

    @FXML
    private void fullScreen(){
        if(!isFullWindow){
            stage.setFullScreen(true);
            if(isButtonVisible){
                hboxCenter.setVisible(false);
                isButtonVisible = false;
            }
            isFullWindow = true;
        } else {
            stage.setFullScreen(false);
            if(!isButtonVisible){
                hboxCenter.setVisible(true);
                isButtonVisible = true;
            }
            isFullWindow = false;
        }
    }

    @FXML
    private void closeVideo(){
        if(mp != null){
            mp.stop();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Warning");
            alert.setHeaderText("No Video has been loaded");
            alert.show();
        }
    }


    @FXML
    private void Exit(){
        stage.close();
    }

    @FXML
    private void About(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(VideoPlayerApplication.class.getResource("about.fxml"));
        Parent root = fxmlLoader.load();
        Stage newStage=new Stage();
        newStage.setTitle("About");
        newStage.setScene(new Scene(root, 550, 190));
        newStage.show();
    }

//    @FXML
//    private void setting() throws IOException {
//        FXMLLoader fxmlLoader = new FXMLLoader(VideoPlayerApplication.class.getResource("setting.fxml"));
//        Parent root = fxmlLoader.load();
//        Stage newStage=new Stage();
//        newStage.setTitle("Settings");
//        newStage.setScene(new Scene(root, 550, 190));
//        newStage.show();
//    }
}
