package com.gps.offlinenavigation.map.routedirection.free;

import com.gps.offlinenavigation.map.routedirection.free.POJO.Example;
import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Query;

public abstract interface RetrofitMaps
{
  @GET("api/directions/json?key=AIzaSyB-IkOHwXFwc_ekLEEQobAASvUD5ZIG3ww")
  public abstract Call<Example> getDistanceDuration(@Query("units") String paramString1, @Query("origin") String paramString2, @Query("destination") String paramString3, @Query("mode") String paramString4);
}