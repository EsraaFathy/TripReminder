package com.example.tripreminder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.example.tripreminder.RoomDataBase.TripTable;
import com.example.tripreminder.RoomDataBase.TripViewModel;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.DirectionsApi;
import com.google.maps.DirectionsApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.model.DirectionsLeg;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.DirectionsRoute;
import com.google.maps.model.DirectionsStep;
import com.google.maps.model.EncodedPolyline;
//import com.mad41.tripreminder.R;
//import com.mad41.tripreminder.constants.Constants;
//import com.mad41.tripreminder.room_database.trip.Trip;
//import com.mad41.tripreminder.room_database.view_model.TripViewModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class Map extends AppCompatActivity implements OnMapReadyCallback {
    private GoogleMap map;
    private MarkerOptions place1,place2;
    private String TAG = "so47492459";
    private List<TripTable> trips;
    private TripViewModel tripViewModel;
    private String address1;
    private String address2;
    private Geocoder geocoder;
    private double latitude;
    private double longitude;
    private List<Address> addresses;
    private List<MarkerOptions> starts;
    private List<MarkerOptions> ends;
    private int count;



    Handler handler=new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            // Obtain the SupportMapFragment and get notified when the map is ready to be used.
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map);
            mapFragment.getMapAsync(Map.this::onMapReady);

            for(TripTable trip:trips){

                address1 = trip.getFrom();
                address2 = trip.getTo();


                latitude = trip.getLatStart();
                longitude = trip.getLongStart();
                place1 = new MarkerOptions().position(new LatLng(latitude,longitude)).title(address1);

                latitude = trip.getLatEnd();
                longitude = trip.getLongEnd();
                place2 = new MarkerOptions().position(new LatLng(latitude,longitude)).title(address2);


                starts.add(place1);
                ends.add(place2);

                count++;

            }
            return false;
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        tripViewModel = new ViewModelProvider(Map.this, ViewModelProvider.AndroidViewModelFactory.getInstance(Map.this.getApplication())).get(TripViewModel.class);
        getHistory();
        geocoder = new Geocoder(this);
        starts = new LinkedList<>();
        ends = new LinkedList<>();
        count = 0;

    }

    private void getHistory(){
        tripViewModel.getHistoryDone("Done").observe(Map.this, new Observer<List<TripTable>>() {
            @Override
            public void onChanged(List<TripTable> tripTables) {
                trips=tripTables;
                Log.d("TAG", "onChanged: ");
                handler.sendEmptyMessage(0);
            }
        });
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        map=googleMap;
        int i=0;
        if(count!=0){
            for(MarkerOptions marker:starts){
                drawOnMap(marker,ends.get(i));
                i++;
            }
        }
    }
    private void drawOnMap(MarkerOptions place1,MarkerOptions place2){
        map.addMarker(place1);
        map.addMarker(place2);

        //Define list to get all latlng for the route
        List<LatLng> path = new ArrayList();

        //Execute Directions API request
        GeoApiContext context = new GeoApiContext.Builder().apiKey("AIzaSyA7dH75J8SZ0-GkeHqHANbflPhdpbfU5yI").build();
        DirectionsApiRequest req = DirectionsApi.getDirections(context, ""+place1.getPosition().latitude+","+place1.getPosition().longitude, ""+place2.getPosition().latitude+","+place2.getPosition().longitude);

        try {
            DirectionsResult res = req.await();

            //Loop through legs and steps to get encoded polylines of each step
            if (res.routes != null && res.routes.length > 0) {
                DirectionsRoute route = res.routes[0];

                if (route.legs !=null) {
                    for(int i=0; i<route.legs.length; i++) {
                        DirectionsLeg leg = route.legs[i];
                        if (leg.steps != null) {
                            for (int j=0; j<leg.steps.length;j++){
                                DirectionsStep step = leg.steps[j];
                                if (step.steps != null && step.steps.length >0) {
                                    for (int k=0; k<step.steps.length;k++){
                                        DirectionsStep step1 = step.steps[k];
                                        EncodedPolyline points1 = step1.polyline;
                                        if (points1 != null) {
                                            //Decode polyline and add points to list of route coordinates
                                            List<com.google.maps.model.LatLng> coords1 = points1.decodePath();
                                            for (com.google.maps.model.LatLng coord1 : coords1) {
                                                path.add(new LatLng(coord1.lat, coord1.lng));
                                            }
                                        }
                                    }
                                } else {
                                    EncodedPolyline points = step.polyline;
                                    if (points != null) {
                                        //Decode polyline and add points to list of route coordinates
                                        List<com.google.maps.model.LatLng> coords = points.decodePath();
                                        for (com.google.maps.model.LatLng coord : coords) {
                                            path.add(new LatLng(coord.lat, coord.lng));
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch(Exception ex) {
//            Log.e(TAG, ex.getLocalizedMessage());
        }

        //Draw the polyline
        if (path.size() > 0) {
            Random rnd = new Random();
            int randomColor = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
            Log.e("COLOR",""+randomColor);
            PolylineOptions opts = new PolylineOptions().addAll(path).color(randomColor).width(5);
            Log.i("maps ",opts.toString());
            map.addPolyline(opts);
        }
        map.getUiSettings().setZoomControlsEnabled(true);
    }
}
