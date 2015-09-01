package com.example.sabrina.hsvcolorpicker;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sabrina.hsvcolorpicker.db.ColorTable;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;


public class ColorCursorAdapter extends CursorAdapter {

    // Fields from the database (projection)
    // Must include the _id column for the adapter to work
    static private final int NAME = 0;
    static private final int HEX = 1;
    static private final int HUE = 2;
    static private final int SATURATION = 3;
    static private final int VALUE = 4;
    static private final int ID = 5;

    static public final String[] PROJECTION
            = new String[] {
            ColorTable.COLUMN_NAME,
            ColorTable.COLUMN_HEX,
            ColorTable.COLUMN_HUE,
            ColorTable.COLUMN_SATURATION,
            ColorTable.COLUMN_VALUE,
            ColorTable.COLUMN_ID
    };

    static public int currentOrder = -1;

    static public final String[] ORDER_BY
            = new String[]{
            ColorTable.COLUMN_HUE + "," + ColorTable.COLUMN_SATURATION + "," + ColorTable.COLUMN_VALUE,
            ColorTable.COLUMN_HUE + "," + ColorTable.COLUMN_VALUE + "," + ColorTable.COLUMN_SATURATION,
            ColorTable.COLUMN_SATURATION + "," + ColorTable.COLUMN_HUE + "," + ColorTable.COLUMN_VALUE,
            ColorTable.COLUMN_SATURATION + "," + ColorTable.COLUMN_VALUE + "," + ColorTable.COLUMN_HUE,
            ColorTable.COLUMN_VALUE + "," + ColorTable.COLUMN_HUE + "," + ColorTable.COLUMN_SATURATION,
            ColorTable.COLUMN_VALUE + "," + ColorTable.COLUMN_SATURATION + "," + ColorTable.COLUMN_HUE
    };

    static private class ViewHolder {
        TextView label;
        TextView preview;
        Map<String, String> extraInfo;
    }

    private LayoutInflater mInflater;

    public ColorCursorAdapter(Context context, Cursor cursor, int flags) {
        super(context, cursor, flags);

        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {

        View row = mInflater.inflate(R.layout.color_row, parent, false);

        // cache the row's Views in a ViewHolder
        ViewHolder viewHolder = new ViewHolder();
        viewHolder.label = (TextView) row.findViewById( R.id.label );
        viewHolder.preview = (TextView) row.findViewById( R.id.preview );

        row.setTag( viewHolder );

        return row;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        ViewHolder viewHolder = updateViewHolderValues(view, cursor);
    }

    private ViewHolder updateViewHolderValues(View view, Cursor cursor) {
        ViewHolder viewHolder = (ViewHolder) view.getTag();

        viewHolder.label.setText(cursor.getString(NAME));
        viewHolder.preview.setBackgroundColor(Color.parseColor(cursor.getString(HEX)));

        viewHolder.extraInfo = new HashMap<>();

        viewHolder.extraInfo.put( ColorTable.COLUMN_NAME, cursor.getString(NAME));
        viewHolder.extraInfo.put( ColorTable.COLUMN_HUE, Integer.toString(cursor.getInt(HUE)));
        viewHolder.extraInfo.put( ColorTable.COLUMN_SATURATION, Float.toString(cursor.getFloat(SATURATION)));
        viewHolder.extraInfo.put( ColorTable.COLUMN_VALUE, Float.toString(cursor.getFloat(VALUE)));
        viewHolder.extraInfo.put( ColorTable.COLUMN_HEX, cursor.getString(HEX));
        return viewHolder;
    }

    public static Map<String, String> getExtraInfo( View view ) {
        ViewHolder viewHolder = (ViewHolder) view.getTag();
        return viewHolder.extraInfo;
    }
}
