package com.example.thanhtin.ungdungtimdobimat.fragments;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.thanhtin.ungdungtimdobimat.Class.News;
import com.example.thanhtin.ungdungtimdobimat.InfoUser;
import com.example.thanhtin.ungdungtimdobimat.R;
import com.example.thanhtin.ungdungtimdobimat.adapter.ViewPostData;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;



import java.util.List;
import java.util.Locale;

public class Map extends AppCompatActivity implements OnMapReadyCallback {

    private LinearLayout linearLayout;
    private ImageView back , btnSearch;
    private MapView mapView;
    private RadioGroup rdoGroup;
    private RadioButton rdoDefault, rdoMyPositon;
    private Button button;
    private EditText editText;


    private static final int REQUEST_LOCATION_PERMISSION_1 = 2001;
    private static final int REQUEST_LOCATION_PERMISSION_2 = 2002;

    private GoogleMap myMap;
    private Marker maker;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private Double x = 0.0 , y = 0.0, a, b, m, n;
    private String myAddress;
    private String result = "";
    private CompoundButton.OnCheckedChangeListener listenerRadio = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if(isChecked)
                result = buttonView.getText().toString();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);



        init();
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        setMyMap();
        loadHere();
        setSearch();
        setControl();
    }

    private void setSearch() {
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editText.getText().toString().matches("")){
                    Toast.makeText(Map.this, "Không tìm thấy!", Toast.LENGTH_LONG).show();
                }else {
                    try {
                        Geocoder geocoder = new Geocoder(Map.this);
                        List<Address> addresses;
                        addresses = geocoder.getFromLocationName(editText.getText().toString(), 1);
                        if(addresses.size() > 0) {
                            a = (Double)addresses.get(0).getLatitude();
                            b = (Double)addresses.get(0).getLongitude();
                            getMyAddress(a,b);
                            rdoMyPositon.setText(myAddress);
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
                        Toast.makeText(Map.this, "Tìm không ra!", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

    }

    private void setControl() {
        rdoDefault.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(rdoDefault.getText().toString().matches("Ðang lấy vị trí...")){
                    result = "";
                }else {
                    m = x;
                    n = y;
                    rdoMyPositon.setChecked(false);
                }
            }
        });
        rdoMyPositon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                m = a; n = b;
                rdoDefault.setChecked(false);
            }
        });
        rdoDefault.setOnCheckedChangeListener(listenerRadio);
        rdoMyPositon.setOnCheckedChangeListener(listenerRadio);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(rdoDefault.isChecked() == false && rdoMyPositon.isChecked() == false&& result.matches(""))
                    Toast.makeText(Map.this, "Chưa chọn vị trí", Toast.LENGTH_SHORT).show();
                else
                if(result.matches("Chưa chọn vị trí của bạn"))
                    Toast.makeText(Map.this, "Di chuyển cờ tới vị trí chính xác của bạn", Toast.LENGTH_SHORT).show();
                else {
                    Toast.makeText(Map.this, "Ðã chọn địa chỉ: " + result, Toast.LENGTH_SHORT).show();
                    if(getIntent().getStringExtra("Map")!=null)
                    {
                        Intent sendIntent = new Intent(Map.this, PostNew.class);
                        sendIntent.putExtra("address", result);
                        sendIntent.putExtra("lat", m);
                        sendIntent.putExtra("lng", n);
                        startActivity(sendIntent);
                    }
                    else if(getIntent().getStringExtra("uMap")!=null)
                    {
                        News newss= (News) getIntent().getSerializableExtra("sendMap");

                        Intent sendIntent = new Intent(Map.this, UpdateNewsActivity.class);
                        sendIntent.putExtra("address", result);
                        sendIntent.putExtra("lat", m);
                        sendIntent.putExtra("lng", n);
                        sendIntent.putExtra("pass",newss);
                        startActivity(sendIntent);
                    }
                    else {
                        Intent sendIntent = new Intent(Map.this, InfoUser.class);
                        sendIntent.putExtra("address", result);
                        sendIntent.putExtra("lat", m);
                        sendIntent.putExtra("lng", n);
                        startActivity(sendIntent);
                    }
                    finish();
                }
            }
        });
    }

    private void setMyMap() {
        mapView = findViewById(R.id.map_seller);

        if (mapView != null) {
            mapView.onCreate(null);
            mapView.onResume();
            mapView.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        myMap = googleMap;

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
        myMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        myMap.setMyLocationEnabled(true);
        myMap.getUiSettings().setMyLocationButtonEnabled(true);
        myMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(Marker marker) {
                Toast.makeText(Map.this, "Di chuyển cờ tới vị trí chính xác của bạn!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onMarkerDrag(Marker marker) {

            }

            @Override
            public void onMarkerDragEnd(Marker marker) {
                rdoMyPositon.setChecked(false);
                a = marker.getPosition().latitude;
                b = marker.getPosition().longitude;
                if (maker == null){
                    getMyAddress(a,b);
                    maker = myMap.addMarker(new MarkerOptions()
                            .position(new LatLng(a, b))
                            .title("Ví trí của bạn").snippet(myAddress)
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET)).draggable(true));
                    rdoMyPositon.setText(myAddress);
                }else {
                    marker.remove();
                    getMyAddress(a,b);
                    maker = myMap.addMarker(new MarkerOptions()
                            .position(new LatLng(a, b))
                            .title("Ví trí của bạn").snippet(myAddress)
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET)).draggable(true));
                    rdoMyPositon.setText(myAddress);
                }
            }
        });
        myMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                Double a,b;
                a = marker.getPosition().latitude;
                b = marker.getPosition().longitude;
                myMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(a, b), 17.0f));
                return false;
            }
        });
    }


    private void loadHere() {
        Toast.makeText(this, "Ðang lấy vị trí...", Toast.LENGTH_LONG).show();
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
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                x = location.getLatitude();
                y = location.getLongitude();
                if (maker == null){
                    getMyAddress(x,y);
                    maker = myMap.addMarker(new MarkerOptions()
                            .position(new LatLng(x, y))
                            .title("Vị trí của bạn").snippet(myAddress)
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET)).draggable(true));
                    rdoDefault.setText(myAddress);
                    myMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(x, y), 16.0f));
                }

            }
            @Override
            public void onProviderDisabled(String provider) {
                Log.d("Latitude","disable");
            }

            @Override
            public void onProviderEnabled(String provider) {
                Log.d("Latitude","enable");
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
                Log.d("Latitude","status");
            }
        };
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
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
    private void init() {
        back = findViewById(R.id.update_seller_btn_back_map);
        button = findViewById(R.id.btn_choose_postion);
        rdoGroup = findViewById(R.id.rdo_group);
        rdoDefault = findViewById(R.id.rdo_default);
        rdoMyPositon = findViewById(R.id.rdo_myoption);
        editText = findViewById(R.id.input_search);
        btnSearch = findViewById(R.id.btn_search_position);
    }

    private int getStatusBarHeight() {
        int h;
        Resources myResources = getResources();
        int idh = myResources.getIdentifier("status_bar_height", "dimen", "android");
        if(idh > 0){
            h = getResources().getDimensionPixelSize(idh);
        }
        else {
            h = 0;
        }
        return h;
    }
}