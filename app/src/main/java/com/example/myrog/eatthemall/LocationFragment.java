package com.example.myrog.eatthemall;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.myrog.eatthemall.ViewHolder.PlaceAutoCompleteAdapter;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;


public class LocationFragment extends Fragment implements
        OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        GoogleMap.OnMarkerClickListener,
        LocationListener {

    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private LocationRequest mLocationRequest;
    private Marker currentLocationMarker;
    private Bitmap smallMarker;
    public static final String MAP_FRAGMENT_TAG = "mapFragment";

    private ImageView btnLocation;
    private AutoCompleteTextView mAutocompleteTextView;
    private ImageView mPlaceClear;
    private PlaceAutoCompleteAdapter mPlaceAdapter;
    private static final LatLngBounds BOUNDS = new LatLngBounds(
            new LatLng(8.407168163601074, 104.1448974609375),
            new LatLng(23.170663827102235, 108.0780029296875));
    private static final String LOG_TAG = "LocationFragment";
    private static final int GOOGLE_API_CLIENT_ID = 0;

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    public static final long INTERVAL_TIME_MILISECOND=1000;

    public LocationFragment() {
        // Required empty public constructor
    }

    public static LocationFragment newInstance() {
        LocationFragment fragment = new LocationFragment();
        return fragment;
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_location, container, false);
        FragmentManager fm = getChildFragmentManager();
        SupportMapFragment mapFragment = (SupportMapFragment) fm.findFragmentByTag(MAP_FRAGMENT_TAG);
        if (mapFragment == null) {
            mapFragment = new SupportMapFragment();
            FragmentTransaction ft = fm.beginTransaction();
            ft.add(R.id.mapFragmentContainer, mapFragment, MAP_FRAGMENT_TAG);
            ft.commit();
            fm.executePendingTransactions();
        }
        mapFragment.getMapAsync(this);


        makeSmallMarker();
        connectAPIClient();
        initLocationButton(view);
        initSearchView(view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        mPlaceAdapter.setGoogleApiClient(mGoogleApiClient);
    }

    private void connectAPIClient() {
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .addApi(Places.GEO_DATA_API)
                    .addApi(Places.PLACE_DETECTION_API)
                    .enableAutoManage(getActivity(), GOOGLE_API_CLIENT_ID, this)
                    .build();
            mGoogleApiClient.connect();
        }
    }

    private void initLocationButton(View view) {
        btnLocation = (ImageView) view.findViewById(R.id.btnLocation);
        btnLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveToCurrentLocation();
                if (mLastLocation != null)
                    setEmployeeMarker(new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude()));
            }
        });
    }

    private void initSearchView(View view) {
        AutocompleteFilter typeFilter = new AutocompleteFilter.Builder()
                .setCountry("VN")
                .build();

        mAutocompleteTextView = (AutoCompleteTextView) view.findViewById(R.id.editPlaceSearch);
        mAutocompleteTextView.setThreshold(3);
        mAutocompleteTextView.setOnItemClickListener(mAutocompleteClickListener);

        mPlaceAdapter = new PlaceAutoCompleteAdapter(getActivity(), android.R.layout.simple_list_item_1,
                BOUNDS, typeFilter);
        mAutocompleteTextView.setAdapter(mPlaceAdapter);

        mPlaceClear = (ImageView) view.findViewById(R.id.imgPlaceClear);
        mPlaceClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAutocompleteTextView.setText("");
            }
        });
    }

    private AdapterView.OnItemClickListener mAutocompleteClickListener
            = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            final PlaceAutoCompleteAdapter.PlaceAutocomplete item = mPlaceAdapter.getItem(position);
            final String placeId = String.valueOf(item.placeId);
            Log.i(LOG_TAG, "Selected: " + item.description);
            PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                    .getPlaceById(mGoogleApiClient, placeId);
            placeResult.setResultCallback(mUpdatePlaceDetailsCallback);
            Log.i(LOG_TAG, "Fetching details for ID: " + item.placeId);
        }
    };

    private ResultCallback<PlaceBuffer> mUpdatePlaceDetailsCallback
            = new ResultCallback<PlaceBuffer>() {
        @Override
        public void onResult(PlaceBuffer places) {
            if (!places.getStatus().isSuccess()) {
                Log.e(LOG_TAG, "Place query did not complete. Error: " +
                        places.getStatus().toString());
                return;
            }
            // Selecting the first object buffer.
            final Place place = places.get(0);
            CharSequence attributions = places.getAttributions();


        }
    };

    public void makeSmallMarker() {
        BitmapDrawable bitmapDrawable = (BitmapDrawable) getResources()
                .getDrawable(R.drawable.marker_my_location);
        Bitmap bitmap = bitmapDrawable.getBitmap();
        smallMarker = Bitmap.createScaledBitmap(bitmap, 120, 120, false);

    }
    public Bitmap makeSmallMarker(int markerId) {
        BitmapDrawable bitmapDrawable = (BitmapDrawable) getResources()
                .getDrawable(markerId);
        Bitmap bitmap = bitmapDrawable.getBitmap();
        return Bitmap.createScaledBitmap(bitmap, 120, 120, false);

    }

    private void setUpMap() {
        if (ActivityCompat.checkSelfPermission(getContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]
                    {android.Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
            return;
        }
        mMap.setMyLocationEnabled(false);
        mMap.getUiSettings().setMyLocationButtonEnabled(false);
        mMap.setOnMarkerClickListener(this);

        LocationAvailability locationAvailability =
                LocationServices.FusedLocationApi.getLocationAvailability(mGoogleApiClient);
        if (null != locationAvailability && locationAvailability.isLocationAvailable()) {
            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            if (mLastLocation != null) {
                LatLng currentLocation = new LatLng(mLastLocation.getLatitude(), mLastLocation
                        .getLongitude());
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 12));

                setCurentLocationMarker(mLastLocation);
            }
        }
    }

    private void moveToCurrentLocation() {
        if (mLastLocation != null) {
            LatLng currentLocation = new LatLng(mLastLocation.getLatitude(), mLastLocation
                    .getLongitude());
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 12));
        }
    }

    private void setCurentLocationMarker(Location location) {

        currentLocationMarker = mMap.addMarker(new MarkerOptions()
                .flat(true)
                .icon(BitmapDescriptorFactory
                        .fromBitmap(smallMarker))
                .anchor(0.5f, 0.5f)
                .position(
                        new LatLng(location.getLatitude(), location
                                .getLongitude())));
    }

    protected void setEmployeeMarker(LatLng latLng) {
        mMap.addMarker(new MarkerOptions()
                .position(latLng)
                .title("Title")
                .snippet("5")
                .icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource
                        (getResources(), R.drawable.marker_employee))));

