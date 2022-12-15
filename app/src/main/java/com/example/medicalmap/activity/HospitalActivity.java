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
import com.example.medicalmap.assist.Item2;
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

public class HospitalActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {
    private GoogleMap mMap2;
    Geocoder geocoder;

    Helper helper;

    List<Address> list2;
    double lat, lon = 0.0;
    Item2 item2;

    LinearLayout layout2;

    LatLng firstLatLng;

    TextView title;

    String dong;

    Button nextBtn;
    int start = 0;
    int end = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hospital);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map2);
        layout2 = (LinearLayout) findViewById(R.id.map_layout2);
        title = (TextView) findViewById(R.id.map_text2);
        nextBtn = (Button) findViewById(R.id.next);
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start += end;
                ArrayList<Item2> itemList2 = helper.selectAll2(start, end);
                try {

                    for (int i = 0; i < itemList2.size(); i++) {
                        list2 = geocoder.getFromLocationName(itemList2.get(i).getAddress(), 10);
                        if (list2 != null) {
                            if (list2.size() == 0) {
                                Log.e("에러!", "올바른 주소 아님 ");
                            } else {
                                Address address = list2.get(0);

                                LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());

                                MarkerOptions markerOptions = new MarkerOptions();

                                markerOptions.position(latLng);
                                markerOptions.title("" + itemList2.get(i).getName());
                                markerOptions.snippet("" + itemList2.get(i).getAddress() + " / " + itemList2.get(i).getTel());
                                mMap2.addMarker(markerOptions);

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


    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {

        mMap2 = googleMap;

        ArrayList<Item2> itemList2 = helper.selectAll2(start, end);
        Log.e("dong", "end");
        try {

            for (int i = 0; i < itemList2.size(); i++) {
                list2 = geocoder.getFromLocationName(itemList2.get(i).getAddress(), 10);
                if (list2 != null) {
                    if (list2.size() == 0) {
                        Log.e("에러!", "올바른 주소 아님 ");
                    } else {
                        Address address = list2.get(0);

                        LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());

                        MarkerOptions markerOptions = new MarkerOptions();

                        markerOptions.position(latLng);
                        markerOptions.title("" + itemList2.get(i).getName());
                        markerOptions.snippet("" + itemList2.get(i).getAddress() + " / " + itemList2.get(i).getTel());
                        mMap2.addMarker(markerOptions);

                        if (i == 0) {
                            firstLatLng = latLng;
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        mMap2.moveCamera(CameraUpdateFactory.newLatLngZoom(firstLatLng, 10));


        mMap2.setOnMarkerClickListener(this);
        mMap2.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(@NonNull LatLng latLng) {
                layout2.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public boolean onMarkerClick(@NonNull Marker marker) {
        layout2.setVisibility(View.VISIBLE);
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

    LocationListener listener2 = new LocationListener() {
        @Override
        public void onLocationChanged(@NonNull Location location2) {
            String provider2 = location2.getProvider();
            double longitude2 = location2.getLongitude();
            double latitude2 = location2.getLatitude();
            double altitude2 = location2.getAltitude();
        }
    };
}

