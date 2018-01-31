package com.gps.offlinenavigation.map.routedirection.free;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnClickListener;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AlertDialog.Builder;
import android.widget.Toast;

public class Permissions extends AppCompatActivity {
  private static final int PERMISSION_CALLBACK_CONSTANT = 100;
  private static final int REQUEST_PERMISSION_SETTING = 101;
  private SharedPreferences permissionStatus;
  String[] permissionsRequired = new String[]{"android.permission.WRITE_EXTERNAL_STORAGE", "android.permission.ACCESS_COARSE_LOCATION", "android.permission.ACCESS_FINE_LOCATION"};
  private boolean sentToSettings = false;

  public Permissions() {
  }

  private void proceedAfterPermission() {
    this.startActivity(new Intent(this, Dashboard.class));
    this.finish();
  }

  public void checkMultipermission() {
    if(ActivityCompat.checkSelfPermission(this, this.permissionsRequired[0]) == 0 && ActivityCompat.checkSelfPermission(this, this.permissionsRequired[1]) == 0 && ActivityCompat.checkSelfPermission(this, this.permissionsRequired[2]) == 0) {
      this.proceedAfterPermission();
    } else {
      Builder var1;
      if(!ActivityCompat.shouldShowRequestPermissionRationale(this, this.permissionsRequired[0]) && !ActivityCompat.shouldShowRequestPermissionRationale(this, this.permissionsRequired[1]) && !ActivityCompat.shouldShowRequestPermissionRationale(this, this.permissionsRequired[2])) {
        if(this.permissionStatus.getBoolean(this.permissionsRequired[0], false)) {
          var1 = new Builder(this);
          var1.setTitle("Need Multiple Permissions");
          var1.setMessage("This app needs Follwoing permissions.");
          var1.setPositiveButton("Grant", new OnClickListener() {
            public void onClick(DialogInterface var1, int var2) {
              var1.cancel();
              Permissions.this.sentToSettings = true;
              Intent var3 = new Intent("android.settings.APPLICATION_DETAILS_SETTINGS");
              var3.setData(Uri.fromParts("package", Permissions.this.getPackageName(), (String)null));
              Permissions.this.startActivityForResult(var3, 101);
              Toast.makeText(Permissions.this.getBaseContext(), "Go to Permissions Permission", 1).show();
            }
          });
          var1.setNegativeButton("Cancel", new OnClickListener() {
            public void onClick(DialogInterface var1, int var2) {
              Intent var3 = new Intent(Permissions.this, Permissions.class);
              Permissions.this.startActivity(var3);
              Permissions.this.finish();
              var1.cancel();
            }
          });
          var1.show();
        } else {
          ActivityCompat.requestPermissions(this, this.permissionsRequired, 100);
        }
      } else {
        var1 = new Builder(this);
        var1.setTitle("Need Multiple Permissions");
        var1.setMessage("This app needs Follwoing permissions.");
        var1.setPositiveButton("Grant", new OnClickListener() {
          public void onClick(DialogInterface var1, int var2) {
            var1.cancel();
            ActivityCompat.requestPermissions(Permissions.this, Permissions.this.permissionsRequired, 100);
          }
        });
        var1.setNegativeButton("Cancel", new OnClickListener() {
          public void onClick(DialogInterface var1, int var2) {
            Intent var3 = new Intent(Permissions.this, Permissions.class);
            Permissions.this.startActivity(var3);
            Permissions.this.finish();
            var1.cancel();
          }
        });
        var1.show();
      }

      Editor var2 = this.permissionStatus.edit();
      var2.putBoolean(this.permissionsRequired[0], true);
      var2.apply();
    }
  }

  protected void onActivityResult(int var1, int var2, Intent var3) {
    super.onActivityResult(var1, var2, var3);
    if(var1 == 101 && ActivityCompat.checkSelfPermission(this, this.permissionsRequired[0]) == 0) {
      this.proceedAfterPermission();
    }

  }

  protected void onCreate(Bundle var1) {
    super.onCreate(var1);
    this.setContentView(2130968616);
    this.permissionStatus = this.getSharedPreferences("Khalil", 0);
    this.checkMultipermission();
  }

  protected void onPostResume() {
    super.onPostResume();
    if(this.sentToSettings && ActivityCompat.checkSelfPermission(this, this.permissionsRequired[0]) == 0) {
      this.proceedAfterPermission();
    }

  }

  public void onRequestPermissionsResult(int var1, @NonNull String[] var2, @NonNull int[] var3) {
    super.onRequestPermissionsResult(var1, var2, var3);
    if(var1 == 100) {
      boolean var5 = false;

      for(int var4 = 0; var4 < var3.length; ++var4) {
        if(var3[var4] != 0) {
          var5 = false;
          break;
        }

        var5 = true;
      }

      if(!var5) {
        if(!ActivityCompat.shouldShowRequestPermissionRationale(this, this.permissionsRequired[0]) && !ActivityCompat.shouldShowRequestPermissionRationale(this, this.permissionsRequired[1]) && !ActivityCompat.shouldShowRequestPermissionRationale(this, this.permissionsRequired[2])) {
          Toast.makeText(this.getBaseContext(), "Unable to get Permission", 1).show();
          return;
        }

        Builder var6 = new Builder(this);
        var6.setTitle("Need Multiple Permissions");
        var6.setMessage("This app needs Following permissions.");
        var6.setPositiveButton("Grant", new OnClickListener() {
          public void onClick(DialogInterface var1, int var2) {
            var1.cancel();
            ActivityCompat.requestPermissions(Permissions.this, Permissions.this.permissionsRequired, 100);
          }
        });
        var6.setNegativeButton("Cancel", new OnClickListener() {
          public void onClick(DialogInterface var1, int var2) {
            Intent var3 = new Intent(Permissions.this, Permissions.class);
            Permissions.this.startActivity(var3);
            Permissions.this.finish();
            var1.cancel();
          }
        });
        var6.show();
        return;
      }

      this.proceedAfterPermission();
    }

  }

  public void openWifySettings() {
    Builder var1 = new Builder(this);
    var1.setMessage("Internet Disabled. Do You Want To Enable It?").setCancelable(false).setPositiveButton("Yes", new OnClickListener() {
      public void onClick(DialogInterface var1, int var2) {
        Intent var3 = new Intent("android.intent.action.MAIN");
        var3.setClassName("com.android.settings", "com.android.settings.wifi.WifiSettings");
        Permissions.this.startActivity(var3);
      }
    }).setNegativeButton("No", new OnClickListener() {
      public void onClick(DialogInterface var1, int var2) {
        var1.cancel();
      }
    });
    var1.create().show();
  }
}
