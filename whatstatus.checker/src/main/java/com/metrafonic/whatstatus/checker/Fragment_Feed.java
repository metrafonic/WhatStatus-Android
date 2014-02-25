package com.metrafonic.whatstatus.checker;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Mathias on 25.02.14.
 */
public class Fragment_Feed extends Fragment {
    public static  Fragment_Feed newInstance(int sectionNumber) {
        Fragment_Feed fragment = new  Fragment_Feed();
        Bundle args = new Bundle();
        args.putInt("section", sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_feed, container, false);

        return rootView;
    }

}