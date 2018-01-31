package com.gps.offlinenavigation.map.routedirection.free.model.util;

import android.content.Context;
import android.util.Log;
import com.gps.offlinenavigation.map.routedirection.free.model.dataType.MyMap;
import java.io.*;
import java.util.*;
import org.json.JSONException;
import org.json.JSONObject;
import org.mapsforge.core.model.LatLong;

public class Variable
{

  private static Variable variable;
  private boolean advancedSetting;
  private List cloudMaps;
  private Context context;
  private String country;
  private boolean directionsON;
  private int downloadStatus;
  private LatLong lastLocation;
  private int lastZoomLevel;
  private List localMaps;
  private String mapDirectory;
  private int mapFinishedPercentage;
  private String mapLastModified;
  private String mapUrlList;
  private File mapsFolder;
  private String pausedMapName;
  private volatile boolean prepareInProgress;
  private List recentDownloadedMaps;
  private String routingAlgorithms;
  private int sportCategoryIndex;
  private String trackingDirectory;
  private File trackingFolder;
  private String travelMode;
  private String weighting;
  private int zoomLevelMax;
  private int zoomLevelMin;

  private Variable()
  {
    travelMode = "foot";
    weighting = "fastest";
    routingAlgorithms = "astarbi";
    zoomLevelMax = 22;
    zoomLevelMin = 1;
    lastZoomLevel = 4;
    lastLocation = null;
    country = null;
    mapsFolder = null;
    context = null;
    advancedSetting = false;
    directionsON = true;
    mapDirectory = "/pocketmaps/maps/";
    trackingDirectory = "/pocketmaps/tracking/";
    mapUrlList = "http://folk.ntnu.no/junjung/pocketmaps/map_url_list";
    localMaps = new ArrayList();
    recentDownloadedMaps = new ArrayList();
    cloudMaps = new ArrayList();
    sportCategoryIndex = 0;
    resetDownloadMapVariables();
  }

  public static Variable getVariable()
  {
    if(variable == null)
    {
      variable = new Variable();
    }
    return variable;
  }

  private boolean hasUnfinishedDownload()
  {
    boolean flag1 = false;
    String as[] = getMapsFolder().list(new FilenameFilter() {
      public boolean accept(File file, String s1)
      {
        return s1 != null && s1.endsWith(".ghz");
      }
    });
    String as1[] = getMapsFolder().list(new FilenameFilter() {
      public boolean accept(File file, String s1)
      {
        return s1 != null && s1.endsWith("-gh");
      }
    });
    int k = as.length;
    int i = 0;
    do
    {
      String s;
      label0:
      {
        boolean flag = flag1;
        if(i < k)
        {
          s = as[i];
          int l = as1.length;
          for(int j = 0; j < l; j++)
          {
            if(as1[j].contains(s.replace(".ghz", "")))
            {
              (new File(getMapsFolder(), s)).delete();
            }
          }

          getVariable().addLocalMap(new MyMap(s));
          if(!s.contains(getPausedMapName()))
          {
            break label0;
          }
          flag = true;
        }
        return flag;
      }
      (new File(getMapsFolder(), s)).delete();
      i++;
    } while(true);
  }

  private void log(String s)
  {
    Log.i(getClass().getSimpleName(), (new StringBuilder()).append("-------").append(s).toString());
  }

  public void addLocalMap(MyMap mymap)
  {
    if(!getLocalMapNameList().contains(mymap.getMapName()))
    {
      localMaps.add(mymap);
    }
  }

  public void addLocalMaps(List list)
  {
    localMaps.addAll(list);
  }

  public void addRecentDownloadedMap(MyMap mymap)
  {
    recentDownloadedMaps.add(mymap);
  }

  public List getCloudMaps()
  {
    return cloudMaps;
  }

  public Context getContext()
  {
    return context;
  }

  public String getCountry()
  {
    return country;
  }

  public String getDirectionsON()
  {
    if(isDirectionsON())
    {
      return "true";
    } else
    {
      return "false";
    }
  }

  public int getDownloadStatus()
  {
    return downloadStatus;
  }

  public LatLong getLastLocation()
  {
    return lastLocation;
  }

  public int getLastZoomLevel()
  {
    return lastZoomLevel;
  }

