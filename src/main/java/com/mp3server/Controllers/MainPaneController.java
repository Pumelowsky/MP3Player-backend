package com.mp3server.Controllers;

import javafx.animation.ScaleTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.util.Duration;

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
    private ComboBox<Double> speedBox;


    @FXML
    private Label songLabel;

    @FXML
    public void initialize() {
        addScaleTransition(playButton);
        addScaleTransition(pauseButton);
        addScaleTransition(resetSongButton);
        addScaleTransition(prevSongButton);
        addScaleTransition(nextSongButton);

        addPulsingEffect(songLabel);
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

    }

    @FXML
    private void pauseSong() {

    }

    @FXML
    private void resetSong() {

    }

    @FXML
    private void playPrevSong() {
        // Kod obsługi przycisku Prev
    }

    @FXML
    private void playNextSong() {

    }

    @FXML
    private void changeSpeed() {

    }
}
