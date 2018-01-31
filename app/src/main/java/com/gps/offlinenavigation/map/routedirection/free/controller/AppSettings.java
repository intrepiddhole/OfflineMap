package com.gps.offlinenavigation.map.routedirection.free.controller;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog.Builder;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;
import com.gps.offlinenavigation.map.routedirection.free.model.map.Tracking;
import com.gps.offlinenavigation.map.routedirection.free.model.util.Variable;
import java.text.SimpleDateFormat;

public class AppSettings {
  private static AppSettings appSettings;
  private Activity activity;
  private RadioGroup algoRG;
  private ViewGroup appSettingsVP;
  private ViewGroup changeMapItemVP;
  private ViewGroup trackingAnalyticsVP;
  private TextView tvdistance;
  private TextView tvdisunit;
  private TextView tvspeed;

  private AppSettings() {
  }

  private void advancedSetting() {
    CheckBox var1 = (CheckBox)this.activity.findViewById(2131624166);
    var1.setChecked(Variable.getVariable().isAdvancedSetting());
    var1.setOnCheckedChangeListener(new OnCheckedChangeListener() {
      public void onCheckedChanged(CompoundButton var1, boolean var2) {
        Variable.getVariable().setAdvancedSetting(var2);

        for(int var3 = 0; var3 < AppSettings.this.algoRG.getChildCount(); ++var3) {
          AppSettings.this.algoRG.getChildAt(var3).setEnabled(var2);
        }

      }
    });

    for(int var2 = 0; var2 < this.algoRG.getChildCount(); ++var2) {
      this.algoRG.getChildAt(var2).setEnabled(Variable.getVariable().isAdvancedSetting());
    }

    this.algoRG.setOnCheckedChangeListener(new android.widget.RadioGroup.OnCheckedChangeListener() {
      public void onCheckedChanged(RadioGroup var1, int var2) {
        switch(var2) {
          case 2131624168:
            Variable.getVariable().setRoutingAlgorithms("dijkstrabi");
            return;
          case 2131624169:
            Variable.getVariable().setRoutingAlgorithms("dijkstraOneToMany");
            return;
          case 2131624170:
            Variable.getVariable().setRoutingAlgorithms("astarbi");
            return;
          case 2131624171:
            Variable.getVariable().setRoutingAlgorithms("astar");
            return;
          default:
        }
      }
    });
    String var3 = Variable.getVariable().getRoutingAlgorithms();
    byte var4 = -1;
    switch(var3.hashCode()) {
      case -703972934:
        if(var3.equals("astarbi")) {
          var4 = 2;
        }
        break;
      case -82905014:
        if(var3.equals("dijkstraOneToMany")) {
          var4 = 1;
        }
        break;
      case 93122099:
        if(var3.equals("astar")) {
          var4 = 3;
        }
        break;
      case 655249565:
        if(var3.equals("dijkstrabi")) {
          var4 = 0;
        }
    }

    switch(var4) {
      case 0:
        ((RadioButton)this.activity.findViewById(2131624168)).setChecked(true);
        return;
      case 1:
        ((RadioButton)this.activity.findViewById(2131624169)).setChecked(true);
        return;
      case 2:
        ((RadioButton)this.activity.findViewById(2131624170)).setChecked(true);
        return;
      case 3:
        ((RadioButton)this.activity.findViewById(2131624171)).setChecked(true);
        return;
      default:
    }
  }

  private void alternateRoute() {
    ((RadioGroup)this.activity.findViewById(2131624162)).setOnCheckedChangeListener(new android.widget.RadioGroup.OnCheckedChangeListener() {
      public void onCheckedChanged(RadioGroup var1, int var2) {
        switch(var2) {
          case 2131624163:
            Variable.getVariable().setWeighting("fastest");
            return;
          case 2131624164:
            Variable.getVariable().setWeighting("shortest");
            return;
          default:
        }
      }
    });
    RadioButton var1 = (RadioButton)this.activity.findViewById(2131624163);
    RadioButton var2 = (RadioButton)this.activity.findViewById(2131624164);
    if(Variable.getVariable().getWeighting().equalsIgnoreCase("fastest")) {
      var1.setChecked(true);
    } else {
      var2.setChecked(true);
    }
  }

