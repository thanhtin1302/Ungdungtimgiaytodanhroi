package com.example.thanhtin.ungdungtimdobimat.fragments;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.thanhtin.ungdungtimdobimat.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;
import java.util.Locale;

public class MapPost extends AppCompatActivity implements OnMapReadyCallback {
    private MapView mapView;
    private GoogleMap myMap;
    private String myAddress;
    private Double a, b;
    private Marker maker;
    private ImageView imgback;
    private static final int REQUEST_LOCATION_PERMISSION_1 = 1001;
    private static final int REQUEST_LOCATION_PERMISSION_2 = 1002;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_post);
        setMyMap();
        imgback = findViewById(R.id.postupdate_seller_btn_back_map1);
        imgback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void setMyMap() {
        mapView = findViewById(R.id.map_seller1);

        if (mapView != null) {
            mapView.onCreate(null);
            mapView.onResume();
            mapView.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        myMap = googleMap;
        myMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]
                            {Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION_PERMISSION_1);
            ActivityCompat.requestPermissions(this, new String[]
                            {Manifest.permission.ACCESS_COARSE_LOCATION},
                    REQUEST_LOCATION_PERMISSION_2);
            return;
        }
        myMap.setMyLocationEnabled(true);
        myMap.getUiSettings().setMyLocationButtonEnabled(true);
        try {
            String pos = getIntent().getStringExtra("post");
            Geocoder geocoder = new Geocoder(MapPost.this);
            List<Address> addresses;
            addresses = geocoder.getFromLocationName(pos, 1);
            if(addresses.size() > 0) {
                a = (Double)addresses.get(0).getLatitude();
                b = (Double)addresses.get(0).getLongitude();
                getMyAddress(a,b);
                if(maker != null) {
                    maker.remove();
                    maker = myMap.addMarker(new MarkerOptions()
                            .position(new LatLng(a, b))
                            .title("Vị trí của bạn").snippet(myAddress)
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)).draggable(true));
                }else {
                    maker = myMap.addMarker(new MarkerOptions()
                            .position(new LatLng(a, b))
                            .title("Vị trí của bạn").snippet(myAddress)
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)).draggable(true));
                }
                myMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(a, b), 17.0f));
            }
        }catch (Exception ex){
            Log.d("search_address", "Lỗi "+ ex);
            Toast.makeText(MapPost.this, "Tìm không ra!", Toast.LENGTH_LONG).show();
        }
    }
    private void getMyAddress(double a, double b) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(a, b, 1);
            if (addresses != null) {
                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder("");

                for (int i = 0; i <= returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i));
                }
                myAddress = strReturnedAddress.toString();
            } else {
                myAddress = "Trống";
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.w("W", "Không lấy được địa chỉ!!!");
        }
    }
}
