package com.example.clientereto1;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import android.widget.SearchView;
import androidx.appcompat.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;

import com.example.clientereto1.adapters.MyTableAdapter;
import com.example.clientereto1.network.FavouritesRequest;
import com.example.clientereto1.network.NetworkUtilites;
import com.example.clientereto1.models.Song;
import com.example.clientereto1.network.SongsRequest;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class SongList extends AppCompatActivity {
    ArrayList<Song> songList;
    ArrayList<Song> favList;
    NetworkUtilites networkUtilites;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_community);
        getSupportActionBar().hide();

        songList = new ArrayList<>();
        favList = new ArrayList<>();

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        networkUtilites = new NetworkUtilites(SongList.this);

        community_onCreate();
    }

    @SuppressLint("MissingInflatedId")
    public void community_onCreate() {
        songList = networkUtilites.makeRequest(new SongsRequest());

        setContentView(R.layout.layout_community);
        ((ListView) findViewById(R.id.allSongsListView)).setAdapter(new MyTableAdapter(this, R.layout.myrow_layout, songList, favList));

        Toolbar hiToolbar = findViewById(R.id.hiToolbar);
        hiToolbar.setTitle(hiToolbar.getTitle().toString() + sharedPreferences.getString("username", ""));

        findViewById(R.id.favoritesButton).setOnClickListener(view -> {
            favourites_onCreate();
        });

        ListView songListView = (ListView) findViewById(R.id.allSongsListView);
        SearchView searchView = (SearchView) findViewById(R.id.communitySearchBar);


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String queryText) {
                searchSong(queryText, songListView);
                return false;
            }
        });
    }

    public void favourites_onCreate(){
        favList = networkUtilites.makeRequest(new FavouritesRequest(this));

        setContentView(R.layout.layout_favourites);
        ((ListView) findViewById(R.id.favouritesListView)).setAdapter(new MyTableAdapter(this, R.layout.myrow_layout, favList));

        Toolbar hiToolbar = findViewById(R.id.hiToolbar);
        hiToolbar.setTitle(hiToolbar.getTitle().toString() + sharedPreferences.getString("username", ""));

        findViewById(R.id.allSongsButton).setOnClickListener(v -> {
            community_onCreate();
        });

        ListView songListView = (ListView) findViewById(R.id.favouritesListView);
        SearchView searchView = (SearchView) findViewById(R.id.favouritesSearchView);


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String queryText) {
                searchSong(queryText, songListView);
                return false;
            }
        });

    }

    public void searchSong(String queryText, ListView songlistView) {
        for (int i = 0; i < songlistView.getChildCount(); i++) {
            System.out.println(i);
            LinearLayout listViewLayout = (LinearLayout) songlistView.getChildAt(i);
            TextView songTitleTextView = (TextView) ((TableRow) listViewLayout.getChildAt(0)).getChildAt(0);

            if (songTitleTextView.getText().toString().toLowerCase().contains(queryText.toLowerCase())) {
                listViewLayout.setLayoutParams(new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                listViewLayout.setVisibility(View.VISIBLE);
            }
            else {
                listViewLayout.setLayoutParams(new AbsListView.LayoutParams(-1,1));
                listViewLayout.setVisibility(View.GONE);
            }
        }
    }


}