  private void chooseMapBtn(ViewGroup var1) {
    this.changeMapItemVP.setOnTouchListener(new OnTouchListener() {
      public boolean onTouch(View var1, MotionEvent var2) {
        switch(var2.getAction()) {
          case 0:
            AppSettings.this.changeMapItemVP.setBackgroundColor(AppSettings.this.activity.getResources().getColor(2131558478));
            return true;
          case 1:
            AppSettings.this.changeMapItemVP.setBackgroundColor(AppSettings.this.activity.getResources().getColor(2131558472));
            AppSettings.this.startMainActivity();
            return true;
          default:
            return false;
        }
      }
    });
  }

  private void clearBtn(final ViewGroup var1, final ViewGroup var2) {
    ((ImageButton)this.activity.findViewById(2131624150)).setOnClickListener(new OnClickListener() {
      public void onClick(View var1x) {
        var1.setVisibility(View.INVISIBLE);
        var2.setVisibility(View.VISIBLE);
      }
    });
  }

  private void confirmWindow() {
    Builder var1 = new Builder(this.activity);
    final EditText var2 = new EditText(this.activity);
    var1.setTitle(this.activity.getResources().getString(2131230796));
    var1.setMessage("path: " + Variable.getVariable().getTrackingFolder().getAbsolutePath() + "/");
    var2.setText((new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss")).format(Long.valueOf(System.currentTimeMillis())));
    var1.setView(var2);
    var1.setPositiveButton(2131230809, new android.content.DialogInterface.OnClickListener() {
      public void onClick(DialogInterface var1, int var2x) {
        Tracking.getTracking().saveAsGPX(var2.getText().toString());
        Tracking.getTracking().stopTracking();
        AppSettings.this.trackingBtnClicked();
      }
    }).setNeutralButton(2131230828, new android.content.DialogInterface.OnClickListener() {
      public void onClick(DialogInterface var1, int var2) {
        Tracking.getTracking().stopTracking();
        AppSettings.this.trackingBtnClicked();
      }
    }).setNegativeButton(2131230791, new android.content.DialogInterface.OnClickListener() {
      public void onClick(DialogInterface var1, int var2) {
        var1.dismiss();
      }
    });
    var1.create().show();
  }

  private void directions() {
    CheckBox var1 = (CheckBox)this.activity.findViewById(2131624165);
    var1.setChecked(Variable.getVariable().isDirectionsON());
    var1.setOnCheckedChangeListener(new OnCheckedChangeListener() {
      public void onCheckedChanged(CompoundButton var1, boolean var2) {
        Variable.getVariable().setDirectionsON(var2);
      }
    });
  }

  public static AppSettings getAppSettings() {
    if(appSettings == null) {
      appSettings = new AppSettings();
    }

    return appSettings;
  }

  private void log(String var1) {
    Log.i(this.getClass().getSimpleName(), "=======" + var1);
  }

  private void logToast(String var1) {
    this.log(var1);
    Toast.makeText(this.activity, var1, 0).show();
  }

  private void openAnalyticsActivity() {
    Intent var1 = new Intent(this.activity, Analytics.class);
    this.activity.startActivity(var1);
  }

  private void resetAnalyticsItem() {
    this.changeMapItemVP.setVisibility(View.GONE);
    this.trackingAnalyticsVP.setVisibility(View.VISIBLE);
    this.trackingAnalyticsBtn();
    this.tvspeed = (TextView)this.activity.findViewById(2131624159);
    this.tvdistance = (TextView)this.activity.findViewById(2131624157);
    this.tvdisunit = (TextView)this.activity.findViewById(2131624158);
    this.updateAnalytics(Tracking.getTracking().getAvgSpeed(), Tracking.getTracking().getDistance());
  }

  private void startMainActivity() {
    Intent var1 = new Intent(this.activity, MainActivity.class);
    var1.putExtra("SELECTNEWMAP", true);
    this.activity.startActivity(var1);
  }

  private void trackingAnalyticsBtn() {
    this.trackingAnalyticsVP.setOnTouchListener(new OnTouchListener() {
      public boolean onTouch(View var1, MotionEvent var2) {
        switch(var2.getAction()) {
          case 0:
            AppSettings.this.trackingAnalyticsVP.setBackgroundColor(AppSettings.this.activity.getResources().getColor(2131558478));
            return true;
          case 1:
            AppSettings.this.trackingAnalyticsVP.setBackgroundColor(AppSettings.this.activity.getResources().getColor(2131558472));
            AppSettings.this.openAnalyticsActivity();
            return true;
          default:
            return false;
        }
      }
    });
  }

  private void trackingBtn(final ViewGroup var1) { // wrong
    this.trackingBtnClicked();
    final ViewGroup var1_1 = (ViewGroup)this.activity.findViewById(2131624152);
    var1_1.setOnTouchListener(new OnTouchListener() {
      public boolean onTouch(View var1x, MotionEvent var2) {
        switch(var2.getAction()) {
          case 0:
            var1_1.setBackgroundColor(AppSettings.this.activity.getResources().getColor(2131558478));
            return true;
          case 1:
            var1_1.setBackgroundColor(AppSettings.this.activity.getResources().getColor(2131558472));
            if(Tracking.getTracking().isTracking()) {
              AppSettings.this.confirmWindow();
            } else {
              Tracking.getTracking().startTracking();
            }

            AppSettings.this.trackingBtnClicked();
            return true;
          default:
            return false;
        }
      }
    });
  }

  public ViewGroup getAppSettingsVP() {
    return this.appSettingsVP;
  }

  public void set(Activity var1, ViewGroup var2) {
    this.activity = var1;
    this.appSettingsVP = (ViewGroup)var1.findViewById(2131624148);
    this.trackingAnalyticsVP = (ViewGroup)var1.findViewById(2131624156);
    this.changeMapItemVP = (ViewGroup)var1.findViewById(2131624151);
    this.clearBtn(this.appSettingsVP, var2);
    this.algoRG = (RadioGroup)var1.findViewById(2131624167);
    this.chooseMapBtn(this.appSettingsVP);
    this.trackingBtn(this.appSettingsVP);
    this.alternateRoute();
    this.advancedSetting();
    this.appSettingsVP.setVisibility(View.VISIBLE);
    var2.setVisibility(View.INVISIBLE);
    this.directions();
    if(Tracking.getTracking().isTracking()) {
      this.resetAnalyticsItem();
    }

  }

  public void trackingBtnClicked() {
    ImageView var1 = (ImageView)this.activity.findViewById(2131624153);
    TextView var2 = (TextView)this.activity.findViewById(2131624155);
    if(Tracking.getTracking().isTracking()) {
      var1.setImageResource(2130837696);
      var2.setTextColor(this.activity.getResources().getColor(2131558468));
      var2.setText(2131230841);
      this.resetAnalyticsItem();
    } else {
      var1.setImageResource(2130837688);
      var2.setTextColor(this.activity.getResources().getColor(2131558475));
      var2.setText(2131230840);
      this.trackingAnalyticsVP.setVisibility(View.GONE);
      this.changeMapItemVP.setVisibility(View.VISIBLE);
    }
  }

  public void updateAnalytics(double var1, double var3) {
    if(var3 < 1000.0D) {
      this.tvdistance.setText(String.valueOf(Math.round(var3)));
      this.tvdisunit.setText(2131230807);
    } else {
      this.tvdistance.setText(String.format("%.1f", new Object[]{Double.valueOf(var3 / 1000.0D)}));
      this.tvdisunit.setText(2131230805);
    }

    this.tvspeed.setText(String.format("%.1f", new Object[]{Double.valueOf(var1)}));
  }
}
