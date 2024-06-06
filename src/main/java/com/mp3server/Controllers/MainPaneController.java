package com.mp3server.Controllers;

import com.mp3server.client.SongData;
import com.mp3server.server.Song;
import javafx.animation.ScaleTransition;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Duration;
import javazoom.jl.player.Player;


import java.io.File;
import java.io.IOException;
import java.util.*;

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

    private Media media;
    private MediaPlayer mediaPlayer;

    private File directory;
    private File[] files;
    private ArrayList<File> songs;
    private int songNumber;
    private int[] speeds = {25,50,75,100,125,150,175,200};
    private Timer timer;
    private TimerTask task;
    private boolean running;



    //private List<Song> songs;
    private Thread playerThread;
    private Player player = null;
    private File currentSong = null;
    private int currentSongIndex;
    private static final String BASE_URL = "http://localhost:8080";
    private boolean paused = true;
    private final Object pauseLock = new Object();

    @FXML
    public void initialize() throws Exception {
        // Ścieżka do folderu
        String folderPath = "src/main/java/com/mp3server/music";

        addScaleTransition(playButton);
        addScaleTransition(pauseButton);
        addScaleTransition(resetSongButton);
        addScaleTransition(prevSongButton);
        addScaleTransition(nextSongButton);

        addPulsingEffect(songLabel);
        songNameColumn.setCellValueFactory(new PropertyValueFactory<>("tytul"));
        songDurationColumn.setCellValueFactory(new PropertyValueFactory<>("duration"));
        songArtistColumn.setCellValueFactory(new PropertyValueFactory<>("wykonawca"));
        //loadSongs();

        songs = new ArrayList<File>();

        directory = new File(folderPath);

        files = directory.listFiles();
        try {
            songTable.getItems().addAll(SongData.getAllSongs());
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(files != null) {

            for(File file : files) {

                songs.add(file);
            }
        }else{
            System.err.println("Nie znaleziono nic w folderze music");
        }


        media = new Media(songs.get(songNumber).toURI().toString());
        mediaPlayer = new MediaPlayer(media);

        songLabel.setText(songs.get(songNumber).getName());

        for(int i = 0; i < speeds.length; i++) {

            speedBox.getItems().add(Integer.toString(speeds[i])+"%");
        }

        speedBox.setOnAction(this::changeSpeed);

        volumeSlider.valueProperty().addListener(new ChangeListener<Number>() {

            @Override
            public void changed(ObservableValue<? extends Number> arg0, Number arg1, Number arg2) {

                //mediaPlayer.setVolume(volumeSlider.getValue() * 0.01);
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
        mediaPlayer.play();
    }

    @FXML
    private void pauseSong() {
        mediaPlayer.pause();
    }

    @FXML
    private void resetSong() {
        mediaPlayer.seek(Duration.seconds(0));
    }

    @FXML
    private void playPrevSong() {
        // Kod obsługi przycisku Prev
    }

    @FXML
    private void playNextSong() {
        if(songNumber<songs.size()-1){
            songNumber++;
            mediaPlayer.stop();
        }
    }

    @FXML
    private void changeSpeed(ActionEvent event) {

    }

    public void beginTimmer(){

    }
    public void cancelTimmer(){

    }

}
