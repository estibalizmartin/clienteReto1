package com.example.clientereto1.network;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class DeleteFavouriteRequest extends NetConfiguration implements Runnable {

    private final String theUrl = theBaseUrl + "/favorites";
    private int response;
    private String userDataJson;

    public DeleteFavouriteRequest(String userDataJson) {
        this.userDataJson = userDataJson;
    }

    @Override
    public void run() {
        try {

            URL url = new URL(theUrl);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("DELETE");
            httpURLConnection.setRequestProperty("Content-Type", "application/json");

            httpURLConnection.setDoOutput(true);
            try(OutputStream os = httpURLConnection.getOutputStream()) {
                byte[] input = userDataJson.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            int responseCode = httpURLConnection.getResponseCode();
            System.out.println(responseCode);

            if (responseCode == 432){
                this.response = 0;
            } else if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader bufferedReader = new BufferedReader(
                        new InputStreamReader( httpURLConnection.getInputStream()));
                StringBuffer response = new StringBuffer();
                String inputLine;
                while ((inputLine = bufferedReader.readLine()) != null) {
                    response.append( inputLine );
                }
                bufferedReader.close();

                this.response = Integer.parseInt(response.toString());
            }

        } catch (Exception e) {
            System.out.println("entro porque he fallado");
            e.printStackTrace();
        }

    }

    public int getResponse() {
        return response;
    }

}
