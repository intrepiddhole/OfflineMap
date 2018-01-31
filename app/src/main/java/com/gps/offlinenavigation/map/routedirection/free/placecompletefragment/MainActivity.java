package com.gps.offlinenavigation.map.routedirection.free.placecompletefragment;

import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.location.places.ui.PlaceAutocomplete.IntentBuilder;
import com.gps.offlinenavigation.map.routedirection.free.SampleActivityBase;

public class MainActivity extends SampleActivityBase {
  private static final int REQUEST_CODE_AUTOCOMPLETE = 1;
  private TextView mPlaceAttribution;
  private TextView mPlaceDetailsText;

  public MainActivity() {
  }

  private static Spanned formatPlaceDetails(Resources var0, CharSequence var1, String var2, CharSequence var3, CharSequence var4, Uri var5) {
    Log.e("SampleActivityBase", var0.getString(2131230819, new Object[]{var1, var2, var3, var4, var5}));
    return Html.fromHtml(var0.getString(2131230819, new Object[]{var1, var2, var3, var4, var5}));
  }

  private void openAutocompleteActivity() {
    try {
      this.startActivityForResult((new IntentBuilder(1)).build(this), 1);
    } catch (GooglePlayServicesRepairableException var2) {
      GoogleApiAvailability.getInstance().getErrorDialog(this, var2.getConnectionStatusCode(), 0).show();
    } catch (GooglePlayServicesNotAvailableException var3) {
      String var1 = "Google Play Services is not available: " + GoogleApiAvailability.getInstance().getErrorString(var3.errorCode);
      Log.e("SampleActivityBase", var1);
      Toast.makeText(this, var1, 0).show();
    }
  }

  protected void onActivityResult(int var1, int var2, Intent var3) {
    super.onActivityResult(var1, var2, var3);
    if(var1 == 1) {
      if(var2 == -1) {
        Place var4 = PlaceAutocomplete.getPlace(this, var3);
        Log.i("SampleActivityBase", "Place Selected: " + var4.getName());
        this.mPlaceDetailsText.setText(formatPlaceDetails(this.getResources(), var4.getName(), var4.getId(), var4.getAddress(), var4.getPhoneNumber(), var4.getWebsiteUri()));
        CharSequence var5 = var4.getAttributions();
        if(TextUtils.isEmpty(var5)) {
          this.mPlaceAttribution.setText("");
          return;
        }

        this.mPlaceAttribution.setText(Html.fromHtml(var5.toString()));
      } else {
        if(var2 == 2) {
          Status var6 = PlaceAutocomplete.getStatus(this, var3);
          Log.e("SampleActivityBase", "Error: Status = " + var6.toString());
          return;
        }

        if(var2 == 0) {
          return;
        }
      }
    }

  }

  protected void onCreate(Bundle var1) {
    super.onCreate(var1);
    this.setContentView(2130968607);
    ((Button)this.findViewById(2131624090)).setOnClickListener(new OnClickListener() {
      public void onClick(View var1) {
        MainActivity.this.openAutocompleteActivity();
      }
    });
    this.mPlaceDetailsText = (TextView)this.findViewById(2131624091);
    this.mPlaceAttribution = (TextView)this.findViewById(2131624092);
  }
}