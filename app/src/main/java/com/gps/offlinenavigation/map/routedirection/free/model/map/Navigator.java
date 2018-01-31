package com.gps.offlinenavigation.map.routedirection.free.model.map;

import com.gps.offlinenavigation.map.routedirection.free.model.listeners.NavigatorListener;
import com.gps.offlinenavigation.map.routedirection.free.model.util.Variable;
import com.graphhopper.GHResponse;
import com.graphhopper.util.Helper;
import com.graphhopper.util.Instruction;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Navigator {
  private static Navigator navigator = null;
  private GHResponse ghResponse = null;
  private List<NavigatorListener> listeners = new ArrayList();
  private boolean on = false;

  private Navigator() {
  }

  public static Navigator getNavigator() {
    if(navigator == null) {
      navigator = new Navigator();
    }

    return navigator;
  }

  public void addListener(NavigatorListener var1) {
    this.listeners.add(var1);
  }

  protected void broadcast() {
    Iterator var1 = this.listeners.iterator();

    while(var1.hasNext()) {
      ((NavigatorListener)var1.next()).statusChanged(this.isOn());
    }

  }

  public String getDirectionDescription(Instruction var1) {
    if(var1.getSign() == 4) {
      return "Navigation End";
    } else {
      String var4 = var1.getName();
      int var2 = var1.getSign();
      if(var2 == 0) {
        return Helper.isEmpty(var4)?"Continue":"Continue onto " + var4;
      } else {
        String var3 = "";
        String var5 = var3;
        switch(var2) {
          case -6:
            var5 = "Leave roundabout";
          case -5:
          case -4:
          case 0:
          case 4:
            break;
          case -3:
            var5 = "Turn sharp left";
            break;
          case -2:
            var5 = "Turn left";
            break;
          case -1:
            var5 = "Turn slight left";
            break;
          case 1:
            var5 = "Turn slight right";
            break;
          case 2:
            var5 = "Turn right";
            break;
          case 3:
            var5 = "Turn sharp right";
            break;
          case 5:
            var5 = "Reached via";
            break;
          case 6:
            var5 = "Use roundabout";
            break;
          default:
            var5 = var3;
        }

        return Helper.isEmpty(var4)?var5:var5 + " onto " + var4;
      }
    }
  }

  public int getDirectionSign(Instruction var1) {
    int var2 = 2130837691;
    switch(var1.getSign()) {
      case -5:
      case -4:
      default:
        var2 = 0;
      case -6:
      case 6:
        return var2;
      case -3:
        return 2130837702;
      case -2:
        return 2130837700;
      case -1:
        return 2130837704;
      case 0:
        return 2130837660;
      case 1:
        return 2130837705;
      case 2:
        return 2130837701;
      case 3:
        return 2130837703;
      case 4:
        return 2130837670;
      case 5:
        return 2130837689;
    }
  }

  public String getDistance() {
    if(this.getGhResponse() == null) {
      return "0 2131230805";
    } else {
      double var1 = this.getGhResponse().getDistance();
      return var1 < 1000.0D?Math.round(var1) + " meter":(float)((int)(var1 / 100.0D)) / 10.0F + " km";
    }
  }

  public String getDistance(Instruction var1) {
    if(var1.getSign() == 4) {
      return "";
    } else {
      double var2 = var1.getDistance();
      return var2 < 1000.0D?Math.round(var2) + " meter":(float)((int)(var2 / 100.0D)) / 10.0F + " km";
    }
  }

  public GHResponse getGhResponse() {
    return this.ghResponse;
  }

  public String getTime() {
    if(this.getGhResponse() == null) {
      return " ";
    } else {
      int var1 = Math.round((float)(this.getGhResponse().getMillis() / 60000L));
      return var1 < 60?var1 + " min":var1 / 60 + " h: " + var1 % 60 + " m";
    }
  }

  public String getTime(Instruction var1) {
    return Math.round((float)(this.getGhResponse().getMillis() / 60000L)) + " min";
  }

  public int getTravelModeResId(boolean var1) {
    byte var3 = 0;
    byte var4 = -1;
    String var2;
    if(var1) {
      var2 = Variable.getVariable().getTravelMode();
      switch(var2.hashCode()) {
        case 98260:
          var3 = var4;
          if(var2.equals("car")) {
            var3 = 2;
          }
          break;
        case 3023841:
          var3 = var4;
          if(var2.equals("bike")) {
            var3 = 1;
          }
          break;
        case 3148910:
          var3 = var4;
          if(var2.equals("foot")) {
            var3 = 0;
          }
          break;
        default:
          var3 = var4;
      }

      switch(var3) {
        case 0:
          return 2130837666;
        case 1:
          return 2130837661;
        case 2:
          return 2130837663;
      }
    } else {
      label49: {
        var2 = Variable.getVariable().getTravelMode();
        switch(var2.hashCode()) {
          case 98260:
            if(var2.equals("car")) {
              var3 = 2;
              break label49;
            }
            break;
          case 3023841:
            if(var2.equals("bike")) {
              var3 = 1;
              break label49;
            }
            break;
          case 3148910:
            if(var2.equals("foot")) {
              break label49;
            }
        }

        var3 = -1;
      }

      switch(var3) {
        case 0:
          return 2130837667;
        case 1:
          return 2130837662;
        case 2:
          return 2130837664;
      }
    }

    throw new NullPointerException("this method can only used when Variable class is ready!");
  }

  public boolean isOn() {
    return this.on;
  }

  public void setGhResponse(GHResponse var1) {
    this.ghResponse = var1;
    if(var1 == null) {
      ;
    }

    boolean var2;
    if(var1 != null) {
      var2 = true;
    } else {
      var2 = false;
    }

    this.setOn(var2);
  }

  public void setOn(boolean var1) {
    this.on = var1;
    this.broadcast();
  }

  public String toString() {
    String var1 = "";
    String var2 = var1;
    if(this.ghResponse.getInstructions() != null) {
      Iterator var3 = this.ghResponse.getInstructions().iterator();

      while(true) {
        var2 = var1;
        if(!var3.hasNext()) {
          break;
        }

        Instruction var4 = (Instruction)var3.next();
        var1 = var1 + "------>\ntime <long>: " + var4.getTime() + "\nname: street name" + var4.getName() + "\nannotation <InstructionAnnotation>" + var4.getAnnotation() + "\ndistance" + var4.getDistance() + "\nsign <int>:" + var4.getSign() + "\nPoints <PointsList>: " + var4.getPoints() + "\n";
      }
    }

    return var2;
  }
}