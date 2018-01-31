package com.gps.offlinenavigation.map.routedirection.free.model.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

public class DBhelper extends SQLiteOpenHelper {
  public static final String DB_NAME = "pocketmaps.db";
  public static final int DB_VERSION = 1;
  public final String COLUMN_ALTITUDE = "altitude";
  public final String COLUMN_DATETIME = "datetime";
  public final String COLUMN_LATITUDE = "latitude";
  public final String COLUMN_LONGITUDE = "longitude";
  public final String COMMA_SEP = ",";
  private final String CREATE_TRACK_TABLE = "CREATE TABLE tracking(datetime NUMERIC,longitude REAL,latitude REAL,altitude REAL,PRIMARY KEY (datetime,longitude,latitude) )";
  public final String DELETE_TRACK_TABLE = "DROP TABLE IF EXISTS tracking";
  public final String NUMERIC_TYPE = " NUMERIC";
  public final String PRIMARY_KEY = "PRIMARY KEY (datetime,longitude,latitude)";
  public final String REAL_TYPE = " REAL";
  public final String TABLE_NAME = "tracking";

  public DBhelper(Context var1) {
    super(var1, "pocketmaps.db", (CursorFactory)null, 1);
  }

  public void onCreate(SQLiteDatabase var1) {
    var1.execSQL("CREATE TABLE tracking(datetime NUMERIC,longitude REAL,latitude REAL,altitude REAL,PRIMARY KEY (datetime,longitude,latitude) )");
  }

  public void onUpgrade(SQLiteDatabase var1, int var2, int var3) {
    var1.execSQL("DROP TABLE IF EXISTS tracking");
    this.onCreate(var1);
  }
}