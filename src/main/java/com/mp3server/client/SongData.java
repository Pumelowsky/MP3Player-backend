package com.mp3server.client;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

public class SongData {
    private String id;
    private String wykonawca;
    private String tytul;
    private String album;
    private String file_name;
    private String duration;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getWykonawca() {
        return wykonawca;
    }

    public void setWykonawca(String wykonawca) {
        this.wykonawca = wykonawca;
    }

    public String getTytul() {
        return tytul;
    }

    public void setTytul(String tytul) {
        this.tytul = tytul;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getFile_name() {
        return file_name;
    }

    public void setFile_name(String file_name) {
        this.file_name = file_name;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public static List<SongData> getAllSongs() throws Exception {
        String url = "http://localhost:8080/allsongs";
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("GET");

        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        JSONArray jsonArray = new JSONArray(response.toString());
        List<SongData> songDataList = new ArrayList<>();

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            SongData songData = new SongData();
            songData.id = jsonObject.getString("id");
            songData.wykonawca = jsonObject.getString("wykonawca");
            songData.tytul = jsonObject.getString("tytul");
            songData.album = jsonObject.getString("album");
            songData.file_name = jsonObject.getString("file_name");
            songData.duration = jsonObject.getString("duration");
            songDataList.add(songData);
        }
        System.out.println(songDataList.size());
        return songDataList;
    }
}