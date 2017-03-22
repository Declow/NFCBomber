package dk.sdu.mmmi.ap.g14.nfcbomber;

import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.Formatter;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.net.InetAddress;

public class Lobby extends AppCompatActivity {

    private static final String TAG = "Lobby";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_host);

        Intent intent = getIntent();
        String gameType = intent.getStringExtra(Home.GAME_SETUP);
        if (gameType.equals(Home.GAME_HOST))
            setTitle(getResources().getString(R.string.app_name) + getResources().getString(R.string.app_type_host));
        else
            setTitle(getResources().getString(R.string.app_name) + getResources().getString(R.string.app_type_client));

        Log.wtf(TAG, gameType);
        int ip = checkWifi();
        if (ip == 0) {
            Toast toast = Toast.makeText(getApplicationContext(), "No wifi connection", Toast.LENGTH_LONG);
            toast.show();
        } else {
            showInfo(Integer.toString(ip));
        }
    }

    private void showInfo(String ip){
        TextView ipAddress = (TextView) findViewById(R.id.ip_address);
        String ipString = "";
        try {
            ipString = InetAddress.getLocalHost().getHostAddress();
            ipAddress.setText(ipString);
        } catch (Exception e) {
            Log.wtf(TAG, e.toString());
            Log.wtf(TAG, "Unable to get ip");
            ipAddress.setText("Unable to get ip");
        }

    }

    private int checkWifi() {
        WifiManager wifi = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
        WifiInfo wifiInfo = wifi.getConnectionInfo();
        int ip = wifiInfo.getIpAddress();
        Log.wtf("WIFI ", Integer.toString(ip));
        return ip;
    }
}
