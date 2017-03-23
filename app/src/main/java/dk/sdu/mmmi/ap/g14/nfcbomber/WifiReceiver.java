package dk.sdu.mmmi.ap.g14.nfcbomber;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

/**
 * Created by declow on 3/22/17.
 */

public class WifiReceiver extends BroadcastReceiver {


    private callBacks callBack;
    public WifiReceiver(callBacks callback) {
        this.callBack = callback;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.wtf("Wifi", "Receiver!");
        callBack.connectionChanged();
    }
}