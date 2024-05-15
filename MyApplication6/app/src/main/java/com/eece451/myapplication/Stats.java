package com.eece451.myapplication;

import android.app.DatePickerDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import net.sourceforge.jtds.jdbc.DateTime;

import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

public class Stats extends AppCompatActivity {
    TextView AvgConnTimePerOperator, AvgConnTimePerNetwork, AvgPowerPerNetwork, AvgPowerPerDevice, AvgSNRPerNetwork ;
    String from_year;
    String from_month;
    String from_day;
    String from_date;
    String to_year;
    String to_month;
    String to_day;
    String to_date;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_stats);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Button button_start = findViewById(R.id.button2);
        button_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialogFrom();

            }
        });
        Button button_end = findViewById(R.id.button3);
        button_end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialogTo();

            }
        });
        Button button_get = findViewById(R.id.button4);
        button_get.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getStats();
            }
        });
        AvgConnTimePerOperator= findViewById(R.id.AvgConnTimePerOperator);
        AvgConnTimePerNetwork=findViewById(R.id.AvgConnTimePerNetwork);
        AvgPowerPerNetwork = findViewById(R.id.AvgPowerPerNetwork);
        AvgPowerPerDevice=findViewById(R.id.AvgPowerPerDevice);
        AvgSNRPerNetwork=findViewById(R.id.AvgSNRPerNetwork);

    }
    private void openDialogFrom(){
        DatePickerDialog dialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener(){
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                from_year = String.valueOf(year);
                from_month = String.valueOf(month+1);
                from_day  = String.valueOf(day);
                if (day<10){
                    from_day="0"+from_day;
                }
                if (month+1<10){
                    from_month="0"+from_month;
                }
                from_date = from_year+"-"+from_month + "-"+from_day+ " 00:00:00.000000";
                Log.e("DateStart", from_date);
            }
        }, 2024,3, 26);
        dialog.show();
    }
    private void openDialogTo(){
        DatePickerDialog dialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener(){
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                to_year = String.valueOf(year);
                to_month = String.valueOf(month+1);
                to_day  = String.valueOf(day);
                if (day<10){
                    to_day="0"+to_day;
                }
                if (month+1<10){
                    to_month="0"+to_month;
                }

                to_date = to_year+"-"+to_month + "-"+to_day+ " 00:00:00.000000";
                Log.e("DateEnd", to_date);
            }
        }, 2024,3, 27);
        dialog.show();
    }
    private void getStats() {
        if (from_date!=null && to_date!=null) {
            String jsonString = String.format("{" +
                    "\"start_datetime\":\"%s\"," +
                    "\"end_datetime\":\"%s\"" + "}", from_date, to_date);
            Log.e("dates", jsonString);
            try {
                String res = new GetStats().execute("http://192.168.1.175:5000/get_statistics", jsonString).get();
                JSONObject jsonObject = new JSONObject(res);

                //avg conn/operator
                String operator_per=jsonObject.get("operator").toString();
                operator_per = operator_per.substring(1, operator_per.length() - 1).replaceAll("\"","").replaceAll(",",", ");
                AvgConnTimePerOperator.setText(operator_per);

                // avg conn/network
                String network_per=jsonObject.get("net_type").toString();
                network_per=network_per.substring(1, network_per.length() - 1).replaceAll("\"","").replaceAll(",",", ");
                AvgConnTimePerNetwork.setText(network_per);

                //avg signal power/network
                String avgpwnetwork = jsonObject.get("average_power").toString();
                avgpwnetwork = avgpwnetwork.substring(1, avgpwnetwork.length() - 1).replaceAll("\"","").replaceAll(",",", ");
                AvgPowerPerNetwork.setText(avgpwnetwork);
                //Log.e("res", avgpwnetwork);

                //avg signal power/device
                String avgpowerdevice = jsonObject.get("average_power_per_device").toString();
                avgpowerdevice = avgpowerdevice.substring(1, avgpowerdevice.length() - 1).replaceAll("\"","").replaceAll(",",", ");
                AvgPowerPerDevice.setText(avgpowerdevice);

                //avg snr/network
                String avgsnrnetwork = jsonObject.get("average_power_per_device").toString();
                avgsnrnetwork = avgsnrnetwork.substring(1, avgsnrnetwork.length() - 1).replaceAll("\"","").replaceAll(",",", ");
                AvgSNRPerNetwork.setText(avgsnrnetwork);
            }
            catch(Exception e){
                Log.e("res", "fail");
            }

        }
    }

}