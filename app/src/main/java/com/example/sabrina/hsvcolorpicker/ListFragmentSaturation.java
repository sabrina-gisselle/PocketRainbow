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

public class ListFragmentSaturation extends Fragment {

    private List<GradientDrawable> mListSaturation = null;
    private HSVColorAdapter mAdapter = null;
    private ListView listView = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_saturation_value, container, false);

        listView = (ListView) rootView.findViewById(R.id.list);

        if (mListSaturation == null )
        {
            mListSaturation = HSVColor.populateSaturationList(HSVColor.getHueLeft(), HSVColor.getHueRight() );
        }
        if(mAdapter == null) {
            mAdapter = new HSVColorAdapter(getActivity().getApplicationContext(), mListSaturation);
        }
        if(listView != null) {
            listView.setAdapter(mAdapter);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, final View view, final int position, long id) {
                    HSVColor.setSaturationByPosition(position);

                    view.animate().setDuration(300).alpha(0).withEndAction(
                            new Runnable() {
                                @Override
                                public void run() {
                                    getFragmentManager().beginTransaction().replace(R.id.container, new ListFragmentValue(), ColorExplorerActivity.FRAGMENT_VALUE).addToBackStack(null).commit();
                                }
                            });
                }
            });
        }
        return rootView;
    }

    public void updateFragmentData() {
        mListSaturation.clear();
        mListSaturation.addAll(HSVColor.populateSaturationList(HSVColor.getHueLeft(), HSVColor.getHueRight()));
        mAdapter.notifyDataSetChanged();
    }
}