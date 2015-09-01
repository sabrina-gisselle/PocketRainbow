package com.example.sabrina.hsvcolorpicker;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.sabrina.hsvcolorpicker.db.ColorDBHelper;

import java.io.IOException;
import java.sql.SQLException;


public class ColorExplorerActivity extends ActionBarActivity {
    static final public String FRAGMENT_HUE = "fragmentHue";
    static final public String FRAGMENT_SATURATION = "fragmentSaturation";
    static final public String FRAGMENT_VALUE = "fragmentValue";
    static final public String FRAGMENT_NAME = "fragmentName";


    static final private String CURRENT_SORTED_ORDER = "sortedOrder";
    static final private String CURRENT_SATURATION_NUMBER = "saturatedNumber";
    static final private String CURRENT_VALUE_NUMBER = "valueNumber";
    static final private String CURRENT_HUE_NUMBER = "hueNumber";
    static final private String CURRENT_HUE_CENTER = "hueCenter";
    static final private String HUE_DIALOG_OPEN = "hueDialogOpen";
    static final private String SAT_VAL_DIALOG_OPEN = "Sat_Val_DialogOpen";


    private int mSortedEntry = -1;
    private int mSaturationEntry = 10;
    private int mValueEntry = 10;
    private int mHueEntry = 10;
    private int mHueCenter = 0;
    public boolean mHueDialogIsOpen = false;
    public boolean mSatValueDialogIsOpen = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSortedEntry = getPreferences(MODE_PRIVATE).getInt(CURRENT_SORTED_ORDER, -1);
        mSaturationEntry = getPreferences(MODE_PRIVATE).getInt(CURRENT_SATURATION_NUMBER, 10);
        mValueEntry = getPreferences(MODE_PRIVATE).getInt(CURRENT_VALUE_NUMBER, 10);
        mHueEntry = getPreferences(MODE_PRIVATE).getInt(CURRENT_HUE_NUMBER, 10);
        mHueCenter = getPreferences(MODE_PRIVATE).getInt(CURRENT_HUE_CENTER, 0);
        mHueDialogIsOpen = getPreferences(MODE_PRIVATE).getBoolean(HUE_DIALOG_OPEN, false);
        mSatValueDialogIsOpen = getPreferences(MODE_PRIVATE).getBoolean(SAT_VAL_DIALOG_OPEN, false);



        ColorCursorAdapter.currentOrder = mSortedEntry;
        HSVColor.setSaturationDelta(mSaturationEntry);
        HSVColor.setValueDelta(mValueEntry);
        HSVColor.setHueDelta(mHueEntry);
        HSVColor.setHueCenter(mHueCenter);

        ColorDBHelper myDbHelper = new ColorDBHelper(this);

        try {

            myDbHelper.createDatabase();

        } catch (IOException ioe) {

            throw new Error("Unable to create database");

        }

