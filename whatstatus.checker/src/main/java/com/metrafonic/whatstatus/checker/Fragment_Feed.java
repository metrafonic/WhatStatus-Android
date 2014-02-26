package com.metrafonic.whatstatus.checker;

import android.app.DownloadManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;


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
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_feed, container, false);
        refresh(rootView, inflater, container);
        return rootView;

    }

    void refresh(View rootView, final LayoutInflater inflater, final ViewGroup container){
        final LinearLayout linearTwitter = (LinearLayout) rootView.findViewById(R.id.linearTwitter);
        final String url = "https://api.twitter.com/1.1/statuses/user_timeline.json?screen_name=whatcdstatus";
        final AsyncHttpClient client = new AsyncHttpClient();
        final AsyncHttpResponseHandler asyncHttpResponseHandler = new AsyncHttpResponseHandler() {

            public void onSuccess(String response) {
     /*--------- DidReceiveData ---------*/

                JSONArray jsonResponse = null;
                try{
                    jsonResponse = new JSONArray(response);
                    linearTwitter.removeAllViews();
                    for (int i = 0; i < jsonResponse.length(); i++) {
                        View cell = inflater.inflate(R.layout.cell_twitter, container, false);
                        final TextView twitterText = (TextView) cell.findViewById(R.id.tweetFeed);
                        final TextView twitterHours = (TextView) cell.findViewById(R.id.tweetHours);
                        final String TWITTER="EEE MMM dd HH:mm:ss ZZZZZ yyyy";

                        SimpleDateFormat dateFormatGmt = new SimpleDateFormat("EEE MMM dd HH:mm:ss ZZZZZ yyyy");
                        dateFormatGmt.setTimeZone(TimeZone.getTimeZone("CST"));


                        String date = jsonResponse.getJSONObject(i).getString("created_at");

                        try {
                            long milliseconds = getTwitterDate(date, TWITTER).getTime() - getTwitterDate(dateFormatGmt.format(new Date()), TWITTER).getTime();
                            long hours = ((milliseconds*-1 / (1000*60*60)));
                            twitterHours.setText(hours + " hours ago");
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        twitterText.append(jsonResponse.getJSONObject(i).getString("text"));

                        linearTwitter.addView(cell);
                    }
                }catch (JSONException e){
                    e.printStackTrace();
                }
            };
        };



/*
 * OAuth Starts Here
 */
        RequestParams requestParams = new RequestParams();
        requestParams.put("grant_type", "client_credentials");
        AsyncHttpClient httpClient = new AsyncHttpClient();
        httpClient.addHeader("Authorization", "Basic " + Base64.encodeToString(("GObZMIEY9MUAXYiErfs2NQ" + ":" + "XG24opjXpgzfjbg8nlD0e3HLnJFKzGR9k6gGklY").getBytes(), Base64.NO_WRAP));
        httpClient.addHeader("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
        httpClient.post("https://api.twitter.com/oauth2/token", requestParams, new AsyncHttpResponseHandler() {

            public void onSuccess(String responce) {

                try {
                    JSONObject jsonObject = new JSONObject(
                            responce);
                    Log.e("", "token_type " + jsonObject.getString("token_type") + " access_token " + jsonObject.getString("access_token"));
                    client.addHeader("Authorization", jsonObject.getString("token_type") + " " + jsonObject.getString("access_token"));
                    client.get(url, asyncHttpResponseHandler);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            };

            public void onFailure(Throwable error, String response) {
                Log.e("", "error " + error.toString() + " response " + response);

            };

        });
    }
    public static Date getTwitterDate(String date, String format) throws ParseException {


        SimpleDateFormat sf = new SimpleDateFormat(format);
        sf.setLenient(true);
        return sf.parse(date);
    }
}