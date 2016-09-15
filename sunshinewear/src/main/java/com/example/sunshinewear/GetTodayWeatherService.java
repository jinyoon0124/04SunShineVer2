package com.example.sunshinewear;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.WearableListenerService;

public class GetTodayWeatherService extends WearableListenerService {
    public GetTodayWeatherService() {
    }

    private final String PATH = "/today-weather";
    String mLowTemp;

    @Override
    public void onDataChanged(DataEventBuffer dataEvents) {
        Log.v("DATACHANGED :::", "method called");
        for(DataEvent dataEvent : dataEvents){
            if(dataEvent.getType() == DataEvent.TYPE_CHANGED){
                DataMap dataMap = DataMapItem.fromDataItem(dataEvent.getDataItem()).getDataMap();
                String path = dataEvent.getDataItem().getUri().getPath();
                Log.v("DATACHANGED :::", "inside for and if statement");
                if(path.equals(PATH)){
                    Log.v("DATACHANGED :::", "PAth equals");
                    mLowTemp = dataMap.getString("lowTemp");
                    SharedPreferences spf = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    SharedPreferences.Editor ed = spf.edit();
                    ed.putString("lowTemp", mLowTemp);
                    ed.commit();
                }

            }
        }
    }
}
