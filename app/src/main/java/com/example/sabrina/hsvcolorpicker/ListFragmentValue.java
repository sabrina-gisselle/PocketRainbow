package com.example.sabrina.hsvcolorpicker;

import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.List;

/**
 * Created by Sabrina on 4/15/2015.
 */
public class ListFragmentValue extends Fragment {

    private List<GradientDrawable> mListValue = null;
    private HSVColorAdapter mAdapter = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_saturation_value, container, false);

        final ListView listView = (ListView) rootView.findViewById(R.id.list);

        if (mListValue == null )
        {
            mListValue = HSVColor.populateValueList(HSVColor.getHueLeft(), HSVColor.getHueRight(), HSVColor.getSaturation());
        }
        if(mAdapter == null) {
            mAdapter = new HSVColorAdapter(getActivity().getApplicationContext(), mListValue);
        }
        if(listView != null) {
            listView.setAdapter(mAdapter);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, final View view, final int position, long id) {
                    HSVColor.setValueByPosition(position);

                    view.animate().setDuration(300).alpha(0).withEndAction(
                            new Runnable() {
                                @Override
                                public void run() {
                                    getFragmentManager().beginTransaction().replace(R.id.container, new ListFragmentName(), ColorExplorerActivity.FRAGMENT_NAME).addToBackStack(null).commit();
                                }
                            });
                }
            });
        }
        return rootView;
    }

    public void updateFragmentData() {
        mListValue.clear();
        mListValue.addAll(HSVColor.populateValueList(HSVColor.getHueLeft(), HSVColor.getHueRight(), HSVColor.getSaturation()));
        mAdapter.notifyDataSetChanged();
    }
}