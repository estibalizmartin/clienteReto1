package com.example.clientereto1.adapters;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.clientereto1.R;
import com.example.clientereto1.models.Song;

import java.util.ArrayList;


public class MyTableAdapter extends ArrayAdapter<Song>{
    private final ArrayList <Song> listado;
    private final Context context;

    public MyTableAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Song> listado) {
        super( context, resource, listado );
        this.listado = listado;
        this.context = context;
    }

    @Override
    public int getCount (){
        return super.getCount();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate ( R.layout.myrow_layout, null);

        ((TextView) view.findViewById( R.id.songIdTextView)).setText(listado.get(position).getId() + "");
        ((TextView) view.findViewById( R.id.songAuthorTextView)).setText(listado.get(position).getAuthor() + "");
        ((TextView) view.findViewById( R.id.songTitleTextView)).setText(listado.get(position).getTitle());
        ((TextView) view.findViewById( R.id.songUrlTextView)).setText(listado.get(position).getUrl());

        return view;
    }
}
