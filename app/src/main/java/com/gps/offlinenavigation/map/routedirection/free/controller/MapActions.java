package com.gps.offlinenavigation.map.routedirection.free.controller;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.location.Location;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.*;
import android.view.animation.OvershootInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.*;
import com.gps.offlinenavigation.map.routedirection.free.model.dataType.Destination;
import com.gps.offlinenavigation.map.routedirection.free.model.listeners.MapHandlerListener;
import com.gps.offlinenavigation.map.routedirection.free.model.listeners.NavigatorListener;
import com.gps.offlinenavigation.map.routedirection.free.model.map.MapHandler;
import com.gps.offlinenavigation.map.routedirection.free.model.map.Navigator;
import com.gps.offlinenavigation.map.routedirection.free.model.util.*;
import com.graphhopper.GHResponse;
import org.mapsforge.core.model.LatLong;
import org.mapsforge.map.android.view.MapView;
import org.mapsforge.map.model.MapViewPosition;
import org.mapsforge.map.model.Model;

// Referenced classes of package com.gps.offlinenavigation.map.routedirection.free.controller:
//            AppSettings, MapActivity

public class MapActions
        implements NavigatorListener, MapHandlerListener
{

  private Activity activity;
  protected FloatingActionButton controlBtn;
  private EditText fromLocalET;
  private boolean menuVisible;
  private ViewGroup navInstructionListVP;
  private ViewGroup navInstructionVP;
  private ViewGroup navSettingsFromVP;
  private ViewGroup navSettingsToVP;
  private ViewGroup navSettingsVP;
  protected FloatingActionButton navigationBtn;
  private boolean onStartPoint;
  protected FloatingActionButton settingsBtn;
  protected FloatingActionButton showPositionBtn;
  private ViewGroup sideBarMenuVP;
  private ViewGroup sideBarVP;
  private EditText toLocalET;
  protected FloatingActionButton zoomInBtn;
  protected FloatingActionButton zoomOutBtn;

  public MapActions(Activity activity1, MapView mapview)
  {
    try
    {
      activity = activity1;
      showPositionBtn = (FloatingActionButton)activity1.findViewById(0x7f0e00a5);
      navigationBtn = (FloatingActionButton)activity1.findViewById(0x7f0e0146);
      settingsBtn = (FloatingActionButton)activity1.findViewById(0x7f0e0143);
      controlBtn = (FloatingActionButton)activity1.findViewById(0x7f0e0147);
      zoomInBtn = (FloatingActionButton)activity1.findViewById(0x7f0e0145);
      zoomOutBtn = (FloatingActionButton)activity1.findViewById(0x7f0e0144);
      sideBarVP = (ViewGroup)activity1.findViewById(0x7f0e0141);
      sideBarMenuVP = (ViewGroup)activity1.findViewById(0x7f0e0142);
      navSettingsVP = (ViewGroup)activity1.findViewById(0x7f0e0122);
      navSettingsFromVP = (ViewGroup)activity1.findViewById(0x7f0e0132);
      navSettingsToVP = (ViewGroup)activity1.findViewById(0x7f0e013a);
      navInstructionListVP = (ViewGroup)activity1.findViewById(0x7f0e0110);
      fromLocalET = (EditText)activity1.findViewById(0x7f0e012c);
      toLocalET = (EditText)activity1.findViewById(0x7f0e0130);
      menuVisible = false;
      onStartPoint = true;
      MapHandler.getMapHandler().setMapHandlerListener(this);
      Navigator.getNavigator().addListener(this);
      controlBtnHandler();
      zoomControlHandler(mapview);
      showMyLocation(mapview);
      navBtnHandler();
      navSettingsHandler();
      settingsBtnHandler();
      return;
    }
    // Misplaced declaration of an exception variable
    catch(Activity activity1)
    {
      activity1.printStackTrace();
    }
  }

  private void activeDirections()
  {
    try
    {
      RecyclerView recyclerview = (RecyclerView)activity.findViewById(0x7f0e0121);
      recyclerview.setHasFixedSize(true);
      recyclerview.setLayoutManager(new LinearLayoutManager(activity));
      recyclerview.setAdapter(new InstructionAdapter(Navigator.getNavigator().getGhResponse().getInstructions()));
      initNavListView();
      return;
    }
    catch(Exception exception)
    {
      exception.printStackTrace();
    }
  }

  private void activeNavigator()
  {
    LatLong latlong;
    LatLong latlong1;
    Object obj;
    try
    {
      latlong = Destination.getDestination().getStartPoint();
      latlong1 = Destination.getDestination().getEndPoint();
    }
    catch(Exception exception)
    {
      exception.printStackTrace();
      return;
    }
    if(latlong == null || latlong1 == null)
    {
      break MISSING_BLOCK_LABEL_88;
    }
    navSettingsVP.setVisibility(View.INVISIBLE);
    obj = activity.findViewById(0x7f0e0131);
    ((View) (obj)).setVisibility(View.VISIBLE);
    ((View) (obj)).bringToFront();
    obj = MapHandler.getMapHandler();
    ((MapHandler) (obj)).calcPath(latlong.latitude, latlong.longitude, latlong1.latitude, latlong1.longitude);
    if(Variable.getVariable().isDirectionsON())
    {
      ((MapHandler) (obj)).setNeedPathCal(true);
    }
  }

  private void addFromMarker(LatLong latlong)
  {
    MapHandler.getMapHandler().addStartMarker(latlong);
  }

  private void addToMarker(LatLong latlong)
  {
    MapHandler.getMapHandler().addEndMarker(latlong);
  }

  private void chooseFromFavoriteHandler()
  {
    final ViewGroup chooseFavorite = (ViewGroup)activity.findViewById(0x7f0e0137);
    chooseFavorite.setOnTouchListener(new android.view.View.OnTouchListener() {

      final MapActions this$0;
      final ViewGroup val$chooseFavorite;

      public boolean onTouch(View view, MotionEvent motionevent)
      {
        switch(motionevent.getAction())
        {
          default:
            return false;

          case 0: // '\0'
            chooseFavorite.setBackgroundColor(activity.getResources().getColor(0x7f0d004e));
            return true;

          case 1: // '\001'
            chooseFavorite.setBackgroundColor(activity.getResources().getColor(0x7f0d004b));
            break;
        }
        return true;
      }


      {
        this$0 = MapActions.this;
        chooseFavorite = viewgroup;
        super();
      }
    });
  }

  private void controlBtnHandler()
  {
    final ScaleAnimation anim = new ScaleAnimation(0.0F, 1.0F, 0.0F, 1.0F);
    anim.setFillBefore(true);
    anim.setFillAfter(true);
    anim.setFillEnabled(true);
    anim.setDuration(300L);
    anim.setInterpolator(new OvershootInterpolator());
    controlBtn.setOnClickListener(new android.view.View.OnClickListener() {

      final MapActions this$0;
      final ScaleAnimation val$anim;

      public void onClick(View view)
      {
        if(isMenuVisible())
        {
          setMenuVisible(false);
          sideBarMenuVP.setVisibility(View.INVISIBLE);
          controlBtn.setImageResource(0x7f0200aa);
          controlBtn.startAnimation(anim);
          return;
        } else
        {
          setMenuVisible(true);
          sideBarMenuVP.setVisibility(View.VISIBLE);
          controlBtn.setImageResource(0x7f0200a8);
          controlBtn.startAnimation(anim);
          return;
        }
      }


      {
        this$0 = MapActions.this;
        anim = scaleanimation;
        super();
      }
    });
  }

  private void fillNavListSummaryValues()
  {
    try
    {
      ((ImageView)activity.findViewById(0x7f0e0113)).setImageResource(Navigator.getNavigator().getTravelModeResId(true));
      TextView textview = (TextView)activity.findViewById(0x7f0e0117);
      TextView textview1 = (TextView)activity.findViewById(0x7f0e0119);
      TextView textview2 = (TextView)activity.findViewById(0x7f0e011d);
      TextView textview3 = (TextView)activity.findViewById(0x7f0e011f);
      textview.setText(Destination.getDestination().getStartPointToString());
      textview1.setText(Destination.getDestination().getEndPointToString());
      textview2.setText(Navigator.getNavigator().getDistance());
      textview3.setText(Navigator.getNavigator().getTime());
      return;
    }
    catch(Exception exception)
    {
      exception.printStackTrace();
    }
  }

  private void initNavListView()
  {
    try
    {
      fillNavListSummaryValues();
      navSettingsVP.setVisibility(View.INVISIBLE);
      navInstructionListVP.setVisibility(View.VISIBLE);
      ImageButton imagebutton = (ImageButton)activity.findViewById(0x7f0e0115);
      ImageButton imagebutton1 = (ImageButton)activity.findViewById(0x7f0e0114);
      imagebutton.setOnClickListener(new android.view.View.OnClickListener() {

        final MapActions this$0;

        public void onClick(View view)
        {
          view = new android.support.v7.app.AlertDialog.Builder(activity);
          view.setMessage(0x7f08006e).setTitle(0x7f08006d).setPositiveButton(0x7f080059, new android.content.DialogInterface.OnClickListener() {

            final _cls14 this$1;

            public void onClick(DialogInterface dialoginterface, int i)
            {
              Navigator.getNavigator().setOn(false);
              removeNavigation();
              navInstructionListVP.setVisibility(View.INVISIBLE);
              navSettingsVP.setVisibility(View.VISIBLE);
              dialoginterface.dismiss();
            }


            {
              this$1 = _cls14.this;
              super();
            }
          }).setNegativeButton(0x7f080047, new android.content.DialogInterface.OnClickListener() {

            final _cls14 this$1;

            public void onClick(DialogInterface dialoginterface, int i)
            {
              dialoginterface.dismiss();
            }


            {
              this$1 = _cls14.this;
              super();
            }
          });
          view.create().show();
        }


        {
          this$0 = MapActions.this;
          super();
        }
      });
      imagebutton1.setOnClickListener(new android.view.View.OnClickListener() {

        final MapActions this$0;

        public void onClick(View view)
        {
          navInstructionListVP.setVisibility(View.INVISIBLE);
          sideBarVP.setVisibility(View.VISIBLE);
        }


        {
          this$0 = MapActions.this;
          super();
        }
      });
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

  private void navBtnHandler()
  {
    navigationBtn.setOnClickListener(new android.view.View.OnClickListener() {

      final MapActions this$0;

      public void onClick(View view)
      {
        sideBarVP.setVisibility(View.INVISIBLE);
        if(Navigator.getNavigator().isOn())
        {
          navInstructionListVP.setVisibility(View.VISIBLE);
          return;
        } else
        {
          navSettingsVP.setVisibility(View.VISIBLE);
          return;
        }
      }


      {
        this$0 = MapActions.this;
        super();
      }
    });
  }

  private void navSettingsHandler()
  {
    try
    {
      ((ImageButton)activity.findViewById(0x7f0e0127)).setOnClickListener(new android.view.View.OnClickListener() {

        final MapActions this$0;

        public void onClick(View view)
        {
          try
          {
            navSettingsVP.setVisibility(View.INVISIBLE);
            sideBarVP.setVisibility(View.VISIBLE);
            return;
          }
          // Misplaced declaration of an exception variable
          catch(View view)
          {
            view.printStackTrace();
          }
        }


        {
          this$0 = MapActions.this;
          super();
        }
      });
      ((ImageButton)activity.findViewById(0x7f0e0128)).setOnClickListener(new android.view.View.OnClickListener() {

        final MapActions this$0;

        public void onClick(View view)
        {
          try
          {
            searchBtnActions();
            return;
          }
          // Misplaced declaration of an exception variable
          catch(View view)
          {
            view.printStackTrace();
          }
        }


        {
          this$0 = MapActions.this;
          super();
        }
      });
      travelModeSetting();
      settingsFromItemHandler();
      settingsToItemHandler();
      return;
    }
    catch(Exception exception)
    {
      exception.printStackTrace();
    }
  }

  private void pointOnMapHandler()
  {
    try
    {
      final ViewGroup pointItem = (ViewGroup)activity.findViewById(0x7f0e0138);
      pointItem.setOnTouchListener(new android.view.View.OnTouchListener() {

        final MapActions this$0;
        final ViewGroup val$pointItem;

        public boolean onTouch(View view, MotionEvent motionevent)
        {
          switch(motionevent.getAction())
          {
            default:
              return false;

            case 0: // '\0'
              try
              {
                pointItem.setBackgroundColor(activity.getResources().getColor(0x7f0d004e));
              }
              // Misplaced declaration of an exception variable
              catch(View view)
              {
                view.printStackTrace();
                return true;
              }
              return true;

            case 1: // '\001'
              break;
          }
          try
          {
            pointItem.setBackgroundColor(activity.getResources().getColor(0x7f0d004b));
            onStartPoint = true;
            navSettingsFromVP.setVisibility(View.INVISIBLE);
            Toast.makeText(activity, "Touch on Map to choose your start Location", 0).show();
            MapHandler.getMapHandler().setNeedLocation(true);
          }
          // Misplaced declaration of an exception variable
          catch(View view)
          {
            view.printStackTrace();
            return true;
          }
          return true;
        }


        {
          this$0 = MapActions.this;
          pointItem = viewgroup;
          super();
        }
      });
      return;
    }
    catch(Exception exception)
    {
      exception.printStackTrace();
    }
  }

  private void removeNavigation()
  {
    try
    {
      MapHandler.getMapHandler().removeMarkers();
      fromLocalET.setText("");
      toLocalET.setText("");
      Navigator.getNavigator().setOn(false);
      Destination.getDestination().setStartPoint(null);
      Destination.getDestination().setEndPoint(null);
      return;
    }
    catch(Exception exception)
    {
      exception.printStackTrace();
    }
  }

  private void searchBtnActions()
  {
    LatLong latlong;
    LatLong latlong1;
    String s;
    String s1;
    try
    {
      s1 = fromLocalET.getText().toString();
      s = toLocalET.getText().toString();
    }
    catch(Exception exception)
    {
      exception.printStackTrace();
      return;
    }
    latlong = null;
    latlong1 = null;
    if(s1.length() > 2)
    {
      latlong = MyUtility.getLatLong(s1);
    }
    if(s.length() > 2)
    {
      latlong1 = MyUtility.getLatLong(s);
    }
    if(latlong == null || latlong1 != null)
    {
      break MISSING_BLOCK_LABEL_76;
    }
    MapHandler.getMapHandler().centerPointOnMap(latlong, 0);
    addFromMarker(latlong);
    if(latlong != null || latlong1 == null)
    {
      break MISSING_BLOCK_LABEL_97;
    }
    MapHandler.getMapHandler().centerPointOnMap(latlong1, 0);
    addToMarker(latlong1);
    if(latlong == null || latlong1 == null)
    {
      break MISSING_BLOCK_LABEL_133;
    }
    addFromMarker(latlong);
    addToMarker(latlong1);
    Destination.getDestination().setStartPoint(latlong);
    Destination.getDestination().setEndPoint(latlong1);
    activeNavigator();
    if(latlong != null || latlong1 != null)
    {
      break MISSING_BLOCK_LABEL_155;
    }
    Toast.makeText(activity, "Check your input (use coordinates)!\nExample:\nuse degrees: 63\260 25\u2032 47\u2033" +
                    " N, 10\260 23\u2032 36\u2033 E\nor use digital: 63.429722, 10.393333"
            , 1).show();
  }

  private void settingsBtnHandler()
  {
    try
    {
      settingsBtn.setOnClickListener(new android.view.View.OnClickListener() {

        final MapActions this$0;

        public void onClick(View view)
        {
          try
          {
            AppSettings.getAppSettings().set(activity, sideBarVP);
            return;
          }
          // Misplaced declaration of an exception variable
          catch(View view)
          {
            view.printStackTrace();
          }
        }


        {
          this$0 = MapActions.this;
          super();
        }
      });
      return;
    }
    catch(Exception exception)
    {
      exception.printStackTrace();
    }
  }

  private void settingsFromItemHandler()
  {
    try
    {
      final ViewGroup fromFieldVG = (ViewGroup)activity.findViewById(0x7f0e0129);
      fromFieldVG.setOnTouchListener(new android.view.View.OnTouchListener() {

        final MapActions this$0;
        final ViewGroup val$fromFieldVG;

        public boolean onTouch(View view, MotionEvent motionevent)
        {
          switch(motionevent.getAction())
          {
            default:
              return false;

            case 0: // '\0'
              try
              {
                fromFieldVG.setBackgroundColor(activity.getResources().getColor(0x7f0d004e));
              }
              // Misplaced declaration of an exception variable
              catch(View view)
              {
                view.printStackTrace();
                return true;
              }
              return true;

            case 1: // '\001'
              break;
          }
          try
          {
            fromFieldVG.setBackgroundColor(activity.getResources().getColor(0x7f0d004b));
            navSettingsVP.setVisibility(View.INVISIBLE);
            navSettingsFromVP.setVisibility(View.VISIBLE);
          }
          // Misplaced declaration of an exception variable
          catch(View view)
          {
            view.printStackTrace();
            return true;
          }
          return true;
        }


        {
          this$0 = MapActions.this;
          fromFieldVG = viewgroup;
          super();
        }
      });
      ((ImageButton)activity.findViewById(0x7f0e0136)).setOnClickListener(new android.view.View.OnClickListener() {

        final MapActions this$0;

        public void onClick(View view)
        {
          try
          {
            navSettingsVP.setVisibility(View.VISIBLE);
            navSettingsFromVP.setVisibility(View.INVISIBLE);
            return;
          }
          // Misplaced declaration of an exception variable
          catch(View view)
          {
            view.printStackTrace();
          }
        }


        {
          this$0 = MapActions.this;
          super();
        }
      });
      useCurrentLocationHandler();
      pointOnMapHandler();
      return;
    }
    catch(Exception exception)
    {
      exception.printStackTrace();
    }
  }

  private void settingsToItemHandler()
  {
    try
    {
      final ViewGroup toItemVG = (ViewGroup)activity.findViewById(0x7f0e012d);
      toItemVG.setOnTouchListener(new android.view.View.OnTouchListener() {

        final MapActions this$0;
        final ViewGroup val$toItemVG;

        public boolean onTouch(View view, MotionEvent motionevent)
        {
          switch(motionevent.getAction())
          {
            default:
              return false;

            case 0: // '\0'
              try
              {
                toItemVG.setBackgroundColor(activity.getResources().getColor(0x7f0d004e));
              }
              // Misplaced declaration of an exception variable
              catch(View view)
              {
                view.printStackTrace();
                return true;
              }
              return true;

            case 1: // '\001'
              break;
          }
          try
          {
            toItemVG.setBackgroundColor(activity.getResources().getColor(0x7f0d004b));
            navSettingsVP.setVisibility(View.INVISIBLE);
            navSettingsToVP.setVisibility(View.VISIBLE);
          }
          // Misplaced declaration of an exception variable
          catch(View view)
          {
            view.printStackTrace();
            return true;
          }
          return true;
        }


        {
          this$0 = MapActions.this;
          toItemVG = viewgroup;
          super();
        }
      });
      ((ImageButton)activity.findViewById(0x7f0e013d)).setOnClickListener(new android.view.View.OnClickListener() {

        final MapActions this$0;

        public void onClick(View view)
        {
          try
          {
            navSettingsVP.setVisibility(View.VISIBLE);
            navSettingsToVP.setVisibility(View.INVISIBLE);
            return;
          }
          // Misplaced declaration of an exception variable
          catch(View view)
          {
            view.printStackTrace();
          }
        }


        {
          this$0 = MapActions.this;
          super();
        }
      });
      toUseCurrentLocationHandler();
      toPointOnMapHandler();
      return;
    }
    catch(Exception exception)
    {
      exception.printStackTrace();
    }
  }

  private void toChooseFromFavoriteHandler()
  {
    final ViewGroup chooseFavorite = (ViewGroup)activity.findViewById(0x7f0e013e);
    chooseFavorite.setOnTouchListener(new android.view.View.OnTouchListener() {

      final MapActions this$0;
      final ViewGroup val$chooseFavorite;

      public boolean onTouch(View view, MotionEvent motionevent)
      {
        switch(motionevent.getAction())
        {
          default:
            return false;

          case 0: // '\0'
            chooseFavorite.setBackgroundColor(activity.getResources().getColor(0x7f0d004e));
            return true;

          case 1: // '\001'
            chooseFavorite.setBackgroundColor(activity.getResources().getColor(0x7f0d004b));
            break;
        }
        return true;
      }


      {
        this$0 = MapActions.this;
        chooseFavorite = viewgroup;
        super();
      }
    });
  }

  private void toPointOnMapHandler()
  {
    final ViewGroup pointItem = (ViewGroup)activity.findViewById(0x7f0e013f);
    try
    {
      pointItem.setOnTouchListener(new android.view.View.OnTouchListener() {

        final MapActions this$0;
        final ViewGroup val$pointItem;

        public boolean onTouch(View view, MotionEvent motionevent)
        {
          switch(motionevent.getAction())
          {
            default:
              return false;

            case 0: // '\0'
              try
              {
                pointItem.setBackgroundColor(activity.getResources().getColor(0x7f0d004e));
              }
              // Misplaced declaration of an exception variable
              catch(View view)
              {
                view.printStackTrace();
                return true;
              }
              return true;

            case 1: // '\001'
              break;
          }
          try
          {
            pointItem.setBackgroundColor(activity.getResources().getColor(0x7f0d004b));
            onStartPoint = false;
            navSettingsToVP.setVisibility(View.INVISIBLE);
            MapHandler.getMapHandler().setNeedLocation(true);
          }
          // Misplaced declaration of an exception variable
          catch(View view)
          {
            view.printStackTrace();
            return true;
          }
          return true;
        }


        {
          this$0 = MapActions.this;
          pointItem = viewgroup;
          super();
        }
      });
      return;
    }
    catch(Exception exception)
    {
      exception.printStackTrace();
    }
  }

  private void toUseCurrentLocationHandler()
  {
    try
    {
      final ViewGroup useCurrentLocal = (ViewGroup)activity.findViewById(0x7f0e0140);
      useCurrentLocal.setOnTouchListener(new android.view.View.OnTouchListener() {

        final MapActions this$0;
        final ViewGroup val$useCurrentLocal;

        public boolean onTouch(View view, MotionEvent motionevent)
        {
          motionevent.getAction();
          JVM INSTR tableswitch 0 1: default 28
          //                               0 30
          //                               1 61;
                       goto _L1 _L2 _L3
          _L1:
          return false;
          _L2:
          try
          {
            useCurrentLocal.setBackgroundColor(activity.getResources().getColor(0x7f0d004e));
          }
          // Misplaced declaration of an exception variable
          catch(View view)
          {
            view.printStackTrace();
            return true;
          }
          return true;
          _L3:
          useCurrentLocal.setBackgroundColor(activity.getResources().getColor(0x7f0d004b));
          if(MapActivity.getmCurrentLocation() == null)
          {
            break; /* Loop/switch isn't completed */
          }
          Destination.getDestination().setEndPoint(new LatLong(MapActivity.getmCurrentLocation().getLatitude(), MapActivity.getmCurrentLocation().getLongitude()));
          toLocalET.setText(Destination.getDestination().getEndPointToString());
          addToMarker(Destination.getDestination().getEndPoint());
          navSettingsToVP.setVisibility(View.INVISIBLE);
          navSettingsVP.setVisibility(View.VISIBLE);
          activeNavigator();
          return true;
          view;
          view.printStackTrace();
          if(true) goto _L1; else goto _L4
          _L4:
          Toast.makeText(activity, "Current Location not available, Check your GPS signal!", 0).show();
          return true;
        }


        {
          this$0 = MapActions.this;
          useCurrentLocal = viewgroup;
          super();
        }
      });
      return;
    }
    catch(Exception exception)
    {
      exception.printStackTrace();
    }
  }

  private void travelModeSetting()
  {
    byte byte0;
    final ImageButton bikeBtn;
    final ImageButton carBtn;
    final ImageButton footBtn;
    String s;
    try
    {
      footBtn = (ImageButton)activity.findViewById(0x7f0e0124);
      bikeBtn = (ImageButton)activity.findViewById(0x7f0e0125);
      carBtn = (ImageButton)activity.findViewById(0x7f0e0126);
      s = Variable.getVariable().getTravelMode();
    }
    catch(Exception exception)
    {
      exception.printStackTrace();
      return;
    }
    byte0 = -1;
    s.hashCode();
    JVM INSTR lookupswitch 3: default 227
    //                   98260: 174
    //                   3023841: 158
    //                   3148910: 142;
           goto _L1 _L2 _L3 _L4
    _L5:
    footBtn.setOnClickListener(new android.view.View.OnClickListener() {

      final MapActions this$0;
      final ImageButton val$bikeBtn;
      final ImageButton val$carBtn;
      final ImageButton val$footBtn;

      public void onClick(View view)
      {
        if(!Variable.getVariable().getTravelMode().equalsIgnoreCase("foot"))
        {
          Variable.getVariable().setTravelMode("foot");
          footBtn.setImageResource(0x7f0200a2);
          bikeBtn.setImageResource(0x7f02009e);
          carBtn.setImageResource(0x7f0200a0);
          activeNavigator();
        }
      }


      {
        this$0 = MapActions.this;
        footBtn = imagebutton;
        bikeBtn = imagebutton1;
        carBtn = imagebutton2;
        super();
      }
    });
    bikeBtn.setOnClickListener(new android.view.View.OnClickListener() {

      final MapActions this$0;
      final ImageButton val$bikeBtn;
      final ImageButton val$carBtn;
      final ImageButton val$footBtn;

      public void onClick(View view)
      {
        if(!Variable.getVariable().getTravelMode().equalsIgnoreCase("bike"))
        {
          Variable.getVariable().setTravelMode("bike");
          footBtn.setImageResource(0x7f0200a3);
          bikeBtn.setImageResource(0x7f02009d);
          carBtn.setImageResource(0x7f0200a0);
          activeNavigator();
        }
      }


      {
        this$0 = MapActions.this;
        footBtn = imagebutton;
        bikeBtn = imagebutton1;
        carBtn = imagebutton2;
        super();
      }
    });
    carBtn.setOnClickListener(new android.view.View.OnClickListener() {

      final MapActions this$0;
      final ImageButton val$bikeBtn;
      final ImageButton val$carBtn;
      final ImageButton val$footBtn;

      public void onClick(View view)
      {
        if(!Variable.getVariable().getTravelMode().equalsIgnoreCase("car"))
        {
          Variable.getVariable().setTravelMode("car");
          footBtn.setImageResource(0x7f0200a3);
          bikeBtn.setImageResource(0x7f02009e);
          carBtn.setImageResource(0x7f02009f);
          activeNavigator();
        }
      }


      {
        this$0 = MapActions.this;
        footBtn = imagebutton;
        bikeBtn = imagebutton1;
        carBtn = imagebutton2;
        super();
      }
    });
    return;
    _L4:
    if(s.equals("foot"))
    {
      byte0 = 0;
    }
          goto _L1
    _L3:
    if(s.equals("bike"))
    {
      byte0 = 1;
    }
          goto _L1
    _L2:
    if(s.equals("car"))
    {
      byte0 = 2;
    }
          goto _L1
    _L6:
    footBtn.setImageResource(0x7f0200a2);
          goto _L5
    _L7:
    bikeBtn.setImageResource(0x7f02009d);
          goto _L5
    _L8:
    carBtn.setImageResource(0x7f02009f);
          goto _L5
    _L1:
    byte0;
    JVM INSTR tableswitch 0 2: default 256
    //                   0 190
    //                   1 206
    //                   2 216;
           goto _L5 _L6 _L7 _L8
  }

  private void useCurrentLocationHandler()
  {
    final ViewGroup useCurrentLocal = (ViewGroup)activity.findViewById(0x7f0e0139);
    useCurrentLocal.setOnTouchListener(new android.view.View.OnTouchListener() {

      final MapActions this$0;
      final ViewGroup val$useCurrentLocal;

      public boolean onTouch(View view, MotionEvent motionevent)
      {
        switch(motionevent.getAction())
        {
          default:
            return false;

          case 0: // '\0'
            useCurrentLocal.setBackgroundColor(activity.getResources().getColor(0x7f0d004e));
            return true;

          case 1: // '\001'
            useCurrentLocal.setBackgroundColor(activity.getResources().getColor(0x7f0d004b));
            break;
        }
        if(MapActivity.getmCurrentLocation() != null)
        {
          Destination.getDestination().setStartPoint(new LatLong(MapActivity.getmCurrentLocation().getLatitude(), MapActivity.getmCurrentLocation().getLongitude()));
          addFromMarker(Destination.getDestination().getStartPoint());
          fromLocalET.setText(Destination.getDestination().getStartPointToString());
          navSettingsFromVP.setVisibility(View.INVISIBLE);
          navSettingsVP.setVisibility(View.VISIBLE);
          activeNavigator();
          return true;
        } else
        {
          Toast.makeText(activity, "Current Location not available, Check your GPS signal!", 0).show();
          return true;
        }
      }


      {
        this$0 = MapActions.this;
        useCurrentLocal = viewgroup;
        super();
      }
    });
  }

  public boolean homeBackKeyPressed()
  {
    if(navSettingsVP.getVisibility() == 0)
    {
      navSettingsVP.setVisibility(View.INVISIBLE);
      sideBarVP.setVisibility(View.VISIBLE);
      return false;
    }
    if(navSettingsFromVP.getVisibility() == 0)
    {
      navSettingsFromVP.setVisibility(View.INVISIBLE);
      navSettingsVP.setVisibility(View.VISIBLE);
      return false;
    }
    if(navSettingsToVP.getVisibility() == 0)
    {
      navSettingsToVP.setVisibility(View.INVISIBLE);
      navSettingsVP.setVisibility(View.VISIBLE);
      return false;
    }
    if(navInstructionListVP.getVisibility() == 0)
    {
      navInstructionListVP.setVisibility(View.INVISIBLE);
      sideBarVP.setVisibility(View.VISIBLE);
      return false;
    }
    if(AppSettings.getAppSettings().getAppSettingsVP() != null && AppSettings.getAppSettings().getAppSettingsVP().getVisibility() == 0)
    {
      AppSettings.getAppSettings().getAppSettingsVP().setVisibility(View.INVISIBLE);
      sideBarVP.setVisibility(View.VISIBLE);
      return false;
    } else
    {
      return true;
    }
  }

  public boolean isMenuVisible()
  {
    return menuVisible;
  }

  public void onPressLocation(LatLong latlong)
  {
    if(!onStartPoint)
    {
      break MISSING_BLOCK_LABEL_45;
    }
    Destination.getDestination().setStartPoint(latlong);
    addFromMarker(latlong);
    fromLocalET.setText(Destination.getDestination().getStartPointToString());
    _L1:
    navSettingsVP.setVisibility(View.VISIBLE);
    activeNavigator();
    return;
    try
    {
      Destination.getDestination().setEndPoint(latlong);
      addToMarker(latlong);
      toLocalET.setText(Destination.getDestination().getEndPointToString());
    }
    // Misplaced declaration of an exception variable
    catch(LatLong latlong)
    {
      latlong.printStackTrace();
      return;
    }
          goto _L1
  }

  public void pathCalculating(boolean flag)
  {
    if(!flag && Navigator.getNavigator().getGhResponse() != null)
    {
      activeDirections();
    }
  }

  public void setMenuVisible(boolean flag)
  {
    menuVisible = flag;
  }

  protected void showMyLocation(MapView mapview)
  {
    showPositionBtn.setOnClickListener(new android.view.View.OnClickListener() {

      final MapActions this$0;

      public void onClick(View view)
      {
        if(MapActivity.getmCurrentLocation() != null)
        {
          showPositionBtn.setImageResource(0x7f0200b4);
          MapHandler.getMapHandler().centerPointOnMap(new LatLong(MapActivity.getmCurrentLocation().getLatitude(), MapActivity.getmCurrentLocation().getLongitude()), 0);
          return;
        } else
        {
          showPositionBtn.setImageResource(0x7f0200ad);
          Toast.makeText(activity, "No Location Available", 0).show();
          return;
        }
      }


      {
        this$0 = MapActions.this;
        super();
      }
    });
  }

  public void statusChanged(boolean flag)
  {
    if(flag)
    {
      navigationBtn.setImageResource(0x7f0200a4);
      return;
    } else
    {
      navigationBtn.setImageResource(0x7f0200b6);
      return;
    }
  }

  protected void zoomControlHandler(final MapView mapView)
  {
    zoomInBtn.setImageResource(0x7f020097);
    zoomOutBtn.setImageResource(0x7f0200ba);
    zoomInBtn.setOnClickListener(new android.view.View.OnClickListener() {

      MapViewPosition mvp;
      final MapActions this$0;
      final MapView val$mapView;

      public void onClick(View view)
      {
        if(mvp.getZoomLevel() < Variable.getVariable().getZoomLevelMax())
        {
          mvp.zoomIn();
        }
      }


      {
        this$0 = MapActions.this;
        mapView = mapview;
        super();
        mvp = mapView.getModel().mapViewPosition;
      }
    });
    zoomOutBtn.setOnClickListener(new android.view.View.OnClickListener() {

      MapViewPosition mvp;
      final MapActions this$0;
      final MapView val$mapView;

      public void onClick(View view)
      {
        if(mvp.getZoomLevel() > Variable.getVariable().getZoomLevelMin())
        {
          mvp.zoomOut();
        }
      }


      {
        this$0 = MapActions.this;
        mapView = mapview;
        super();
        mvp = mapView.getModel().mapViewPosition;
      }
    });
  }












/*
    static boolean access$502(MapActions mapactions, boolean flag)
    {
        mapactions.onStartPoint = flag;
        return flag;
    }

*/




}
