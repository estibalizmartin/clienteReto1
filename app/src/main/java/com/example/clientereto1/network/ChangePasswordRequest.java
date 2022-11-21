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

public class ChangePasswordRequest extends NetConfiguration implements Runnable {

    private final String theUrl = theBaseUrl + "/changePasswordNoToken";
    private RequestResponse response;
    private String userDataJson;
    private Resources res;

    public ChangePasswordRequest(String userDataJson, Context context) {
        this.userDataJson = userDataJson;
        res = context.getResources();
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

            if (responseCode == 432 || responseCode == 433) {

                this.response.setAccess(false);
                this.response.setMessage(res.getString(R.string.user_doesnt_exist));

            } else if (responseCode == HttpURLConnection.HTTP_ACCEPTED) {

                BufferedReader bufferedReader = new BufferedReader(
                        new InputStreamReader( httpURLConnection.getInputStream()));
                StringBuffer response = new StringBuffer();
                String inputLine;
                while ((inputLine = bufferedReader.readLine()) != null) {
                    response.append( inputLine );
                }
                bufferedReader.close();

                this.response.setAccess(true);
                this.response.setMessage(res.getString(R.string.password_changed));

            } else {
                this.response.setAccess(false);
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
