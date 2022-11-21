package com.example.clientereto1.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.preference.PreferenceManager;
import androidx.annotation.NonNull;

import com.example.clientereto1.R;
import com.example.clientereto1.models.Song;
import com.example.clientereto1.network.CreateFavouriteRequest;
import com.example.clientereto1.network.FavouritesRequest;
import com.example.clientereto1.network.NetworkUtilites;

import java.util.ArrayList;
import java.util.List;


public class MyTableAdapter extends ArrayAdapter<Song> {
    private ArrayList<Song> songList;
    private ArrayList<Song> favList;
    private final Context context;
    private WebView webView;
    NetworkUtilites networkUtilites;
    SharedPreferences sharedPreferences;

    public MyTableAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Song> songList, @NonNull ArrayList<Song> favList) {
        super(context, resource, songList);
        this.songList = songList;
        this.context = context;
        this.favList = favList;
    }

    public MyTableAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Song> favList) {
        super(context, resource, favList);
        this.context = context;
        this.favList = favList;
    }

    @Override
    public int getCount() {
        return super.getCount();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.myrow_layout, null);

        List<Song> listToShow = (songList == null) ? favList : songList;

        ((TextView) view.findViewById(R.id.songIdTextView)).setText(listToShow.get(position).getId() + "");
        ((TextView) view.findViewById(R.id.songAuthorTextView)).setText(listToShow.get(position).getAuthor() + "");
        ((TextView) view.findViewById(R.id.songTitleTextView)).setText(listToShow.get(position).getTitle());
        ((TextView) view.findViewById(R.id.songUrlTextView)).setText(listToShow.get(position).getUrl());
        view.findViewById(R.id.playButton).setOnClickListener(v -> {
            String url = listToShow.get(position).getUrl();
            Uri uri = Uri.parse(url);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            intent.setPackage("com.android.chrome");
            context.startActivity(intent);
        });

        if (songList != null) {
            for (Song favSong : favList) {
                if (favSong.getId() == listToShow.get(position).getId()) {
                    ImageView iv = (ImageView) view.findViewById(R.id.btn_star_big);
                    iv.setOnClickListener(view1 -> {
                    });
                    iv.setImageResource(android.R.drawable.btn_star_big_on);
                }
            }
        } else {
            ImageView iv = (ImageView) view.findViewById(R.id.btn_star_big);
            iv.setOnClickListener(view1 -> {
            });
            iv.setImageResource(android.R.drawable.btn_star_big_on);
        }

        view.findViewById(R.id.btn_star_big).setOnClickListener(v -> {
            System.out.println("Entrando al onlick"+view);
            CreateFavouriteRequest createFavouriteRequest= new CreateFavouriteRequest(generateFavouriteDataJson(view));
            System.out.println(createFavouriteRequest.getResponse());
            networkUtilites.makeRequest(new CreateFavouriteRequest(generateFavouriteDataJson(view)));
            System.out.println("Saliendo del onlick");
        });

        return view;
    }

    public void emptyView() {
        LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.myrow_layout, null);
    }


    private String generateFavouriteDataJson(View view) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        return "{" +
                "\"iduser\": \"" + sharedPreferences.getInt("user_id", -1) + "\"," +
                "\"idsong\": \"" + ((TextView) view.findViewById(R.id.songIdTextView)).getText().toString() + "\"" +
                "}";
    }
}