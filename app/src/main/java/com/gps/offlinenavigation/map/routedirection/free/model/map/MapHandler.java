package com.gps.offlinenavigation.map.routedirection.free.model.map;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Path;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import com.gps.offlinenavigation.map.routedirection.free.model.listeners.MapHandlerListener;
import com.gps.offlinenavigation.map.routedirection.free.model.util.Variable;
import com.graphhopper.*;
import com.graphhopper.routing.util.WeightingMap;
import com.graphhopper.util.PointList;
import com.graphhopper.util.StopWatch;
import java.io.File;
import java.util.List;
import org.mapsforge.core.graphics.*;
import org.mapsforge.core.model.*;
import org.mapsforge.map.android.graphics.AndroidGraphicFactory;
import org.mapsforge.map.android.util.AndroidUtil;
import org.mapsforge.map.android.view.MapView;
import org.mapsforge.map.layer.*;
import org.mapsforge.map.layer.cache.TileCache;
import org.mapsforge.map.layer.overlay.Marker;
import org.mapsforge.map.layer.overlay.Polyline;
import org.mapsforge.map.layer.renderer.TileRendererLayer;
import org.mapsforge.map.model.*;
import org.mapsforge.map.reader.MapDatabase;
import org.mapsforge.map.reader.header.MapFileInfo;
import org.mapsforge.map.rendertheme.InternalRenderTheme;

public class MapHandler
{

  private static MapHandler mapHandler;
  private Activity activity;
  private String currentArea;
  private Marker endMarker;
  private GraphHopper hopper;
  private MapHandlerListener mapHandlerListener;
  private MapView mapView;
  private File mapsFolder;
  private boolean needLocation;
  private boolean needPathCal;
  private Polyline polylinePath;
  private Polyline polylineTrack;
  private volatile boolean shortestPathRunning;
  private Marker startMarker;
  private TileCache tileCache;
  private PointList trackingPointList;

  private MapHandler()
  {
    try
    {
      setShortestPathRunning(false);
      startMarker = null;
      endMarker = null;
      polylinePath = null;
      needLocation = false;
      needPathCal = false;
      return;
    }
    catch(Exception exception)
    {
      exception.printStackTrace();
    }
  }

  public static MapHandler getMapHandler()
  {
    if(mapHandler == null)
    {
      reset();
    }
    return mapHandler;
  }

  private void loadGraphStorage()
  {
    try
    {
      (new AsyncTask() {

        String error = "";

        protected Path doInBackground(Void avoid[])
        {
          try
          {
            GraphHopper avoid_1 = (new GraphHopper()).forMobile();
            avoid_1.load((new File(mapsFolder, currentArea)).getAbsolutePath());
            hopper = avoid_1;
          }
          catch(Exception e)
          {
            error = (new StringBuilder()).append("error: ").append(e.getMessage()).toString();
          }
          return null;
        }

        protected Object doInBackground(Object aobj[])
        {
          return doInBackground((Void[])aobj);
        }

        protected void onPostExecute(Path path)
        {
          if(error != "")
          {
            logToast((new StringBuilder()).append("An error happened while creating graph:").append(error).toString());
          }
          Variable.getVariable().setPrepareInProgress(false);
        }

        protected void onPostExecute(Object obj)
        {
          onPostExecute((Path)obj);
        }
      }).execute(new Void[0]);
      return;
    }
    catch(Exception exception)
    {
      exception.printStackTrace();
    }
  }

  private void log(String s)
  {
    Log.i(getClass().getSimpleName(), (new StringBuilder()).append("-----------------").append(s).toString());
  }

  private void logToast(String s)
  {
    Toast.makeText(activity, s, Toast.LENGTH_LONG).show();
  }

  private boolean myOnTap(LatLong latlong, Point point, Point point1)
  {
    try {
      if (!isReady())
        return false;
      if (isShortestPathRunning() || !needLocation) {
        return false;
      }
      if (mapHandlerListener != null) {
        mapHandlerListener.onPressLocation(latlong);
      }
      needLocation = false;
      return true;
    }catch (Exception e) {
      e.printStackTrace();
      return false;
    }
  }

  public static void reset()
  {
    mapHandler = new MapHandler();
  }

  private void setShortestPathRunning(boolean flag)
  {
    shortestPathRunning = flag;
    if(mapHandlerListener != null && needPathCal)
    {
      mapHandlerListener.pathCalculating(flag);
    }
  }

  public void addEndMarker(LatLong latlong)
  {
    addMarkers(null, latlong);
  }

