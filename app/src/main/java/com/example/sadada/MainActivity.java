package com.example.sadada;

import android.content.Context;
import android.content.pm.PackageManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private WifiManager wifiManager;
    private TextView wifiDeviceTextView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkWifi();


        Button scanButton = findViewById(R.id.button);
        scanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                checkWifi();

            }
        });

    }
    private double calculateDistance(int rssi) {
        int txPower = -65; // Reference signal strength at 1 meter, you may need to adjust this value based on your environment
        double exp = (txPower - rssi) / (10 * 2.0); // 2.0 is the path loss exponent
        return Math.pow(10, exp);
    }

    public void checkWifi(){
        wifiDeviceTextView = findViewById(R.id.textView);

        wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        if (wifiManager != null) {
            if (!wifiManager.isWifiEnabled()) {
                wifiManager.setWifiEnabled(true);
            }

            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                return;
            }
            List<ScanResult> scanResults = wifiManager.getScanResults();
            StringBuilder wifiDevices = new StringBuilder();
            StringBuilder data = new StringBuilder();

            for (ScanResult scanResult : scanResults) {
                int rssi = scanResult.level;
                double distance = calculateDistance(rssi);

                wifiDevices.append(scanResult.SSID).append("\n");
                data.append(scanResult.SSID+"\n"+distance).append("\n\n");
                // You can get more information like BSSID, signal strength, etc. from ScanResult
            }

            wifiDeviceTextView.setText(data.toString());

        } else {
            wifiDeviceTextView.setText("WiFi Manager is not available");
        }
        Toast.makeText(this, "Wifi refreshed",
                Toast.LENGTH_SHORT).show();
    }

}




