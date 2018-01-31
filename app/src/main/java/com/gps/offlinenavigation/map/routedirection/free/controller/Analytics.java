package com.gps.offlinenavigation.map.routedirection.free.controller;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;
import com.gps.offlinenavigation.map.routedirection.free.model.dataType.SportCategory;
import com.gps.offlinenavigation.map.routedirection.free.model.listeners.TrackingListener;
import com.gps.offlinenavigation.map.routedirection.free.model.map.Tracking;
import com.gps.offlinenavigation.map.routedirection.free.model.util.Calorie;
import com.gps.offlinenavigation.map.routedirection.free.model.util.SetStatusBarColor;
import com.gps.offlinenavigation.map.routedirection.free.model.util.SpinnerAdapter;
import com.gps.offlinenavigation.map.routedirection.free.model.util.Variable;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.LegendRenderer.LegendAlign;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import java.util.ArrayList;

public class Analytics extends AppCompatActivity implements TrackingListener {
  private TextView avgSpeedTV;
  private Handler calorieUpdateHandler;
  private TextView caloriesTV;
  private LineGraphSeries<DataPoint> distanceGraphSeries;
  private TextView distanceTV;
  private TextView distanceUnitTV;
  private Handler durationHandler;
  private long durationStartTime;
  private TextView durationTV;
  private GraphView graph;
  private boolean hasNewPoint;
  private TextView maxSpeedTV;
  private double maxXaxis;
  private double maxY1axis;
  private double maxY2axis;
  private LineGraphSeries<DataPoint> speedGraphSeries;
  private Spinner spinner;
  private Runnable updateCalorieThread = new Runnable() {
    public void run() {
      Analytics.this.updateCalorieBurned();
      Analytics.this.calorieUpdateHandler.postDelayed(this, 10000L);
      if(Analytics.this.hasNewPoint) {
        Tracking.getTracking().requestDistanceGraphSeries();
        Analytics.this.hasNewPoint = false;
      }

    }
  };
  private Runnable updateTimerThread = new Runnable() {
    public void run() {
      int var1 = (int)((System.currentTimeMillis() - Analytics.this.durationStartTime) / 1000L);
      int var2 = var1 / 60;
      int var3 = var2 / 60;
      Analytics.this.durationTV.setText("" + String.format("%02d", new Object[]{Integer.valueOf(var3)}) + ":" + String.format("%02d", new Object[]{Integer.valueOf(var2 % 60)}) + ":" + String.format("%02d", new Object[]{Integer.valueOf(var1 % 60)}));
      Analytics.this.durationHandler.postDelayed(this, 500L);
    }
  };

  public Analytics() {
  }

  private double getMaxValue(double var1, double var3) {
    return var3 > 1.1D * var1?var3:this.getMaxValue(var1, 2.0D * var3);
  }

  private double getSportCategory() {
    return ((SportCategory)this.spinner.getSelectedItem()).getSportMET();
  }

  private void initGraph() {
    this.hasNewPoint = false;
    this.maxXaxis = 0.1D;
    this.maxY1axis = 10.0D;
    this.maxY2axis = 0.4D;
    this.graph = (GraphView)this.findViewById(2131624076);
    this.speedGraphSeries = new LineGraphSeries();
    this.graph.addSeries(this.speedGraphSeries);
    this.graph.getGridLabelRenderer().setVerticalLabelsColor(-16738680);
    this.graph.getViewport().setYAxisBoundsManual(true);
    this.resetGraphY1MaxValue();
    this.distanceGraphSeries = new LineGraphSeries();
    this.graph.getViewport().setScalable(true);
    this.graph.getViewport().setScrollable(true);
    this.graph.getViewport().setMinX(0.0D);
    this.graph.getSecondScale().addSeries(this.distanceGraphSeries);
    this.graph.getSecondScale().setMinY(0.0D);
    this.resetGraphY2MaxValue();
    this.graph.getGridLabelRenderer().setVerticalLabelsSecondScaleColor(-43230);
    this.speedGraphSeries.setTitle("Speed km/h");
    this.speedGraphSeries.setColor(-16738680);
    this.distanceGraphSeries.setTitle("Distance km");
    this.distanceGraphSeries.setColor(-43230);
    this.graph.getLegendRenderer().setVisible(true);
    this.graph.getLegendRenderer().setAlign(LegendAlign.TOP);
  }

  private void initStatus() {
    this.durationStartTime = Tracking.getTracking().getTimeStart();
    this.distanceTV = (TextView)this.findViewById(2131624069);
    this.distanceUnitTV = (TextView)this.findViewById(2131624070);
    this.caloriesTV = (TextView)this.findViewById(2131624072);
    this.maxSpeedTV = (TextView)this.findViewById(2131624073);
    this.durationTV = (TextView)this.findViewById(2131624074);
    this.avgSpeedTV = (TextView)this.findViewById(2131624075);
    this.updateDis(Tracking.getTracking().getDistance());
    this.updateAvgSp(Tracking.getTracking().getAvgSpeed());
    this.updateMaxSp(Tracking.getTracking().getMaxSpeed());
    this.updateCalorieBurned();
  }

