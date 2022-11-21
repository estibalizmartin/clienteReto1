package com.example.clientereto1.network;

import android.content.Context;
import android.content.res.Resources;

import com.example.clientereto1.R;
import com.example.clientereto1.models.UserResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class LogInRequest extends NetConfiguration implements Runnable{
    private final String theUrl = theBaseUrl + "/loginNoToken";
    public UserResponse response;
    public static Resources res;
    String userDataJson;

    public LogInRequest(String userDataJson, Context context) {
        this.userDataJson = userDataJson;
        response = new UserResponse();
        res = context.getResources();
    }

    @Override
    public void run() {

        try {
            URL url = new URL( theUrl);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod( "POST" );
            httpURLConnection.setRequestProperty("Content-Type", "application/json");
            httpURLConnection.setDoOutput(true);

            try (OutputStream os = httpURLConnection.getOutputStream()) {
                byte[] input = userDataJson.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            int responseCode = httpURLConnection.getResponseCode();

            if (responseCode == 432){
                response.setAccess(false);
                response.setMessage("El usuario no existe");
            }else if(responseCode == 400){
                response.setAccess(false);
                response.setMessage("Los datos son erróneos");
            }else if(responseCode == 433){
                response.setAccess(false);
                response.setMessage("La contraseña es errónea");
            }else if(responseCode == 202){
                BufferedReader bufferedReader = new BufferedReader(
                        new InputStreamReader( httpURLConnection.getInputStream() ) );
                StringBuilder response = new StringBuilder();
                String inputLine;
                while ((inputLine = bufferedReader.readLine()) != null) {
                    response.append( inputLine );
                }
                bufferedReader.close();

                String id = response.substring(2, response.indexOf(",") - 1).replace("\"\"", "");
                String username = response.substring(response.indexOf(",") + 2, response.length() - 2).replace("\"\"", "");


                this.response.setAccess(true);
                this.response.setMessage(res.getString(R.string.welcome)+ " " + username);
                this.response.setUsername(username);
                this.response.setId(Integer.parseInt(id));
            }

        } catch (MalformedURLException | ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } /*catch (ClassNotFoundException e) {
            e.printStackTrace();
        }*/
    }
    public UserResponse getResponse(){return response;}
}
