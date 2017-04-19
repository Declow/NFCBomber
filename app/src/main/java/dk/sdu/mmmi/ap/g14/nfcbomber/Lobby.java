package dk.sdu.mmmi.ap.g14.nfcbomber;

import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.NfcEvent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.Random;

import dk.sdu.mmmi.ap.g14.nfcbomber.network.Com;
import dk.sdu.mmmi.ap.g14.nfcbomber.network.NetObject;
import dk.sdu.mmmi.ap.g14.nfcbomber.server.Server;

public class Lobby extends AppCompatActivity implements CallBack, NfcAdapter.CreateNdefMessageCallback {

    private static final String TAG = "Lobby";
    WifiReceiver receiver;
    NfcAdapter mNfcAdapter;
    Server server;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_host);

        //wifiReceiver
        Intent intent = getIntent();
        IntentFilter inf = new IntentFilter();
        inf.addAction("android.net.wifi.STATE_CHANGE");

        receiver = new WifiReceiver(this);

        this.registerReceiver(receiver, inf);


        //NFC checker
        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if (mNfcAdapter == null) {
            Toast.makeText(this, "NFC is not available", Toast.LENGTH_SHORT).show();
            finish();
            return;
        } else {
            Log.wtf(TAG, "NFC available");
        }

        mNfcAdapter.setNdefPushMessageCallback(this, this);

        createServer();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.unregisterReceiver(receiver);
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

    private InetAddress getInetAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                        return inetAddress;
                    }
                }
            }
        } catch (SocketException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    private String getLocalIpAddress() {
        return getInetAddress().getHostAddress();
    }

    @Override
    public void wifiChanged() {
        TextView text = (TextView) findViewById(R.id.ip_address);
        Button b = (Button) findViewById(R.id.start_host);
        if (checkWifi() != 0) {
            text.setText(getLocalIpAddress());
            b.setEnabled(true);
        } else {
            text.setText(R.string.wait_wifi);
            b.setEnabled(false);
        }
    }

    @Override
    public void updateUI(final int i) {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.wtf(TAG, "updateUI");
                TextView text = (TextView) findViewById(R.id.connectedDevices);

                    text.setText(Integer.toString(i));
            }
        });
    }

    @Override
    public NdefMessage createNdefMessage(NfcEvent e) {

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] msg = new byte[1];
        try {
            ObjectOutputStream os = new ObjectOutputStream(out);
            os.writeObject(getInetAddress());
            msg = out.toByteArray();
            os.close();
            out.close();
        } catch (IOException e1) {
            e1.printStackTrace();
        }

        return new NdefMessage(
                new NdefRecord[] { NdefRecord.createMime(
                        "application/dk.sdu.mmmi.ap.g14.nfcbomber", msg)
                });
    }

    private void createServer() {
        server = new Server(getApplicationContext().getResources().getInteger(R.integer.port), this);
    }

    public void startGame(View v) {
        int bombTimer = new Random().nextInt(10) + 5;

        server.sendToEveryConnectedDevice(new NetObject(bombTimer, Com.START_GAME));
        Intent intent = new Intent(this, Game.class);
        intent.putExtra(Game.BOMB_TIME_EXTRA, bombTimer);
        startActivity(intent);
    }


}
