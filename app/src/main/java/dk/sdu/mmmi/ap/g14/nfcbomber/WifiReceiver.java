package dk.sdu.mmmi.ap.g14.nfcbomber;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by declow on 3/22/17.
 */

public class WifiReceiver extends BroadcastReceiver {


    private CallBack callBack;
    public WifiReceiver(CallBack callback) {
        this.callBack = callback;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        callBack.callBack();
    }
}