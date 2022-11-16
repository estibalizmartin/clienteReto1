package com.example.clientereto1;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.clientereto1.adapters.MyTableAdapter;
import com.example.clientereto1.connection.FavouritesRequest;
import com.example.clientereto1.connection.SongsRequest;
import com.example.clientereto1.models.Song;
import com.example.clientereto1.network.SongsRequest;

import java.util.ArrayList;

public class SongList extends AppCompatActivity {
    ArrayList<Song> listado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.layout_community);
        listado = new ArrayList<>();
        makeRequest(new SongsRequest());
    }
    public boolean isConnected() {
        boolean ret = false;
        try {

            ConnectivityManager connectivityManager = (ConnectivityManager) getApplicationContext()
                    .getSystemService( Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            if ((networkInfo != null) && (networkInfo.isAvailable()) && (networkInfo.isConnected()))
                ret = true;
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), getString(R.string.error_communication), Toast.LENGTH_SHORT).show();
        }
        return ret;
    }

    public void makeRequest(SongsRequest songsRequest) {
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

            listado = new ArrayList();
            listado.addAll( listSongs );

            setContentView(R.layout.layout_community);
            ((ListView) findViewById( R.id.allSongsListView)).setAdapter (new MyTableAdapter(this, R.layout.myrow_layout, listado));

            //Para ir a favoritos
            findViewById(R.id.favoritesButton).setOnClickListener(view -> {
                makeRequest(new FavouritesRequest());
            });
        }
    }

    public void makeRequest(FavouritesRequest favouritesRequest) {
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

            listado = new ArrayList();
            listado.addAll( listSongs );

            setContentView(R.layout.layout_favourites);
            ((ListView) findViewById( R.id.favouritesListView)).setAdapter (new MyTableAdapter (this, R.layout.myrow_layout, listado));

            //Para ir a community
            findViewById(R.id.allSongsButton).setOnClickListener(view -> {
                makeRequest(new SongsRequest());
            });
        }
    }
}
