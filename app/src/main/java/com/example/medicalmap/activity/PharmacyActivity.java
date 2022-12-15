package com.example.medicalmap.activity;

import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.medicalmap.assist.Helper;
import com.example.medicalmap.assist.Item;
import com.example.medicalmap.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PharmacyActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {
    private GoogleMap mMap;
    Geocoder geocoder;

    Helper helper;

    List<Address> list;
    double lat, lon = 0.0;
    Item item;

    LinearLayout layout;

    LatLng firstLatLng;

    String userAddr;

    TextView title;

    String dong;

    Button nextBtn;
    int start = 0;
    int end = 10;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pharmacy);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        layout = (LinearLayout) findViewById(R.id.map_layout);
        title = (TextView) findViewById(R.id.map_text);
        nextBtn = (Button) findViewById(R.id.next1);
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start += end;
                ArrayList<Item> itemList = helper.selectAll(start, end);
                try {

                    for (int i=0; i<itemList.size(); i++) {
                        list  = geocoder.getFromLocationName(itemList.get(i).getAddress(),10);
                        if (list != null) {
                            if (list.size() == 0) {
                                Log.e("에러!" , "올바른 주소 아님 ");
                            }
                            else {
                                Address address = list.get(0);

                                LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());

                                MarkerOptions markerOptions = new MarkerOptions();

                                markerOptions.position(latLng);
                                markerOptions.title("" + itemList.get(i).getName());
                                markerOptions.snippet("" + itemList.get(i).getAddress() + " / " + itemList.get(i).getTel());
                                mMap.addMarker(markerOptions);

                                if (i == 0) {
                                    firstLatLng = latLng;
                                }
                            }
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        dong = getIntent().getStringExtra("dong");

        mapFragment.getMapAsync(this);
        geocoder = new Geocoder(this);


        helper = Helper.getInstance(this);
/*
        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "위치권한 설정해주세요.", Toast.LENGTH_SHORT);
            return;
        } else {

        }
        Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        lat = location.getLatitude();
        lon = location.getLongitude();

        Log.e("123123", ""+lat + " / " + lon);
        try {
            List<Address> list = geocoder.getFromLocation(lat, lon, 10);
            String[] tempDong = list.get(0).toString().split(" ");
            for (int i=0; i<tempDong.length; i++) {
                if (tempDong[i].contains("동")) {
                    userAddr = tempDong[i];
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
*/
    }


    @Override
    public void onMapReady(final GoogleMap googleMap) {

        mMap = googleMap;

        ArrayList<Item> itemList = helper.selectAll(start, end);
        Log.e("dong", "end");

        try {
            for (int i=0; i<itemList.size(); i++) {
                list  = geocoder.getFromLocationName(itemList.get(i).getAddress(),10);
                if (list != null) {
                    if (list.size() == 0) {
                        Log.e("에러!" , "올바른 주소 아님 ");
                    }
                    else {
                        Address address = list.get(0);

                        LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());

                        MarkerOptions markerOptions = new MarkerOptions();

                        markerOptions.position(latLng);
                        markerOptions.title("" + itemList.get(i).getName());
                        markerOptions.snippet("" + itemList.get(i).getAddress() + " / " + itemList.get(i).getTel());
                        mMap.addMarker(markerOptions);

                        if (i == 0) {
                            firstLatLng = latLng;
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(firstLatLng, 10));


        mMap.setOnMarkerClickListener(this);
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(@NonNull LatLng latLng) {
                layout.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public boolean onMarkerClick(@NonNull Marker marker) {
        layout.setVisibility(View.VISIBLE);
        layout.setVisibility(View.VISIBLE);
        String snippet = marker.getSnippet();
        String address = snippet.split(" / ")[0];
        String tel = snippet.split(" / ")[1];
        String text = "";
        text += marker.getTitle() + "\n";
        text += address + "\n";
        text += tel;

        title.setText(text);


        return false;
    }

    LocationListener listener = new LocationListener() {
        @Override
        public void onLocationChanged(@NonNull Location location) {
            String provider = location.getProvider();
            double longitude = location.getLongitude();
            double latitude = location.getLatitude();
            double altitude = location.getAltitude();
        }
    };
}