  public List getLocalMapNameList()
  {
    ArrayList arraylist = new ArrayList();
    for(Iterator iterator = getLocalMaps().iterator(); iterator.hasNext(); arraylist.add(((MyMap)iterator.next()).getMapName())) { }
    return arraylist;
  }

  public List getLocalMaps()
  {
    return localMaps;
  }

  public String getMapDirectory()
  {
    return mapDirectory;
  }

  public int getMapFinishedPercentage()
  {
    return mapFinishedPercentage;
  }

  public String getMapLastModified()
  {
    return mapLastModified;
  }

  public String getMapUrlList()
  {
    return mapUrlList;
  }

  public File getMapsFolder()
  {
    return mapsFolder;
  }

  public String getPausedMapName()
  {
    return pausedMapName;
  }

  public List getRecentDownloadedMaps()
  {
    return recentDownloadedMaps;
  }

  public String getRoutingAlgorithms()
  {
    return routingAlgorithms;
  }

  public int getSportCategoryIndex()
  {
    return sportCategoryIndex;
  }

  public String getTrackingDirectory()
  {
    return trackingDirectory;
  }

  public File getTrackingFolder()
  {
    return trackingFolder;
  }

  public String getTravelMode()
  {
    return travelMode;
  }

  public String getWeighting()
  {
    return weighting;
  }

  public int getZoomLevelMax()
  {
    return zoomLevelMax;
  }

  public int getZoomLevelMin()
  {
    return zoomLevelMin;
  }

  public boolean isAdvancedSetting()
  {
    return advancedSetting;
  }

  public boolean isDirectionsON()
  {
    return directionsON;
  }

  public boolean isPrepareInProgress()
  {
    return prepareInProgress;
  }

  public boolean loadVariables()
  {
    String obj_1 = readFile();
    if(obj_1 == null)
      return false;
    boolean flag1 = false;
    boolean flag = false;
    double d;
    double d1;
    try
    {
      JSONObject obj = new JSONObject(obj_1);
      setTravelMode(obj.getString("travelMode"));
      setWeighting(obj.getString("weighting"));
      setRoutingAlgorithms(obj.getString("routingAlgorithms"));
      setDirectionsON(obj.getBoolean("directionsON"));
      setAdvancedSetting(obj.getBoolean("advancedSetting"));
      setZoomLevelMax(obj.getInt("zoomLevelMax"));
      setZoomLevelMin(obj.getInt("zoomLevelMin"));
      setLastZoomLevel(obj.getInt("lastZoomLevel"));
      d = obj.getDouble("latitude");
      d1 = obj.getDouble("longitude");
      if(!(d == 0.0D || d1 == 0.0D))
        setLastLocation(new LatLong(d, d1));
      if(obj.getString("country") != "")
      {
        setCountry(obj.getString("country"));
        flag = true;
      }
      setMapDirectory(obj.getString("mapDirectory"));
      setMapsFolder(new File(obj.getString("mapsFolderAbsPath")));
      setSportCategoryIndex(obj.getInt("sportCategoryIndex"));
      setDownloadStatus(obj.getInt("mapDownloadStatus"));
      setMapLastModified(obj.getString("mapLastModified"));
      setMapFinishedPercentage(obj.getInt("mapFinishedPercentage"));
      setPausedMapName(obj.getString("pausedMapName"));
      if(getPausedMapName() != "")
      {
        flag = false;
      }
      flag1 = flag;
      if(hasUnfinishedDownload())
        return flag1;
      resetDownloadMapVariables();
      return flag;
    }
    catch(JSONException jsonexception)
    {
      jsonexception.printStackTrace();
      return false;
    }
  }

  public String readFile()
  {
    BufferedReader bufferedreader;
    StringBuilder stringbuilder;
    try
    {
      bufferedreader = new BufferedReader(new InputStreamReader(context.openFileInput("pocketmapssavedfile.txt")));
      stringbuilder = new StringBuilder();
      String s;
      while(true) {
        String s1 = bufferedreader.readLine();
        if (s1 == null) {
          s = stringbuilder.toString();
          return s;
        }
        stringbuilder.append(s1);
      }
    }
    catch(FileNotFoundException filenotfoundexception)
    {
      filenotfoundexception.printStackTrace();
      return null;
    }
    catch(IOException ioexception)
    {
      ioexception.printStackTrace();
      return null;
    }
    catch(Exception exception)
    {
      exception.getStackTrace();
      return null;
    }
  }

