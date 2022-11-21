package com.example.clientereto1.network;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.example.clientereto1.models.Song;
import com.example.clientereto1.network.NetConfiguration;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class FavouritesRequest extends NetConfiguration implements Runnable{

    SharedPreferences sharedPreferences;
    int idUser;
    private String theUrl;
    private ArrayList<Song> response;

    public FavouritesRequest(Context context){
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        idUser = sharedPreferences.getInt("user_id", -1);
        theUrl = theBaseUrl + "/favoritesnotoken/"+idUser+"/user";

    }

    @Override
    public void run() {
        try {

            URL url = new URL( theUrl);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod( "GET" );
            // Sending...
            int responseCode = httpURLConnection.getResponseCode();
            System.out.println(responseCode);
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

                    System.out.println(object.getInt("id"));

                    song = new Song();
                    song.setId(object.getInt("id"));
                    song.setAuthor(object.getString("author"));
                    song.setTitle( object.getString("title"));
                    song.setUrl( object.getString("url"));
                    this.response.add( song );
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public ArrayList<Song> getResponse() {
        return response;
    }
}

