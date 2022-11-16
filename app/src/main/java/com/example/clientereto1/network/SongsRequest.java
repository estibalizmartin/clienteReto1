package com.example.clientereto1.network;

import com.example.clientereto1.models.Song;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class SongsRequest extends NetConfiguration implements Runnable{

    private final String theUrl = theBaseUrl + "/songs";
    private ArrayList<Song> response;
    @Override
    public void run() {

        try {
            // The URL
            URL url = null;
            url = new URL( theUrl);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod( "GET" );
            System.out.println("Hola");
            // Sending...
            int responseCode = httpURLConnection.getResponseCode();
            System.out.println("adios");
            if (responseCode == 513){
                // No se han podido cargar las canciones
                this.response = null;
            }else if(responseCode == HttpURLConnection.HTTP_OK){
                BufferedReader bufferedReader = new BufferedReader(
                        new InputStreamReader( httpURLConnection.getInputStream() ) );
                StringBuffer response = new StringBuffer();
                String inputLine;
                while ((inputLine = bufferedReader.readLine()) != null) {
                    response.append( inputLine );
                }
                bufferedReader.close();

                // Processing the JSON...
                String theUnprocessedJSON = response.toString();

                JSONArray jsonArray = new JSONArray (theUnprocessedJSON);

                this.response = new ArrayList<Song>();

                Song song;
                for(int i=0; i < jsonArray.length(); i++) {
                    JSONObject object = jsonArray.getJSONObject( i );

                    song = new Song();
                    song.setId(object.getInt("id"));
                    song.setAuthor(object.getString("author"));
                    song.setTitle( object.getString("title"));
                    song.setUrl( object.getString("url"));
                    this.response.add( song );
                }
            }
        } catch (Exception e) {
            System.out.println("entro porque he fallado");
            e.printStackTrace();
        }

    }
    public ArrayList<Song> getResponse() {
        return response;
    }
}