  public void removeLocalMap(MyMap mymap)
  {
    localMaps.remove(mymap);
  }

  public MyMap removeRecentDownloadedMap(int i)
          throws Exception
  {
    return (MyMap)recentDownloadedMaps.remove(i);
  }

  public void resetDownloadMapVariables()
  {
    downloadStatus = 3;
    pausedMapName = "";
    mapLastModified = "";
    mapFinishedPercentage = -1;
  }

  public boolean saveStringToFile(String s)
  {
    try
    {
      FileOutputStream fileoutputstream = context.openFileOutput("pocketmapssavedfile.txt", 0);
      fileoutputstream.write(s.getBytes());
      fileoutputstream.close();
    }
    catch(Exception e)
    {
      e.printStackTrace();
      return false;
    }
    return true;
  }

  public boolean saveVariables()
  {
    JSONObject obj = new JSONObject();
    try
    {
      obj.put("travelMode", getTravelMode());
      obj.put("weighting", getWeighting());
      obj.put("routingAlgorithms", getRoutingAlgorithms());
      obj.put("advancedSetting", isAdvancedSetting());
      obj.put("directionsON", isDirectionsON());
      obj.put("zoomLevelMax", getZoomLevelMax());
      obj.put("zoomLevelMin", getZoomLevelMin());
      obj.put("lastZoomLevel", getLastZoomLevel());
      if(getLastLocation() == null) {
        obj.put("latitude", 0);
        obj.put("longitude", 0);
      } else {
        obj.put("latitude", getLastLocation().latitude);
        obj.put("longitude", getLastLocation().longitude);
      }
      if(getCountry() != null)
        obj.put("country", getCountry());
      else
        obj.put("country", "");
      obj.put("mapDirectory", getMapDirectory());
      obj.put("mapsFolderAbsPath", getMapsFolder().getAbsolutePath());
      obj.put("sportCategoryIndex", getSportCategoryIndex());
      obj.put("mapDownloadStatus", getDownloadStatus());
      obj.put("mapLastModified", getMapLastModified());
      obj.put("mapFinishedPercentage", getMapFinishedPercentage());
      obj.put("pausedMapName", getPausedMapName());
      return saveStringToFile(obj.toString());
    }
    catch(Exception e)
    {
      e.printStackTrace();
      return false;
    }
  }

  public void setAdvancedSetting(boolean flag)
  {
    advancedSetting = flag;
  }

  public void setCloudMaps(List list)
  {
    cloudMaps = list;
  }

  public void setContext(Context context1)
  {
    context = context1;
  }

  public void setCountry(String s)
  {
    country = s;
  }

  public void setDirectionsON(boolean flag)
  {
    directionsON = flag;
  }

  public void setDownloadStatus(int i)
  {
    downloadStatus = i;
  }

  public void setLastLocation(LatLong latlong)
  {
    lastLocation = latlong;
  }

  public void setLastZoomLevel(int i)
  {
    lastZoomLevel = i;
  }

  public void setLocalMaps(List list)
  {
    localMaps = list;
  }

  public void setMapDirectory(String s)
  {
    mapDirectory = s;
  }

  public void setMapFinishedPercentage(int i)
  {
    mapFinishedPercentage = i;
  }

  public void setMapLastModified(String s)
  {
    mapLastModified = s;
  }

  public void setMapsFolder(File file)
  {
    mapsFolder = file;
  }

  public void setPausedMapName(String s)
  {
    pausedMapName = s;
  }

  public void setPrepareInProgress(boolean flag)
  {
    prepareInProgress = flag;
  }

  public void setRecentDownloadedMaps(List list)
  {
    recentDownloadedMaps = list;
  }

  public void setRoutingAlgorithms(String s)
  {
    routingAlgorithms = s;
  }

  public void setSportCategoryIndex(int i)
  {
    sportCategoryIndex = i;
  }

  public void setTrackingFolder(File file)
  {
    trackingFolder = file;
  }

  public void setTravelMode(String s)
  {
    travelMode = s;
  }

  public void setWeighting(String s)
  {
    weighting = s;
  }

  public void setZoomLevelMax(int i)
  {
    zoomLevelMax = i;
  }

  public void setZoomLevelMin(int i)
  {
    zoomLevelMin = i;
  }

  public void setZoomLevels(int i, int j)
  {
    setZoomLevelMax(i);
    setZoomLevelMin(j);
  }
}
