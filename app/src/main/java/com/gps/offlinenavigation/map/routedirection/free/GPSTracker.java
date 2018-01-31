package com.gps.offlinenavigation.map.routedirection.free;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.*;
import android.os.Bundle;
import android.util.Log;

public class GPSTracker
        implements LocationListener
{

  private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10L;
  private static final long MIN_TIME_BW_UPDATES = 60000L;
  boolean canGetLocation;
  boolean isGPSEnabled;
  boolean isNetworkEnabled;
  double latitude;
  Location location;
  protected LocationManager locationManager;
  double longitude;
  private final Context mContext;
  private Location m_Location;

  public GPSTracker(Context context)
  {
    isGPSEnabled = false;
    isNetworkEnabled = false;
    canGetLocation = false;
    location = null;
    mContext = context;
    m_Location = getLocation();
  }

  public boolean canGetLocation()
  {
    return canGetLocation;
  }

  public double getLatitude()
  {
    if(location != null)
    {
      latitude = location.getLatitude();
    }
    return latitude;
  }

  @SuppressLint("MissingPermission")
  public Location getLocation()
  {
    locationManager = (LocationManager)mContext.getSystemService(Context.LOCATION_SERVICE);
    isGPSEnabled = locationManager.isProviderEnabled("gps");
    isNetworkEnabled = locationManager.isProviderEnabled("network");
    if(!isGPSEnabled) {
      boolean flag = isNetworkEnabled;
      if (!flag)
        return location;
    }
    try
    {
      canGetLocation = true;
      if(isNetworkEnabled)
      {
        locationManager.requestLocationUpdates("network", 60000L, 10F, this);
        Log.d("Network", "Network Enabled");
        if(locationManager != null)
        {
          location = locationManager.getLastKnownLocation("network");
          if(location != null)
          {
            latitude = location.getLatitude();
            longitude = location.getLongitude();
          }
        }
      }
      if(isGPSEnabled && location == null)
      {
        locationManager.requestLocationUpdates("gps", 60000L, 10F, this);
        Log.d("GPS", "GPS Enabled");
        if(locationManager != null)
        {
          location = locationManager.getLastKnownLocation("gps");
          if(location != null)
          {
            latitude = location.getLatitude();
            longitude = location.getLongitude();
          }
        }
      }
    }
    catch(Exception exception)
    {
      exception.printStackTrace();
    }
    return location;
  }

  public double getLongitude()
  {
    if(location != null)
    {
      longitude = location.getLongitude();
    }
    return longitude;
  }

  public void onLocationChanged(Location location1)
  {
  }

  public void onProviderDisabled(String s)
  {
  }

  public void onProviderEnabled(String s)
  {
  }

  public void onStatusChanged(String s, int i, Bundle bundle)
  {
  }

  public void stopUsingGPS()
  {
    if(locationManager != null)
    {
      locationManager.removeUpdates(this);
    }
  }
}
