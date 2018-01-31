package com.gps.offlinenavigation.map.routedirection.free.controller;

import android.app.Activity;
import android.content.Intent;
import android.location.*;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.*;
import android.widget.Toast;
import com.gps.offlinenavigation.map.routedirection.free.model.map.MapHandler;
import com.gps.offlinenavigation.map.routedirection.free.model.map.Tracking;
import com.gps.offlinenavigation.map.routedirection.free.model.util.Variable;
import com.graphhopper.GraphHopper;
import java.io.File;
import org.mapsforge.core.model.LatLong;
import org.mapsforge.core.model.MapPosition;
import org.mapsforge.map.android.graphics.AndroidGraphicFactory;
import org.mapsforge.map.android.view.MapView;
import org.mapsforge.map.layer.LayerManager;
import org.mapsforge.map.layer.Layers;
import org.mapsforge.map.layer.overlay.Marker;
import org.mapsforge.map.model.MapViewPosition;
import org.mapsforge.map.model.Model;

public class MapActivity extends Activity
        implements LocationListener
{

  private static Location mCurrentLocation;
  private LocationManager locationManager;
  private Location mLastLocation;
  private Marker mPositionMarker;
  private MapActions mapActions;
  private MapView mapView;

  public MapActivity()
  {
  }

  private void checkGpsAvailability()
  {
    try
    {
      if(!((LocationManager)getSystemService(LOCATION_SERVICE)).isProviderEnabled("gps"))
      {
        startActivity(new Intent("android.settings.LOCATION_SOURCE_SETTINGS"));
      }
      return;
    }
    catch(Exception exception)
    {
      exception.printStackTrace();
    }
  }

  private void customMapView()
  {
    try
    {
      ViewGroup viewgroup = (ViewGroup)findViewById(0x7f0e00a3);
      viewgroup.addView(LayoutInflater.from(this).inflate(0x7f040022, null));
      viewgroup.getParent().bringChildToFront(viewgroup);
      mapActions = new MapActions(this, mapView);
      return;
    }
    catch(Exception exception)
    {
      exception.printStackTrace();
    }
  }

  private void getMyLastLocation()
  {
    if(ActivityCompat.checkSelfPermission(this, "android.permission.ACCESS_FINE_LOCATION") != 0 && ActivityCompat.checkSelfPermission(this, "android.permission.ACCESS_COARSE_LOCATION") != 0)
    {
      return;
    }
    Object obj;
    Location location;
    obj = locationManager.getLastKnownLocation("gps");
    location = locationManager.getLastKnownLocation("network");
    if(location != null)
    {
      try
      {
        mLastLocation = location;
      }
      catch(Exception e)
      {
        e.printStackTrace();
      }
      return;
    }
    if(obj == null)
      mLastLocation = null;
    else
      mLastLocation = ((Location) (obj));
  }

  public static Location getmCurrentLocation()
  {
    return mCurrentLocation;
  }

  private void log(String s)
  {
    Log.i(getClass().getSimpleName(), (new StringBuilder()).append("-------").append(s).toString());
  }

  private void updateCurrentLocation(Location location)
  {
    if(location == null) {
      if(mLastLocation != null && mCurrentLocation == null)
      {
        mCurrentLocation = mLastLocation;
      }
    }else
      mCurrentLocation = location;
    try {
      if (mCurrentLocation != null) {
        LatLong location_1 = new LatLong(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());
        if (Tracking.getTracking().isTracking()) {
          MapHandler.getMapHandler().addTrackPoint(location_1);
          Tracking.getTracking().addPoint(mCurrentLocation);
        }
        Layers layers = mapView.getLayerManager().getLayers();
        MapHandler.getMapHandler().removeLayer(layers, mPositionMarker);
        mPositionMarker = MapHandler.getMapHandler().createMarker(location_1, 0x7f0200b3);
        layers.add(mPositionMarker);
        mapActions.showPositionBtn.setImageResource(0x7f0200b4);
        return;
      }
      mapActions.showPositionBtn.setImageResource(0x7f0200ad);
    }catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void onBackPressed()
  {
    startActivity(new Intent(this, MainActivity.class));
    finish();
  }

  protected void onCreate(Bundle bundle)
  {
    super.onCreate(bundle);
    getWindow().addFlags(128);
    setContentView(0x7f040021);
    locationManager = (LocationManager)getSystemService(LOCATION_SERVICE);
    if(ActivityCompat.checkSelfPermission(this, "android.permission.ACCESS_FINE_LOCATION") != 0 && ActivityCompat.checkSelfPermission(this, "android.permission.ACCESS_COARSE_LOCATION") != 0)
    {
      return;
    }
    locationManager.requestLocationUpdates("gps", 3000L, 5F, this);
    Variable.getVariable().setContext(getApplicationContext());
    Variable.getVariable().setZoomLevels(22, 12);
    AndroidGraphicFactory.createInstance(getApplication());
    mapView = new MapView(this);
    mapView.setClickable(true);
    mapView.setBuiltInZoomControls(true);
    try {
      MapHandler.getMapHandler().init(this, mapView, Variable.getVariable().getCountry(), Variable.getVariable().getMapsFolder());
      MapHandler.getMapHandler().loadMap(new File(Variable.getVariable().getMapsFolder().getAbsolutePath(), (new StringBuilder()).append(Variable.getVariable().getCountry()).append("-gh").toString()));
    }catch (Exception e) {
      Toast.makeText(getApplicationContext(), "Error Loading Map", Toast.LENGTH_SHORT).show();
      e.printStackTrace();
    }
    try
    {
      customMapView();
      checkGpsAvailability();
      getMyLastLocation();
      updateCurrentLocation(null);
    }
    catch(Exception e)
    {
      e.printStackTrace();
    }
  }

  protected void onDestroy()
  {
    try
    {
      super.onDestroy();
      if(MapHandler.getMapHandler().getHopper() != null)
      {
        MapHandler.getMapHandler().getHopper().close();
      }
      MapHandler.getMapHandler().setHopper(null);
      System.gc();
      return;
    }
    catch(Exception exception)
    {
      exception.printStackTrace();
    }
  }

  public void onLocationChanged(Location location)
  {
    updateCurrentLocation(location);
  }

  protected void onPause()
  {
    super.onPause();
  }

  public void onProviderDisabled(String s)
  {
    Toast.makeText(getBaseContext(), "Gps is turned off!!", Toast.LENGTH_SHORT).show();
  }

  public void onProviderEnabled(String s)
  {
    Toast.makeText(getBaseContext(), "Gps is turned on!! ", Toast.LENGTH_SHORT).show();
  }

  public void onResume()
  {
    super.onResume();
  }

  protected void onStart()
  {
    super.onStart();
  }

  public void onStatusChanged(String s, int i, Bundle bundle)
  {
  }

  protected void onStop()
  {
    try
    {
      super.onStop();
      if(mCurrentLocation != null)
      {
        Variable.getVariable().setLastLocation(mapView.getModel().mapViewPosition.getMapPosition().latLong);
      }
      if(mapView != null)
      {
        Variable.getVariable().setLastZoomLevel(mapView.getModel().mapViewPosition.getZoomLevel());
      }
      Variable.getVariable().saveVariables();
      return;
    }
    catch(Exception exception)
    {
      exception.printStackTrace();
    }
  }
}
