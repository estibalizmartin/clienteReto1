package com.example.clientereto1.network;

import android.content.Context;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.ListView;
import android.widget.Toast;

import com.example.clientereto1.R;
import com.example.clientereto1.adapters.MyTableAdapter;
import com.example.clientereto1.models.RequestResponse;
import com.example.clientereto1.models.Song;
import com.example.clientereto1.models.UserResponse;

import java.util.ArrayList;

public class NetworkUtilites {

    Context context;
    Resources res;

    public NetworkUtilites(Context context) {
        this.context = context;
        res = context.getResources();
    }

    public ArrayList<Song> makeRequest(SongsRequest songsRequest) {
        if (isConnected()) {

            Thread thread = new Thread(songsRequest);
            try {
                thread.start();
                thread.join(); // Awaiting response from the server...
            } catch (InterruptedException e) {
                // Nothing to do here...
            }
            // Processing the answer
            ArrayList<Song> listSongs = songsRequest.getResponse();

            return listSongs;
        } else
            return new ArrayList<>();
    }

    public ArrayList<Song> makeRequest(FavouritesRequest favouritesRequest) {
        if (isConnected()) {

            Thread thread = new Thread(favouritesRequest);
            try {
                thread.start();
                thread.join(); // Awaiting response from the server...
            } catch (InterruptedException e) {
                // Nothing to do here...
            }
            // Processing the answer
            ArrayList<Song> listSongs = favouritesRequest.getResponse();

            return listSongs;
        } else
            return new ArrayList<>();
    }

    public UserResponse makeRequest(CreateUserRequest createUserRequest) {
        if (isConnected()) {

            Thread thread = new Thread(createUserRequest);
            try {
                thread.start();
                thread.join(); // Awaiting response from the server...
            } catch (InterruptedException e) {
                // Nothing to do here...
            }
            // Processing the answer
            UserResponse response = createUserRequest.getResponse();

            return response;


        } else
            return new UserResponse(false, res.getString(R.string.error_communication));
    }

    public UserResponse makeRequest(LogInRequest logInRequest) {
        if (isConnected()) {

            Thread thread = new Thread(logInRequest);
            try {
                thread.start();
                thread.join(); // Awaiting response from the server...
            } catch (InterruptedException e) {
                // Nothing to do here...
            }
            // Processing the answer
            UserResponse response = logInRequest.getResponse();

            return response;


        } else
            return new UserResponse(false, res.getString(R.string.error_communication));
    }

    public RequestResponse makeRequest(ChangePasswordRequest changePasswordRequest) {
        if (isConnected()) {

            Thread thread = new Thread(changePasswordRequest);
            try {
                thread.start();
                thread.join();
            } catch (InterruptedException e) {
            }

            RequestResponse response = changePasswordRequest.getResponse();

            return response;

        } else
            return new RequestResponse(res.getString(R.string.request_error));
    }


    public boolean isConnected() {
        boolean ret = false;
        try {

            ConnectivityManager connectivityManager = (ConnectivityManager) context
                    .getSystemService( Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            if ((networkInfo != null) && (networkInfo.isAvailable()) && (networkInfo.isConnected()))
                ret = true;
        } catch (Exception e) {
            Toast.makeText(context, context.getString(R.string.error_communication), Toast.LENGTH_SHORT).show();
        }
        return ret;
    }
}
