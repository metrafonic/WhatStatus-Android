package com.metrafonic.whatstatus.checker;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Mathias on 25.02.14.
 */
public class Fragment_Status extends Fragment {
    public static  Fragment_Status newInstance(int sectionNumber) {
        Fragment_Status fragment = new  Fragment_Status();
        Bundle args = new Bundle();
        args.putInt("section", sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_status, container, false);
        refresh(rootView);
        return rootView;
    }

    void refresh (View rootView){
        final TextView textSite = (TextView) rootView.findViewById(R.id.textViewSite);
        final TextView textTracker = (TextView) rootView.findViewById(R.id.textViewTracker);
        final TextView textIRC = (TextView) rootView.findViewById(R.id.textViewIRC);
        ImageView imageSite = (ImageView) rootView.findViewById(R.id.imageViewSite);
        ImageView imageTracker = (ImageView) rootView.findViewById(R.id.imageViewTracker);
        ImageView imageIRC = (ImageView) rootView.findViewById(R.id.imageViewIRC);
        final AsyncHttpClient client = new AsyncHttpClient();

        client.get("https://whatstatus.info/api/status", new AsyncHttpResponseHandler(){
            @Override
            public void onSuccess(String response) {
                JSONObject jsonResponse = null;
                try{
                    jsonResponse = new JSONObject(response);
                    switch (Integer.parseInt(jsonResponse.getString("site"))){
                        case 0: textSite.setText("Down");
                        case 1: textSite.setText("Up");
                    }
                    switch (Integer.parseInt(jsonResponse.getString("tracker"))){
                        case 0: textTracker.setText("Down");
                        case 1: textTracker.setText("Up");
                    }
                    switch (Integer.parseInt(jsonResponse.getString("irc"))){
                        case 0: textIRC.setText("Down");
                        case 1: textIRC.setText("Up");
                    }
                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
        });
    }

}
