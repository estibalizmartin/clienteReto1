package com.example.clientereto1.network;

import android.content.Context;
import android.content.res.Resources;

import com.example.clientereto1.R;
import com.example.clientereto1.models.RequestResponse;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class CreateFavouriteRequest extends NetConfiguration implements Runnable {

    private final String theUrl = theBaseUrl + "/favoritesNoToken";
    private RequestResponse response;
    private String userDataJson;
    Resources res;

    public CreateFavouriteRequest(String userDataJson, Context context) {
        res = context.getResources();
        this.userDataJson = userDataJson;
    }

    @Override
    public void run() {
        try {

            URL url = new URL(theUrl);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod( "POST" );
            httpURLConnection.setRequestProperty("Content-Type", "application/json");

            httpURLConnection.setDoOutput(true);
            try(OutputStream os = httpURLConnection.getOutputStream()) {
                byte[] input = userDataJson.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            int responseCode = httpURLConnection.getResponseCode();

            response = new RequestResponse();

            if (responseCode == 512){
                this.response.setMessage(res.getString(R.string.song_already_favourite));

            } else if (responseCode == HttpURLConnection.HTTP_CREATED) {


                this.response.setMessage(res.getString(R.string.song_favourites));
            } else {
                this.response.setMessage(res.getString(R.string.request_error));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public RequestResponse getResponse() {
        return response;
    }
}
