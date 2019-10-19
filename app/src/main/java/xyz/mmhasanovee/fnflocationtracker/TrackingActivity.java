package xyz.mmhasanovee.fnflocationtracker;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import xyz.mmhasanovee.fnflocationtracker.Model.MyLocation;
import xyz.mmhasanovee.fnflocationtracker.Utils.Commonx;

public class TrackingActivity extends FragmentActivity implements OnMapReadyCallback, ValueEventListener {

    private GoogleMap mMap;
    float hue = 337;

    DatabaseReference trackingUserLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracking);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        
        registerEventRealTime();
    }

    private void registerEventRealTime() {

        trackingUserLocation = FirebaseDatabase.getInstance()
                .getReference(Commonx.PUBLIC_LOCATION)
                .child(Commonx.trackingUser.getUid());

        trackingUserLocation.addValueEventListener(this);


    }

    @Override
    protected void onResume() {
        super.onResume();
        trackingUserLocation.addValueEventListener(this);
    }

    @Override
    protected void onStop() {

        trackingUserLocation.removeEventListener(this);
        super.onStop();
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // enable zoom ui

        mMap.getUiSettings().setZoomControlsEnabled(true);

        //set skin

        boolean success = googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this,R.raw.my_uber_style));

    }


    @Override
    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


        if(dataSnapshot.getValue()!=null){

            mMap.clear();

            MyLocation location = dataSnapshot.getValue(MyLocation.class);

            //adding marker


            LatLng userMarker = new LatLng(location.getLatitude(),location.getLongitude());


            mMap.addMarker(new MarkerOptions().position(userMarker)
            .title(Commonx.trackingUser.getEmail())
                    .icon(BitmapDescriptorFactory
                            .defaultMarker(hue))
            .snippet(Commonx.getDateFormatted(Commonx.convertTimeStampToDate(location.getTime())))).showInfoWindow();



            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(userMarker,16F));





        }

    }

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {

    }
}
