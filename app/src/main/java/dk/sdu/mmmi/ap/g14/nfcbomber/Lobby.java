package dk.sdu.mmmi.ap.g14.nfcbomber;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

public class Lobby extends AppCompatActivity implements callBacks {

    private static final String TAG = "Lobby";
    WifiReceiver receiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_host);

        Intent intent = getIntent();
        IntentFilter inf = new IntentFilter();
        inf.addAction("android.net.wifi.STATE_CHANGE");

        receiver = new WifiReceiver(this);

        this.registerReceiver(receiver, inf);

        String gameType = intent.getStringExtra(Home.GAME_SETUP);
        if (gameType.equals(Home.GAME_HOST))
            setTitle(getResources().getString(R.string.app_name) + getResources().getString(R.string.app_type_host));
        else
            setTitle(getResources().getString(R.string.app_name) + getResources().getString(R.string.app_type_client));

        Log.wtf(TAG, gameType);

    }

    //Check the state of wifi
    //return 0 equals to wifi not connected
    private int checkWifi() {
        WifiManager wifi = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
        WifiInfo wifiInfo = wifi.getConnectionInfo();
        int ip = wifiInfo.getIpAddress();
        Log.wtf("WIFI ", Integer.toString(ip));
        return ip;
    }

    public String getLocalIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                        return inetAddress.getHostAddress();
                    }
                }
            }
        } catch (SocketException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    @Override
    public void connectionChanged() {
        TextView text = (TextView) findViewById(R.id.ip_address);
        Button b = (Button) findViewById(R.id.start_host);
        if (checkWifi() != 0) {
            text.setText(getLocalIpAddress());
            b.setEnabled(true);
        } else {
            text.setText(R.string.loading);
            b.setEnabled(false);
        }
    }
}