  private void intSpinner() {
    this.spinner = (Spinner)this.findViewById(2131624071);
    ArrayList var1 = new ArrayList();
    var1.add(new SportCategory("run", Integer.valueOf(2130837665), 7.0D));
    var1.add(new SportCategory("walk", Integer.valueOf(2130837667), 3.0D));
    var1.add(new SportCategory("bike", Integer.valueOf(2130837662), 8.0D));
    SpinnerAdapter var2 = new SpinnerAdapter(this, 2130968617, var1);
    var2.setDropDownViewResource(17367049);
    this.spinner.setAdapter(var2);
    this.spinner.setSelection(Variable.getVariable().getSportCategoryIndex());
    this.spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
      public void onItemSelected(AdapterView<?> var1, View var2, int var3, long var4) {
        Analytics.this.updateCalorieBurned();
        Variable.getVariable().setSportCategoryIndex(var3);
      }

      public void onNothingSelected(AdapterView<?> var1) {
      }
    });
  }

  private void log(String var1) {
    System.out.println(this.getClass().getSimpleName() + "-------------------" + var1);
    this.logT(var1);
  }

  private void logT(String var1) {
    Toast.makeText(this, var1, 0).show();
  }

  private void updateCalorieBurned() {
    this.caloriesTV.setText(String.format("%.2f", new Object[]{Double.valueOf(Calorie.CalorieBurned(this.getSportCategory(), Tracking.getTracking().getDurationInHours()))}));
  }

  private void updateMaxSp(double var1) {
    this.maxSpeedTV.setText(String.format("%.2f", new Object[]{Double.valueOf(var1)}));
  }

  public void addDistanceGraphSeriesPoint(DataPoint var1, DataPoint var2) {
    this.hasNewPoint = true;
  }

  protected void onCreate(Bundle var1) {
    super.onCreate(var1);
    this.setContentView(2130968603);
    if(this.getSupportActionBar() != null) {
      this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    (new SetStatusBarColor()).setStatusBarColor(this.findViewById(2131624068), this.getResources().getColor(2131558475), this);
    this.durationStartTime = 0L;
    this.intSpinner();
    this.initStatus();
    this.durationHandler = new Handler();
    this.calorieUpdateHandler = new Handler();
    this.initGraph();
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

  public void onPause() {
    super.onPause();
    this.durationHandler.removeCallbacks(this.updateTimerThread);
    this.calorieUpdateHandler.removeCallbacks(this.updateCalorieThread);
    Tracking.getTracking().removeListener(this);
  }

  public void onResume() {
    super.onResume();
    this.durationHandler.postDelayed(this.updateTimerThread, 500L);
    this.calorieUpdateHandler.postDelayed(this.updateCalorieThread, 60000L);
    Tracking.getTracking().addListener(this);
    Tracking.getTracking().requestDistanceGraphSeries();
  }

  public void resetGraphXMaxValue() {
    double var1 = Tracking.getTracking().getDurationInHours();
    if(var1 > this.maxXaxis * 0.9D) {
      this.maxXaxis = this.getMaxValue(var1, this.maxXaxis);
    }

    this.graph.getViewport().setMaxX(this.maxXaxis);
  }

  public void resetGraphY1MaxValue() {
    double var1 = Tracking.getTracking().getMaxSpeed();
    if(var1 > this.maxY1axis) {
      int var3 = (int)(0.9999D + var1);
      this.maxY1axis = (double)(var3 + 4 - var3 % 4);
    }

    this.graph.getViewport().setMaxY(this.maxY1axis);
  }

  public void resetGraphY2MaxValue() {
    double var1 = Tracking.getTracking().getDistanceKm();
    if(var1 > this.maxY2axis * 0.9D) {
      this.maxY2axis = this.getMaxValue(var1, this.maxY2axis);
    }

    this.graph.getSecondScale().setMaxY(this.maxY2axis);
  }

  public void updateAvgSp(double var1) {
    this.avgSpeedTV.setText(String.format("%.2f", new Object[]{Double.valueOf(var1)}));
  }

  public void updateAvgSpeed(Double var1) {
    this.updateAvgSp(var1.doubleValue());
  }

  public void updateDis(double var1) {
    if(var1 < 1000.0D) {
      this.distanceTV.setText(String.valueOf(Math.round(var1)));
      this.distanceUnitTV.setText(2131230807);
    } else {
      this.distanceTV.setText(String.format("%.2f", new Object[]{Double.valueOf(var1 / 1000.0D)}));
      this.distanceUnitTV.setText(2131230805);
    }
  }

  public void updateDistance(Double var1) {
    this.updateDis(var1.doubleValue());
  }

  public void updateDistanceGraphSeries(DataPoint[][] var1) {
    this.resetGraphY1MaxValue();
    this.resetGraphY2MaxValue();
    this.speedGraphSeries.resetData(var1[0]);
    this.distanceGraphSeries.resetData(var1[1]);
    double var2 = this.speedGraphSeries.getHighestValueY();
    Tracking.getTracking().setMaxSpeed(var2);
    this.updateMaxSpeed(Double.valueOf(var2));
  }

  public void updateMaxSpeed(Double var1) {
    this.updateMaxSp(var1.doubleValue());
  }
}