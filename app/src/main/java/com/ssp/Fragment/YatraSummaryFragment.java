package com.ssp.Fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ssp.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class YatraSummaryFragment extends Fragment {

    public YatraSummaryFragment() {
        // Required empty public constructor
    }

    public static YatraSummaryFragment newInstance() {
        YatraSummaryFragment fragment = new YatraSummaryFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_yatra_summary, container, false);
        return view;
    }

}
