package com.example.sabrina.hsvcolorpicker;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class ColorMatchFragment extends Fragment {

    TextView mSwatchColor = null;
    TextView mTextView = null;
    TextView mTitleView = null;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_color_match, container, false);

        mTextView = (TextView) rootView.findViewById(R.id.exactMatchText);
        mSwatchColor = (TextView) rootView.findViewById(R.id.swatch);
        mTitleView = (TextView) rootView.findViewById(R.id.titleText);

        ListFragmentName nameFragment = (ListFragmentName) getActivity().getSupportFragmentManager().findFragmentByTag(IdentifyColorActivity.FRAGMENT_NAME);
        nameFragment.setText = true;
        String data = "\nHue: " + HSVColor.getExactHue() +  " Saturation: " + HSVColor.getSaturation() + " Value: " + HSVColor.getValue();

        String text = nameFragment.exactMatch(getActivity()) ? getActivity().getString(R.string.exact_match_found) + data : getActivity().getString(R.string.no_match_found);
        
        if (nameFragment.exactMatch(getActivity())){
            mSwatchColor.setBackgroundColor(Color.HSVToColor(new float[]{HSVColor.getExactHue(), HSVColor.getSaturation(), HSVColor.getValue()}));
        }else{
            mSwatchColor.setVisibility(View.GONE);
        }
        mTextView.setText(text);
        return rootView;
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
