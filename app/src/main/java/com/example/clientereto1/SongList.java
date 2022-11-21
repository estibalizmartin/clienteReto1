package com.example.clientereto1;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;

import com.example.clientereto1.adapters.MyTableAdapter;
import com.example.clientereto1.network.CreateUserRequest;
import com.example.clientereto1.network.FavouritesRequest;
import com.example.clientereto1.network.NetworkUtilites;
import com.example.clientereto1.network.SongsRequest;
import com.example.clientereto1.models.Song;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class SongList extends AppCompatActivity {
    ArrayList<Song> listado;
    NetworkUtilites networkUtilites;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.layout_community);
        listado = new ArrayList<>();
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        networkUtilites = new NetworkUtilites(SongList.this);

        community_onCreate();
    }

    public void community_onCreate() {
        listado = networkUtilites.makeRequest(new SongsRequest());

        setContentView(R.layout.layout_community);
        ((ListView) findViewById( R.id.allSongsListView)).setAdapter (new MyTableAdapter(this, R.layout.myrow_layout, listado));

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
                System.out.println(queryText);
                for (int i = 0; i < songListView.getChildCount(); i++) {
                    LinearLayout listViewLayout = (LinearLayout) songListView.getChildAt(0);
                    TextView songTitleTextView = (TextView) ((TableRow) listViewLayout.getChildAt(0)).getChildAt(0);

                    if (songTitleTextView.getText().toString().contains(queryText)) {
                        System.out.println("Yass");
                    }

                }
                return false;
            }
        });
    }

    public void favourites_onCreate(){
        listado = networkUtilites.makeRequest(new FavouritesRequest());

        setContentView(R.layout.layout_favourites);
        ((ListView) findViewById( R.id.favouritesListView)).setAdapter (new MyTableAdapter (this, R.layout.myrow_layout, listado));

        Toolbar hiToolbar = findViewById(R.id.hiToolbar);
        hiToolbar.setTitle(hiToolbar.getTitle().toString() + sharedPreferences.getString("username", ""));

        findViewById(R.id.allSongsButton).setOnClickListener(v -> {
            community_onCreate();
        });
    }


}
