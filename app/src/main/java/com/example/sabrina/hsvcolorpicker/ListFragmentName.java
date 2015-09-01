package com.example.sabrina.hsvcolorpicker;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.content.CursorLoader;
import android.database.Cursor;
import android.support.v4.app.LoaderManager;
import android.os.Bundle;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.sabrina.hsvcolorpicker.contentprovider.ColorContentProvider;
import com.example.sabrina.hsvcolorpicker.db.ColorDBHelper;
import com.example.sabrina.hsvcolorpicker.db.ColorTable;
import java.util.Map;

public class ListFragmentName extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>  {

    private ColorCursorAdapter mAdapter;
    private TextView mTitleTextView;
    public boolean setText = false;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_name, container, false);
        mTitleTextView = (TextView) rootView.findViewById(R.id.titleText);

        if (setText){
            mTitleTextView.setText(getActivity().getString(R.string.similar_colors));
        }

        String[] args = ColorDBHelper.getSelectionArgs();
        Log.d("ListFragmentName",
                "LHue: " + args[0]
                + " RHue: " + args[1]
                + " LSat: " + args[2]
                + " RSat: " + args[3]
                + " LValue: " + args[4]
                + " RValue: " + args[5]);

        final ListView listView = (ListView) rootView.findViewById(R.id.list);
        getLoaderManager().initLoader(0, null, this);

        mAdapter = new ColorCursorAdapter( getActivity(), // context
                null, // cursor
                0     // flags
        );

        if(listView != null) {
            listView.setAdapter(mAdapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, final View view, final int position, long id) {
                    Map<String, String> extra = ColorCursorAdapter.getExtraInfo(view);



                    String toastText = "Name: " + extra.get(ColorTable.COLUMN_NAME)
                            + "\nHue: " + extra.get(ColorTable.COLUMN_HUE) + "\u00b0"
                            + "\nSaturation: " + extra.get(ColorTable.COLUMN_SATURATION)
                            + "\nValue: " + extra.get(ColorTable.COLUMN_VALUE)
                            + "\nHex: " + extra.get(ColorTable.COLUMN_HEX);
                    Toast.makeText(rootView.getContext(), toastText, Toast.LENGTH_LONG).show();
                }
            });
        }
        return rootView;
    }

    /****************************************
     ** LoaderManager.LoaderCallbacks
     *****************************************/

    // creates a new loader after the initLoader () call
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] arguments = ColorDBHelper.getSelectionArgs();
        String selection = Float.valueOf(arguments[0]) >= Float.valueOf(arguments[1]) ? ColorDBHelper.SELECTION_OR : ColorDBHelper.SELECTION_AND;
        CursorLoader cursorLoader
                = new CursorLoader( getActivity(),
                ColorContentProvider.CONTENT_URI,
                ColorCursorAdapter.PROJECTION,
                selection,
                ColorDBHelper.getSelectionArgs(),
                ColorDBHelper.getOrder() );

        return cursorLoader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        // data is not available anymore, delete reference
        mAdapter.swapCursor(null);
    }

    public void updateFragmentData(){
        String[] arguments = ColorDBHelper.getSelectionArgs();
        String selection = Float.valueOf(arguments[0]) >= Float.valueOf(arguments[1]) ? ColorDBHelper.SELECTION_OR : ColorDBHelper.SELECTION_AND;

        Cursor cursor = getActivity().getContentResolver().query(
                ColorContentProvider.CONTENT_URI,
                ColorCursorAdapter.PROJECTION,
                selection,
                arguments,
                ColorDBHelper.getOrder() );
        onLoadFinished(null, cursor);
        mAdapter.notifyDataSetChanged();
    }

    public boolean exactMatch(Activity activity){
        String[] arguments = ColorDBHelper.getExactSelectionArgs();
        String selection = ColorDBHelper.SELECTION_EXACT;

        Cursor cursor = activity.getContentResolver().query(
                ColorContentProvider.CONTENT_URI,
                ColorCursorAdapter.PROJECTION,
                selection,
                arguments,
                null );
        if(cursor != null){
            return cursor.getCount() > 0;
        }
        return false;
    }
}