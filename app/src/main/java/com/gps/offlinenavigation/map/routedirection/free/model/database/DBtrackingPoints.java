package com.gps.offlinenavigation.map.routedirection.free.model.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.util.Log;
import com.gps.offlinenavigation.map.routedirection.free.model.map.Tracking;
import com.jjoe64.graphview.series.DataPoint;

public class DBtrackingPoints {
  private SQLiteDatabase database;
  private DBhelper dbHelper;

  public DBtrackingPoints(Context var1) {
    this.dbHelper = new DBhelper(var1);
    this.open();
  }

  private void log(String var1) {
    Log.i(this.getClass().getSimpleName(), "-----------------" + var1);
  }

  public long addLocation(Location var1) {
    ContentValues var4 = new ContentValues();
    this.dbHelper.getClass();
    var4.put("datetime", Long.valueOf(var1.getTime()));
    this.dbHelper.getClass();
    var4.put("longitude", Double.valueOf(var1.getLongitude()));
    this.dbHelper.getClass();
    var4.put("latitude", Double.valueOf(var1.getLatitude()));
    this.dbHelper.getClass();
    var4.put("altitude", Double.valueOf(var1.getAltitude()));
    SQLiteDatabase var5 = this.database;
    this.dbHelper.getClass();
    long var2 = var5.insert("tracking", (String)null, var4);
    if(var2 >= 0L) {
      ;
    }

    return var2;
  }

  public void close() {
    this.dbHelper.close();
    this.database.close();
  }

  public int deleteAllRows() {
    SQLiteDatabase var1 = this.database;
    this.dbHelper.getClass();
    return var1.delete("tracking", "1", (String[])null);
  }

  public Cursor getCursor() {
    SQLiteDatabase var1 = this.database;
    this.dbHelper.getClass();
    StringBuilder var2 = new StringBuilder();
    this.dbHelper.getClass();
    Cursor var3 = var1.query("tracking", (String[])null, (String)null, (String[])null, (String)null, (String)null, var2.append("datetime").append(" ASC").toString());
    var3.moveToFirst();
    return var3;
  }

  public DBhelper getDbHelper() {
    return this.dbHelper;
  }

  public DataPoint[][] getGraphSeries() {
    int var6 = this.getRowCount();
    if(var6 > 2) {
      long var11 = Tracking.getTracking().getTimeStart();
      long var7 = 0L;
      double var13 = 0.0D;
      Location var1 = null;
      DataPoint[] var3 = new DataPoint[var6];
      DataPoint[] var4 = new DataPoint[var6];
      var3[0] = new DataPoint(0.0D, 0.0D);
      var4[0] = new DataPoint(0.0D, 0.0D);
      SQLiteDatabase var2 = this.database;
      this.dbHelper.getClass();
      StringBuilder var5 = new StringBuilder();
      this.dbHelper.getClass();
      Cursor var22 = var2.query("tracking", (String[])null, (String)null, (String[])null, (String)null, (String)null, var5.append("datetime").append(" ASC").toString());
      var22.moveToFirst();
      var6 = 1;

      while(!var22.isAfterLast()) {
        this.dbHelper.getClass();
        double var17 = var22.getDouble(var22.getColumnIndex("longitude"));
        this.dbHelper.getClass();
        double var19 = var22.getDouble(var22.getColumnIndex("latitude"));
        this.dbHelper.getClass();
        long var9 = var22.getLong(var22.getColumnIndex("datetime"));
        double var15 = (double)(var9 - var11) / 3600000.0D;
        if(var1 == null) {
          var1 = new Location("");
          var1.setLatitude(var19);
          var1.setLongitude(var17);
        } else {
          Location var21 = new Location("");
          var21.setLatitude(var19);
          var21.setLongitude(var17);
          var17 = (double)var1.distanceTo(var21) / 1000.0D;
          var13 += var17;
          var3[var6] = new DataPoint(var15, var13);
          var4[var6] = new DataPoint(var15, var17 / ((double)(var9 - var7) / 3600000.0D));
          var1 = var21;
          ++var6;
        }

        var7 = var9;
        var22.moveToNext();
      }

      var22.close();
      return new DataPoint[][]{var4, var3};
    } else {
      return (DataPoint[][])null;
    }
  }

  public int getRowCount() {
    StringBuilder var2 = (new StringBuilder()).append("SELECT  * FROM ");
    this.dbHelper.getClass();
    String var3 = var2.append("tracking").toString();
    Cursor var4 = this.database.rawQuery(var3, (String[])null);
    int var1 = var4.getCount();
    var4.close();
    return var1;
  }

  public void open() {
    this.database = this.dbHelper.getWritableDatabase();
  }
}