  public void addMarkers(LatLong latlong, LatLong latlong1)
  {
    Layers layers;
    try
    {
      layers = mapView.getLayerManager().getLayers();
    }
    catch(Exception e)
    {
      e.printStackTrace();
      return;
    }
    if(latlong != null)
    {
      removeLayer(layers, startMarker);
      startMarker = createMarker(latlong, 0x7f0200ae);
      layers.add(startMarker);
    }
    if(latlong1 != null)
    {
      removeLayer(layers, endMarker);
      endMarker = createMarker(latlong1, 0x7f0200ab);
      layers.add(endMarker);
    }
  }

  public void addStartMarker(LatLong latlong)
  {
    addMarkers(latlong, null);
  }

  public void addTrackPoint(LatLong latlong)
  {
    int i = mapView.getLayerManager().getLayers().indexOf(polylineTrack);
    ((Polyline)mapView.getLayerManager().getLayers().get(i)).getLatLongs().add(latlong);
  }

  public void calcPath(final double fromLat, final double fromLon, final double toLat, final double toLon)
  {
    try
    {
      removeLayer(mapView.getLayerManager().getLayers(), polylinePath);
      polylinePath = null;
      (new AsyncTask() {

        float time;

        protected GHResponse doInBackground(Void avoid[])
        {
          StopWatch avoid_1 = (new StopWatch()).start();
          GHRequest obj = new GHRequest(fromLat, fromLon, toLat, toLon);
          obj.setAlgorithm("dijkstrabi");
          obj.getHints().put("instructions", Variable.getVariable().getDirectionsON());
          obj.setVehicle(Variable.getVariable().getTravelMode());
          obj.setWeighting(Variable.getVariable().getWeighting());
          GHResponse obj_1 = hopper.route(obj);
          time = avoid_1.stop().getSeconds();
          return obj_1;
        }

        protected Object doInBackground(Object aobj[])
        {
          return doInBackground((Void[])aobj);
        }

        protected void onPostExecute(GHResponse ghresponse)
        {
          if(!ghresponse.hasErrors())
          {
            polylinePath = createPolyline(ghresponse.getPoints(), activity.getResources().getColor(0x7f0d004d), 20);
            mapView.getLayerManager().getLayers().add(polylinePath);
            if(Variable.getVariable().isDirectionsON())
            {
              Navigator.getNavigator().setGhResponse(ghresponse);
            }
          } else
          {
            logToast((new StringBuilder()).append("Error:").append(ghresponse.getErrors()).toString());
          }
          try
          {
            activity.findViewById(0x7f0e0131).setVisibility(View.GONE);
            activity.findViewById(0x7f0e0122).setVisibility(View.VISIBLE);
          }
          catch(Exception e)
          {
            e.getStackTrace();
          }
          setShortestPathRunning(false);
        }

        protected void onPostExecute(Object obj)
        {
          onPostExecute((GHResponse)obj);
        }

        protected void onPreExecute()
        {
          super.onPreExecute();
          setShortestPathRunning(true);
        }
      }).execute(new Void[0]);
      return;
    }
    catch(Exception exception)
    {
      exception.printStackTrace();
    }
  }

  public void centerPointOnMap(LatLong latlong, int i)
  {
    if(i == 0)
    {
      try
      {
        mapView.getModel().mapViewPosition.setMapPosition(new MapPosition(latlong, mapView.getModel().mapViewPosition.getZoomLevel()));
        return;
      }
      catch(Exception e)
      {
        e.printStackTrace();
      }
      return;
    }
    mapView.getModel().mapViewPosition.setMapPosition(new MapPosition(latlong, (byte)i));
  }

  public Marker createMarker(LatLong latlong, int i)
  {
    Bitmap bitmap = AndroidGraphicFactory.convertToBitmap(activity.getResources().getDrawable(i));
    return new Marker(latlong, bitmap, 0, -bitmap.getHeight() / 2);
  }

  public Polyline createPolyline(PointList pointlist, int i, int j)
  {
    Object obj = AndroidGraphicFactory.INSTANCE.createPaint();
    ((Paint) (obj)).setStyle(Style.STROKE);
    ((Paint) (obj)).setStrokeJoin(Join.ROUND);
    ((Paint) (obj)).setStrokeCap(Cap.ROUND);
    ((Paint) (obj)).setColor(i);
    ((Paint) (obj)).setStrokeWidth(j);
    obj = new Polyline(((Paint) (obj)), AndroidGraphicFactory.INSTANCE);
    List list = ((Polyline) (obj)).getLatLongs();
    for(i = 0; i < pointlist.getSize(); i++)
    {
      list.add(new LatLong(pointlist.getLatitude(i), pointlist.getLongitude(i)));
    }

    return ((Polyline) (obj));
  }

