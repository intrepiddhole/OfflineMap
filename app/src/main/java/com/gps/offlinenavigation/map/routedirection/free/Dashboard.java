package com.gps.offlinenavigation.map.routedirection.free;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.Toast;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.maps.model.LatLng;
import com.gps.offlinenavigation.map.routedirection.free.controller.MainActivity;
import com.heyzap.sdk.ads.HeyzapAds;
import com.startapp.android.publish.adsCommon.Ad;
import com.startapp.android.publish.adsCommon.StartAppAd;
import com.startapp.android.publish.adsCommon.StartAppSDK;
import com.startapp.android.publish.adsCommon.adListeners.AdDisplayListener;
import com.startapp.android.publish.adsCommon.adListeners.AdEventListener;

public class Dashboard extends AppCompatActivity
        implements LocationListener
{

  private static final float MIN_DISTANCE = 1000F;
  private static final long MIN_TIME = 400L;
  private static final int MY_COUNTER = 100;
  public static LatLng latLng;
  public static Double latitude;
  public static Double longitude;
  int afteradddisplay;
  ImageButton drivingRoute2D;
  ImageButton drivingRoute3D;
  ImageButton find_friend;
  ImageButton gps_map;
  ImageButton gps_voice;
  ImageButton gpsoffnow;
  private LocationManager locationManager;
  private InterstitialAd mInterstitialAd;
  ImageButton more_apps;
  ImageButton offline;
  ImageButton pakmap;
  ImageButton privacy_policy;
  String publisherId_heyZap;
  ImageButton quit;
  ImageButton rate_us;
  ImageButton share_app;
  ImageButton share_loc;
  ImageButton streetwali;

  public Dashboard()
  {
    afteradddisplay = 0;
    publisherId_heyZap = "336ed9f4da5e6fef61f39f1193fcc721";
  }

  private void KhalilPremissionCheck()
  {
    if(android.os.Build.VERSION.SDK_INT >= 23 && getApplicationContext().checkSelfPermission("android.permission.ACCESS_COARSE_LOCATION") != 0 && getApplicationContext().checkSelfPermission("android.permission.ACCESS_FINE_LOCATION") != 0 && getApplicationContext().checkSelfPermission("android.permission.WRITE_EXTERNAL_STORAGE") != 0 && getApplicationContext().checkSelfPermission("android.permission.READ_EXTERNAL_STORAGE") != 0)
    {
      requestPermissions(new String[] {
              "android.permission.ACCESS_COARSE_LOCATION", "android.permission.ACCESS_FINE_LOCATION", "android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE"
      }, 100);
    }
  }

  private void buildAlertMessageNoGps()
  {
    android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
    builder.setMessage("Your GPS seems to be disabled, do you want to enable it?").setCancelable(false).setPositiveButton("Yes", new android.content.DialogInterface.OnClickListener() {
      public void onClick(DialogInterface dialoginterface, int i)
      {
        startActivity(new Intent("android.settings.LOCATION_SOURCE_SETTINGS"));
      }
    }).setNegativeButton("No", new android.content.DialogInterface.OnClickListener() {
      public void onClick(DialogInterface dialoginterface, int i)
      {
        dialoginterface.cancel();
      }
    });
    builder.create().show();
  }

  private void goToNextLevel()
  {
    if(locationManager.isProviderEnabled("gps") && isNetworkAvailable()) {
      if(afteradddisplay == 1)
      {
        startActivity(new Intent(this, MainActivity.class));
        finish();
      } else
      if(afteradddisplay == 2)
      {
        startActivity(new Intent(this, MapsActivity.class));
        finish();
      } else
      if(afteradddisplay == 3)
      {
        google_Maps_Intent();
      } else
      if(afteradddisplay == 4)
      {
        startActivity(new Intent(this, ShareYourLocationToFriend.class));
        finish();
      }
    } else {
      if (locationManager.isProviderEnabled("gps")) {
        if (afteradddisplay != 1) {
          if (!isNetworkAvailable()) {
            openWifySettings();
          }
        } else {
          startActivity(new Intent(this, MainActivity.class));
          finish();
        }
      } else
        buildAlertMessageNoGps();
    }
    mInterstitialAd = newInterstitialAd();
    loadInterstitial();
  }

  private boolean isNetworkAvailable()
  {
    NetworkInfo networkinfo = ((ConnectivityManager)getSystemService(CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
    return networkinfo != null && networkinfo.isConnected();
  }

  private void loadInterstitial()
  {
    com.google.android.gms.ads.AdRequest adrequest = (new com.google.android.gms.ads.AdRequest.Builder()).build();
    mInterstitialAd.loadAd(adrequest);
  }

  private InterstitialAd newInterstitialAd()
  {
    InterstitialAd interstitialad = new InterstitialAd(this);
    interstitialad.setAdUnitId(getString(0x7f080053));
    interstitialad.setAdListener(new AdListener() {
      public void onAdClosed()
      {
        goToNextLevel();
      }

      public void onAdFailedToLoad(int i)
      {
      }

      public void onAdLoaded()
      {
      }
    });
    return interstitialad;
  }

  private void openWebUrl(String s)
  {
    startActivity(new Intent("android.intent.action.VIEW", Uri.parse(s)));
  }

  private void showInterstitial()
  {
    if(mInterstitialAd != null && mInterstitialAd.isLoaded())
    {
      mInterstitialAd.show();
    }
  }

  public void MoreAppsDialogue()
  {
    try
    {
      AlertDialog alertdialog = (new android.support.v7.app.AlertDialog.Builder(this)).create();
      alertdialog.setTitle("World Live Earth Map Studio");
      alertdialog.setMessage("Do you want to see more apps?");
      alertdialog.setButton(-1, "YES", new android.content.DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialoginterface, int i)
        {
          openWebUrl("https://play.google.com/store/apps/developer?id=World+Live+Earth+Map+Studio");
          dialoginterface.dismiss();
        }
      });
      alertdialog.setButton(-3, "NO", new android.content.DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialoginterface, int i)
        {
          dialoginterface.dismiss();
        }
      });
      alertdialog.show();
      return;
    }
    catch(Exception exception)
    {
      exception.printStackTrace();
    }
  }

  public void RateUs()
  {
    final Dialog dd = new Dialog(this);
    dd.setCancelable(false);
    dd.setContentView(0x7f04003a);
    Button button = (Button)dd.findViewById(0x7f0e010a);
    ((RatingBar)dd.findViewById(0x7f0e0108)).setOnRatingBarChangeListener(new android.widget.RatingBar.OnRatingBarChangeListener() {
      public void onRatingChanged(RatingBar ratingbar, float f, boolean flag)
      {
        if(ratingbar.getRating() <= 3F)
        {
          Intent ratingbar_1 = new Intent("android.intent.action.SEND");
          ratingbar_1.putExtra("android.intent.extra.EMAIL", new String[] {
                  "appsworldltd@gmail.com"
          });
          ratingbar_1.putExtra("android.intent.extra.SUBJECT", "App Review");
          ratingbar_1.putExtra("android.intent.extra.TEXT", "Write your Sugesstion:");
          ratingbar_1.setType("message/rfc822");
          ratingbar_1.setPackage("com.google.android.gm");
          startActivity(ratingbar_1);
        } else {
          openWebUrl((new StringBuilder()).append("https://play.google.com/store/apps/details?id=").append(getPackageName()).toString());
        }
      }
    });
    button.setOnClickListener(new android.view.View.OnClickListener() {
      public void onClick(View view)
      {
        dd.dismiss();
      }
    });
    dd.show();
  }

  public void findViewsById()
  {
    android.view.animation.Animation animation = AnimationUtils.loadAnimation(this, 0x7f050014);
    offline = (ImageButton)findViewById(0x7f0e00ec);
    drivingRoute2D = (ImageButton)findViewById(0x7f0e00ed);
    drivingRoute3D = (ImageButton)findViewById(0x7f0e00ee);
    share_loc = (ImageButton)findViewById(0x7f0e00ef);
    share_app = (ImageButton)findViewById(0x7f0e00f0);
    more_apps = (ImageButton)findViewById(0x7f0e00f1);
    rate_us = (ImageButton)findViewById(0x7f0e00f3);
    privacy_policy = (ImageButton)findViewById(0x7f0e00f4);
    find_friend = (ImageButton)findViewById(0x7f0e00f6);
    find_friend.startAnimation(animation);
    gps_map = (ImageButton)findViewById(0x7f0e00f7);
    gps_map.startAnimation(animation);
    gps_voice = (ImageButton)findViewById(0x7f0e00f8);
    gps_voice.startAnimation(animation);
    streetwali = (ImageButton)findViewById(0x7f0e00f9);
    streetwali.startAnimation(animation);
    pakmap = (ImageButton)findViewById(0x7f0e00fa);
    pakmap.startAnimation(animation);
    gpsoffnow = (ImageButton)findViewById(0x7f0e00fb);
    gpsoffnow.startAnimation(animation);
  }

  public void google_Maps_Intent()
  {
    Intent intent;
    try
    {
      intent = new Intent("android.intent.action.VIEW");
      intent.setPackage("com.google.android.apps.maps");
      if(intent.resolveActivity(getPackageManager()) == null)
      {
        Toast.makeText(getApplicationContext(), "Install Google Maps Application", Toast.LENGTH_SHORT).show();
      }else
        startActivity(intent);
      startActivity(intent);
    }
    catch(Exception exception)
    {
      exception.printStackTrace();
    }
  }

  public void onBackPressed()
  {
  }

  public void onClickListeners()
  {
    offline.setOnClickListener(new android.view.View.OnClickListener() {
      public void onClick(View view)
      {
        afteradddisplay = 1;
        if(isNetworkAvailable())
        {
          showInterstitial();
          return;
        } else
        {
          goToNextLevel();
          return;
        }
      }
    });
    drivingRoute2D.setOnClickListener(new android.view.View.OnClickListener() {
      public void onClick(View view)
      {
        afteradddisplay = 2;
        if(isNetworkAvailable())
        {
          showInterstitial();
          return;
        } else
        {
          goToNextLevel();
          return;
        }
      }
    });
    drivingRoute3D.setOnClickListener(new android.view.View.OnClickListener() {
      public void onClick(View view)
      {
        afteradddisplay = 3;
        if(isNetworkAvailable())
        {
          showInterstitial();
          return;
        } else
        {
          goToNextLevel();
          return;
        }
      }
    });
    share_loc.setOnClickListener(new android.view.View.OnClickListener() {
      public void onClick(View view)
      {
        afteradddisplay = 4;
        if(isNetworkAvailable())
        {
          showInterstitial();
          return;
        } else
        {
          goToNextLevel();
          return;
        }
      }
    });
    share_app.setOnClickListener(new android.view.View.OnClickListener() {
      public void onClick(View view)
      {
        String view_1 = (new StringBuilder()).append("https://play.google.com/store/apps/details?id=").append(getPackageName()).toString();
        Intent intent = new Intent("android.intent.action.SEND");
        intent.setType("text/plain");
        intent.putExtra("android.intent.extra.SUBJECT", "");
        intent.putExtra("android.intent.extra.TEXT", view_1);
        startActivity(Intent.createChooser(intent, "Share Using"));
      }
    });
    more_apps.setOnClickListener(new android.view.View.OnClickListener() {
      public void onClick(View view)
      {
        MoreAppsDialogue();
      }
    });
    rate_us.setOnClickListener(new android.view.View.OnClickListener() {
      public void onClick(View view)
      {
        RateUs();
      }
    });
    privacy_policy.setOnClickListener(new android.view.View.OnClickListener() {
      public void onClick(View view)
      {
        Intent view_1 = new Intent(Dashboard.this, PrivacyPolicy.class);
        startActivity(view_1);
        finish();
      }
    });
    find_friend.setOnClickListener(new android.view.View.OnClickListener() {
      public void onClick(View view)
      {
        android.support.v7.app.AlertDialog.Builder view_1 = new android.support.v7.app.AlertDialog.Builder(Dashboard.this, 0x7f0a00df);
        view_1.setCancelable(false);
        view_1.setTitle("Download This");
        view_1.setPositiveButton("Download", new android.content.DialogInterface.OnClickListener() {
          public void onClick(DialogInterface dialoginterface, int i)
          {
            openWebUrl("https://play.google.com/store/apps/details?id=com.findfriend.routetracker.family" +
                    "gps.locator.map"
            );
          }
        });
        view_1.setNegativeButton("No", new android.content.DialogInterface.OnClickListener() {
          public void onClick(DialogInterface dialoginterface, int i)
          {
          }
        });
        view_1.create().show();
      }
    });
    gps_map.setOnClickListener(new android.view.View.OnClickListener() {
      public void onClick(View view)
      {
        android.support.v7.app.AlertDialog.Builder view_1 = new android.support.v7.app.AlertDialog.Builder(Dashboard.this, 0x7f0a00df);
        view_1.setCancelable(false);
        view_1.setTitle("Download This");
        view_1.setPositiveButton("Download", new android.content.DialogInterface.OnClickListener() {
          public void onClick(DialogInterface dialoginterface, int i)
          {
            openWebUrl("https://play.google.com/store/apps/details?id=com.gps.earthlocation.find.driving" +
                    ".routetracking.map.free"
            );
          }
        });
        view_1.setNegativeButton("No", new android.content.DialogInterface.OnClickListener() {
          public void onClick(DialogInterface dialoginterface, int i)
          {
          }
        });
        view_1.create().show();
      }
    });
    gps_voice.setOnClickListener(new android.view.View.OnClickListener() {
      public void onClick(View view)
      {
        android.support.v7.app.AlertDialog.Builder view_1 = new android.support.v7.app.AlertDialog.Builder(Dashboard.this, 0x7f0a00df);
        view_1.setCancelable(false);
        view_1.setTitle("Download This");
        view_1.setPositiveButton("Download", new android.content.DialogInterface.OnClickListener() {
          public void onClick(DialogInterface dialoginterface, int i)
          {
            openWebUrl("https://play.google.com/store/apps/details?id=com.gpsvoice.directionroute.naviga" +
                    "tionplanner.map"
            );
          }
        });
        view_1.setNegativeButton("No", new android.content.DialogInterface.OnClickListener() {
          public void onClick(DialogInterface dialoginterface, int i)
          {
          }
        });
        view_1.create().show();
      }
    });
    streetwali.setOnClickListener(new android.view.View.OnClickListener() {
      public void onClick(View view)
      {
        android.support.v7.app.AlertDialog.Builder view_1 = new android.support.v7.app.AlertDialog.Builder(Dashboard.this, 0x7f0a00df);
        view_1.setCancelable(false);
        view_1.setTitle("Download This");
        view_1.setPositiveButton("Download", new android.content.DialogInterface.OnClickListener() {
          public void onClick(DialogInterface dialoginterface, int i)
          {
            openWebUrl("https://play.google.com/store/apps/details?id=com.liveview.streetmap.makeroute.e" +
                    "arth.tracking.free"
            );
          }
        });
        view_1.setNegativeButton("No", new android.content.DialogInterface.OnClickListener() {
          public void onClick(DialogInterface dialoginterface, int i)
          {
          }
        });
        view_1.create().show();
      }
    });
    pakmap.setOnClickListener(new android.view.View.OnClickListener() {
      public void onClick(View view)
      {
        android.support.v7.app.AlertDialog.Builder view_1 = new android.support.v7.app.AlertDialog.Builder(Dashboard.this, 0x7f0a00df);
        view_1.setCancelable(false);
        view_1.setTitle("Download This");
        view_1.setPositiveButton("Download", new android.content.DialogInterface.OnClickListener() {
          public void onClick(DialogInterface dialoginterface, int i)
          {
            openWebUrl("https://play.google.com/store/apps/details?id=com.offline.navigationmap.customro" +
                    "ute.earth.free"
            );
          }
        });
        view_1.setNegativeButton("No", new android.content.DialogInterface.OnClickListener() {
          public void onClick(DialogInterface dialoginterface, int i)
          {
          }
        });
        view_1.create().show();
      }
    });
    gpsoffnow.setOnClickListener(new android.view.View.OnClickListener() {
      public void onClick(View view)
      {
        android.support.v7.app.AlertDialog.Builder view_1 = new android.support.v7.app.AlertDialog.Builder(Dashboard.this, 0x7f0a00df);
        view_1.setCancelable(false);
        view_1.setTitle("Download This");
        view_1.setPositiveButton("Download", new android.content.DialogInterface.OnClickListener() {
          public void onClick(DialogInterface dialoginterface, int i)
          {
            openWebUrl("https://play.google.com/store/apps/details?id=com.offlineroutes.gpsfinder.naviga" +
                    "tion.mapstracking.free"
            );
          }
        });
        view_1.setNegativeButton("No", new android.content.DialogInterface.OnClickListener() {
          public void onClick(DialogInterface dialoginterface, int i)
          {
          }
        });
        view_1.create().show();
      }
    });
  }

  protected void onCreate(Bundle bundle)
  {
    super.onCreate(bundle);
    setContentView(0x7f04002b);
    locationManager = (LocationManager)getSystemService(LOCATION_SERVICE);
    if(isNetworkAvailable()) {
      if(!(locationManager.isProviderEnabled("gps") && isNetworkAvailable() || locationManager.isProviderEnabled("gps")))
        buildAlertMessageNoGps();
    } else
      openWifySettings();
    KhalilPremissionCheck();
    locationManager = (LocationManager)getSystemService(LOCATION_SERVICE);
    mInterstitialAd = newInterstitialAd();
    loadInterstitial();
    int i;
    if(ActivityCompat.checkSelfPermission(this, "android.permission.ACCESS_FINE_LOCATION") != 0)
    {
      i = ActivityCompat.checkSelfPermission(this, "android.permission.ACCESS_COARSE_LOCATION");
      if(i != 0)
        return;
    }
    AdView adview;
    try
    {
      locationManager.requestLocationUpdates("network", 400L, 1000F, this);
    }
    catch(Exception e)
    {
      e.printStackTrace();
    }
    AdRequest bundle_1 = (new com.google.android.gms.ads.AdRequest.Builder()).build();
    adview = (AdView)findViewById(0x7f0e00f2);
    if(!isNetworkAvailable())
      adview.setVisibility(View.GONE);
    else
      adview.loadAd(bundle_1);
    quit = (ImageButton)findViewById(0x7f0e00f5);
    quit.setOnClickListener(new android.view.View.OnClickListener() {
      public void onClick(View view)
      {
        //wrong
      }
    });
    findViewsById();
    onClickListeners();
  }

  protected void onDestroy()
  {
    super.onDestroy();
  }

  public void onLocationChanged(Location location)
  {
    latitude = Double.valueOf(location.getLatitude());
    longitude = Double.valueOf(location.getLongitude());
    latLng = new LatLng(latitude.doubleValue(), longitude.doubleValue());
    locationManager.removeUpdates(this);
  }

  public void onProviderDisabled(String s)
  {
    Toast.makeText(getBaseContext(), "Gps is turned off!!", Toast.LENGTH_SHORT).show();
  }

  public void onProviderEnabled(String s)
  {
    Toast.makeText(getBaseContext(), "Gps is turned on!! ", Toast.LENGTH_SHORT).show();
  }

  protected void onResume()
  {
    super.onResume();
  }

  protected void onStart()
  {
    super.onStart();
  }

  public void onStatusChanged(String s, int i, Bundle bundle)
  {
  }

  protected void onStop()
  {
    super.onStop();
  }

  public void openWifySettings()
  {
    android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
    builder.setMessage("Internet Disabled. Do You Want To Enable It?").setCancelable(false).setPositiveButton("Yes", new android.content.DialogInterface.OnClickListener() {
      public void onClick(DialogInterface dialoginterface, int i)
      {
        Intent dialoginterface_1 = new Intent("android.intent.action.MAIN");
        dialoginterface_1.setClassName("com.android.settings", "com.android.settings.wifi.WifiSettings");
        startActivity(dialoginterface_1);
      }
    }).setNegativeButton("No", new android.content.DialogInterface.OnClickListener() {
      public void onClick(DialogInterface dialoginterface, int i)
      {
        dialoginterface.cancel();
      }
    });
    builder.create().show();
  }
}
