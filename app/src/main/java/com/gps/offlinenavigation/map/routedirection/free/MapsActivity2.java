package com.gps.offlinenavigation.map.routedirection.free;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.*;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.*;
import com.gps.offlinenavigation.map.routedirection.free.POJO.Distance;
import com.gps.offlinenavigation.map.routedirection.free.POJO.Duration;
import com.gps.offlinenavigation.map.routedirection.free.POJO.Example;
import com.gps.offlinenavigation.map.routedirection.free.POJO.Leg;
import com.gps.offlinenavigation.map.routedirection.free.POJO.OverviewPolyline;
import com.gps.offlinenavigation.map.routedirection.free.POJO.Route;
import java.util.ArrayList;
import java.util.List;
import retrofit.*;

public class MapsActivity2 extends FragmentActivity
        implements OnMapReadyCallback
{

  public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
  ArrayList<LatLng> MarkerPoints;
  TextView ShowDistanceDuration;
  LatLng dest;
  Polyline line;
  private GoogleMap mMap;
  LatLng origin;

  public MapsActivity2()
  {
  }

  private void build_retrofit_and_get_response(String s)
  {
    ((RetrofitMaps)(new retrofit.Retrofit.Builder()).baseUrl("https://maps.googleapis.com/maps/").addConverterFactory(GsonConverterFactory.create()).build().create(RetrofitMaps.class)).getDistanceDuration("metric", (new StringBuilder()).append(origin.latitude).append(",").append(origin.longitude).toString(), (new StringBuilder()).append(dest.latitude).append(",").append(dest.longitude).toString(), s).enqueue(new Callback() {

      @Override
      public void onResponse(Response response) {
        onResponse(response, null);
      }

      public void onFailure(Throwable throwable)
      {
        Log.d("onFailure", throwable.toString());
      }

      public void onResponse(Response<Example> response, Retrofit retrofit)
      {
        Log.d("RESPONSE", ((Example)response.body()).getRoutes().toString());
        if(line != null)
        {
          line.remove();
        }
        int i = 0;
        try {
          while (i < ((Example) response.body()).getRoutes().size()) {
            String retrofit_1 = ((Leg) ((Route) ((Example) response.body()).getRoutes().get(i)).getLegs().get(i)).getDistance().getText();
            String s1 = ((Leg) ((Route) ((Example) response.body()).getRoutes().get(i)).getLegs().get(i)).getDuration().getText();
            ShowDistanceDuration.setText((new StringBuilder()).append("Distance:").append(retrofit_1).append(", Duration:").append(s1).toString());
            String retrofit_2 = ((Route) ((Example) response.body()).getRoutes().get(0)).getOverviewPolyline().getPoints();
            List retrofit_3 = decodePoly(retrofit_2);
            line = mMap.addPolyline((new PolylineOptions()).addAll(retrofit_3).width(20F).color(0xffff0000).geodesic(true));
            i++;
          }
        }catch (Exception e){
          Log.d("onResponse", "There is an error");
          e.printStackTrace();
        }
      }
    });
  }

  private List decodePoly(String s)
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

  private boolean isGooglePlayServicesAvailable()
  {
    GoogleApiAvailability googleapiavailability = GoogleApiAvailability.getInstance();
    int i = googleapiavailability.isGooglePlayServicesAvailable(this);
    if(i != 0)
    {
      if(googleapiavailability.isUserResolvableError(i))
      {
        googleapiavailability.getErrorDialog(this, i, 0).show();
      }
      return false;
    } else
    {
      return true;
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

  protected void onCreate(Bundle bundle)
  {
    super.onCreate(bundle);
    setContentView(0x7f040024);
    ShowDistanceDuration = (TextView)findViewById(0x7f0e00b0);
    if(android.os.Build.VERSION.SDK_INT >= 23)
    {
      checkLocationPermission();
    }
    MarkerPoints = new ArrayList();
    if(!isGooglePlayServicesAvailable())
    {
      Log.d("onCreate", "Google Play Services not available. Ending Test case.");
      finish();
    } else
    {
      Log.d("onCreate", "Google Play Services available. Continuing.");
    }
    ((SupportMapFragment)getSupportFragmentManager().findFragmentById(0x7f0e00af)).getMapAsync(this);
  }

  public void onMapReady(GoogleMap googlemap)
  {
    mMap = googlemap;
    LatLng googlemap_1 = new LatLng(28.715872699999998D, 77.191073799999998D);
    mMap.addMarker((new MarkerOptions()).position(googlemap_1).title("Marker in Sydney"));
    mMap.moveCamera(CameraUpdateFactory.newLatLng(googlemap_1));
    mMap.animateCamera(CameraUpdateFactory.zoomTo(11F));
    mMap.setOnMapClickListener(new com.google.android.gms.maps.GoogleMap.OnMapClickListener() {
      public void onMapClick(LatLng latlng)
      {
        MarkerOptions markeroptions;
        if(MarkerPoints.size() > 1)
        {
          mMap.clear();
          MarkerPoints.clear();
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
          origin = (LatLng)MarkerPoints.get(0);
          dest = (LatLng)MarkerPoints.get(1);
        }
      }
    });
    ((Button)findViewById(0x7f0e00b4)).setOnClickListener(new android.view.View.OnClickListener() {
      public void onClick(View view)
      {
        build_retrofit_and_get_response("driving");
      }
    });
    ((Button)findViewById(0x7f0e00b5)).setOnClickListener(new android.view.View.OnClickListener() {
      public void onClick(View view)
      {
        build_retrofit_and_get_response("walking");
      }
    });
  }
}
