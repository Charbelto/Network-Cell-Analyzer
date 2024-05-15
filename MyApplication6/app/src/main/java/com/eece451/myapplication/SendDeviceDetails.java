package com.eece451.myapplication;

import android.os.AsyncTask;
import android.util.Log;


import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SendDeviceDetails extends AsyncTask<String, Void, Void> {

    @Override
    protected Void doInBackground(String... params) {
        OkHttpClient client = new OkHttpClient();
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        // put your json here
        RequestBody body = RequestBody.create(JSON, params[1]);
        Request request = new Request.Builder()
                .url("http://192.168.1.175:5000/add_network_info")
                .post(body)
                .build();


        try {
            Response response = client.newCall(request).execute();
            String resStr = response.body().string();
            Log.e("Reponse", resStr);
        } catch (IOException e) {
            Log.e("ERROR", "ERR");
        }


        return null;
    };
}
