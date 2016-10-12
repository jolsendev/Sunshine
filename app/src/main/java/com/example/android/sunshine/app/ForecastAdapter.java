package com.example.android.sunshine.app;
import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.sunshine.app.data.WeatherContract;

public class ForecastAdapter extends CursorAdapter {
    private final int VIEW_TYPE_TODAY = 0;
    private final int VIEW_TYPE_FUTURE_DAY= 1;
    Context mContext;
    private boolean mUseTodayLayout;

    public ForecastAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
        this.mContext = context;
    }

    /**
     * Prepare the weather high/lows for presentation.
     */
    private String formatHighLows(double high, double low) {
        boolean isMetric = Utility.isMetric(mContext);
        String highLowStr = Utility.formatTemperature(mContext, high, isMetric) + "/" + Utility.formatTemperature(mContext, low, isMetric);
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
        if(viewType == VIEW_TYPE_TODAY)
            layoutId = R.layout.list_item_forecast_today;
        if(viewType == VIEW_TYPE_FUTURE_DAY)
            layoutId = R.layout.list_item_forecast;

        View view = LayoutInflater.from(context).inflate(layoutId, parent, false);
        ViewHolder mViewHolder = new ViewHolder(view);
        view.setTag(mViewHolder);
        return view;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        return (position == 0 && mUseTodayLayout) ? VIEW_TYPE_TODAY : VIEW_TYPE_FUTURE_DAY;
    }

    /*
                This is where we fill-in the views with the contents of the cursor.
             */
    public void setUserTodayLayout(boolean useTodayLayout){
        mUseTodayLayout = useTodayLayout;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        // our view is pretty simple here --- just a text view
        // we'll keep the UI functional with a simple (and slow!) binding.

        int viewType = getItemViewType(cursor.getPosition());

        ViewHolder holder = (ViewHolder) view.getTag();

        boolean isMetric = Utility.isMetric(context);
        int weatherColmn = cursor.getColumnIndex(WeatherContract.WeatherEntry.COLUMN_WEATHER_ID);
        int weatherId = cursor.getInt(weatherColmn);

        if(viewType == VIEW_TYPE_TODAY)
            holder.imageView.setImageResource(Utility.getArtResourceForWeatherCondition(weatherId));
        else
            holder.imageView.setImageResource(Utility.getIconResourceForWeatherCondition(weatherId));

        long dateInMilli = cursor.getLong(ForecastFragment.COL_WEATHER_DATE);
        holder.dateView.setText(Utility.getFriendlyDayString(context, dateInMilli));

        long highText = cursor.getLong(ForecastFragment.COL_WEATHER_MAX_TEMP);
        holder.highView.setText(Utility.formatTemperature(context, highText, isMetric));

        long lowText = cursor.getLong(ForecastFragment.COL_WEATHER_MIN_TEMP);
        holder.lowView.setText(Utility.formatTemperature(context, lowText, isMetric));

        String forecastText = cursor.getString(ForecastFragment.COL_WEATHER_DESC);
        holder.descView.setText(forecastText);

    }
}

