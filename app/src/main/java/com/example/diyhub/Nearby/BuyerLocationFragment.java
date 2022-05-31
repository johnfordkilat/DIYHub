package com.example.diyhub.Nearby;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.diyhub.Fragments.ShopsList;
import com.example.diyhub.MESSAGES.User;
import com.example.diyhub.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.net.DatagramSocket;
import java.util.HashMap;

public class BuyerLocationFragment extends Fragment implements OnMapReadyCallback {

    //google map object
    private GoogleMap mMap;
    SupportMapFragment mapFragment;
    //current and destination location objects
    Location myLocation = null;
    Circle circle;
    Marker marker,startMarker;
    protected LatLng start = null;
    protected LatLng end = null;
    protected LatLng latLng =  null;
    CameraUpdate cameraUpdate;
    Uri avatarMarkers;
    //to get location permissions.
    boolean locationPermission = false;

    FusedLocationProviderClient fusedLocationProviderClient;
    String lawyerUID;
    ImageView backBtn;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();


    //For connections
    DatagramSocket connection = null;

    //Markers
    Bitmap bitMapImageMarker = null, pinMarker = null;
    BitmapDrawable bitmapdraw = null;
    MarkerOptions markerOptions = new MarkerOptions();
    MarkerOptions lawyerMarkerOption = new MarkerOptions();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_buyer_location, null, false);

        requestPermission();


        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getContext());
        return view;
    }
    private void requestPermission() {
        if (ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
        } else {
            locationPermission = true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //if permission granted.
                    locationPermission = true;
                    getMyLocation();

                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
        }
    }

    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        getMyLocation();
    }

    private void getMyLocation() {
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);

        //get avatar from the firebase

        fusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(@NonNull Location location) {
                if (location != null){

                    latLng = new LatLng(location.getLatitude(),location.getLongitude());
                    markerOptions.position(latLng);
                    markerOptions.title("You");

                    DatabaseReference databaseUserProfileReference = FirebaseDatabase.getInstance().getReference().child("Users").child(mAuth.getCurrentUser().getUid());
                    databaseUserProfileReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            User user = snapshot.getValue(User.class);
                            Uri myMarker;
                            if(user.getImageUrl().equals("")) {
                                myMarker = Uri.parse(UserImageProfile.userMarkerAvatar);
                            }
                            else {
                                myMarker = Uri.parse(user.getImageUrl());
                            }
                            if(getContext()!=null) {
                                Glide.with(getContext()).asBitmap().load(myMarker).diskCacheStrategy(DiskCacheStrategy.DATA).override(64, 64).circleCrop().into(new CustomTarget<Bitmap>() {
                                    @Override
                                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(resource));
                                        markerOptions.rotation(location.getBearing());
                                        markerOptions.anchor((float) 0.5, (float) 0.5);
                                        startMarker = mMap.addMarker(markerOptions);
                                        startMarker.showInfoWindow();
                                    }

                                    @Override
                                    public void onLoadCleared(@Nullable Drawable placeholder) {

                                    }
                                });
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                    if (!isNetworkConnected()) {

                    }
                    else {
                        if (circle != null) {
                            circle.remove();
                        }
                        if (startMarker != null) {
                            startMarker.remove();
                        }

                        start = new LatLng(location.getLatitude(),location.getLongitude());


                        CircleOptions circleOptions = new CircleOptions();
                        circleOptions.center(start);
                        circleOptions.radius(2000); // 2km
                        circleOptions.strokeWidth(3);
                        circleOptions.strokeColor(Color.RED);
                        circle = mMap.addCircle(circleOptions);

                        FirebaseDatabase.getInstance().getReference().child("Shops").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for(DataSnapshot dsp : snapshot.getChildren()) {
                                    ShopsList lawFirm = dsp.getValue(ShopsList.class);
                                    DatabaseReference userLocationDR = FirebaseDatabase.getInstance().getReference().child("UserLocation").child(mAuth.getCurrentUser().getUid());
                                    userLocationDR.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            UserLocation userLocation;
                                            userLocation = snapshot.getValue(UserLocation.class);
                                            if(Double.parseDouble(userLocation.getLatitude()) != 0.0 && Double.parseDouble(userLocation.getLongitude()) != 0.0) {
                                                end = new LatLng(Double.parseDouble(lawFirm.getLatitude()), Double.parseDouble(lawFirm.getLongitude()));
                                                String distance = showResult(start, end).toString();
                                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                                DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference();
                                                HashMap<String, Object> hashMap1 = new HashMap<>();
                                                hashMap1.put("distance", distance);
                                                reference1.child("Shops").child(lawFirm.getShopName()).updateChildren(hashMap1);

                                                DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
                                                HashMap<String, Object> hashMap = new HashMap<>();
                                                hashMap.put("distance", distance);
                                                hashMap.put("name", lawFirm.getSellerID());
                                                hashMap.put("shopImage", lawFirm.getShopImage());
                                                reference.child("Distance").child(user.getUid()).child(lawFirm.getSellerID()).updateChildren(hashMap);
                                                reference.child("Distance").child(user.getUid()).child(lawFirm.getSellerID()).addValueEventListener(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                        DistanceList distanceModel = snapshot.getValue(DistanceList.class);
                                                        if(marker != null) {
                                                            marker.remove();
                                                        }
                                                        if(lawFirm.getShopImage().equals("")) {
                                                            avatarMarkers = Uri.parse(UserImageProfile.userMarkerAvatar);
                                                        }
                                                        else {
                                                            avatarMarkers = Uri.parse(lawFirm.getShopImage());
                                                        }
                                                        if(getContext()!=null) {
                                                            Glide.with(getContext()).asBitmap().load(avatarMarkers).diskCacheStrategy(DiskCacheStrategy.DATA).override(64, 64).circleCrop().into(new CustomTarget<Bitmap>() {
                                                                @Override
                                                                public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                                                    end = new LatLng(Double.parseDouble(lawFirm.getLatitude()), Double.parseDouble(lawFirm.getLongitude()));
                                                                    lawyerMarkerOption.position(end);
                                                                    lawyerMarkerOption.icon(BitmapDescriptorFactory.fromBitmap(resource));
                                                                    lawyerMarkerOption.rotation(location.getBearing());
                                                                    lawyerMarkerOption.title(lawFirm.getShopName() + "\n" + distanceModel.getDistance() + "km");
                                                                    lawyerMarkerOption.anchor((float) 0.5, (float) 0.5);
                                                                    marker = mMap.addMarker(lawyerMarkerOption);
                                                                    marker.setTag(lawFirm.getSellerID());
                                                                    marker.showInfoWindow();
                                                                }
                                                                @Override
                                                                public void onLoadCleared(@Nullable Drawable placeholder) {

                                                                }
                                                            });
                                                        }
                                                        else {
                                                            return;
                                                        }
                                                    }
                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError error) {

                                                    }
                                                });
                                            }
                                        }
                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });
                                }
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                        /*
                        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                            @SuppressLint("PotentialBehaviorOverride")
                            @Override
                            public boolean onMarkerClick(@NonNull Marker marker) {
                                if(marker.getTag()!=null) {
                                    Intent lawFirmIntent = new Intent(getContext(), Shop_Map_Details.class);
                                    lawFirmIntent.putExtra("ShopID", marker.getTag().toString());
                                    startActivity(lawFirmIntent);
                                }
                                return true;
                            }
                        });

                         */
                        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

                            @Override
                            public View getInfoWindow(Marker arg0) {
                                return null;
                            }

                            @Override
                            public View getInfoContents(Marker marker) {

                                LinearLayout info = new LinearLayout(getContext());
                                info.setOrientation(LinearLayout.VERTICAL);

                                TextView title = new TextView(getContext());
                                title.setTextColor(Color.BLACK);
                                title.setTypeface(null, Typeface.BOLD);
                                title.setText(marker.getTitle());

                                TextView snippet = new TextView(getContext());
                                snippet.setTextColor(Color.WHITE);
                                snippet.setWidth(0);
                                snippet.setHeight(0);
                                snippet.setText(marker.getSnippet());

                                info.addView(title);
                                info.addView(snippet);

                                return info;
                            }
                        });
                    }
                    cameraUpdate = CameraUpdateFactory.newLatLngZoom(start, 14f);
                    mMap.animateCamera(cameraUpdate);
                }
            }
        });

        mMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange(Location location) {

            }
        });
    }

    //Haversine Formula
    public double distance(double lat1, double lon1, double lat2, double lon2) {
        // Radius of the earth
        int R = 6371;

        double dLat = Math.toRadians(lat2 - lat1); //distance of the latitude
        double dLon = Math.toRadians(lon2 - lon1); //distance of the longitude

        lat1 = Math.toRadians(lat1); //convert to radians
        lat2 = Math.toRadians(lat2);

        //Math pow + Math pow * Math cos * Math cos
        double a = Math.pow(Math.sin(dLat / 2), 2) + Math.pow(Math.sin(dLon / 2), 2) * Math.cos(lat1) * Math.cos(lat2);

        double c = 2 * Math.asin(Math.sqrt(a));

        return R * c; // return distance
    }

    private SpannableStringBuilder showResult(LatLng point1, LatLng point2) {
        double distance = distance(point1.latitude, point1.longitude, point2.latitude, point2.longitude);

        StyleSpan boldSpan = new StyleSpan(Typeface.BOLD);
        ForegroundColorSpan redSpan = new ForegroundColorSpan(Color.BLACK);
        SpannableStringBuilder ssb = new SpannableStringBuilder();

        int start = 0;
        SpannableString ss = new SpannableString(String.format("%.3f", distance));
        ss.setSpan(boldSpan, start, ss.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(redSpan, start, ss.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        ssb.append(ss);

        return ssb;
    }
    //Connectivity Manager
    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }
}