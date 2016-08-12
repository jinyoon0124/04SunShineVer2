package com.example.android.sunshine.app.muzei;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;

import com.example.android.sunshine.app.MainActivity;
import com.example.android.sunshine.app.Utility;
import com.example.android.sunshine.app.data.WeatherContract;
import com.example.android.sunshine.app.sync.SunshineSyncAdapter;
import com.google.android.apps.muzei.api.Artwork;
import com.google.android.apps.muzei.api.MuzeiArtSource;

/**
 * Created by Jin Yoon on 8/12/2016.
 */
public class WeatherMuzeiSource extends MuzeiArtSource {
    /**
     * Remember to call this constructor from an empty constructor!
     *
     * @param name Should be an ID-style name for your source, usually just the class name. This is
     *             not user-visible and is only used for {@linkplain #getSharedPreferences()
     *             storing preferences} and in system log output.
     */
    public final String LOG_TAG = WeatherMuzeiSource.class.getSimpleName();
    private final String[] WEATHER_PROJECTION = {
            WeatherContract.WeatherEntry.COLUMN_WEATHER_ID,
            WeatherContract.WeatherEntry.COLUMN_SHORT_DESC};

    static final int INDEX_WEATHER_ID = 0;
    static final int INDEX_SHORT_DESC = 1;

    public WeatherMuzeiSource() {
        super("WeatherMuzeiSource");
    }

    @Override
    protected void onUpdate(int reason) {
        String locationQuery = Utility.getPreferredLocation(this);
        Uri weatherUri = WeatherContract.WeatherEntry.buildWeatherLocationWithDate(
                locationQuery, System.currentTimeMillis());

        Cursor cursor = getContentResolver().query(
                weatherUri,
                WEATHER_PROJECTION,
                null,
                null,
                WeatherContract.WeatherEntry.COLUMN_DATE + " ASC");

        if(cursor.moveToFirst()){
            int weatherId = cursor.getInt(INDEX_WEATHER_ID);
            String desc = cursor.getString(INDEX_SHORT_DESC);

            String imageUrl = Utility.getImageUrlForWeatherCondition(weatherId);
            if(imageUrl!=null){
                publishArtwork(new Artwork.Builder()
                        .imageUri(Uri.parse((imageUrl)))
                        .title(desc)
                        .byline(locationQuery)
                        .viewIntent(new Intent(this, MainActivity.class))
                        .build());
            }
        }
        cursor.close();
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        super.onHandleIntent(intent);
        boolean dataUpdated = intent !=null &&
                SunshineSyncAdapter.ACTION_DATA_UPDATED.equals(intent.getAction());
        if(dataUpdated && isEnabled()){
            onUpdate(UPDATE_REASON_OTHER);
        }
    }
}