//        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
//            @Override
//            public View getInfoWindow(Marker marker) {
//                return null;
//            }
//
//            @Override
//            public View getInfoContents(Marker marker) {
//                View view = null;
//                try {
//                    view = getActivity().getLayoutInflater().inflate(R.layout.infowindow_employee, null);
//
//
//                    TextView txtName = (TextView) view.findViewById(R.id.tvEmployee);
//                    txtName.setText(marker.getTitle());
//                    RatingBar ratingBar = (RatingBar) view.findViewById(R.id.rbEmployee);
//                    try {
//                        ratingBar.setRating(Float.parseFloat(marker.getSnippet()));
//                    } catch (Exception e) {
//                    }
//
//                } catch (Exception ev) {
//                    System.out.print(ev.getMessage());
//                }
//                return view;
//            }
//        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
    }

    @Override
    public void onLocationChanged(Location location) {
        mLastLocation = new Location(location);
        if (currentLocationMarker != null) {
            currentLocationMarker.remove();
        }

        setCurentLocationMarker(location);
    }



    @Override
    public void onConnected(@Nullable Bundle bundle) {
        setUpMap();
        //update current location
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(INTERVAL_TIME_MILISECOND * 5);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        if (ContextCompat.checkSelfPermission(getContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest,
                    this);
        }

        mPlaceAdapter.setGoogleApiClient(mGoogleApiClient);
        Log.i(LOG_TAG, "Google Places API connected.");
    }

    @Override
    public void onConnectionSuspended(int i) {
        mPlaceAdapter.setGoogleApiClient(null);
        Log.e(LOG_TAG, "Google Places API connection suspended.");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.e(LOG_TAG, "Google Places API connection failed with error code: "
                + connectionResult.getErrorCode());

        Toast.makeText(getActivity(),
                "Google Places API connection failed with error code:" +
                        connectionResult.getErrorCode(),
                Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }


}
