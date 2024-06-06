package com.mp3server.Controllers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mp3server.server.Song;
import javafx.animation.ScaleTransition;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.Duration;
import javazoom.jl.player.Player;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
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
    private TableView<Song> songTable;

    @FXML
    private TableColumn<Song, String> songNameColumn;

    @FXML
    private TableColumn<Song, String> songDurationColumn;

    @FXML
    private TableColumn<Song, String> songArtistColumn;
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
    private double[] speeds = {0.25,0.5,0.75,1,1.25,1.5,1.75,2};
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
    public void initialize() {
        // Ścieżka do folderu
        String folderPath = "src/main/java/com/mp3server/music";

        addScaleTransition(playButton);
        addScaleTransition(pauseButton);
        addScaleTransition(resetSongButton);
        addScaleTransition(prevSongButton);
        addScaleTransition(nextSongButton);

        addPulsingEffect(songLabel);
        //loadSongs();

        songs = new ArrayList<File>();

        directory = new File(folderPath);

        files = directory.listFiles();

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
        beginTimmer();
        changeSpeed(null);
        mediaPlayer.setVolume(volumeSlider.getValue() * 0.01);
        if(speedBox.getValue()==null){
            mediaPlayer.setRate(1);
        }else{
            mediaPlayer.setRate(Double.parseDouble(speedBox.getValue()));
        }
        mediaPlayer.play();
    }

    @FXML
    private void pauseSong() {
        cancelTimmer();
        mediaPlayer.pause();
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

            media=new Media(songs.get(songNumber).toURI().toString());
            mediaPlayer = new MediaPlayer(media);
            songLabel.setText(songs.get(songNumber).getName());

            mediaPlayer.setVolume(volumeSlider.getValue() * 0.01);
            if(speedBox.getValue()==null){
                mediaPlayer.setRate(1);
            }else{
                mediaPlayer.setRate(Double.parseDouble(speedBox.getValue()));
            }

            mediaPlayer.play();
        }else {
            songNumber=songs.size()-1;
            mediaPlayer.stop();

            songProgresBar.setProgress(0);

            media=new Media(songs.get(songNumber).toURI().toString());
            mediaPlayer = new MediaPlayer(media);
            songLabel.setText(songs.get(songNumber).getName());

            mediaPlayer.setVolume(volumeSlider.getValue() * 0.01);
            if(speedBox.getValue()==null){
                mediaPlayer.setRate(1);
            }else{
                mediaPlayer.setRate(Double.parseDouble(speedBox.getValue()));
            }

            mediaPlayer.play();
        }
    }

    @FXML
    private void playNextSong() {
        if(songNumber<songs.size()-1){
            songNumber++;
            mediaPlayer.stop();

            songProgresBar.setProgress(0);

            media=new Media(songs.get(songNumber).toURI().toString());
            mediaPlayer = new MediaPlayer(media);
            songLabel.setText(songs.get(songNumber).getName());

            mediaPlayer.setVolume(volumeSlider.getValue() * 0.01);
            if(speedBox.getValue()==null){
                mediaPlayer.setRate(1);
            }else{
                mediaPlayer.setRate(Double.parseDouble(speedBox.getValue()));
            }

            mediaPlayer.play();
        }else {
            songNumber=0;
            mediaPlayer.stop();

            songProgresBar.setProgress(0);

            media=new Media(songs.get(songNumber).toURI().toString());
            mediaPlayer = new MediaPlayer(media);
            songLabel.setText(songs.get(songNumber).getName());

            mediaPlayer.setVolume(volumeSlider.getValue() * 0.01);
            if(speedBox.getValue()==null){
                mediaPlayer.setRate(1);
            }else{
                mediaPlayer.setRate(Double.parseDouble(speedBox.getValue()));
            }

            mediaPlayer.play();
        }
    }

    @FXML
    private void changeSpeed(ActionEvent event) {
        if(speedBox.getValue()==null){
            mediaPlayer.setRate(1);
        }else{
            mediaPlayer.setRate(Double.parseDouble(speedBox.getValue()));
        }

    }

    public void beginTimmer(){
        timer = new Timer();
        task = new TimerTask() {
            @Override
            public void run() {
                running=true;
                double current = mediaPlayer.getCurrentTime().toSeconds();
                double end = media.getDuration().toSeconds();
                songProgresBar.setProgress(current/end);

                if(current/end==1){
                    cancelTimmer();
                }
            }
        };

        timer.scheduleAtFixedRate(task,1000,1000);

    }
    public void cancelTimmer(){
        running=false;
        timer.cancel();
    }

    @FXML
    private List<Song> getAllSongs() throws IOException {
        URL url = new URL(BASE_URL + "/allsongs");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.connect();

        int responseCode = conn.getResponseCode();

        if (responseCode != 200) {
            throw new RuntimeException("HttpResponseCode: " + responseCode);
        } else {
            Scanner scanner = new Scanner(url.openStream());
            StringBuilder inline = new StringBuilder();
            while (scanner.hasNext()) {
                inline.append(scanner.nextLine());
            }
            scanner.close();

            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(inline.toString(), new TypeReference<List<Song>>() {});
        }
    }


}
