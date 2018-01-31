package com.gps.offlinenavigation.map.routedirection.free.model.map;

import android.location.Location;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;

import com.gps.offlinenavigation.map.routedirection.free.controller.AppSettings;
import com.gps.offlinenavigation.map.routedirection.free.model.database.DBtrackingPoints;
import com.gps.offlinenavigation.map.routedirection.free.model.listeners.TrackingListener;
import com.gps.offlinenavigation.map.routedirection.free.model.util.GenerateGPX;
import com.gps.offlinenavigation.map.routedirection.free.model.util.Variable;
import com.jjoe64.graphview.series.DataPoint;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Tracking {
  private static Tracking tracking;
  private double avgSpeed;
  private DBtrackingPoints dBtrackingPoints = new DBtrackingPoints(MapHandler.getMapHandler().getActivity().getApplicationContext());
  private double distance;
  private boolean isOnTracking = false;
  private List<TrackingListener> listeners = new ArrayList();
  private double maxSpeed;
  private Location startLocation;
  private long timeStart;

  private Tracking() {
  }

  private void broadcast(DataPoint var1, DataPoint var2) {
    Iterator var3 = this.listeners.iterator();

    while(var3.hasNext()) {
      ((TrackingListener)var3.next()).addDistanceGraphSeriesPoint(var1, var2);
    }

  }

  private void broadcast(Double var1, Double var2, Double var3, DataPoint[][] var4) {
    Iterator var5 = this.listeners.iterator();

    while(var5.hasNext()) {
      TrackingListener var6 = (TrackingListener)var5.next();
      if(var1 != null) {
        var6.updateAvgSpeed(var1);
      }

      if(var2 != null) {
        var6.updateMaxSpeed(var2);
      }

      if(var3 != null) {
        var6.updateDistance(var3);
      }

      if(var4 != null) {
        var6.updateDistanceGraphSeries(var4);
      }
    }

  }

  public static Tracking getTracking() {
    if(tracking == null) {
      tracking = new Tracking();
    }

    return tracking;
  }

  private void intAnalytics() {
    this.avgSpeed = 0.0D;
    this.maxSpeed = 0.0D;
    this.distance = 0.0D;
    this.timeStart = System.currentTimeMillis();
    this.startLocation = null;
  }

  private void log(String var1) {
    Log.i(this.getClass().getSimpleName(), "-----------------" + var1);
  }

  private void updateDisSpeed(Location var1) {
    if(this.startLocation != null) {
      float var2 = this.startLocation.distanceTo(var1);
      this.distance += (double)var2;
      this.avgSpeed = this.distance / (double)(this.getDurationInMilliS() / 3600L);
      if(AppSettings.getAppSettings().getAppSettingsVP().getVisibility() == View.VISIBLE) {
        AppSettings.getAppSettings().updateAnalytics(this.avgSpeed, this.distance);
      }

      this.broadcast(Double.valueOf(this.avgSpeed), (Double)null, Double.valueOf(this.distance), (DataPoint[][])null);
    }

  }

  private void updateMaxSpeed(Location var1) {
    if(this.startLocation != null) {
      double var2 = (double)this.startLocation.distanceTo(var1) / ((double)(var1.getTime() - this.startLocation.getTime()) / 1000.0D);
      double var4 = (double)(var1.getTime() - this.getTimeStart()) / 3600000.0D;
      this.broadcast(new DataPoint(var4, var2), new DataPoint(var4, this.distance));
      var2 *= 3.0D;
      if(this.maxSpeed < var2) {
        this.maxSpeed = (double)((float)var2);
        this.broadcast((Double)null, Double.valueOf(this.maxSpeed), (Double)null, (DataPoint[][])null);
      }
    }

  }

  public void addListener(TrackingListener var1) {
    this.listeners.add(var1);
  }

  public void addPoint(Location var1) {
    this.dBtrackingPoints.open();
    this.dBtrackingPoints.addLocation(var1);
    this.dBtrackingPoints.close();
    this.updateDisSpeed(var1);
    this.updateMaxSpeed(var1);
    this.startLocation = var1;
  }

  public double getAvgSpeed() {
    return this.avgSpeed;
  }

  public double getDistance() {
    return this.distance;
  }

  public double getDistanceKm() {
    return this.distance / 1000.0D;
  }

  public double getDurationInHours() {
    return (double)this.getDurationInMilliS() / 3600000.0D;
  }

  public long getDurationInMilliS() {
    return System.currentTimeMillis() - this.timeStart;
  }

  public double getMaxSpeed() {
    return this.maxSpeed;
  }

  public long getTimeStart() {
    return this.timeStart;
  }

  public int getTotalPoints() {
    this.dBtrackingPoints.open();
    int var1 = this.dBtrackingPoints.getRowCount();
    this.dBtrackingPoints.close();
    return var1;
  }

  public void init() {
    this.dBtrackingPoints.open();
    this.dBtrackingPoints.deleteAllRows();
    this.dBtrackingPoints.close();
    this.isOnTracking = false;
  }

  public boolean isTracking() {
    return this.isOnTracking;
  }

  public void removeListener(TrackingListener var1) {
    this.listeners.remove(var1);
  }

  public void requestDistanceGraphSeries() {
    (new AsyncTask<URL, Integer, DataPoint[][]>() {
      protected DataPoint[][] doInBackground(URL... var1) {
        try {
          Tracking.this.dBtrackingPoints.open();
          DataPoint[][] var3 = Tracking.this.dBtrackingPoints.getGraphSeries();
          Tracking.this.dBtrackingPoints.close();
          return var3;
        } catch (Exception var2) {
          var2.printStackTrace();
          return (DataPoint[][])null;
        }
      }

      protected void onPostExecute(DataPoint[][] var1) {
        super.onPostExecute(var1);
        Tracking.this.broadcast((Double)null, (Double)null, (Double)null, var1);
      }
    }).execute(new URL[0]);
  }

  public void saveAsGPX(final String var1) {
    final File var2 = new File(Variable.getVariable().getTrackingFolder().getAbsolutePath());
    var2.mkdirs();
    final File var3 = new File(var2, var1);
    if(!var3.exists()) {
      try {
        var3.createNewFile();
      } catch (IOException var5) {
        var5.printStackTrace();
      }
    }

    (new AsyncTask() {
      protected Object doInBackground(Object[] var1x) {
        try {
          (new GenerateGPX()).writeGpxFile(var1, Tracking.this.dBtrackingPoints, var3);
        } catch (IOException var2x) {
          var2x.printStackTrace();
        }

        return null;
      }

      protected void onPostExecute(Object var1x) {
        super.onPostExecute(var1x);
        if(var3.exists()) {
          var3.renameTo(new File(var2, var1 + ".gpx"));
        }

      }
    }).execute(new Object[0]);
  }

  public void setMaxSpeed(double var1) {
    this.maxSpeed = var1;
  }

  public void startTracking() {
    this.init();
    this.intAnalytics();
    MapHandler.getMapHandler().startTrack();
    this.isOnTracking = true;
  }

  public void stopTracking() {
    this.isOnTracking = false;
    this.intAnalytics();
    AppSettings.getAppSettings().updateAnalytics(0.0D, 0.0D);
  }
}