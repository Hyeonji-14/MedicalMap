package com.example.medicalmap.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.medicalmap.assist.Helper;
import com.example.medicalmap.assist.NetworkThread;
import com.example.medicalmap.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends Activity {
    Button btn1, btn2;
    Handler handler;
    Handler handler2;
    Helper helper;
    ProgressDialog progressDialog;


    ArrayAdapter<String> adapter;

    protected void onCreate(Bundle savedlnstanceState) {
        super.onCreate(savedlnstanceState);
        setContentView(R.layout.activity_main);
        ImageButton medical1 = (ImageButton) findViewById(R.id.medical);
        ImageButton medical2 = (ImageButton) findViewById(R.id.medi);
        helper = Helper.getInstance(this);


        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("데이터 동기화중...");
        progressDialog.setCancelable(true);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        medical1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, HospitalActivity.class);
                startActivity(intent);
            }

        });
        medical2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, PharmacyActivity.class);
                startActivity(intent);
            }
        });


        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.obj != null) {
                    String result = (String) msg.obj;
                    //약국이면
                    setJsonToData(result, "약국");
                    progressDialog.dismiss();
                }
            }
        };

        handler2 = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.obj != null) {
                    String result = (String) msg.obj;
                    //병원이면
                    setJsonToData(result, "병원");
                    progressDialog.dismiss();
                }
            }
        };


        progressDialog.show();
        String query = "page=1" + "&perPage=300" + "&returnType=" + NetworkThread.API_TYPE + "&serviceKey=" + NetworkThread.API_KEY1;
        String query2 = "page=1" + "&perPage=200" + "&returnType=" + NetworkThread.API_TYPE + "&serviceKey=" + NetworkThread.API_KEY2;
        NetworkThread thread = new NetworkThread(handler, query, "약국");
        NetworkThread thread2 = new NetworkThread(handler2, query2, "병원");

        thread.start();
        thread2.start();
    }

    private void setJsonToData(String result, String type) {
        try {
            JSONObject jsonObj = new JSONObject(result);
            JSONArray jsonArray = jsonObj.getJSONArray("data");
            Log.e("res", "" + result);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);
                if (type.equals("약국")) {
                    helper.insert(obj.getString("약국구분"), obj.getString("약국명칭"), obj.getString("약국소재지(도로명)"), obj.getString("약국전화번호"));
                }

                if (type.equals("병원")) {
                    helper.insert2(obj.getInt("순번"), obj.getString("의료기관명"), obj.getString("의료기관주소(도로명)"), obj.getString("의료기관전화번호"));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}