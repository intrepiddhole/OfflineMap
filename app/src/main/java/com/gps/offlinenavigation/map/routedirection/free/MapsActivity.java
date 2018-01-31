package com.gps.offlinenavigation.map.routedirection.free;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.gps.offlinenavigation.map.routedirection.free.POJO.Distance;
import com.gps.offlinenavigation.map.routedirection.free.POJO.Duration;
import com.gps.offlinenavigation.map.routedirection.free.POJO.Example;
import com.gps.offlinenavigation.map.routedirection.free.POJO.Leg;
import com.gps.offlinenavigation.map.routedirection.free.POJO.OverviewPolyline;
import com.gps.offlinenavigation.map.routedirection.free.POJO.Route;
import java.util.ArrayList;
import java.util.List;
import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

public class MapsActivity extends FragmentActivity
        implements LocationListener, OnMapReadyCallback, PlaceSelectionListener, com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks, com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener
{

  private static final float MIN_DISTANCE = 1000F;
  private static final long MIN_TIME = 400L;
  public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
  public static LatLng current_latlng;
  public static LatLng dest;
  public static LatLng destination_latlng;
  public static LatLng latLng;
  public static Double latitude;
  public static Double longitude;
  public static InterstitialAd mInterstitialAd;
  public static LatLng origin;
  ArrayList MarkerPoints;
  TextView ShowDistanceDuration;
  TextView ShowDistanceDuration2;
  ImageButton button_direction;
  CardView cardView;
  Marker current;
  Marker destination;
  Polyline line;
  private LocationManager locationManager;
  Marker mCurrLocationMarker;
  GoogleApiClient mGoogleApiClient;
  LocationRequest mLocationRequest;
  private GoogleMap mMap;
  SupportMapFragment mapFragment;
  ImageButton normal;
  Retrofit retrofit;
  ImageButton satellite;
  CardView search_fragment1;
  RetrofitMaps service;
  ImageButton terrain;
  String url;

  public MapsActivity()
  {
  }

  private void build_retrofit_and_get_response1(String s)
  {
    try
    {
      url = "https://maps.googleapis.com/maps/";
      retrofit = (new retrofit.Retrofit.Builder()).baseUrl(url).addConverterFactory(GsonConverterFactory.create()).build();
      service = (RetrofitMaps)retrofit.create(RetrofitMaps.class);
      service.getDistanceDuration("metric", (new StringBuilder()).append(latLng.latitude).append(",").append(latLng.longitude).toString(), (new StringBuilder()).append(destination_latlng.latitude).append(",").append(destination_latlng.longitude).toString(), s).enqueue(new Callback() {
        @Override
        public void onResponse(Response response) {
          onResponse(response, null);
        }

        public void onFailure(Throwable throwable)
        {
          Log.d("onFailure", throwable.toString());
        }

        public void onResponse(Response response, Retrofit retrofit1)
        {
          if(line != null)
          {
            line.remove();
          }
          int i = 0;
          try {
            while (i < ((Example) response.body()).getRoutes().size()) {
              String retrofit1_1 = ((Leg) ((Route) ((Example) response.body()).getRoutes().get(i)).getLegs().get(i)).getDistance().getText();
              String s1 = ((Leg) ((Route) ((Example) response.body()).getRoutes().get(i)).getLegs().get(i)).getDuration().getText();
              ShowDistanceDuration.setText(String.valueOf((new StringBuilder()).append("Distance:").append(retrofit1_1).toString()));
              ShowDistanceDuration2.setText(String.valueOf((new StringBuilder()).append("Duration:").append(s1).toString()));
              String retrofit1_2 = ((Route) ((Example) response.body()).getRoutes().get(0)).getOverviewPolyline().getPoints();
              List retrofit1_3 = decodePoly(retrofit1_2);
              line = mMap.addPolyline((new PolylineOptions()).addAll(retrofit1_3).width(20F).color(0xffff0000).geodesic(true));
              i++;
            }
          } catch (Exception e) {
            Log.d("onResponse", "There is an error");
            e.printStackTrace();
          }
        }
      });
      return;
    }
    catch(Exception e)
    {
      e.printStackTrace();
    }
  }

  private void build_retrofit_and_get_response2(String s)
  {
    url = "https://maps.googleapis.com/maps/";
    retrofit = (new retrofit.Retrofit.Builder()).baseUrl(url).addConverterFactory(GsonConverterFactory.create()).build();
    service = (RetrofitMaps)retrofit.create(RetrofitMaps.class);
    service.getDistanceDuration("metric", (new StringBuilder()).append(current_latlng.latitude).append(",").append(current_latlng.longitude).toString(), (new StringBuilder()).append(destination_latlng.latitude).append(",").append(destination_latlng.longitude).toString(), s).enqueue(new Callback() {
      @Override
      public void onResponse(Response response) {
        onResponse(response, null);
      }

      public void onFailure(Throwable throwable)
      {
        Log.d("onFailure", throwable.toString());
      }

      public void onResponse(Response response, Retrofit retrofit1)
      {
        if(line != null)
        {
          line.remove();
        }
        int i = 0;
        try {
          while (i < ((Example) response.body()).getRoutes().size()) {
            String retrofit1_1 = ((Leg) ((Route) ((Example) response.body()).getRoutes().get(i)).getLegs().get(i)).getDistance().getText();
            String s1 = ((Leg) ((Route) ((Example) response.body()).getRoutes().get(i)).getLegs().get(i)).getDuration().getText();
            ShowDistanceDuration.setText(String.valueOf((new StringBuilder()).append("Distance:").append(retrofit1_1).toString()));
            ShowDistanceDuration2.setText(String.valueOf((new StringBuilder()).append("Duration:").append(s1).toString()));
            String retrofit1_2 = ((Route) ((Example) response.body()).getRoutes().get(0)).getOverviewPolyline().getPoints();
            List retrofit1_3 = decodePoly(retrofit1_2);
            line = mMap.addPolyline((new PolylineOptions()).addAll(retrofit1_3).width(20F).color(0xffff0000).geodesic(true));
            i++;
          }
        } catch (Exception e) {
          Log.d("onResponse", "There is an error");
          e.printStackTrace();
        }
      }
    });
  }

  private void build_retrofit_and_get_response3(String s)
  {
    try
    {
      url = "https://maps.googleapis.com/maps/";
      retrofit = (new retrofit.Retrofit.Builder()).baseUrl(url).addConverterFactory(GsonConverterFactory.create()).build();
      service = (RetrofitMaps)retrofit.create(RetrofitMaps.class);
      service.getDistanceDuration("metric", (new StringBuilder()).append(origin.latitude).append(",").append(origin.longitude).toString(), (new StringBuilder()).append(dest.latitude).append(",").append(dest.longitude).toString(), s).enqueue(new Callback() {
        @Override
        public void onResponse(Response response) {
          onResponse(response, null);
        }

        public void onFailure(Throwable throwable)
        {
          Log.d("onFailure", throwable.toString());
        }

        public void onResponse(Response response, Retrofit retrofit1)
        {
          if(line != null)
          {
            line.remove();
          }
          int i = 0;
          try {
            while (i < ((Example) response.body()).getRoutes().size()) {
              String retrofit1_1 = ((Leg) ((Route) ((Example) response.body()).getRoutes().get(i)).getLegs().get(i)).getDistance().getText();
              String s1 = ((Leg) ((Route) ((Example) response.body()).getRoutes().get(i)).getLegs().get(i)).getDuration().getText();
              ShowDistanceDuration.setText(String.valueOf((new StringBuilder()).append("Distance:").append(retrofit1_1).toString()));
              ShowDistanceDuration2.setText(String.valueOf((new StringBuilder()).append("Duration:").append(s1).toString()));
              String retrofit1_2 = ((Route) ((Example) response.body()).getRoutes().get(0)).getOverviewPolyline().getPoints();
              List retrofit1_3 = decodePoly(retrofit1_2);
              line = mMap.addPolyline((new PolylineOptions()).addAll(retrofit1_3).width(20F).color(0xffff0000).geodesic(true));
              i++;
            }
          }catch (Exception e) {
            Log.d("onResponse", "There is an error");
            e.printStackTrace();
          }
        }
      });
      return;
    }
    catch(Exception e)
    {
      e.printStackTrace();
    }
  }

  private List decodePoly(String s) // copied from MapsActivity2
  {
    Object obj = null;
    ArrayList arraylist = new ArrayList();
    int i = 0;
    int k1 = s.length();
    int j;
    int k;
    int l;
    int i1;
    int j1;
    k = 0;
    j = 0;
    if(i >= k1)
      return arraylist;
    j1 = 0;
    i1 = 0;
    l = i;
    try
    {
      while(true) {
        i = l + 1;
        l = s.charAt(l) - 63;
        i1 |= (l & 0x1f) << j1;
        j1 += 5;
        if (l < 32) {
          if ((i1 & 1) != 0) {
            l = ~(i1 >> 1);
          } else {
            l = i1 >> 1;
          }
          j1 = k + l;
          i1 = 0;
          l = 0;
          k = i;
          while (true) {
            i = k + 1;
            k = s.charAt(k) - 63;
            l |= (k & 0x1f) << i1;
            i1 += 5;
            if (k < 32)
              break;
            k = i;
          }
          if ((l & 1) != 0) {
            k = ~(l >> 1);
          } else {
            k = l >> 1;
          }
          j += k;
          arraylist.add(new LatLng((double) j1 / 100000D, (double) j / 100000D));
          k = j1;
          if (i >= k1)
            return arraylist;
          j1 = 0;
          i1 = 0;
          l = i;
        } else
          l = i;
      }
    }
    catch(Exception e)
    {
      e.printStackTrace();
      return arraylist;
    }
  }

  private void requestNewInterstitial()
  {
    com.google.android.gms.ads.AdRequest adrequest = (new com.google.android.gms.ads.AdRequest.Builder()).build();
    mInterstitialAd.loadAd(adrequest);
  }

  protected void buildGoogleApiClient()
  {
    synchronized (this) {
      mGoogleApiClient = (new com.google.android.gms.common.api.GoogleApiClient.Builder(this)).addConnectionCallbacks(this).addOnConnectionFailedListener(this).addApi(LocationServices.API).build();
      mGoogleApiClient.connect();
    }
  }

  public boolean checkLocationPermission()
  {
    if(ContextCompat.checkSelfPermission(this, "android.permission.ACCESS_FINE_LOCATION") != 0)
    {
      if(ActivityCompat.shouldShowRequestPermissionRationale(this, "android.permission.ACCESS_FINE_LOCATION"))
      {
        ActivityCompat.requestPermissions(this, new String[] {
                "android.permission.ACCESS_FINE_LOCATION"
        }, 99);
        return false;
      } else
      {
        ActivityCompat.requestPermissions(this, new String[] {
                "android.permission.ACCESS_FINE_LOCATION"
        }, 99);
        return false;
      }
    } else
    {
      return true;
    }
  }

  public void onBackPressed()
  {
    startActivity(new Intent(this, Dashboard.class));
    finish();
  }

  public void onConnected(Bundle bundle)
  {
    mLocationRequest = new LocationRequest();
    mLocationRequest.setInterval(1000L);
    mLocationRequest.setFastestInterval(1000L);
    mLocationRequest.setPriority(102);
    if(ContextCompat.checkSelfPermission(this, "android.permission.ACCESS_FINE_LOCATION") == 0)
    {
      Log.d("OnConncected", "Connected ");
    }
  }

  public void onConnectionFailed(ConnectionResult connectionresult)
  {
  }

  public void onConnectionSuspended(int i)
  {
  }

  protected void onCreate(Bundle bundle)
  {
    super.onCreate(bundle);
    getWindow().addFlags(128);
    setContentView(0x7f040023);
    AdRequest bundle_1 = (new com.google.android.gms.ads.AdRequest.Builder()).build();
    ((AdView)findViewById(0x7f0e00b3)).loadAd(bundle_1);
    mInterstitialAd = new InterstitialAd(this);
    mInterstitialAd.setAdUnitId("ca-app-pub-4434536017055893/2275795621");
    requestNewInterstitial();
    ShowDistanceDuration = (TextView)findViewById(0x7f0e00b0);
    ShowDistanceDuration2 = (TextView)findViewById(0x7f0e00b1);
    button_direction = (ImageButton)findViewById(0x7f0e00ae);
    cardView = (CardView)findViewById(0x7f0e00a8);
    search_fragment1 = (CardView)findViewById(0x7f0e00aa);
    cardView.setOnClickListener(new android.view.View.OnClickListener() {
      public void onClick(View view)
      {
        try
        {
          AlertDialog view_1 = (new android.support.v7.app.AlertDialog.Builder(MapsActivity.this)).create();
          view_1.setTitle("Use Location?");
          view_1.setButton(-1, "Current", new android.content.DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialoginterface, int i)
            {
              dialoginterface.dismiss();
            }
          });
          view_1.setButton(-3, "Customize", new android.content.DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialoginterface, int i)
            {
              cardView.setVisibility(View.GONE);
              search_fragment1.setVisibility(View.VISIBLE);
            }
          });
          view_1.show();
        }
        catch(Exception e)
        {
          e.printStackTrace();
        }
      }
    });
    mapFragment = (SupportMapFragment)getSupportFragmentManager().findFragmentById(0x7f0e00af);
    mapFragment.getMapAsync(this);
    normal = (ImageButton)findViewById(0x7f0e0019);
    satellite = (ImageButton)findViewById(0x7f0e0044);
    terrain = (ImageButton)findViewById(0x7f0e0045);
    normal.setOnClickListener(new android.view.View.OnClickListener() {
      public void onClick(View view)
      {
        mMap.setMapType(1);
      }
    });
    satellite.setOnClickListener(new android.view.View.OnClickListener() {
      public void onClick(View view)
      {
        if(MapsActivity.mInterstitialAd.isLoaded())
        {
          MapsActivity.mInterstitialAd.show();
          MapsActivity.mInterstitialAd.setAdListener(new AdListener() {
            public void onAdClosed()
            {
              mMap.setMapType(2);
              requestNewInterstitial();
            }
          });
          return;
        }
        try
        {
          mMap.setMapType(2);
          requestNewInterstitial();
          return;
        }
        catch(Exception e)
        {
          e.printStackTrace();
        }
      }
    });
    terrain.setOnClickListener(new android.view.View.OnClickListener() {
      public void onClick(View view)
      {
        try
        {
          mMap.setMapType(3);
        }
        catch(Exception e)
        {
          e.printStackTrace();
        }
      }
    });
    button_direction.setOnClickListener(new android.view.View.OnClickListener() {
      public void onClick(View view)
      {
        try
        {
          if(MapsActivity.latLng != null && MapsActivity.destination_latlng != null && MapsActivity.current_latlng == null)
          {
            build_retrofit_and_get_response1("driving");
            mMap.setOnMapClickListener(new com.google.android.gms.maps.GoogleMap.OnMapClickListener() {
              public void onMapClick(LatLng latlng)
              {
              }
            });
            mapFragment.getView().setClickable(false);
          }
        }
        catch(Exception e)
        {
          e.printStackTrace();
          return;
        }
        if(MapsActivity.latLng != null && MapsActivity.destination_latlng != null && MapsActivity.current_latlng != null)
        {
          build_retrofit_and_get_response2("driving");
          mMap.setOnMapClickListener(new com.google.android.gms.maps.GoogleMap.OnMapClickListener() {
            public void onMapClick(LatLng latlng)
            {
            }
          });
          return;
        }
        if(MapsActivity.latLng != null && MapsActivity.destination_latlng == null && MapsActivity.current_latlng == null && MapsActivity.origin != null && MapsActivity.dest != null)
        {
          build_retrofit_and_get_response3("driving");
          mMap.setOnMapClickListener(new com.google.android.gms.maps.GoogleMap.OnMapClickListener() {
            public void onMapClick(LatLng latlng)
            {
            }
          });
          return;
        }
        Toast.makeText(getApplicationContext(), "Choose Location", Toast.LENGTH_SHORT).show();
        return;
      }
    });
    MarkerPoints = new ArrayList();
    if(MarkerPoints.size() > 1)
    {
      MarkerPoints.clear();
      mMap.clear();
    }
    PlaceAutocompleteFragment bundle_2 = (PlaceAutocompleteFragment)getFragmentManager().findFragmentById(0x7f0e00ab);
    bundle_2.setOnPlaceSelectedListener(this);
    bundle_2.setHint("Your Location");
    PlaceAutocompleteFragment placeautocompletefragment = (PlaceAutocompleteFragment)getFragmentManager().findFragmentById(0x7f0e00ad);
    placeautocompletefragment.setOnPlaceSelectedListener(this);
    placeautocompletefragment.setHint("Choose Destination");
    bundle_2.setOnPlaceSelectedListener(new PlaceSelectionListener() {
      public void onError(Status status)
      {
        Toast.makeText(MapsActivity.this, "NETWORK ERROR ", Toast.LENGTH_SHORT).show();
      }

      public void onPlaceSelected(Place place)
      {
        try
        {
          mMap.clear();
          com.google.android.gms.maps.CameraUpdate cameraupdate = CameraUpdateFactory.newLatLngZoom(place.getLatLng(), 10F);
          MapsActivity.current_latlng = place.getLatLng();
          String s = place.getName().toString();
          current = mMap.addMarker((new MarkerOptions()).position(place.getLatLng()).title(s));
          mMap.animateCamera(cameraupdate);
          return;
        }
        catch(Exception e)
        {
          e.printStackTrace();
        }
      }
    });
    placeautocompletefragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
      public void onError(Status status)
      {
        Toast.makeText(MapsActivity.this, "NETWORK ERROR", Toast.LENGTH_SHORT).show();
      }

      public void onPlaceSelected(Place place)
      {
        try
        {
          com.google.android.gms.maps.CameraUpdate cameraupdate = CameraUpdateFactory.newLatLngZoom(place.getLatLng(), 10F);
          String s = place.getName().toString();
          MapsActivity.destination_latlng = place.getLatLng();
          destination = mMap.addMarker((new MarkerOptions()).position(place.getLatLng()).title(s));
          mMap.animateCamera(cameraupdate);
        }
        catch(Exception e)
        {
          e.printStackTrace();
        }
      }
    });
    locationManager = (LocationManager)getSystemService(LOCATION_SERVICE);
    if(ActivityCompat.checkSelfPermission(this, "android.permission.ACCESS_FINE_LOCATION") != 0 && ActivityCompat.checkSelfPermission(this, "android.permission.ACCESS_COARSE_LOCATION") != 0)
    {
      return;
    } else
    {
      locationManager.requestLocationUpdates("network", 400L, 1000F, this);
      return;
    }
  }

  public void onError(Status status)
  {
  }

  public void onLocationChanged(Location location)
  {
    try
    {
      mMap.getUiSettings().setCompassEnabled(true);
      latitude = Double.valueOf(location.getLatitude());
      longitude = Double.valueOf(location.getLongitude());
      latLng = new LatLng(latitude.doubleValue(), longitude.doubleValue());
      mCurrLocationMarker = mMap.addMarker((new MarkerOptions()).icon(BitmapDescriptorFactory.defaultMarker(240F)).position(latLng));
      CameraUpdate location_1 = CameraUpdateFactory.newLatLngZoom(latLng, 10F);
      mMap.animateCamera(location_1);
      locationManager.removeUpdates(this);
    }
    catch(Exception e)
    {
      e.printStackTrace();
    }
  }

  @SuppressLint("MissingPermission")
  public void onMapReady(GoogleMap googlemap)
  {
    try
    {
      mMap = googlemap;
      mMap.setMapType(1);
      mMap.setMyLocationEnabled(true);
      mMap.getUiSettings().setZoomControlsEnabled(true);
      mMap.getUiSettings().setScrollGesturesEnabled(true);
      mMap.getUiSettings().setTiltGesturesEnabled(true);
      mMap.getUiSettings().setCompassEnabled(true);
      if(MarkerPoints.size() > 1)
      {
        MarkerPoints.clear();
        mMap.clear();
      }
      if(android.os.Build.VERSION.SDK_INT < 23)
      {
        buildGoogleApiClient();
        mMap.setMyLocationEnabled(true);
      } else {
        if (ContextCompat.checkSelfPermission(this, "android.permission.ACCESS_FINE_LOCATION") == 0) {
          buildGoogleApiClient();
          mMap.setMyLocationEnabled(true);
        }
      }
      mMap.setOnMapClickListener(new com.google.android.gms.maps.GoogleMap.OnMapClickListener() {
        public void onMapClick(LatLng latlng)
        {
          MarkerOptions markeroptions;
          if(MarkerPoints.size() > 1)
          {
            MarkerPoints.clear();
            mMap.clear();
            MarkerPoints = new ArrayList();
            ShowDistanceDuration.setText("");
          }
          MarkerPoints.add(latlng);
          markeroptions = new MarkerOptions();
          markeroptions.position(latlng);
          if(MarkerPoints.size() != 1) {
            if(MarkerPoints.size() == 2)
            {
              markeroptions.icon(BitmapDescriptorFactory.defaultMarker(0.0F));
            }
          } else
            markeroptions.icon(BitmapDescriptorFactory.defaultMarker(120F));
          mMap.addMarker(markeroptions);
          if(MarkerPoints.size() >= 2)
          {
            MapsActivity.origin = (LatLng)MarkerPoints.get(0);
            MapsActivity.dest = (LatLng)MarkerPoints.get(1);
            return;
          }
        }
      });
    }
    catch(Exception e)
    {
      e.printStackTrace();
    }
  }

  public void onPlaceSelected(Place place)
  {
  }

  public void onProviderDisabled(String s)
  {
  }

  public void onProviderEnabled(String s)
  {
  }

  public void onRequestPermissionsResult(int i, String as[], int ai[])
  {
    switch (i) {
      default:
        break;
      case 99:
        if(ai.length > 0 && ai[0] == 0)
        {
          if(ContextCompat.checkSelfPermission(this, "android.permission.ACCESS_FINE_LOCATION") == 0)
          {
            if(mGoogleApiClient == null)
            {
              buildGoogleApiClient();
            }
            mMap.setMyLocationEnabled(true);
            return;
          }
        } else
        {
          Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();
          return;
        }
        break;
    }
  }

  public void onStatusChanged(String s, int i, Bundle bundle)
  {
  }
}
