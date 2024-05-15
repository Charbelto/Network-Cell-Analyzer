package com.eece451.myapplication;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ConnectivityHelper {

    private static void sendPost(String jsonString) {
        HttpURLConnection httpURLConnection = null;
        try {
            URL url = new URL("http://192.168.1.175/add_network_info");
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setRequestProperty("Content-Type", "application/json");

            OutputStream out = new BufferedOutputStream(httpURLConnection.getOutputStream());
            out.write(jsonString.getBytes());
            out.flush();
            out.close();

        } catch (IOException e) {
            Log.e("ConnectivityHelper", "Error" + e.getMessage());
        } finally {
            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
            }
        }


    }
    class PostData extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            try {

                // on below line creating a url to post the data.
                URL url = new URL("http://192.168.1.");

                // on below line opening the connection.
                HttpURLConnection client = (HttpURLConnection) url.openConnection();

                // on below line setting method as post.
                client.setRequestMethod("POST");

                // on below line setting content type and accept type.
                client.setRequestProperty("Content-Type", "application/json");
                client.setRequestProperty("Accept", "application/json");

                // on below line setting client.
                client.setDoOutput(true);

                // on below line we are creating an output stream and posting the data.
                try (OutputStream os = client.getOutputStream()) {
                    byte[] input = strings[0].getBytes("utf-8");
                    os.write(input, 0, input.length);
                }

                // on below line creating and initializing buffer reader.
                try (BufferedReader br = new BufferedReader(
                        new InputStreamReader(client.getInputStream(), "utf-8"))) {

                    // on below line creating a string builder.
                    StringBuilder response = new StringBuilder();

                    // on below line creating a variable for response line.
                    String responseLine = null;

                    // on below line writing the response
                    while ((responseLine = br.readLine()) != null) {
                        response.append(responseLine.trim());
                    }

                    // on below line displaying a toast message.
                }

            } catch (Exception e) {

                // on below line handling the exception.
                e.printStackTrace();
            }
            return null;
        }
    }





}
