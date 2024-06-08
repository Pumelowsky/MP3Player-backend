package com.mp3server.Controllers;

import com.mp3server.client.SongData;
import javafx.animation.ScaleTransition;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Duration;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class MainPaneController {
    @FXML
    private Button playButton;
    @FXML
    private Button pauseButton;
    @FXML
    private Button resetSongButton;
    @FXML
    private Button prevSongButton;
    @FXML
    private Button nextSongButton;
    @FXML
    private ComboBox<String> speedBox;
    @FXML
    private Slider volumeSlider;
    @FXML
    private Label songLabel;
    @FXML
    private TableView<SongData> songTable;
    @FXML
    private TableColumn<SongData, String> songNameColumn;
    @FXML
    private TableColumn<SongData, String> songDurationColumn;
    @FXML
    private TableColumn<SongData, String> songArtistColumn;
    @FXML
    private Label currentSongLabel;
    @FXML
    private ProgressBar songProgresBar;
    @FXML
    private Label wykonawcaLabel;

    private Media media;
    private MediaPlayer mediaPlayer;

    private int songNumber;
    private double[] speeds = {0.25,0.5,0.75,1,1.25,1.5,1.75,2};

    private static final String BASE_URL = "http://localhost:8080";

    @FXML
    public void initialize() throws Exception {
        addScaleTransition(playButton);
        addScaleTransition(pauseButton);
        addScaleTransition(resetSongButton);
        addScaleTransition(prevSongButton);
        addScaleTransition(nextSongButton);

        addPulsingEffect(songLabel);
        songNameColumn.setCellValueFactory(new PropertyValueFactory<>("tytul"));
        songDurationColumn.setCellValueFactory(new PropertyValueFactory<>("duration"));
        songArtistColumn.setCellValueFactory(new PropertyValueFactory<>("wykonawca"));

        try {
            songTable.getItems().addAll(SongData.getAllSongs());
        } catch (Exception e) {
            e.printStackTrace();
        }

        media = new Media(BASE_URL + "/play/" + songTable.getItems().get(songNumber).getFile_name().toString());
        mediaPlayer = new MediaPlayer(media);

        mediaPlayer.currentTimeProperty().addListener(ov -> {
            double total = mediaPlayer.getTotalDuration().toMillis();
            double current = mediaPlayer.getCurrentTime().toMillis();
            currentSongLabel.setText(getTimeString(current) + " / " + getTimeString(total));
            songProgresBar.setProgress(current/total);
        });

        songLabel.setText(songTable.getItems().get(songNumber).getTytul());
        wykonawcaLabel.setText(songTable.getItems().get(songNumber).getWykonawca());
        currentSongLabel.setText(getTimeString(mediaPlayer.getCurrentTime().toMillis()));

        for (int i = 0; i < speeds.length; i++) {
            speedBox.getItems().add(Double.toString(speeds[i]));
        }

        speedBox.setOnAction(this::changeSpeed);

        volumeSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> arg0, Number arg1, Number arg2) {
                mediaPlayer.setVolume(volumeSlider.getValue() * 0.01);
            }
        });

        songProgresBar.setStyle("-fx-accent: #00FF00;");

    }
    private void addScaleTransition(Button button) {
        button.setOnMousePressed(event -> {
            ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(100), button);
            scaleTransition.setToX(0.9);
            scaleTransition.setToY(0.9);
            scaleTransition.playFromStart();
        });

        button.setOnMouseReleased(event -> {
            ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(100), button);
            scaleTransition.setToX(1.0);
            scaleTransition.setToY(1.0);
            scaleTransition.playFromStart();
        });
    }

    private void addPulsingEffect(Label label) {
        ScaleTransition scaleTransition = new ScaleTransition(Duration.seconds(1), label);
        scaleTransition.setToX(1.1);
        scaleTransition.setToY(1.1);
        scaleTransition.setCycleCount(ScaleTransition.INDEFINITE);
        scaleTransition.setAutoReverse(true);
        scaleTransition.play();
    }

    @FXML
    private void playSong() {
        changeSpeed(null);
        mediaPlayer.setVolume(volumeSlider.getValue() * 0.01);
        if (speedBox.getValue()==null) {
            mediaPlayer.setRate(1);
        } else {
            mediaPlayer.setRate(Double.parseDouble(speedBox.getValue()));
        }
        mediaPlayer.play();
    }

    @FXML
    private void pauseSong() {
        mediaPlayer.pause();
        mediaPlayer.currentTimeProperty().addListener(ov -> {
            double total = mediaPlayer.getTotalDuration().toMillis();
            double current = mediaPlayer.getCurrentTime().toMillis();
            currentSongLabel.setText(getTimeString(current) + " / " + getTimeString(total));
        });
    }

    @FXML
    private void resetSong() {
        songProgresBar.setProgress(0);
        mediaPlayer.seek(Duration.seconds(0));
    }

    @FXML
    private void playPrevSong() {
        if(songNumber>0){
            songNumber--;
            mediaPlayer.stop();

            songProgresBar.setProgress(0);

            media=new Media(BASE_URL + "/play/" + songTable.getItems().get(songNumber).getFile_name().toString());
            mediaPlayer = new MediaPlayer(media);
            songLabel.setText(songTable.getItems().get(songNumber).getTytul());
            wykonawcaLabel.setText(songTable.getItems().get(songNumber).getWykonawca());
            currentSongLabel.setText(getTimeString(mediaPlayer.getCurrentTime().toMillis()));

            mediaPlayer.setVolume(volumeSlider.getValue() * 0.01);
            if (speedBox.getValue()==null) {
                mediaPlayer.setRate(1);
            } else {
                mediaPlayer.setRate(Double.parseDouble(speedBox.getValue()));
            }

            mediaPlayer.play();
            mediaPlayer.currentTimeProperty().addListener(ov -> {
                double total = mediaPlayer.getTotalDuration().toMillis();
                double current = mediaPlayer.getCurrentTime().toMillis();
                currentSongLabel.setText(getTimeString(current) + " / " + getTimeString(total));
                songProgresBar.setProgress(current/total);
            });
        } else {
            songNumber=songTable.getItems().size()-1;
            mediaPlayer.stop();

            songProgresBar.setProgress(0);

            media=new Media(BASE_URL + "/play/" + songTable.getItems().get(songNumber).getFile_name().toString());
            mediaPlayer = new MediaPlayer(media);
            songLabel.setText(songTable.getItems().get(songNumber).getTytul());
            wykonawcaLabel.setText(songTable.getItems().get(songNumber).getWykonawca());
            currentSongLabel.setText(getTimeString(mediaPlayer.getCurrentTime().toMillis()));

            mediaPlayer.setVolume(volumeSlider.getValue() * 0.01);
            if (speedBox.getValue()==null) {
                mediaPlayer.setRate(1);
            } else {
                mediaPlayer.setRate(Double.parseDouble(speedBox.getValue()));
            }

            mediaPlayer.play();
            mediaPlayer.currentTimeProperty().addListener(ov -> {
                double total = mediaPlayer.getTotalDuration().toMillis();
                double current = mediaPlayer.getCurrentTime().toMillis();
                currentSongLabel.setText(getTimeString(current) + " / " + getTimeString(total));
                songProgresBar.setProgress(current/total);
            });
        }
    }

    @FXML
    private void playNextSong() {
        if(songNumber<songTable.getItems().size()-1){
            songNumber++;
            mediaPlayer.stop();

            songProgresBar.setProgress(0);

            media=new Media(BASE_URL + "/play/" + songTable.getItems().get(songNumber).getFile_name().toString());
            mediaPlayer = new MediaPlayer(media);
            songLabel.setText(songTable.getItems().get(songNumber).getTytul());
            wykonawcaLabel.setText(songTable.getItems().get(songNumber).getWykonawca());
            currentSongLabel.setText(getTimeString(mediaPlayer.getCurrentTime().toMillis()));

            mediaPlayer.setVolume(volumeSlider.getValue() * 0.01);
            if (speedBox.getValue()==null) {
                mediaPlayer.setRate(1);
            } else {
                mediaPlayer.setRate(Double.parseDouble(speedBox.getValue()));
            }

            mediaPlayer.play();
            mediaPlayer.currentTimeProperty().addListener(ov -> {
                double total = mediaPlayer.getTotalDuration().toMillis();
                double current = mediaPlayer.getCurrentTime().toMillis();
                currentSongLabel.setText(getTimeString(current) + " / " + getTimeString(total));
                songProgresBar.setProgress(current/total);
            });
        } else {
            songNumber=0;
            mediaPlayer.stop();

            songProgresBar.setProgress(0);

            media=new Media(BASE_URL + "/play/" + songTable.getItems().get(songNumber).getFile_name().toString());
            mediaPlayer = new MediaPlayer(media);
            songLabel.setText(songTable.getItems().get(songNumber).getTytul());
            wykonawcaLabel.setText(songTable.getItems().get(songNumber).getWykonawca());
            currentSongLabel.setText(getTimeString(mediaPlayer.getCurrentTime().toMillis()));

            mediaPlayer.setVolume(volumeSlider.getValue() * 0.01);
            if (speedBox.getValue()==null) {
                mediaPlayer.setRate(1);
            } else {
                mediaPlayer.setRate(Double.parseDouble(speedBox.getValue()));
            }

            mediaPlayer.play();
            mediaPlayer.currentTimeProperty().addListener(ov -> {
                double total = mediaPlayer.getTotalDuration().toMillis();
                double current = mediaPlayer.getCurrentTime().toMillis();
                currentSongLabel.setText(getTimeString(current) + " / " + getTimeString(total));
                songProgresBar.setProgress(current/total);
            });
        }
    }

    @FXML
    private void changeSpeed(ActionEvent event) {
        if (speedBox.getValue()==null) {
            mediaPlayer.setRate(1);
        } else {
            mediaPlayer.setRate(Double.parseDouble(speedBox.getValue()));
        }
    }

    public static String formatTime(double time) {
        int t = (int)time;
        if (t > 9) { return String.valueOf(t); }
        return "0" + t;
    }

    public static String getTimeString(double millis) {
        millis /= 1000;
        String s = formatTime(millis % 60);
        millis /= 60;
        String m = formatTime(millis % 60);
        return  m + ":" + s;
    }
}
