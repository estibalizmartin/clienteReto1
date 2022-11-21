package com.example.clientereto1.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.clientereto1.R;
import com.example.clientereto1.models.Song;

import java.util.ArrayList;


public class MyTableAdapter extends ArrayAdapter<Song>{
    private final ArrayList<Song> songList;
    private final ArrayList<Song> favList;
    private final Context context;
    private WebView webView;

    public MyTableAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Song> songList, @NonNull ArrayList<Song> favList) {
        super(context, resource, songList);
        this.songList = songList;
        this.context = context;
        this.favList = favList;
    }

    @Override
    public int getCount (){
        return super.getCount();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.myrow_layout, null);

        ((TextView) view.findViewById(R.id.songIdTextView)).setText(songList.get(position).getId() + "");
        ((TextView) view.findViewById(R.id.songAuthorTextView)).setText(songList.get(position).getAuthor() + "");
        ((TextView) view.findViewById(R.id.songTitleTextView)).setText(songList.get(position).getTitle());
        ((TextView) view.findViewById(R.id.songUrlTextView)).setText(songList.get(position).getUrl());
        view.findViewById(R.id.playButton).setOnClickListener(v -> {
            String url = songList.get(position).getUrl();
            Uri uri = Uri.parse(url);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            intent.setPackage("com.android.chrome");
            context.startActivity(intent);
        });
        if (favList.contains(songList.get(position).getId())) {
            ImageView iv = (ImageView) view.findViewById(R.id.btn_star_big);
            iv.setOnClickListener(view1 -> {});
            iv.setImageResource(android.R.drawable.btn_star_big_on);
        }
        return view;
    }

    public void emptyView() {
        LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate ( R.layout.myrow_layout, null);
    }
}
