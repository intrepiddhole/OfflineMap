package com.gps.offlinenavigation.map.routedirection.free;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;
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
import com.google.android.gms.maps.model.PolylineOptions;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.json.JSONObject;

public class ShareLocation extends FragmentActivity
        implements LocationListener, OnMapReadyCallback, PlaceSelectionListener, com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks, com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener
{
  private class FetchUrl extends AsyncTask
  {

    protected Object doInBackground(Object aobj[])
    {
      return doInBackground((String[])aobj);
    }

    protected String doInBackground(String as[])
    {
      String obj = "";
      try
      {
        String as_1 = downloadUrl(as[0]);
        obj = as_1;
        Log.d("Background Task data", as_1);
        return as_1;
      } catch(Exception e) {
        Log.d("Background Task", e.toString());
        return obj;
      }
    }

    protected void onPostExecute(Object obj)
    {
      onPostExecute((String)obj);
    }

    protected void onPostExecute(String s)
    {
      super.onPostExecute(s);
      (new ParserTask()).execute(new String[] {
              s
      });
    }

    private FetchUrl()
    {
      super();
    }

  }

  private class ParserTask extends AsyncTask
  {
    protected Object doInBackground(Object aobj[])
    {
      return doInBackground((String[])aobj);
    }

    protected List doInBackground(String as[])
    {
      Object obj = null;
      List list = (List)obj;
      JSONObject jsonobject;
      try
      {
        jsonobject = new JSONObject(as[0]);
        list = (List)obj;
        Log.d("ParserTask", as[0]);
        list = (List)obj;
        DataParser as_1 = new DataParser();
        list = (List)obj;
        Log.d("ParserTask", as_1.toString());
        list = (List)obj;
        List as_2 = as_1.parse(jsonobject);
        list = (List)as_2;
        Log.d("ParserTask", "Executing routes");
        list = (List)as_2;
        Log.d("ParserTask", as_2.toString());
        return as_2;
      }
      catch(Exception e)
      {
        Log.d("ParserTask", e.toString());
        e.printStackTrace();
        return list;
      }
    }

    protected void onPostExecute(Object obj)
    {
      onPostExecute((List)obj);
    }

    protected void onPostExecute(List list)
    {
      PolylineOptions polylineoptions = null;
      for(int i = 0; i < list.size(); i++)
      {
        ArrayList arraylist = new ArrayList();
        polylineoptions = new PolylineOptions();
        List list1 = (List)list.get(i);
        for(int j = 0; j < list1.size(); j++)
        {
          HashMap hashmap = (HashMap)list1.get(j);
          arraylist.add(new LatLng(Double.parseDouble((String)hashmap.get("lat")), Double.parseDouble((String)hashmap.get("lng"))));
        }

        polylineoptions.addAll(arraylist);
        polylineoptions.width(10F);
        polylineoptions.color(0xffff0000);
        Log.d("onPostExecute", "onPostExecute lineoptions decoded");
      }

      if(polylineoptions != null)
      {
        mMap.addPolyline(polylineoptions);
        return;
      } else
      {
        Log.d("onPostExecute", "without Polylines drawn");
        return;
      }
    }

    private ParserTask()
    {
      super();
    }

  }


  private static final float MIN_DISTANCE = 1000F;
  private static final long MIN_TIME = 400L;
  public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
  public static LatLng current_latlng;
  public static LatLng destination_latlng;
  public static LatLng latLng;
  public static Double latitude;
  public static Double longitude;
  ArrayList MarkerPoints;
  ImageButton button_direction;
  CardView cardView;
  Marker current;
  Marker destination;
  private LocationManager locationManager;
  Marker mCurrLocationMarker;
  GoogleApiClient mGoogleApiClient;
  LocationRequest mLocationRequest;
  private GoogleMap mMap;
  SupportMapFragment mapFragment;
  ImageButton normal;
  ImageButton satellite;
  CardView search_fragment1;
  ImageButton terrain;
  String url1;
  String url2;

  public ShareLocation()
  {
  }

  private String downloadUrl(String s_p)
          throws IOException
  {
    String s1;
    Object obj;
    HttpURLConnection s2;
    Object obj1;
    HttpURLConnection s3;
    InputStream inputstream;
    String s4;
    BufferedReader bufferedreader;
    s4 = "";
    bufferedreader = null;
    inputstream = null;
    s3 = null;
    s2 = null;
    s1 = s4;
    obj = inputstream;
    obj1 = bufferedreader;
    HttpURLConnection s = (HttpURLConnection)(new URL(s_p)).openConnection();
    s1 = s4;
    obj = inputstream;
    s2 = s;
    obj1 = bufferedreader;
    s3 = s;
    s.connect();
    s1 = s4;
    obj = inputstream;
    s2 = s;
    obj1 = bufferedreader;
    s3 = s;
    inputstream = s.getInputStream();
    s1 = s4;
    obj = inputstream;
    s2 = s;
    obj1 = inputstream;
    s3 = s;
    bufferedreader = new BufferedReader(new InputStreamReader(inputstream));
    s1 = s4;
    obj = inputstream;
    s2 = s;
    obj1 = inputstream;
    s3 = s;
    StringBuffer stringbuffer = new StringBuffer();
    while(true) {
      s1 = s4;
      obj = inputstream;
      s2 = s;
      obj1 = inputstream;
      s3 = s;
      String s5 = bufferedreader.readLine();
      if (s5 == null) {
        s1 = s4;
        obj = inputstream;
        s2 = s;
        obj1 = (Object) inputstream;
        s3 = s;
        s4 = stringbuffer.toString();
        s1 = s4;
        obj = inputstream;
        s2 = s;
        obj1 = (Object) inputstream;
        s3 = s;
        Log.d("downloadUrl", s4);
        s1 = s4;
        obj = inputstream;
        s2 = s;
        obj1 = (Object) inputstream;
        s3 = s;
        bufferedreader.close();
        if (inputstream != null) {
          inputstream.close();
        }
        obj = s4;
        if (s == null)
          return (String) obj;
        s.disconnect();
        return s4;
      }
      s1 = s4;
      obj = inputstream;
      s2 = s;
      obj1 = inputstream;
      s3 = s;
      stringbuffer.append(s5);
    }
  }

  private String getUrl(LatLng latlng, LatLng latlng1)
  {
    String latlng_1 = (new StringBuilder()).append("origin=").append(latlng.latitude).append(",").append(latlng.longitude).toString();
    String latlng1_1 = (new StringBuilder()).append("destination=").append(latlng1.latitude).append(",").append(latlng1.longitude).toString();
    String latlng_2 = (new StringBuilder()).append(latlng_1).append("&").append(latlng1_1).append("&").append("sensor=false").toString();
    return (new StringBuilder()).append("https://maps.googleapis.com/maps/api/directions/").append("json").append("?").append(latlng_2).toString();
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

  public void onConnected(Bundle bundle)
  {
    mLocationRequest = new LocationRequest();
    mLocationRequest.setInterval(1000L);
    mLocationRequest.setFastestInterval(1000L);
    mLocationRequest.setPriority(102);
    if(ContextCompat.checkSelfPermission(this, "android.permission.ACCESS_FINE_LOCATION") == 0)
    {
      Log.d("onPostExecute", "onPostExecute lineoptions decoded");
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
    setContentView(0x7f040023);
    button_direction = (ImageButton)findViewById(0x7f0e00ae);
    cardView = (CardView)findViewById(0x7f0e00a8);
    search_fragment1 = (CardView)findViewById(0x7f0e00aa);
    cardView.setOnClickListener(new android.view.View.OnClickListener() {
      public void onClick(View view)
      {
        try
        {
          AlertDialog view_1 = (new android.support.v7.app.AlertDialog.Builder(ShareLocation.this)).create();
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
        } catch(Exception e) {
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
        mMap.setMapType(2);
      }
    });
    terrain.setOnClickListener(new android.view.View.OnClickListener() {
      public void onClick(View view)
      {
        mMap.setMapType(3);
      }
    });
    button_direction.setOnClickListener(new android.view.View.OnClickListener() {
      public void onClick(View view)
      {
        try
        {
          com.google.android.gms.maps.model.LatLngBounds.Builder builder;
          FetchUrl view_1 = new FetchUrl();
          builder = new com.google.android.gms.maps.model.LatLngBounds.Builder();
          if(ShareLocation.current_latlng != null || ShareLocation.destination_latlng != null) {
            if(ShareLocation.current_latlng != null)
            {
              url2 = getUrl(ShareLocation.current_latlng, ShareLocation.destination_latlng);
              view_1.execute(new String[] {
                      url2
              });
              builder.include(ShareLocation.current_latlng);
              builder.include(ShareLocation.destination_latlng);
              CameraUpdate view_2 = CameraUpdateFactory.newLatLngBounds(builder.build(), 50);
              mMap.animateCamera(view_2, new com.google.android.gms.maps.GoogleMap.CancelableCallback() {
                public void onCancel()
                {
                }

                public void onFinish()
                {
                  com.google.android.gms.maps.CameraUpdate cameraupdate = CameraUpdateFactory.zoomBy(-1F);
                  mMap.animateCamera(cameraupdate);
                }
              });
            } else {
              url1 = getUrl(ShareLocation.latLng, ShareLocation.destination_latlng);
              view_1.execute(new String[]{
                      url1
              });
              builder.include(ShareLocation.latLng);
              builder.include(ShareLocation.destination_latlng);
              CameraUpdate view_2 = CameraUpdateFactory.newLatLngBounds(builder.build(), 50);
              mMap.animateCamera(view_2, new com.google.android.gms.maps.GoogleMap.CancelableCallback() {
                public void onCancel() {
                }

                public void onFinish() {
                  com.google.android.gms.maps.CameraUpdate cameraupdate = CameraUpdateFactory.zoomBy(-1F);
                  mMap.animateCamera(cameraupdate);
                }
              });
            }
          } else
            Toast.makeText(ShareLocation.this, "Select Location", Toast.LENGTH_SHORT).show();
          mMap.setOnMapClickListener(new com.google.android.gms.maps.GoogleMap.OnMapClickListener() {
            public void onMapClick(LatLng latlng)
            {
            }
          });
          mapFragment.getView().setClickable(false);
        }
        catch(Exception e)
        {
          e.printStackTrace();
        }
      }
    });
    MarkerPoints = new ArrayList();
    PlaceAutocompleteFragment bundle_2 = (PlaceAutocompleteFragment)getFragmentManager().findFragmentById(0x7f0e00ab);
    bundle_2.setOnPlaceSelectedListener(this);
    bundle_2.setHint("Your Location");
    PlaceAutocompleteFragment placeautocompletefragment = (PlaceAutocompleteFragment)getFragmentManager().findFragmentById(0x7f0e00ad);
    placeautocompletefragment.setOnPlaceSelectedListener(this);
    placeautocompletefragment.setHint("Choose Destination");
    bundle_2.setOnPlaceSelectedListener(new PlaceSelectionListener() {
      public void onError(Status status)
      {
        Toast.makeText(ShareLocation.this, "Fragment ONE: ERROR ", Toast.LENGTH_SHORT).show();
      }

      public void onPlaceSelected(Place place)
      {
        try
        {
          mMap.clear();
          com.google.android.gms.maps.CameraUpdate cameraupdate = CameraUpdateFactory.newLatLngZoom(place.getLatLng(), 10F);
          ShareLocation.current_latlng = place.getLatLng();
          String s = place.getName().toString();
          current = mMap.addMarker((new MarkerOptions()).position(place.getLatLng()).title(s));
          mMap.animateCamera(cameraupdate);
        } catch(Exception e) {
          e.printStackTrace();
        }
      }
    });
    placeautocompletefragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
      public void onError(Status status)
      {
        Toast.makeText(ShareLocation.this, "Fragment TWO: ERROR", Toast.LENGTH_SHORT).show();
      }

      public void onPlaceSelected(Place place)
      {
        try
        {
          com.google.android.gms.maps.CameraUpdate cameraupdate = CameraUpdateFactory.newLatLngZoom(place.getLatLng(), 10F);
          String s = place.getName().toString();
          ShareLocation.destination_latlng = place.getLatLng();
          destination = mMap.addMarker((new MarkerOptions()).position(place.getLatLng()).title(s));
          mMap.animateCamera(cameraupdate);
        } catch(Exception e) {
          e.printStackTrace();
        }
      }
    });
    locationManager = (LocationManager)getSystemService(LOCATION_SERVICE);
    if(ActivityCompat.checkSelfPermission(this, "android.permission.ACCESS_FINE_LOCATION") != 0 && ActivityCompat.checkSelfPermission(this, "android.permission.ACCESS_COARSE_LOCATION") != 0)
      return;
    try
    {
      locationManager.requestLocationUpdates("network", 400L, 1000F, this);
    }
    catch(Exception e)
    {
      e.printStackTrace();
    }
  }

  public void onError(Status status)
  {
  }

  public void onLocationChanged(Location location)
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
      if(android.os.Build.VERSION.SDK_INT < 23)
      {
        buildGoogleApiClient();
        mMap.setMyLocationEnabled(true);
      } else if(ContextCompat.checkSelfPermission(this, "android.permission.ACCESS_FINE_LOCATION") == 0) {
        buildGoogleApiClient();
        mMap.setMyLocationEnabled(true);
      }
      mMap.setOnMapClickListener(new com.google.android.gms.maps.GoogleMap.OnMapClickListener() {
        public void onMapClick(LatLng latlng)
        {
          Object obj;
          if(MarkerPoints.size() > 1)
          {
            MarkerPoints.clear();
            mMap.clear();
          }
          MarkerPoints.add(latlng);
          obj = new MarkerOptions();
          ((MarkerOptions) (obj)).position(latlng);
          if(MarkerPoints.size() != 1) {
            if(MarkerPoints.size() == 2)
              ((MarkerOptions) (obj)).icon(BitmapDescriptorFactory.defaultMarker(0.0F));
          } else
            ((MarkerOptions) (obj)).icon(BitmapDescriptorFactory.defaultMarker(120F));
          mMap.addMarker(((MarkerOptions) (obj)));
          if(MarkerPoints.size() >= 2)
          {
            latlng = (LatLng)MarkerPoints.get(0);
            LatLng obj_1 = (LatLng)MarkerPoints.get(1);
            String obj_2 = getUrl(latlng, obj_1);
            Log.d("onMapClick", obj_2);
            (new FetchUrl()).execute(new String[] {
                    obj_2
            });
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latlng));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(11F));
            mMap.setOnMapClickListener(new com.google.android.gms.maps.GoogleMap.OnMapClickListener() {
              public void onMapClick(LatLng latlng)
              {
              }
            });
            return;
          }
        }
      });
    }
    catch(Exception e)
    {
      e.printStackTrace();
      return;
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
          Toast.makeText(this, "permission denied", 1).show();
          return;
        }
        break;
    }
  }

  public void onStatusChanged(String s, int i, Bundle bundle)
  {
  }



}
