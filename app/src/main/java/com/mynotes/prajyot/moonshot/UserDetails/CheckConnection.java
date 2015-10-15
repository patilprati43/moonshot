package com.mynotes.prajyot.moonshot.UserDetails;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
public class CheckConnection {

    Context c;

    public CheckConnection(Context c) {

        this.c = c;
    }

    public boolean isConnectingToInternet() {

        ConnectivityManager connectivity = (ConnectivityManager) c.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
                for (int i = 0; i < info.length; i++)
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }

        }
//        Intent opps=new Intent(c, OppsActivity.class);
//        opps.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        opps.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        c.startActivity(opps);
        return false;

    }


}