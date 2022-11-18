package com.example.clientereto1.network;

import com.example.clientereto1.models.User;
import com.example.clientereto1.models.UserResponse;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class LoginRequest extends NetConfiguration implements Runnable{
    private final String theUrl = theBaseUrl + "/loginNoToken";
    public UserResponse response;
    private String userDataJson;
    public LoginRequest(String userDataJson) {
        this.userDataJson = userDataJson;
    }
    @Override
    public void run() {

// The URL
        URL url = null;
        try {
            url = new URL( theUrl);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod( "GET" );
            httpURLConnection.setRequestProperty("Content-Type", "application/json");
            httpURLConnection.setDoOutput(true);
            // Sending...
            try(OutputStream os = httpURLConnection.getOutputStream()) {
                byte[] input = userDataJson.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            int responseCode = httpURLConnection.getResponseCode();
            System.out.println(responseCode);
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
                response.setAccess(true);
                ObjectInputStream objectInputStream = new ObjectInputStream(httpURLConnection.getInputStream());
                User user = new User();
                user = (User) objectInputStream.readObject();
                response.setId(user.getId());
                response.setUsername(user.getUsername());
            }
        } catch (MalformedURLException | ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    public UserResponse getResponse(){return response;}
}
