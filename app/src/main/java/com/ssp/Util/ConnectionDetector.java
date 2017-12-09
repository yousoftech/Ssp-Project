package com.ssp.Util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by DELL on 12/3/2017.
 */

public class ConnectionDetector {

    private Context _context;

    public ConnectionDetector (Context context){
        this._context=context;
    }

    public boolean isConnectingToInternet(){

        ConnectivityManager connectivityManager=(ConnectivityManager)_context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null){
            NetworkInfo[] info=connectivityManager.getAllNetworkInfo();

            if (info != null)
            {
                for(int i=0;i<info.length;i++){
                    if(info[i].getState()== NetworkInfo.State.CONNECTED){
                        return true;
                    }
                }
            }
        }

        return false;
    }

}
