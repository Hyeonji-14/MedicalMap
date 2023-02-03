package com.medical.medicalmap.assist;

import android.os.Handler;
import android.os.Message;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class NetworkThread extends Thread {

    public static final String API_KEY1 = "idV3x1BRpqZChAWDBSwjZc7Jo8VNBFDDJZh%2BHSyc%2BLwWGnr%2FRlifZtaE3VUC48eBfEXtSW4VrfIDcFsAglB5CQ%3D%3D";
    public static final String API_KEY2 = "idV3x1BRpqZChAWDBSwjZc7Jo8VNBFDDJZh%2BHSyc%2BLwWGnr%2FRlifZtaE3VUC48eBfEXtSW4VrfIDcFsAglB5CQ%3D%3D";
    public static final String API_URL = "https://api.odcloud.kr/api/3080249/v1/uddi:82f66c8a-d151-4ff6-928a-82ff5404552c_201906111027?";
    public static final String API_URL2 = "https://api.odcloud.kr/api/3080242/v1/uddi:c1c2764e-51be-4f95-8604-b26c34dbc9a5_201906111030?";
    public static final String API_TYPE = "JSON";

    Handler handler;
    String query, type;


    public NetworkThread(Handler handler, String query, String type) {
        this.handler = handler;
        this.query = query;
        this.type = type;
    }

    @Override
    public void run() {
        String apiUrl = "";

        if ("약국".equals(type)) {
            apiUrl = API_URL + query;
        } else {
            apiUrl = API_URL2 + query;
        }
        try {
            URL url = new URL(apiUrl);
            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Content-type", "application/json");


            BufferedReader reader = null;
            if (connection.getResponseCode() == 200) {
                InputStreamReader inputStreamReader = new InputStreamReader(connection.getInputStream());
                reader = new BufferedReader(inputStreamReader);
            } else {
                System.out.println("실패");
            }
            StringBuilder builder = new StringBuilder();
            String temp;
            while ((temp = reader.readLine()) != null) {
                builder.append(temp);
            }

            reader.close();
            connection.disconnect();
            String result = builder.toString();

            Message msg = handler.obtainMessage();
            msg.obj = result;
            handler.sendMessage(msg);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}