package com.nguyenminhtri.projectdocsach.connectinternet;

import android.content.Context;
import android.net.ConnectivityManager;

public class ConnectInternet {
    Context context;

    public ConnectInternet(Context context) {
        this.context = context;
    }

    public boolean checkInternetConnection() {

        try {
            ConnectivityManager connManager =
                    (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connManager.getActiveNetworkInfo() != null
                    && connManager.getActiveNetworkInfo().isAvailable()
                    && connManager.getActiveNetworkInfo().isConnected()) {
                return true;
            } else
                return false;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}

