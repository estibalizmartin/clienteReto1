package com.example.clientereto1.network;

import android.content.Context;
import android.content.res.Resources;

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

    public CreateFavouriteRequest(String userDataJson) {
        this.userDataJson = userDataJson;
    }

    @Override
    public void run() {
        try {
            System.out.println(theUrl);
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
            System.out.println(responseCode);
            response = new RequestResponse();

            if (responseCode == 512){
                this.response.setMessage("");

            } else if (responseCode == HttpURLConnection.HTTP_OK) {


                this.response.setMessage("");
            } else {
                this.response.setMessage("");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public RequestResponse getResponse() {
        return response;
    }
}
