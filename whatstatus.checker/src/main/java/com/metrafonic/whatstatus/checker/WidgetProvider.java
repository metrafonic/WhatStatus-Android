package com.metrafonic.whatstatus.checker;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

public class WidgetProvider extends AppWidgetProvider {


	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
		final int N = appWidgetIds.length;

        for (int i=0; i<N; i++) {
            int appWidgetId = appWidgetIds[i];

            Intent intent = new Intent(context, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.homewidgetlayout);
            views.setOnClickPendingIntent(R.id.layoutwidget, pendingIntent);

            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
		Timer timer = new Timer();
		timer.scheduleAtFixedRate(new MyTime(context, appWidgetManager), 1, 600000);
		
		
	}
	
	private class MyTime extends TimerTask {
		RemoteViews remoteViews;
		AppWidgetManager appWidgetManager;
		ComponentName thisWidget;
		//java.text.DateFormat format = SimpleDateFormat.getTimeInstance(SimpleDateFormat.MEDIUM, Locale.getDefault());
		
	public MyTime(Context context, AppWidgetManager appWidgetManager) {
		this.appWidgetManager = appWidgetManager;
		remoteViews = new RemoteViews(context.getPackageName(), R.layout.homewidgetlayout);
		thisWidget = new ComponentName(context, WidgetProvider.class);
	}

	public void run() {
		/*
		remoteViews.setTextViewText(R.id.trackerStatusText, "TIME = " +format.format(new Date()));
		*/
		appWidgetManager.updateAppWidget(thisWidget, remoteViews);

			String temp[]= connect("https://whatstatus.info/api/status","site", "tracker","irc");
		   		 	// print substrings 
		   		 	if(temp[0].contains("1")){
		   		 	System.out.println("Mathias Online1");
		   		 	remoteViews.setTextColor(R.id.siteStatusText, Color.GREEN);
		   		 }else{remoteViews.setTextColor(R.id.siteStatusText, Color.RED);}
		   		if(temp[1].contains("1")){
		   		 	System.out.println("Mathias Online2");
		   		 	remoteViews.setTextColor(R.id.trackerStatusText, Color.GREEN);
		   		}else{remoteViews.setTextColor(R.id.trackerStatusText, Color.RED);}
		   		if(temp[2].contains("1")){
		   		 	System.out.println("Mathias Online3");
		   		 	remoteViews.setTextColor(R.id.ircStatusText, Color.GREEN);
		   		}else{remoteViews.setTextColor(R.id.ircStatusText, Color.RED);}
		   		 	
		   		
		      System.out.println("Neger 111");
		      return;

	}
		
	} 
	
	private String[] connect(String url, String part1, String part2, String part3){
   	 
        // Create the httpclient
        HttpClient httpclient = new DefaultHttpClient();
 
        // Prepare a request object
        HttpGet httpget = new HttpGet(url); 
 
        // Execute the request
        HttpResponse response;
 
        // return string
        String[] returnString = null;
 
        try {
 
            // Open the webpage.
            response = httpclient.execute(httpget);
 
            if(response.getStatusLine().getStatusCode() == 200){
                // Connection was established. Get the content. 
 
                HttpEntity entity = response.getEntity();
                // If the response does not enclose an entity, there is no need
                // to worry about connection release
 
                if (entity != null) {
                    // A Simple JSON Response Read
                    InputStream instream = entity.getContent();
 
                    // Load the requested page converted to a string into a JSONObject.
                    JSONObject myAwway = new JSONObject(convertStreamToString(instream));
 
                    // Get the query value'
                    String Sitequery = myAwway.getString(part1);
                    String IRCquery = myAwway.getString(part2);
                    String Trackerquery = myAwway.getString(part3);
                    String[] allvalues = new String[]{Sitequery, Trackerquery, IRCquery};
 
 
                    // Build the return string.
                    returnString = allvalues;
 
                    // Cose the stream.
                    instream.close();
                }
            }
            else {
                // code here for a response othet than 200.  A response 200 means the webpage was ok
                // Other codes include 404 - not found, 301 - redirect etc...
                // Display the response line.
                returnString[4] = "Unable to load page - " + response.getStatusLine();
            }
        }
        catch (IOException  ex) {
            // thrown by line 80 - getContent();
            // Connection was not established
        	returnString[4] = "Connection failed; " + ex.getMessage();
        }
        catch (JSONException ex){
            // JSON errors
        	returnString[4] = "JSON failed; " + ex.getMessage();
        }
        return returnString;
    }
 
    private static String convertStreamToString(InputStream is) {
        /*
         * To convert the InputStream to String we use the BufferedReader.readLine()
         * method. We iterate until the BufferedReader return null which means
         * there's no more data to read. Each line will appended to a StringBuilder
         * and returned as String.
         */
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
 
        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }
}	
