package com.example.clientereto1.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.ListView;
import android.widget.Toast;

import com.example.clientereto1.R;
import com.example.clientereto1.adapters.MyTableAdapter;
import com.example.clientereto1.models.Song;

import java.util.ArrayList;

public class NetworkUtilites {

    Context context;

    public NetworkUtilites(Context context) {
        this.context = context;
    }

    public ArrayList<Song> makeRequest(SongsRequest songsRequest) {
        if (isConnected()) {

            Thread thread = new Thread(songsRequest);
            try {
                thread.start();
                thread.join(); // Awaiting response from the server...
            } catch (InterruptedException e) {
                // Nothing to do here...
            }
            // Processing the answer
            ArrayList<Song> listSongs = songsRequest.getResponse();

            return listSongs;
        } else
            return new ArrayList<>();
    }

    public ArrayList<Song> makeRequest(FavouritesRequest favouritesRequest) {
        if (isConnected()) {

            Thread thread = new Thread(favouritesRequest);
            try {
                thread.start();
                thread.join(); // Awaiting response from the server...
            } catch (InterruptedException e) {
                // Nothing to do here...
            }
            // Processing the answer
            ArrayList<Song> listSongs = favouritesRequest.getResponse();

            return listSongs;
        } else
            return new ArrayList<>();
    }

    public int makeRequest(CreateUserRequest createUserRequest) {
        if (isConnected()) {

            Thread thread = new Thread(createUserRequest);
            try {
                thread.start();
                thread.join(); // Awaiting response from the server...
            } catch (InterruptedException e) {
                // Nothing to do here...
            }
            // Processing the answer
            int response = createUserRequest.getResponse();

            System.out.println("Respuesta register: " + response);

            return response;


        } else
            return 0;
    }


    public boolean isConnected() {
        boolean ret = false;
        try {

            ConnectivityManager connectivityManager = (ConnectivityManager) context
                    .getSystemService( Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            if ((networkInfo != null) && (networkInfo.isAvailable()) && (networkInfo.isConnected()))
                ret = true;
        } catch (Exception e) {
            Toast.makeText(context, context.getString(R.string.error_communication), Toast.LENGTH_SHORT).show();
        }
        return ret;
    }
}