  public Activity getActivity()
  {
    return activity;
  }

  public GraphHopper getHopper()
  {
    return hopper;
  }

  public void init(Activity activity1, MapView mapview, String s, File file)
  {
    try
    {
      activity = activity1;
      mapView = mapview;
      currentArea = s;
      mapsFolder = file;
      tileCache = AndroidUtil.createTileCache(activity1, getClass().getSimpleName(), mapview.getModel().displayModel.getTileSize(), 1.0F, mapview.getModel().frameBufferModel.getOverdrawFactor());
    }
    catch(Exception e)
    {
      e.printStackTrace();
    }
  }

  public boolean isNeedLocation()
  {
    return needLocation;
  }

  boolean isReady()
  {
    boolean flag = false;
    if(hopper != null)
    {
      flag = true;
    } else
    if(Variable.getVariable().isPrepareInProgress())
    {
      return false;
    }
    return flag;
  }

  public boolean isShortestPathRunning()
  {
    return shortestPathRunning;
  }

  public void loadMap(File file)
  {
    TileRendererLayer tilerendererlayer;

    try
    {
      logToast((new StringBuilder()).append("Loading map: ").append(currentArea).toString());
      file = new File(file, (new StringBuilder()).append(currentArea).append(".map").toString());
      mapView.getLayerManager().getLayers().clear();
      tilerendererlayer = new TileRendererLayer(tileCache, mapView.getModel().mapViewPosition, false, true, AndroidGraphicFactory.INSTANCE) {
        public boolean onTap(LatLong latlong, Point point, Point point1)
        {
          return myOnTap(latlong, point, point1);
        }
      };
      tilerendererlayer.setMapFile(file);
      tilerendererlayer.setTextScale(0.8F);
      tilerendererlayer.setXmlRenderTheme(InternalRenderTheme.OSMARENDER);
      if(Variable.getVariable().getLastLocation() != null)
      {
        centerPointOnMap(Variable.getVariable().getLastLocation(), 15);
      } else
        centerPointOnMap(tilerendererlayer.getMapDatabase().getMapFileInfo().boundingBox.getCenterPoint(), 6);
      mapView.getLayerManager().getLayers().add(tilerendererlayer);
      android.view.ViewGroup.LayoutParams file_1 = new android.view.ViewGroup.LayoutParams(-1, -1);
      activity.addContentView(mapView, file_1);
      loadGraphStorage();
    }
    catch(Exception e)
    {
      e.printStackTrace();
    }
  }

  public void removeLayer(Layers layers, Layer layer)
  {
    try {
      if (layers == null || layer == null)
        return;
      if (layers.contains(layer))
        layers.remove(layer);
    }catch (Exception e){
      e.printStackTrace();
    }
  }

  public void removeMarkers()
  {
    try
    {
      Layers layers = mapView.getLayerManager().getLayers();
      if(startMarker != null)
      {
        removeLayer(layers, startMarker);
      }
      if(startMarker != null)
      {
        removeLayer(layers, endMarker);
      }
      if(polylinePath != null)
      {
        removeLayer(layers, polylinePath);
      }
      return;
    }
    catch(Exception exception)
    {
      exception.printStackTrace();
    }
  }

  public boolean saveTracking()
  {
    return false;
  }

  public void setHopper(GraphHopper graphhopper)
  {
    hopper = graphhopper;
  }

  public void setMapHandlerListener(MapHandlerListener maphandlerlistener)
  {
    mapHandlerListener = maphandlerlistener;
  }

  public void setNeedLocation(boolean flag)
  {
    needLocation = flag;
  }

  public void setNeedPathCal(boolean flag)
  {
    needPathCal = flag;
  }

  public void startTrack()
  {
    try
    {
      if(polylineTrack != null)
      {
        removeLayer(mapView.getLayerManager().getLayers(), polylineTrack);
      }
      polylineTrack = null;
      trackingPointList = new PointList();
      polylineTrack = createPolyline(trackingPointList, activity.getResources().getColor(0x7f0d0045), 25);
      mapView.getLayerManager().getLayers().add(polylineTrack);
      return;
    }
    catch(android.content.res.Resources.NotFoundException notfoundexception)
    {
      notfoundexception.printStackTrace();
    }
  }
}
