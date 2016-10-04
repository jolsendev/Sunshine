package com.example.android.sunshine.app;
import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.sunshine.app.data.WeatherContract;

import org.w3c.dom.Text;

public class ForecastAdapter extends CursorAdapter {
    private final int VIEW_TYPE_TODAY = 0;
    private final int VIEW_TYPE_FUTURE_DAY= 1;

    public ForecastAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    /**
     * Prepare the weather high/lows for presentation.
     */
    private String formatHighLows(double high, double low) {
        boolean isMetric = Utility.isMetric(mContext);
        String highLowStr = Utility.formatTemperature(high, isMetric) + "/" + Utility.formatTemperature(low, isMetric);
        return highLowStr;
    }

    /*
        This is ported from FetchWeatherTask --- but now we go straight from the cursor to the
        string.
     */
    private String convertCursorRowToUXFormat(Cursor cursor) {
        // get row indices for our cursor
//        int idx_max_temp = cursor.getColumnIndex(WeatherContract.WeatherEntry.COLUMN_MAX_TEMP);
//        int idx_min_temp = cursor.getColumnIndex(WeatherContract.WeatherEntry.COLUMN_MIN_TEMP);
//        int idx_date = cursor.getColumnIndex(WeatherContract.WeatherEntry.COLUMN_DATE);
//        int idx_short_desc = cursor.getColumnIndex(WeatherContract.WeatherEntry.COLUMN_SHORT_DESC);

        String highAndLow = formatHighLows(
                cursor.getDouble(ForecastFragment.COL_WEATHER_MAX_TEMP),
                cursor.getDouble(ForecastFragment.COL_WEATHER_MIN_TEMP));

        return Utility.formatDate(cursor.getLong(ForecastFragment.COL_WEATHER_DATE)) +
                " - " + cursor.getString(ForecastFragment.COL_WEATHER_DESC) +
                " - " + highAndLow;
    }

    /*
        Remember that these views are reused as needed.
     */
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {

        int viewType = getItemViewType(cursor.getPosition());
        int layoutId = -1;

        switch (viewType){
            case VIEW_TYPE_TODAY:
                return LayoutInflater.from(context).inflate(R.layout.list_item_forecast_today, parent,false);
            case VIEW_TYPE_FUTURE_DAY:
                 return LayoutInflater.from(context).inflate(R.layout.list_item_forcast, parent, false);
            default:

        }
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        return (position == 0) ? VIEW_TYPE_TODAY : VIEW_TYPE_FUTURE_DAY;
    }

    /*
                This is where we fill-in the views with the contents of the cursor.
             */
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        // our view is pretty simple here --- just a text view
        // we'll keep the UI functional with a simple (and slow!) binding.
        boolean isMetric = Utility.isMetric(context);

        ImageView forcastIcon = (ImageView) view.findViewById(R.id.list_item_icon);
        forcastIcon.setImageResource(R.drawable.ic_launcher);

        TextView dateView = (TextView) view.findViewById(R.id.list_item_date_textview);
        long dateInMilli = cursor.getLong(ForecastFragment.COL_WEATHER_DATE);
        dateView.setText(Utility.getFriendlyDayString(context, dateInMilli));

        TextView highTextView = (TextView) view.findViewById(R.id.list_item_high_textview);
        long highText = cursor.getLong(ForecastFragment.COL_WEATHER_MAX_TEMP);
        highTextView.setText(Utility.formatTemperature(highText, isMetric));

        TextView lowTextView = (TextView) view.findViewById(R.id.list_item_low_textview);
        long lowText = cursor.getLong(ForecastFragment.COL_WEATHER_MIN_TEMP);
        lowTextView.setText(Utility.formatTemperature(lowText, isMetric));

        TextView forecastTextView = (TextView) view.findViewById(R.id.list_item_forecast_textview);
        String forecastText = cursor.getString(ForecastFragment.COL_WEATHER_DESC);
        forecastTextView.setText(forecastText);



        //TextView tv = (TextView)view;
        //tv.setText(convertCursorRowToUXFormat(cursor));
    }
}