        try {

            myDbHelper.openDatabase();

        }catch(SQLException sqle){

            try {
                throw sqle;
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new ListFragmentHue(), FRAGMENT_HUE)
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void sortingButtonHandler(View view){

        AlertDialog levelDialog;

        final CharSequence[] items = {
                "Hue, Saturation, Value",
                "Hue, Value, Saturation",
                "Saturation, Hue, Value",
                "Saturation, Value, Hue",
                "Value, Hue, Saturation",
                "Value, Saturation, Hue"
        };

        // Creating and Building the Dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.select_sorting_order));

        builder.setSingleChoiceItems(items, mSortedEntry, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                mSortedEntry = item;
                ColorCursorAdapter.currentOrder = item;
                SharedPreferences.Editor editor = getPreferences(MODE_PRIVATE).edit();
                editor.putInt(CURRENT_SORTED_ORDER, item);
                editor.commit();

                dialog.dismiss();

                ListFragmentName nameFragment = (ListFragmentName) getSupportFragmentManager().findFragmentByTag(FRAGMENT_NAME);
                if (nameFragment.isVisible()) {
                    nameFragment.updateFragmentData();
                }
            }
        });
        levelDialog = builder.create();
        levelDialog.show();
    }

    private boolean isSaturationFragment(){
        ListFragmentSaturation saturationFragment = (ListFragmentSaturation) getSupportFragmentManager().findFragmentByTag(FRAGMENT_SATURATION);
        if (saturationFragment.isVisible()) {
            return true;
        }
        return false;
    }

    public void configureSwatchesButtonHandler(View view){
        int maxValue = 256;
        SharedPreferences.Editor editor = getPreferences(MODE_PRIVATE).edit();
        editor.putBoolean(SAT_VAL_DIALOG_OPEN, true);
        editor.commit();

        final boolean isSaturationFragment = isSaturationFragment();

        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.seek_bar_view, (ViewGroup) findViewById(R.id.seekbar_container));

        // Hide other seekbar and textViews

        SeekBar seekbarHidden = (SeekBar) layout.findViewById(R.id.seek_bar_center);
        seekbarHidden.setVisibility(View.GONE);

        RelativeLayout relLayoutHidden = (RelativeLayout) layout.findViewById(R.id.relativeLayout);
        relLayoutHidden.setVisibility(View.GONE);

        TextView textBoxHiddenMin = (TextView) layout.findViewById(R.id.seekbar_center_min);
        textBoxHiddenMin.setVisibility(View.GONE);

        TextView textBoxHiddenCurrent = (TextView) layout.findViewById(R.id.seekbar_center_current);
        textBoxHiddenCurrent.setVisibility(View.GONE);

        TextView textBoxHiddenMax = (TextView) layout.findViewById(R.id.seekbar_center_max);
        textBoxHiddenMax.setVisibility(View.GONE);

        TextView textBoxPreview = (TextView) layout.findViewById(R.id.preview);
        textBoxPreview.setVisibility(View.GONE);
        // END OF HIDDEN VIEWS

        AlertDialog.Builder builder = new AlertDialog.Builder(this).setView(layout);
        builder.setTitle(getString(R.string.select_number_of_swatches));

        final TextView currentTextView = (TextView)layout.findViewById(R.id.seekbar_current);
        SeekBar seekbar = (SeekBar)layout.findViewById(R.id.seek_bar);
        seekbar.setMax(maxValue);
        TextView maxTextView = (TextView)layout.findViewById(R.id.seekbar_max);
        maxTextView.setText(String.valueOf(maxValue));
        seekbar.setProgress(isSaturationFragment ? mSaturationEntry : mValueEntry);
        currentTextView.setText(isSaturationFragment ? String.valueOf(mSaturationEntry) : String.valueOf(mValueEntry));

        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progress = progress != 0 ? progress : 1;
                String progressString = String.valueOf(progress);
                currentTextView.setText(progressString);
                seekBar.setSecondaryProgress(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        builder.setPositiveButton("Ok",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (isSaturationFragment) {
                            // In this case it is a saturation fragment
                            mSaturationEntry = Integer.valueOf(currentTextView.getText().toString());
                            HSVColor.setSaturationDelta(mSaturationEntry);
                            SharedPreferences.Editor editor = getPreferences(MODE_PRIVATE).edit();
                            editor.putInt(CURRENT_SATURATION_NUMBER, mSaturationEntry);
                            editor.commit();
                            ListFragmentSaturation saturationFragment = (ListFragmentSaturation) getSupportFragmentManager().findFragmentByTag(FRAGMENT_SATURATION);
                            if (saturationFragment.isVisible()) {
                                saturationFragment.updateFragmentData();
                            }
                        } else {
                            // In this case it is a value fragment
                            mValueEntry = Integer.valueOf(currentTextView.getText().toString());
                            HSVColor.setValueDelta(mValueEntry);
                            SharedPreferences.Editor editor = getPreferences(MODE_PRIVATE).edit();
                            editor.putInt(CURRENT_VALUE_NUMBER, mValueEntry);
                            editor.commit();
                            ListFragmentValue valueFragment = (ListFragmentValue) getSupportFragmentManager().findFragmentByTag(FRAGMENT_VALUE);
                            if (valueFragment.isVisible()) {
                                valueFragment.updateFragmentData();
                            }
                        }
                        SharedPreferences.Editor editor1 = getPreferences(MODE_PRIVATE).edit();
                        editor1.putBoolean(SAT_VAL_DIALOG_OPEN, false);
                        editor1.commit();
                    }
                }).setNegativeButton("Cancel",
                    new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            SharedPreferences.Editor editor1 = getPreferences(MODE_PRIVATE).edit();
                            editor1.putBoolean(SAT_VAL_DIALOG_OPEN, false);
                            editor1.commit();
                        }
                    }
        );
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public void configureSwatchesHueButtonHandler(View view) {
        SharedPreferences.Editor editor = getPreferences(MODE_PRIVATE).edit();
        editor.putBoolean(HUE_DIALOG_OPEN, true);
        editor.commit();

        int maxValueSwathes = 36;
        int maxValueCenter = 360;
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.seek_bar_view, (ViewGroup) findViewById(R.id.seekbar_container));
        AlertDialog.Builder builder = new AlertDialog.Builder(this).setView(layout);
        builder.setTitle(getString(R.string.central_hue_select_number_of_swatches));

        // Seekbar for setting center
        SeekBar seekbarCenter = (SeekBar)layout.findViewById(R.id.seek_bar_center);
        seekbarCenter.setMax(maxValueCenter);
        seekbarCenter.setProgress(mHueCenter);
        final TextView currentCenterTextView = (TextView)layout.findViewById(R.id.seekbar_center_current);
        final TextView previewTextView = (TextView)layout.findViewById(R.id.preview);
        TextView maxTextViewCenter = (TextView)layout.findViewById(R.id.seekbar_center_max);
        maxTextViewCenter.setText(String.valueOf(maxValueCenter));
        currentCenterTextView.setText(String.valueOf(mHueCenter));
        previewTextView.setBackgroundColor(Color.HSVToColor(new float[]{mHueCenter, 1f, 1f}));

        seekbarCenter.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                previewTextView.setBackgroundColor(Color.HSVToColor(new float[]{progress, 1f, 1f}));
                String progressString = String.valueOf(progress);
                currentCenterTextView.setText(progressString);
                seekBar.setProgress(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        // SeekBar for number of swatches
        SeekBar seekbar = (SeekBar)layout.findViewById(R.id.seek_bar);
        seekbar.setMax(maxValueSwathes);
        seekbar.setProgress(mHueEntry);
        final TextView currentTextView = (TextView)layout.findViewById(R.id.seekbar_current);
        TextView maxTextView = (TextView)layout.findViewById(R.id.seekbar_max);
        maxTextView.setText(String.valueOf(maxValueSwathes));
        currentTextView.setText(String.valueOf(mHueEntry));

        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progress = progress != 0 ? progress : 1;
                String progressString = String.valueOf(progress);
                currentTextView.setText(progressString);
                seekBar.setProgress(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        builder.setPositiveButton("Ok",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mHueEntry = Integer.valueOf(currentTextView.getText().toString());
                        mHueCenter = Integer.valueOf(currentCenterTextView.getText().toString());
                        HSVColor.setHueDelta(mHueEntry);
                        HSVColor.setHueCenter(mHueCenter);
                        SharedPreferences.Editor editor = getPreferences(MODE_PRIVATE).edit();
                        editor.putInt(CURRENT_HUE_NUMBER, mHueEntry);
                        editor.putInt(CURRENT_HUE_CENTER, mHueCenter);
                        editor.commit();
                        ListFragmentHue hueFragment = (ListFragmentHue) getSupportFragmentManager().findFragmentByTag(FRAGMENT_HUE);
                        if (hueFragment.isVisible()) {
                            hueFragment.updateFragmentData();
                        }
                        SharedPreferences.Editor editor1 = getPreferences(MODE_PRIVATE).edit();
                        editor1.putBoolean(HUE_DIALOG_OPEN, false);
                        editor1.commit();
                    }
                });
        builder.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SharedPreferences.Editor editor1 = getPreferences(MODE_PRIVATE).edit();
                        editor1.putBoolean(HUE_DIALOG_OPEN, false);
                        editor1.commit();
                    }
            }
        );
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}


