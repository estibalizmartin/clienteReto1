package com.example.clientereto1.network;

import android.content.res.Resources;

import com.example.clientereto1.MainActivity;
import com.example.clientereto1.R;
import com.example.clientereto1.models.Song;
import com.example.clientereto1.models.UserResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import android.content.res.Resources;

public class CreateUserRequest extends NetConfiguration implements Runnable{

    private final String theUrl = theBaseUrl + "/auth/signup";
    private UserResponse response;
    private String userDataJson;
    public static Resources res;

    public CreateUserRequest (String userDataJson) {
        this.userDataJson = userDataJson;
    }

    @Override
    public void run() {
        try {

            URL url = new URL(theUrl);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setRequestProperty("Content-Type", "application/json");

            httpURLConnection.setDoOutput(true);
            try (OutputStream os = httpURLConnection.getOutputStream()) {
                byte[] input = userDataJson.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            int responseCode = httpURLConnection.getResponseCode();

            this.response = new UserResponse();

            if (responseCode == 432) {

                this.response.setAccess(false);
                this.response.setMessage(res.getString(R.string.user_already_exists));

            } else if (responseCode == 400) {

                this.response.setAccess(false);
                this.response.setMessage(res.getString(R.string.request_error));

            }   if(responseCode == HttpURLConnection.HTTP_OK){

                BufferedReader bufferedReader = new BufferedReader(
                        new InputStreamReader( httpURLConnection.getInputStream() ) );
                StringBuffer response = new StringBuffer();
                String inputLine;
                while ((inputLine = bufferedReader.readLine()) != null) {
                    response.append( inputLine );
                }
                bufferedReader.close();

                this.response.setAccess(true);
                this.response.setMessage(res.getString(R.string.user_created));

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public UserResponse getResponse() {
        return response;
    }

}
