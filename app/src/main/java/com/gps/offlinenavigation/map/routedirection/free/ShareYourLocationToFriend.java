package com.gps.offlinenavigation.map.routedirection.free;

import android.content.*;
import android.location.*;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class ShareYourLocationToFriend extends AppCompatActivity
{

  String AppSelectedLocationName;
  String AppStrCityName;
  AdView AppadView;
  GPSTracker AppgpsTracker;
  LocationManager ApplocationManager;
  SharedPreferences Apppreferences;
  TextView ApptvCityName;
  TextView ApptvCopy;
  TextView ApptvLongShare;
  TextView ApptvMap;
  TextView ApptvPName;
  TextView ApptvShare;
  TextView ApptvlatShare;

  public ShareYourLocationToFriend()
  {
  }

  private boolean isNetworkAvailable()
  {
    android.net.NetworkInfo networkinfo = null;
    try {
      android.net.NetworkInfo networkinfo1 = ((ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
      networkinfo = networkinfo1;
    } catch (Exception e){
      e.printStackTrace();
    }
    return networkinfo != null;
  }

  private void moveMapToMyLocation()
  {
    try
    {
      if(ActivityCompat.checkSelfPermission(this, "android.permission.ACCESS_FINE_LOCATION") != 0 && ActivityCompat.checkSelfPermission(this, "android.permission.ACCESS_COARSE_LOCATION") != 0 && android.os.Build.VERSION.SDK_INT >= 23)
      {
        ActivityCompat.requestPermissions(this, new String[] {
                "android.permission.ACCESS_FINE_LOCATION"
        }, 1);
      }
      return;
    }
    catch(Exception exception)
    {
      exception.printStackTrace();
    }
  }

  public void ShareMyLocation()
  {
    try
    {
      String s = (new StringBuilder()).append("http://maps.google.com/maps?q=loc:").append(AppgpsTracker.getLatitude()).append(",").append(AppgpsTracker.getLongitude()).toString();
      Intent intent = new Intent("android.intent.action.SEND");
      intent.setType("text/plain");
      intent.putExtra("android.intent.extra.SUBJECT", "My Location");
      intent.putExtra("android.intent.extra.TEXT", s);
      startActivity(Intent.createChooser(intent, "Share Using"));
      return;
    }
    catch(Exception exception)
    {
      exception.printStackTrace();
    }
  }

  public void copy2Clipboard(String s)
  {
    try
    {
      ((ClipboardManager)getSystemService(CLIPBOARD_SERVICE)).setPrimaryClip(ClipData.newPlainText("label", s));
      Toast.makeText(this, "Copied", Toast.LENGTH_SHORT).show();
    }
    catch(Exception e)
    {
      e.printStackTrace();
    }
  }

  public void getAddress(double d, double d1)
  {
    try
    {
      Address address = (Address)(new Geocoder(this, Locale.getDefault())).getFromLocation(d, d1, 1).get(0);
      String s = address.getAddressLine(0);
      s = (new StringBuilder()).append(s).append("\n").append(address.getSubAdminArea()).toString();
      Log.v("IGA", (new StringBuilder()).append("Address").append(s).toString());
      AppSelectedLocationName = s;
      AppStrCityName = address.getCountryName();
      ApptvPName.setText(AppSelectedLocationName);
      ApptvCityName.setText(AppStrCityName);
      return;
    }
    catch(IOException ioexception)
    {
      ioexception.printStackTrace();
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
    setContentView(0x7f040026);
    try
    {
      AppgpsTracker = new GPSTracker(this);
      Apppreferences = getSharedPreferences("Khalil", 0);
      AppadView = (AdView)findViewById(0x7f0e00d1);
      ApplocationManager = (LocationManager)getSystemService(LOCATION_SERVICE);
      if(!ApplocationManager.isProviderEnabled("gps"))
      {
        Toast.makeText(getApplicationContext(), "Please Enable Your Location", 1).show();
        startActivityForResult(new Intent("android.settings.LOCATION_SOURCE_SETTINGS"), 12);
      }
      if(!isNetworkAvailable())
      {
        AppadView.setVisibility(View.GONE);
      } else {
        AdRequest bundle_1 = (new com.google.android.gms.ads.AdRequest.Builder()).build();
        AppadView.loadAd(bundle_1);
      }
      ApptvCopy = (TextView)findViewById(0x7f0e00ce);
      ApptvShare = (TextView)findViewById(0x7f0e00cf);
      ApptvMap = (TextView)findViewById(0x7f0e00cd);
      ApptvPName = (TextView)findViewById(0x7f0e00c7);
      ApptvlatShare = (TextView)findViewById(0x7f0e00c9);
      ApptvLongShare = (TextView)findViewById(0x7f0e00ca);
      ApptvCityName = (TextView)findViewById(0x7f0e00c8);
      ApptvPName.setText(AppSelectedLocationName);
      ApptvlatShare.setText(String.valueOf(AppgpsTracker.getLatitude()));
      ApptvLongShare.setText(String.valueOf(AppgpsTracker.getLongitude()));
      getAddress(AppgpsTracker.getLatitude(), AppgpsTracker.getLongitude());
      ApptvMap.setOnClickListener(new android.view.View.OnClickListener() {
        public void onClick(View view)
        {
          try
          {
            Intent view_1 = new Intent("android.intent.action.VIEW", Uri.parse((new StringBuilder()).append("geo:").append(AppgpsTracker.getLatitude()).append(",").append(AppgpsTracker.getLongitude()).toString()));
            view_1.setPackage("com.google.android.apps.maps");
            if(view_1.resolveActivity(getPackageManager()) != null)
            {
              startActivity(view_1);
            }
          }
          catch(Exception e)
          {
            e.printStackTrace();
          }
        }
      });
      ApptvCopy.setOnClickListener(new android.view.View.OnClickListener() {
        public void onClick(View view)
        {
          String view_1 = (new StringBuilder()).append("Place: ").append(ApptvPName.getText().toString()).append("\nCountry: ").append(ApptvCityName.getText().toString()).toString();
          copy2Clipboard(view_1);
        }
      });
      ApptvShare.setOnClickListener(new android.view.View.OnClickListener() {
        public void onClick(View view)
        {
          ShareMyLocation();
        }
      });
    }
    catch(Exception e1)
    {
      e1.printStackTrace();
    }
  }

  public void onRequestPermissionsResult(int i, String as[], int ai[])
  {
    switch(i)
    {
      default:
        return;

      case 1: // '\001'
        break;
    }
    if(ai.length > 0 && ai[0] == 0)
    {
      moveMapToMyLocation();
      return;
    } else
    {
      ActivityCompat.requestPermissions(this, new String[] {
              "android.permission.ACCESS_FINE_LOCATION"
      }, 1);
      return;
    }
  }
}
