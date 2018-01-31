package com.gps.offlinenavigation.map.routedirection.free.model.util;

import android.database.Cursor;
import android.util.Log;
import com.gps.offlinenavigation.map.routedirection.free.model.database.DBhelper;
import com.gps.offlinenavigation.map.routedirection.free.model.database.DBtrackingPoints;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class GenerateGPX {
  private final SimpleDateFormat DF = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
  private final String TAG_GPX = "<gpx version=\"1.1\" creator=\"PocketMaps by JunjunGuo.com - http://junjunguo.com/PocketMaps/\" xmlns=\"http://www.topografix.com/GPX/1/1\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://www.topografix.com/GPX/1/1 http://www.topografix.com/GPX/1/1/gpx.xsd \">";
  private final String XML_HEADER = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>";

  public GenerateGPX() {
  }

  private void log(String var1) {
    Log.i(this.getClass().getSimpleName(), "---GPX--- " + var1);
  }

  public void writeGpxFile(String var1, DBtrackingPoints var2, File var3) throws IOException {
    String var4 = "  <metadata>\n    <link href=\"http://JunjunGuo.com/PocketMaps\">\n      <text>Pocket Maps: Free offline maps with routing functions and more</text>\n    </link>\n    <time>" + this.DF.format(Long.valueOf(System.currentTimeMillis())) + "</time>\n  </metadata>";
    if(!var3.exists()) {
      var3.createNewFile();
    }

    FileWriter var5 = new FileWriter(var3);
    var5.write("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n");
    var5.write("<gpx version=\"1.1\" creator=\"PocketMaps by JunjunGuo.com - http://junjunguo.com/PocketMaps/\" xmlns=\"http://www.topografix.com/GPX/1/1\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://www.topografix.com/GPX/1/1 http://www.topografix.com/GPX/1/1/gpx.xsd \">\n");
    var5.write(var4 + "\n");
    this.writeTrackPoints(var1, var5, var2);
    var5.write("</gpx>");
    var5.close();
  }

  public void writeTrackPoints(String var1, FileWriter var2, DBtrackingPoints var3) throws IOException {
    var3.open();
    Cursor var8 = var3.getCursor();
    DBhelper var4 = var3.getDbHelper();
    var2.write("\t<trk>\n");
    var2.write("\t\t<name>PocketMaps GPS track log</name>\n");
    var2.write("\t\t<trkseg>\n");

    while(!var8.isAfterLast()) {
      StringBuffer var5 = new StringBuffer();
      StringBuilder var6 = (new StringBuilder()).append("\t\t\t<trkpt lat=\"");
      var4.getClass();
      var6 = var6.append(var8.getDouble(var8.getColumnIndex("latitude"))).append("\" lon=\"");
      var4.getClass();
      var5.append(var6.append(var8.getDouble(var8.getColumnIndex("longitude"))).append("\">").toString());
      var6 = (new StringBuilder()).append("<ele>");
      var4.getClass();
      var5.append(var6.append(var8.getDouble(var8.getColumnIndex("altitude"))).append("</ele>").toString());
      var6 = (new StringBuilder()).append("<time>");
      SimpleDateFormat var7 = this.DF;
      var4.getClass();
      var5.append(var6.append(var7.format(new Date(var8.getLong(var8.getColumnIndex("datetime"))))).append("</time>").toString());
      var5.append("</trkpt>\n");
      var2.write(var5.toString());
      var8.moveToNext();
    }

    var3.close();
    var2.write("\t\t</trkseg>\n");
    var2.write("\t</trk>\n");
  }
}