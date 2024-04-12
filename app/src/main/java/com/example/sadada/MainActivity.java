package com.example.sadada;



import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.Manifest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private WifiManager wifiManager;
    private TextView wifiDeviceTextView;

    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button scanButton = findViewById(R.id.button);
        wifiDeviceTextView = findViewById(R.id.textView);

        wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        scanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Check for location permission
                if (ActivityCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // Request location permission
                    ActivityCompat.requestPermissions(MainActivity.this,
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                            LOCATION_PERMISSION_REQUEST_CODE);
                    return;
                }
                if(wifiManager.startScan()){
                    checkWifi();
                }
                else {
                    Toast.makeText(MainActivity.this, "Refresh Problem",
                            Toast.LENGTH_SHORT).show();

                }


            }
        });


    }
 
    
    private double calculateDistance(int rssi) {
        int txPower = -65; // Reference signal strength at 1 meter, you may need to adjust this value based on your environment
        double exp = (txPower - rssi) / (10 * 2.0); // 2.0 is the path loss exponent
        return Math.pow(10, exp);
    }
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    public void checkWifi(){
        wifiDeviceTextView = findViewById(R.id.textView);

        wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        if (wifiManager != null) {
            if (!wifiManager.isWifiEnabled()) {
                wifiManager.setWifiEnabled(true);
            }

            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        LOCATION_PERMISSION_REQUEST_CODE);
                return;
            }
            List<ScanResult> scanResults = wifiManager.getScanResults();
            if(scanResults==null){
                Toast.makeText(this, "No devices !",
                        Toast.LENGTH_SHORT).show();
            }
            StringBuilder wifiDevices = new StringBuilder();
            StringBuilder data = new StringBuilder();

            for (ScanResult scanResult : scanResults) {
                int rssi = scanResult.level;
                double distance = calculateDistance(rssi);
                List<String> macs = new ArrayList<>();
                macs.add("b0:22:7a:a0:47:42");
                macs.add("48:2f:6b:44:e7:e1");
                macs.add("48:2f:6b:44:e7:e2");
                macs.add("48:2f:6b:44:cc:62");
                macs.add("9c:9d:7e:11:1f:8e");
                macs.add("72:1a:b8:d4:6b:01");
                macs.add("50:91:e3:f7:65:4c");
                wifiDevices.append(scanResult.SSID).append("\n");
                if(macs.contains(scanResult.BSSID.toString())){
                    data.append(scanResult.SSID+"\n"+distance+"\n"+scanResult.BSSID).append("\n\n");
                }
//                data.append(scanResult.SSID+"\n"+distance+"\n"+scanResult.BSSID).append("\n\n");

                // You can get more information like BSSID, signal strength, etc. from ScanResult
            }

            wifiDeviceTextView.setText(data.toString());
            Toast.makeText(this, "Wifi refreshed",
                    Toast.LENGTH_SHORT).show();

        } else {
            wifiDeviceTextView.setText("WiFi Manager is not available");
        }

    }
    private static final int PERMISSIONS_REQUEST_LOCATION = 1;
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSIONS_REQUEST_LOCATION) {
            // If request is cancelled, the result arrays are empty.
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // permission was granted, yay! Do the
                // location-related task you need to do.
                checkWifi();
            } else {
                // permission denied, boo! Disable the
                // functionality that depends on this permission.
                Toast.makeText(this, "Permission denied to access location", Toast.LENGTH_SHORT).show();
            }
        }
    }


}




