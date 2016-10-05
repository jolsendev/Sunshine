package com.example.android.sunshine.app;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

/**
 * Created by a5w5nzz on 10/4/2016.
 */

public class ViewHolder {
    public final ImageView imageView;
    public final TextView dateView;
    public final TextView highView;
    public final TextView lowView;
    public final TextView descView;


    public ViewHolder(View view) {
        this.imageView = (ImageView) view.findViewById(R.id.list_item_icon);
        this.dateView = (TextView) view.findViewById(R.id.list_item_date_textview);
        this.highView = (TextView) view.findViewById(R.id.list_item_high_textview);
        this.lowView = (TextView) view.findViewById(R.id.list_item_low_textview);
        this.descView = (TextView) view.findViewById(R.id.list_item_forecast_textview);
    }
}
