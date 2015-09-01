package com.example.sabrina.hsvcolorpicker;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


public class IdentifyColorActivity extends ActionBarActivity {

    static final public String FRAGMENT_COLOR_MATCHER = "fragmentColorMatcher";
    static final public String FRAGMENT_NAME = "fragmentName";

    static final private String CURRENT_SORTED_ORDER = "sortedOrder";

    private int mSortedEntry = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_identify_color);

        mSortedEntry = getPreferences(MODE_PRIVATE).getInt(CURRENT_SORTED_ORDER, -1);
        ColorCursorAdapter.currentOrder = mSortedEntry;

        getSupportFragmentManager().beginTransaction()
                .add(R.id.exactMatch, new ColorMatchFragment(), FRAGMENT_COLOR_MATCHER)
                .add(R.id.nameList, new ListFragmentName(), FRAGMENT_NAME)
                .commit();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_identify_color, menu);
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
}
