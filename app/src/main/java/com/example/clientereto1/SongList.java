package com.example.clientereto1;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.clientereto1.adapters.MyTableAdapter;
import com.example.clientereto1.network.CreateUserRequest;
import com.example.clientereto1.network.FavouritesRequest;
import com.example.clientereto1.network.NetworkUtilites;
import com.example.clientereto1.network.SongsRequest;
import com.example.clientereto1.models.Song;

import java.util.ArrayList;

public class SongList extends AppCompatActivity {
    ArrayList<Song> listado;
    NetworkUtilites networkUtilites;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.layout_community);
        listado = new ArrayList<>();
        networkUtilites = new NetworkUtilites(SongList.this);

        makeSongRequest();
    }

    public void makeSongRequest() {
        listado = networkUtilites.makeRequest(new SongsRequest());

        setContentView(R.layout.layout_community);
        ((ListView) findViewById( R.id.allSongsListView)).setAdapter (new MyTableAdapter(this, R.layout.myrow_layout, listado));

        //Para ir a favoritos
        findViewById(R.id.favoritesButton).setOnClickListener(view -> {
            makeFavouritesRequest();
        });
    }

    public void makeFavouritesRequest(){
        listado = networkUtilites.makeRequest(new FavouritesRequest());

        setContentView(R.layout.layout_favourites);
        ((ListView) findViewById( R.id.favouritesListView)).setAdapter (new MyTableAdapter (this, R.layout.myrow_layout, listado));

        //Para ir a community
        findViewById(R.id.allSongsButton).setOnClickListener(v -> {
            makeSongRequest();
        });
    }
}
