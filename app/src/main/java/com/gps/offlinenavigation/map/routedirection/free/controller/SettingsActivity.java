package com.gps.offlinenavigation.map.routedirection.free.controller;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnTouchListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.CompoundButton.OnCheckedChangeListener;
import com.gps.offlinenavigation.map.routedirection.free.model.util.SetStatusBarColor;
import com.gps.offlinenavigation.map.routedirection.free.model.util.Variable;

public class SettingsActivity extends AppCompatActivity {
  private RadioGroup algoRG;

  public SettingsActivity() {
  }

  private void advancedSetting() {
    CheckBox var1 = (CheckBox)this.findViewById(2131624127);
    var1.setChecked(Variable.getVariable().isAdvancedSetting());
    var1.setOnCheckedChangeListener(new OnCheckedChangeListener() {
      public void onCheckedChanged(CompoundButton var1, boolean var2) {
        Variable.getVariable().setAdvancedSetting(var2);

        for(int var3 = 0; var3 < SettingsActivity.this.algoRG.getChildCount(); ++var3) {
          SettingsActivity.this.algoRG.getChildAt(var3).setEnabled(var2);
        }

      }
    });

    for(int var2 = 0; var2 < this.algoRG.getChildCount(); ++var2) {
      this.algoRG.getChildAt(var2).setEnabled(Variable.getVariable().isAdvancedSetting());
    }

    this.algoRG.setOnCheckedChangeListener(new android.widget.RadioGroup.OnCheckedChangeListener() {
      public void onCheckedChanged(RadioGroup var1, int var2) {
        switch(var2) {
          case 2131624129:
            Variable.getVariable().setRoutingAlgorithms("dijkstrabi");
            return;
          case 2131624130:
            Variable.getVariable().setRoutingAlgorithms("dijkstraOneToMany");
            return;
          case 2131624131:
            Variable.getVariable().setRoutingAlgorithms("astarbi");
            return;
          case 2131624132:
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
        ((RadioButton)this.findViewById(2131624129)).setChecked(true);
        return;
      case 1:
        ((RadioButton)this.findViewById(2131624130)).setChecked(true);
        return;
      case 2:
        ((RadioButton)this.findViewById(2131624131)).setChecked(true);
        return;
      case 3:
        ((RadioButton)this.findViewById(2131624132)).setChecked(true);
        return;
      default:
    }
  }

  private void alternateRoute() {
    ((RadioGroup)this.findViewById(2131624123)).setOnCheckedChangeListener(new android.widget.RadioGroup.OnCheckedChangeListener() {
      public void onCheckedChanged(RadioGroup var1, int var2) {
        switch(var2) {
          case 2131624124:
            Variable.getVariable().setWeighting("fastest");
            return;
          case 2131624125:
            Variable.getVariable().setWeighting("shortest");
            return;
          default:
        }
      }
    });
    RadioButton var1 = (RadioButton)this.findViewById(2131624124);
    RadioButton var2 = (RadioButton)this.findViewById(2131624125);
    if(Variable.getVariable().getWeighting().equalsIgnoreCase("fastest")) {
      var1.setChecked(true);
    } else {
      var2.setChecked(true);
    }
  }

  private void directions() {
    CheckBox var1 = (CheckBox)this.findViewById(2131624126);
    var1.setChecked(Variable.getVariable().isDirectionsON());
    var1.setOnCheckedChangeListener(new OnCheckedChangeListener() {
      public void onCheckedChanged(CompoundButton var1, boolean var2) {
        Variable.getVariable().setDirectionsON(var2);
      }
    });
  }

  private void downloadBtn() {
    final ViewGroup var1 = (ViewGroup)this.findViewById(2131624120);
    var1.setOnTouchListener(new OnTouchListener() {
      public boolean onTouch(View var1x, MotionEvent var2) {
        switch(var2.getAction()) {
          case 0:
            var1.setBackgroundColor(SettingsActivity.this.getResources().getColor(2131558478));
            return true;
          case 1:
            var1.setBackgroundColor(SettingsActivity.this.getResources().getColor(2131558472));
            SettingsActivity.this.startDownloadActivity();
            return true;
          default:
            return false;
        }
      }
    });
  }

  private void startDownloadActivity() {
    this.startActivity(new Intent(this, DownloadMapActivity.class));
  }

  public void init() {
    this.algoRG = (RadioGroup)this.findViewById(2131624128);
    this.downloadBtn();
    this.alternateRoute();
    this.advancedSetting();
    this.directions();
  }

  protected void onCreate(Bundle var1) {
    super.onCreate(var1);
    this.setContentView(2130968613);
    if(this.getSupportActionBar() != null) {
      this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
      this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    (new SetStatusBarColor()).setStatusBarColor(this.findViewById(2131624119), this.getResources().getColor(2131558476), this);
    this.init();
  }

  public boolean onOptionsItemSelected(MenuItem var1) {
    switch(var1.getItemId()) {
      case 16908332:
        this.finish();
        return true;
      default:
        return super.onOptionsItemSelected(var1);
    }
  }
}