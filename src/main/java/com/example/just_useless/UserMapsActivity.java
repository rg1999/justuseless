package com.example.just_useless;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Looper;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryEventListener;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static com.google.android.gms.location.LocationServices.getFusedLocationProviderClient;

public class UserMapsActivity extends FragmentActivity implements OnMapReadyCallback ,TaskLoadedCallback{

    private GoogleMap mMap;
    private static final int MY_PERMISSION_REQUEST_CODE = 7000;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private LocationRequest mLocationRequest;
    private double x = 1000;
    private double y = 1000;
    private LatLng latLng;

    private DatabaseReference mDatabase;
    private GeoFire geoFire;
    private FirebaseAuth mAuth;
    private String uid;
    private  double radius = 1;
    private Boolean ambulanceFound = false;
    private String[] ambulanceID;
    private String ambulanceuid;
    private double ll;
    private double lll;
    private String[] ph = {"aaa","bbb"};

    Marker mar1;


    Marker[] mar;

    private Polyline currentPolyline;

    SupportMapFragment mapFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mAuth = FirebaseAuth.getInstance();
        uid = mAuth.getCurrentUser().getUid().toString();
        mDatabase = FirebaseDatabase.getInstance().getReference("userlocation");
        geoFire = new GeoFire(mDatabase);

        ambulanceID = new String[3];
        mar = new Marker[3];
    }



    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION
            }, MY_PERMISSION_REQUEST_CODE);
        }
        startLocationUpdates();
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);

    }


    public void onLocationChanged(Location location) {

        x = location.getLatitude();
        y = location.getLongitude();

        latLng = new LatLng(x, y);
        getCloserAmbulance();
        System.out.println(ll+"/"+lll);
        LatLng latLng2 = new LatLng(ll,lll);
//
//        MarkerOptions place1 = new MarkerOptions().position(new LatLng(27.658143, 85.3199503)).title("Location 1");
//        MarkerOptions place2 = new MarkerOptions().position(new LatLng(27.667491, 85.3208583)).title("Location 2");
//

        //new FetchURL(UserMapsActivity.this).execute(getUrl(place1.getPosition(), place2.getPosition(), "driving"), "driving");
        geoFire.setLocation(uid, new GeoLocation(x, y), new GeoFire.CompletionListener() {
            @Override
            public void onComplete(String key, DatabaseError error) {
                System.out.println("GPS working");
            }
        });

    }


    private void startLocationUpdates() {

        mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(5000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setSmallestDisplacement(10);

        // Create LocationSettingsRequest object using location request
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        LocationSettingsRequest locationSettingsRequest = builder.build();

        // Check whether location settings are satisfied
        // https://developers.google.com/android/reference/com/google/android/gms/location/SettingsClient
        SettingsClient settingsClient = LocationServices.getSettingsClient(this);
        settingsClient.checkLocationSettings(locationSettingsRequest);

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        getFusedLocationProviderClient(this).requestLocationUpdates(mLocationRequest, new LocationCallback() {
                    @Override
                    public void onLocationResult(LocationResult locationResult) {
                        onLocationChanged(locationResult.getLastLocation());



                    }
                },
                Looper.myLooper());

    }

    private int count = 0;

    private void getCloserAmbulance(){
        final DatabaseReference ambulancelocation = FirebaseDatabase.getInstance().getReference().child("ambulancelocation");

        GeoFire geoFire1 = new GeoFire(ambulancelocation);

        GeoQuery geoQuery = geoFire1.queryAtLocation(new GeoLocation(latLng.latitude,latLng.longitude),20);

        geoQuery.addGeoQueryEventListener(new GeoQueryEventListener() {
            @Override
            public void onKeyEntered(String key, GeoLocation location) {


                if(!ambulanceFound)
                {



                    ambulanceID[count] = key;

                    for(int i=0;i<3;i++) {

                        if(ambulanceID[i] != null){

                            if (mar[i] != null) {
                                mar[i].remove();
                            }

                            if(mar1!=null){
                                mar1.remove();
                            }

                            mar1 = mar[i];

                            final String head = ambulanceID[i].toString();

                            DatabaseReference ambulanceDatabase = ambulancelocation.child(ambulanceID[i].toString()).child("l");
                            final DatabaseReference mdd = FirebaseDatabase.getInstance().getReference().child("users").child(ambulanceID[i].toString());


                            ambulanceDatabase.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                    String ambulanceLatitude = (String) dataSnapshot.child("0").getValue().toString();
                                    String ambulanceLongitude = (String) dataSnapshot.child("1").getValue().toString();

                                    ll = Double.parseDouble(ambulanceLatitude);
                                    lll = Double.parseDouble(ambulanceLongitude);

                                    final LatLng amblatlang = new LatLng(ll, lll);

                                    System.out.println("sssssss"+ph[0]);

                                    mdd.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot1) {

                                            ph[0] = (String) dataSnapshot1.child("phone").getValue().toString();
                                            ph[1] = (String) dataSnapshot1.child("ambulanceNo").getValue().toString();

                                            System.out.println("sssssss"+ph[0]);
                                            mar1 = mMap.addMarker(new MarkerOptions().position(amblatlang).title(ph[0]+"/"+ph[1]));

                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });

                        }
                    }

                    count = count + 1;
                    if(count==3){
                        ambulanceFound = true;
                    }

                }

            }

            @Override
            public void onKeyExited(String key) {

            }

            @Override
            public void onKeyMoved(String key, GeoLocation location) {

            }

            @Override
            public void onGeoQueryReady() {



            }

            @Override
            public void onGeoQueryError(DatabaseError error) {

            }
        });
    }
    private String getUrl(LatLng origin, LatLng dest, String directionMode) {
        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;
        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
        // Mode
        String mode = "mode=" + directionMode;
        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + mode;
        // Output format
        String output = "json";
        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters + "&key=" + getString(R.string.google_maps_key);
        return url;
    }
    @Override
    public void onTaskDone(Object... values) {
        if (currentPolyline != null)
            currentPolyline.remove();
        currentPolyline = mMap.addPolyline((PolylineOptions) values[0]);
    }
}
