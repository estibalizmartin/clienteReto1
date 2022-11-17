package com.example.clientereto1.network;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
public class UserRequest extends NetConfiguration implements Runnable{
    private final String theUrl = theBaseUrl + "/loginNoToken";
    public int response;
    @Override
    public void run() {
// The URL
        URL url = null;
        try {
            url = new URL( theUrl);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod( "GET" );
            // Sending...
            int responseCode = httpURLConnection.getResponseCode();
            if (responseCode == 432){
                response = 432;
            }else if(responseCode == 400){
                response = 400;
            }else if(responseCode == 433){
                response = 433;
            }else if(responseCode == 202){
                response = 202;
            }
        } catch (MalformedURLException | ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public int getResponse(){return response;}
}
