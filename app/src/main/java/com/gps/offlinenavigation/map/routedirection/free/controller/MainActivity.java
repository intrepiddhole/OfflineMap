package com.gps.offlinenavigation.map.routedirection.free.controller;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.*;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.gps.offlinenavigation.map.routedirection.free.Dashboard;
import com.gps.offlinenavigation.map.routedirection.free.model.dataType.MyMap;
import com.gps.offlinenavigation.map.routedirection.free.model.listeners.MapDownloadListener;
import com.gps.offlinenavigation.map.routedirection.free.model.listeners.MapFABonClickListener;
import com.gps.offlinenavigation.map.routedirection.free.model.map.*;
import com.gps.offlinenavigation.map.routedirection.free.model.util.Variable;
import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements MapDownloadListener, MapFABonClickListener
{

  ImageButton button1;
  private boolean changeMap;
  protected Context context;
  private MyMapAdapter mapAdapter;
  private RecyclerView mapsRV;

  public MainActivity()
  {
    context = this;
  }

  private void activeRecyclerView(List list)
  {
    mapsRV = (RecyclerView)findViewById(0x7f0e009e);
    Object obj = new DefaultItemAnimator();
    ((DefaultItemAnimator) (obj)).setAddDuration(2000L);
    ((DefaultItemAnimator) (obj)).setRemoveDuration(600L);
    mapsRV.setItemAnimator(((android.support.v7.widget.RecyclerView.ItemAnimator) (obj)));
    mapsRV.setHasFixedSize(true);
    obj = new LinearLayoutManager(this);
    mapsRV.setLayoutManager(((android.support.v7.widget.RecyclerView.LayoutManager) (obj)));
    mapAdapter = new MyMapAdapter(list, this);
    mapsRV.setAdapter(mapAdapter);
    deleteItemHandler();
  }

  private void addRecentDownloadedFiles()
  {
    try {
      int i = Variable.getVariable().getRecentDownloadedMaps().size() - 1;
      while (i >= 0) {
        MyMap mymap = Variable.getVariable().removeRecentDownloadedMap(i);
        mapAdapter.insert(mymap);
        Variable.getVariable().addLocalMap(mymap);
        i--;
      }
    }catch (Exception exception){
      exception.getStackTrace();
    }
  }

  private void deleteItemHandler()
  {
    (new ItemTouchHelper(new android.support.v7.widget.helper.ItemTouchHelper.SimpleCallback(0, 12) {
      public boolean onMove(RecyclerView recyclerview, android.support.v7.widget.RecyclerView.ViewHolder viewholder, android.support.v7.widget.RecyclerView.ViewHolder viewholder1)
      {
        return false;
      }

      public void onSwiped(android.support.v7.widget.RecyclerView.ViewHolder viewholder, int i)
      {
        MyMap viewholder_1 = mapAdapter.remove(mapsRV.getChildAdapterPosition(viewholder.itemView));
        Variable.getVariable().removeLocalMap(viewholder_1);
        recursiveDelete(new File(viewholder_1.getUrl()));
      }
    })).attachToRecyclerView(mapsRV);
  }

  private void generateList()
  {
    if(Variable.getVariable().getLocalMaps().isEmpty())
    {
      refreshList();
      return;
    } else
    {
      mapAdapter.addAll(Variable.getVariable().getLocalMaps());
      return;
    }
  }

  private void log(String s)
  {
    Log.i(getClass().getSimpleName(), (new StringBuilder()).append("-------").append(s).toString());
  }

  private void quitApp()
  {
    Intent intent = new Intent();
    intent.setAction("ACTION_QUIT");
    LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    finish();
    System.exit(0);
  }

  private void refreshList()
  {
    String as[] = Variable.getVariable().getMapsFolder().list(new FilenameFilter() {
      public boolean accept(File file, String s1)
      {
        return s1 != null && s1.endsWith("-gh");
      }
    });
    int j = as.length;
    for(int i = 0; i < j; i++)
    {
      String s = as[i];
      Variable.getVariable().addLocalMap(new MyMap(s));
    }

    if(!Variable.getVariable().getLocalMaps().isEmpty())
    {
      mapAdapter.addAll(Variable.getVariable().getLocalMaps());
    }
  }

  private void startDownloadActivity()
  {
    if(isOnline())
    {
      startActivity(new Intent(this, DownloadMapActivity.class));
      finish();
      return;
    } else
    {
      Toast.makeText(this, "Add new Map need internet connection!", 1).show();
      return;
    }
  }

  private void startMapActivity()
  {
    Intent intent = new Intent(this, MapActivity.class);
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
    startActivity(intent);
    finish();
  }

  public void downloadFinished(String s)
  {
    addRecentDownloadedFiles();
  }

  public void downloadStart()
  {
  }

  public boolean isOnline()
  {
    NetworkInfo networkinfo = ((ConnectivityManager)getSystemService(CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
    return networkinfo != null && networkinfo.isConnectedOrConnecting();
  }

  public void mapFABonClick(View view)
  {
    try
    {
      Variable.getVariable().setPrepareInProgress(true);
      int i = mapsRV.getChildAdapterPosition(view);
      Variable.getVariable().setCountry(mapAdapter.getItem(i).getMapName());
      if(changeMap)
      {
        Variable.getVariable().setLastLocation(null);
        MapHandler.reset();
        System.gc();
      }
      startMapActivity();
    } catch(Exception e) {
      e.getStackTrace();
    }
  }

  public void onBackPressed()
  {
    startActivity(new Intent(this, Dashboard.class));
    finish();
  }

  protected void onCreate(Bundle bundle)
  {
    super.onCreate(bundle);
    setContentView(0x7f040020);
    AdRequest bundle_1 = (new com.google.android.gms.ads.AdRequest.Builder()).build();
    ((AdView)findViewById(0x7f0e00a2)).loadAd(bundle_1);
    button1 = (ImageButton)findViewById(0x7f0e009f);
    button1.setOnClickListener(new android.view.View.OnClickListener() {
      public void onClick(View view)
      {
        startDownloadActivity();
      }
    });
    try
    {
      Variable.getVariable().setContext(getApplicationContext());
      if(android.os.Build.VERSION.SDK_INT < 19)
      {
        Variable.getVariable().setMapsFolder(new File(Environment.getExternalStorageDirectory(), Variable.getVariable().getMapDirectory()));
      } else {
        if (!Environment.getExternalStorageState().equals("mounted")) {
          Toast.makeText(this, "Pocket Maps is not usable without an external storage!", Toast.LENGTH_SHORT).show();
          return;
        }
        Variable.getVariable().setMapsFolder(new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), Variable.getVariable().getMapDirectory()));
        Variable.getVariable().setTrackingFolder(new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), Variable.getVariable().getTrackingDirectory()));
      }
      if(!Variable.getVariable().getMapsFolder().exists())
      {
        Variable.getVariable().getMapsFolder().mkdirs();
      }
      activeRecyclerView(new ArrayList());
      generateList();
      changeMap = getIntent().getBooleanExtra("SELECTNEWMAP", false);
      if(Variable.getVariable().loadVariables() && !changeMap)
      {
        startMapActivity();
      }
    } catch(Exception e) {
      e.printStackTrace();
    }
  }

  protected void onPause()
  {
    super.onPause();
    DownloadFiles.getDownloader().removeListener(this);
  }

  protected void onResume()
  {
    super.onResume();
    addRecentDownloadedFiles();
    DownloadFiles.getDownloader().addListener(this);
  }

  public void progressUpdate(Integer integer)
  {
  }

  public void recursiveDelete(File file)
  {
    if(file.isDirectory())
    {
      File afile[] = file.listFiles();
      int j = afile.length;
      for(int i = 0; i < j; i++)
      {
        recursiveDelete(afile[i]);
      }
    }
    try
    {
      file.delete();
    }
    catch(Exception e)
    {
      e.getStackTrace();
    }
  }



}
