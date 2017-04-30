package dk.sdu.mmmi.ap.g14.nfcbomber;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Broadcast Receiver for when WIFI state changes
 */
public class WifiReceiver extends BroadcastReceiver {


    private CallBack callBack;
    public WifiReceiver(CallBack callback) {
        this.callBack = callback;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        callBack.wifiChanged();
    }
}