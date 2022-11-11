package com.example.clientereto1.connection;

import com.example.clientereto1.models.Song;

import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class SongsRequest extends NetConfiguration implements Runnable{

    private final String theUrl = theBaseUrl + "songs";
    private ArrayList<Song> response;
    @Override
    public void run() {

        try {
            // The URL
            URL url = null;
            url = new URL( theUrl);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod( "GET" );
            // Sending...
            int responseCode = httpURLConnection.getResponseCode();
            if (responseCode == 513){
                // No se han podido cargar las canciones
                this.response = null;
            }else if(responseCode == HttpURLConnection.HTTP_OK){

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
