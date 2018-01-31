package com.gps.offlinenavigation.map.routedirection.free.common.logger;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ScrollView;

public class LogFragment extends Fragment {
  private LogView mLogView;
  private ScrollView mScrollView;

  public LogFragment() {
  }

  public LogView getLogView() {
    return this.mLogView;
  }

  public View inflateViews() {
    this.mScrollView = new ScrollView(this.getActivity());
    LayoutParams var1 = new LayoutParams(-1, -1);
    this.mScrollView.setLayoutParams(var1);
    this.mLogView = new LogView(this.getActivity());
    var1 = new LayoutParams(var1);
    var1.height = -2;
    this.mLogView.setLayoutParams(var1);
    this.mLogView.setClickable(true);
    this.mLogView.setFocusable(true);
    this.mLogView.setTypeface(Typeface.MONOSPACE);
    double var3 = (double)this.getResources().getDisplayMetrics().density;
    int var2 = (int)((double)16 * var3 + 0.5D);
    this.mLogView.setPadding(var2, var2, var2, var2);
    this.mLogView.setCompoundDrawablePadding(var2);
    this.mLogView.setGravity(80);
    this.mLogView.setTextAppearance(this.getActivity(), 16974079);
    this.mScrollView.addView(this.mLogView);
    return this.mScrollView;
  }

  public View onCreateView(LayoutInflater var1, ViewGroup var2, Bundle var3) {
    View var4 = this.inflateViews();
    this.mLogView.addTextChangedListener(new TextWatcher() {
      public void afterTextChanged(Editable var1) {
        LogFragment.this.mScrollView.fullScroll(130);
      }

      public void beforeTextChanged(CharSequence var1, int var2, int var3, int var4) {
      }

      public void onTextChanged(CharSequence var1, int var2, int var3, int var4) {
      }
    });
    return var4;
  }
}