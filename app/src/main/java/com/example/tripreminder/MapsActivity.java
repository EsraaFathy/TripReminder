package com.example.tripreminder;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.example.tripreminder.RoomDataBase.TripTable;
import com.example.tripreminder.RoomDataBase.TripViewModel;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.List;
import java.util.Random;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleMap.OnPolylineClickListener,
        GoogleMap.OnPolygonClickListener {
    TripViewModel tripViewModel;
    private GoogleMap mMap;
    List<Polyline> polylines;
    List<TripTable> trips;
    Handler handler=new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            // Obtain the SupportMapFragment and get notified when the map is ready to be used.
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map);
            mapFragment.getMapAsync(MapsActivity.this::onMapReady);
            return false;
        }
    });
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        tripViewModel = new ViewModelProvider(MapsActivity.this, ViewModelProvider.AndroidViewModelFactory.getInstance(MapsActivity.this.getApplication())).get(TripViewModel.class);
        getHistory();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setZoomControlsEnabled(true);
        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(30.596427, 32.271449);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Egypt"));
        preparePolyLines();
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 10));


        //TODO
        googleMap.setOnPolylineClickListener(this);
        googleMap.setOnPolygonClickListener(this);
    }

    @Override
    public void onPolygonClick(Polygon polygon) {

    }

    @Override
    public void onPolylineClick(Polyline polyline) {

    }

    private void getHistory(){
            tripViewModel.getHistoryDone("Done").observe(MapsActivity.this, new Observer<List<TripTable>>() {
                @Override
                public void onChanged(List<TripTable> tripTables) {
                    trips=tripTables;
                    Log.d("TAG", "onChanged: ");
                    handler.sendEmptyMessage(0);
                }
            });
    }

    private void preparePolyLines(){
        int[] colors=
                {Color.RED,
                Color.GREEN,
                Color.BLUE,
                Color.YELLOW,
                Color.WHITE,
                Color.BLACK,
                Color.GRAY,
                Color.CYAN,
                Color.LTGRAY,
                Color.MAGENTA};
        for (TripTable table : trips){
            Random rand = new Random(); //instance of random class
            int int_random = rand.nextInt(9);

            //marker to the start of the trip
            mMap.addMarker(new MarkerOptions().position(new LatLng(table.getLatStart(),table.getLongStart())).title("Start trip "+table.getTitle()));
            mMap.addPolyline(new PolylineOptions()
                    .clickable(true)
                    .add(new LatLng(table.getLatStart(),table.getLongStart()),
                            new LatLng(table.getLatEnd(),table.getLongEnd())).color(colors[int_random]));

            // marker to the end of the trip
            mMap.addMarker(new MarkerOptions().position(new LatLng(table.getLatEnd(),table.getLongEnd())).title("End trip "+table.getTitle()));
        }
    }
}