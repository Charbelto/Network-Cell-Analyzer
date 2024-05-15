package com.eece451.myapplication;

import static androidx.core.content.ContentProviderCompat.requireContext;

import android.Manifest;
import android.content.Context;
import android.content.Intent;

import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.telephony.CellIdentityCdma;
import android.telephony.CellIdentityGsm;
import android.telephony.CellIdentityLte;
import android.telephony.CellIdentityWcdma;
import android.telephony.CellInfo;
import android.telephony.CellInfoCdma;
import android.telephony.CellInfoGsm;
import android.telephony.CellInfoLte;
import android.telephony.CellInfoWcdma;
import android.telephony.CellSignalStrength;
import android.telephony.CellSignalStrengthGsm;
import android.telephony.CellSignalStrengthLte;
import android.telephony.CellSignalStrengthTdscdma;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import android.telephony.gsm.GsmCellLocation;
import android.text.format.Formatter;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.cardiomood.android.controls.gauge.SpeedometerGauge;
import com.eece451.myapplication.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.NetworkInterface;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;

import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttp;
import okhttp3.OkHttpClient;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;
    Handler handler = new Handler();
    Runnable runnable;

    Connection connect;
    String ConnectionResult = "";

    private static final int PERMISSION_REQUEST_CODE = 123;
    private TextView operatorTextView, signalStrengthTextView, networkTypeTextView, cellIDTextView, frequencyBandTextView, snrTextView, timestampTextView;
    private PhoneStateListener phoneStateListener;
    private SpeedometerGauge speedometer;
    private Integer delay;
    // Customize SpeedometerGauge
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        delay = 0;
        operatorTextView = findViewById(R.id.operatorName);
        signalStrengthTextView = findViewById(R.id.signalStrength);
        networkTypeTextView = findViewById(R.id.networkType);
        cellIDTextView = findViewById((R.id.cellID));
        frequencyBandTextView = findViewById(R.id.frequencyBand);
        snrTextView = findViewById(R.id.snr);
        speedometer = (SpeedometerGauge) findViewById(R.id.speedometer);
        timestampTextView = findViewById(R.id.timestamp);
        Button button_stats = findViewById(R.id.button);
        button_stats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Stats.class);
                startActivity(intent);
            }
        });
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_NETWORK_STATE) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_WIFI_STATE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.READ_PHONE_STATE, Manifest.permission.INTERNET,Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_NETWORK_STATE, Manifest.permission.ACCESS_WIFI_STATE}, PERMISSION_REQUEST_CODE);
        } else {
            startListeningAndPost();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            startListeningAndPost();
        }
    }
    @Override
    protected void onResume() {
        // Gets Info and Sends to Server Every delay seconds
        handler.postDelayed(runnable = new Runnable() {
            public void run() {
                try {
                    handler.postDelayed(runnable, delay);
                    startListeningAndPost();
                }
                catch(Exception ex_all){
                    Log.d("Error", ex_all.getMessage());;
                }
            }
        }, delay);
        delay=10000; // changes delay in onCreate from 1 to 10 seconds
        super.onResume();

    }
    public class RunTasksTenSec extends TimerTask {
        public void run() {
            try {
                startListeningAndPost();
                Thread.sleep(10000);
            } catch (InterruptedException ex) {
                Log.d("Error", ex.getMessage());
            }

        }
        public void main(String args[]) throws InterruptedException {
            TimerTask timerTask = new RunTasksTenSec(); //reference created for TimerTask class
            Timer timer = new Timer(true);
            timer.scheduleAtFixedRate(timerTask, 0, 10); // 1.task 2.delay 3.period
            Thread.sleep(6000);
            timer.cancel();
        }
    }




    public void startListeningAndPost() {
        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        GsmCellLocation cellLocation = (GsmCellLocation) telephonyManager.getCellLocation();
        String operator = telephonyManager.getSimOperatorName();
        int networkType = telephonyManager.getNetworkType();
        GetNetworkType getNetworkType = new GetNetworkType();
        String networkTypeString = getNetworkType.getNetworkTypeString(networkType);
        operatorTextView.setText("  " + operator);
        networkTypeTextView.setText("  " + networkTypeString);
        // get TimeStamp
        Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());
        String timestamp = currentTimestamp.toString();
        timestampTextView.setText(currentTimestamp.toString());
        // end of get TimeStamp

        // get Cell ID,FreqBand3G,FreqBandLTE
        int lac = cellLocation.getLac() % 0xffff;
        int cid = 0;
        int freq = 0;
        String freqString = "N/A";
        //int x = cellIdentityLte.getCi();
        try {
            CellInfoGsm cellInfoGsm = (CellInfoGsm) telephonyManager.getAllCellInfo().get(0);
            CellIdentityGsm cellIdentityGsm = cellInfoGsm.getCellIdentity();
            cid = cellIdentityGsm.getCid();
            freq = cellIdentityGsm.getArfcn();
            freqString = "N/A";
            freqString = new Integer(freq).toString();
            frequencyBandTextView.setText("N/A");

        } catch (Exception e) {
            if (networkType == 13) {
                CellInfoLte cellInfoLte = (CellInfoLte) telephonyManager.getAllCellInfo().get(0);
                CellIdentityLte cellIdentityLte = cellInfoLte.getCellIdentity();
                cid = cellIdentityLte.getCi();

                BandNameFinderLTE bandNameFinderLTE = new BandNameFinderLTE();
                freqString = bandNameFinderLTE.getBandName(cellIdentityLte.getEarfcn());
                frequencyBandTextView.setText(" " + freqString);

            } else if (networkType == 3 || networkType == 8 || networkType == 9 || networkType == 10 || networkType == 15) {
                CellInfoWcdma cellInfoWcdma = (CellInfoWcdma) telephonyManager.getAllCellInfo().get(0);
                CellIdentityWcdma cellIdentityWcdma = cellInfoWcdma.getCellIdentity();
                cid = cellIdentityWcdma.getCid();
                BandNameFinder3G bandNameFinder3G = new BandNameFinder3G();
                freqString = bandNameFinder3G.getBandName(cellIdentityWcdma.getUarfcn());
                frequencyBandTextView.setText(" " + freqString);
            }
        }
        String cellIDString = lac + "-" + cid;
        cellIDTextView.setText("  " + cellIDString);
        // end of Cell ID,FreqBand3G,FreqBandLTE
        //get Signal Strength
        int signalStrengthValue = 0;
        SignalStrength signalStrength = null;

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            if (signalStrength.getGsmSignalStrength() != 99) {
                signalStrengthValue = signalStrength.getGsmSignalStrength() * 2 - 113;
            }
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            signalStrengthValue = telephonyManager.getSignalStrength().getCellSignalStrengths().get(0).getDbm();
        }

        signalStrengthTextView.setText("  " + signalStrengthValue + " dbm");

        // end of SignalStrength
        // SNR
        String SNR = "N/A";
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            if (telephonyManager.getNetworkType() == TelephonyManager.NETWORK_TYPE_LTE) {
                String signal = signalStrength.toString();
                String[] parts = signal.split(" ");
                snrTextView.setText(" " + parts[11] + " dbm");
            } else if (telephonyManager.getNetworkType() == TelephonyManager.NETWORK_TYPE_UMTS
                    || telephonyManager.getNetworkType() ==
                    TelephonyManager.NETWORK_TYPE_HSDPA
                    || telephonyManager.getNetworkType() ==
                    TelephonyManager.NETWORK_TYPE_HSUPA
                    || telephonyManager.getNetworkType() ==
                    TelephonyManager.NETWORK_TYPE_HSPA
                    || telephonyManager.getNetworkType() ==
                    TelephonyManager.NETWORK_TYPE_HSPAP) {
                String signal = signalStrength.toString();
                String[] parts = signal.split(" ");
                SNR = parts[5];
                snrTextView.setText(" " + parts[5] + " dbm");
            } else {
                snrTextView.setText(" N/A");
            }
        } else {
            try {
                //List<CellInfo> cellInfos = telephonyManager.getAllCellInfo();
                CellInfoLte cellInfoLte = (CellInfoLte) telephonyManager.getAllCellInfo().get(0);
                CellSignalStrengthLte cellSignalStrengthLte = cellInfoLte.getCellSignalStrength();
                SNR = String.valueOf(cellSignalStrengthLte.getRssnr());
                snrTextView.setText(" " + SNR + " dbm");
            } catch (Exception e) {
                snrTextView.setText(" N/A");
                Log.d("Error", "Error");
            }
        }
        // end of SNR
        //get mac and ip address

        WifiManager wm = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
        String ip = Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());
        WifiInfo getInfo = wm.getConnectionInfo();
        String mac = getInfo.getMacAddress();
        //String imei= telephonyManager.getImei();

        //end of get mac


        // connect to Server and Write
        String jsonString = String.format("{" +
                "\"timestamp\":\"%s\"," +
                "\"ip_address\":\"%s\"," +
                "\"mac_address\":\"%s\"," +
                "\"network_type\":\"%s\"," +
                "\"operator\":\"%s\"," +
                "\"signal_strength\":\"%s\"," +
                "\"frequency_band\":\"%s\"," +
                "\"SNR\":\"%s\"," +
                "\"cell_id\":\"%s\"" +
                "}", timestamp, ip, mac, networkTypeString, operator, signalStrengthValue, freqString, SNR, cellIDString);


        Log.e("JSON", jsonString);
        new SendDeviceDetails().execute("http://192.168.1.175:5000/add_network_info", jsonString);
        //end of write
    };


}


