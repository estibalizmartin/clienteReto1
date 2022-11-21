package com.example.clientereto1;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.clientereto1.adapters.MyTableAdapter;
import com.example.clientereto1.network.FavouritesRequest;
import com.example.clientereto1.network.NetworkUtilites;
import com.example.clientereto1.models.Song;
import com.example.clientereto1.network.SongsRequest;

import java.util.ArrayList;

public class SongList extends AppCompatActivity {
    ArrayList<Song> songList;
    ArrayList<Song> favList;
    NetworkUtilites networkUtilites;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_community);
        getSupportActionBar().hide();

        songList = new ArrayList<>();
        favList = new ArrayList<>();
        networkUtilites = new NetworkUtilites(SongList.this);

        makeSongRequest();
    }

    @SuppressLint("MissingInflatedId")
    public void makeSongRequest() {
        songList = networkUtilites.makeRequest(new SongsRequest());

        setContentView(R.layout.layout_community);
        ((ListView) findViewById(R.id.allSongsListView)).setAdapter(new MyTableAdapter(this, R.layout.myrow_layout, songList, favList));

        //Para ir a favoritos
        findViewById(R.id.favoritesButton).setOnClickListener(view -> {
            makeFavouritesRequest();
        });
    }

    public void makeFavouritesRequest(){
        favList = networkUtilites.makeRequest(new FavouritesRequest());

        setContentView(R.layout.layout_favourites);
        ((ListView) findViewById(R.id.favouritesListView)).setAdapter(new MyTableAdapter(this, R.layout.myrow_layout, songList, favList));

        //Para ir a community
        findViewById(R.id.allSongsButton).setOnClickListener(v -> {
            makeSongRequest();
        });
    }
}
