package com.example.sabrina.hsvcolorpicker;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Sabrina on 4/15/2015.
 */
public class HSVColorAdapter extends ArrayAdapter<GradientDrawable> {

    private List<GradientDrawable> colorList;

    public HSVColorAdapter(Context context, List<GradientDrawable> objects) {
        super(context, 0, objects);
        colorList = objects;
    }

    private static class ViewHolder {
        TextView selector;

    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent){

        ViewHolder viewHolder; //cache Views to avoid future lookups

        // Check if an existing view is being recycled; if not
        // inflate a new one
        if (convertView == null){
            viewHolder = new ViewHolder();

            convertView =
                    LayoutInflater.from(getContext())
                            .inflate(R.layout.hsv_color, parent, false);
            viewHolder.selector = (TextView) convertView.findViewById(R.id.selector);

            // cache the Views
            convertView.setTag( viewHolder );
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        GradientDrawable color = getItem( position );

        viewHolder.selector.setBackground(color);

        return convertView;
    }
}
