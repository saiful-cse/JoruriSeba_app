package com.coxtunes.joruriseba;

import android.app.Service;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;


public class Internet {

    private Context context;

    public Internet(Context context) {

        this.context = context;
    }

    public boolean isConnected(){

        ConnectivityManager connectivity = (ConnectivityManager)
                context.getSystemService(Service.CONNECTIVITY_SERVICE);


        if(connectivity != null){
            NetworkInfo info = connectivity.getActiveNetworkInfo();

            if(info != null){

                if(info.getState() == NetworkInfo.State.CONNECTED){
                    return true;
                }

            }
        }


        return false;

    }
}
