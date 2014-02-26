package com.metrafonic.whatstatus.checker;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
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
        final ImageView imageSite = (ImageView) rootView.findViewById(R.id.imageViewSite);
        final ImageView imageTracker = (ImageView) rootView.findViewById(R.id.imageViewTracker);
        final ImageView imageIRC = (ImageView) rootView.findViewById(R.id.imageViewIRC);
        final ProgressBar progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);
        final Button moreStats = (Button) rootView.findViewById(R.id.buttonMoreStats);
        final AsyncHttpClient client = new AsyncHttpClient();

        moreStats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), Activity_MoreStats.class);
                startActivity(i);
            }
        });

        client.get("https://whatstatus.info/api/status", new AsyncHttpResponseHandler(){
            @Override
            public void onSuccess(String response) {
                progressBar.setVisibility(View.GONE);
                moreStats.setVisibility(View.VISIBLE);
                JSONObject jsonResponse = null;
                try{
                    jsonResponse = new JSONObject(response);
                    switch (Integer.parseInt(jsonResponse.getString("site"))){
                        case 0: textSite.setText("Down");imageSite.setImageResource(R.drawable.abc_ic_clear_normal);break;
                        case 1: textSite.setText("Up");imageSite.setImageResource(R.drawable.abc_ic_go);break;
                    }
                    switch (Integer.parseInt(jsonResponse.getString("tracker"))){
                        case 0: textTracker.setText("Down");imageTracker.setImageResource(R.drawable.abc_ic_clear_normal);break;
                        case 1: textTracker.setText("Up");imageTracker.setImageResource(R.drawable.abc_ic_go);break;
                    }
                    switch (Integer.parseInt(jsonResponse.getString("irc"))){
                        case 0: textIRC.setText("Down");imageIRC.setImageResource(R.drawable.abc_ic_clear_normal);break;
                        case 1: textIRC.setText("Up");imageIRC.setImageResource(R.drawable.abc_ic_go);break;
                    }
                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
            public void onFailed(){
                progressBar.setVisibility(View.GONE);
            }
        });
    }

}
