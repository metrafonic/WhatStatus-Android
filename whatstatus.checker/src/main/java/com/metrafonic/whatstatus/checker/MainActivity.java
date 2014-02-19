package com.metrafonic.whatstatus.checker;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

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
import android.widget.ProgressBar;
import android.os.Bundle;
import android.os.Looper;
import android.app.Activity;
import android.graphics.Color;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class MainActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final TextView textSite = (TextView) findViewById(R.id.textSite);
        final TextView textTracker = (TextView) findViewById(R.id.textTracker);
        final TextView textIRC = (TextView) findViewById(R.id.textIRC);
        //final TextView textError = (TextView) findViewById(R.id.textError);
        final ImageView imageSite = (ImageView) findViewById(R.id.ImageViewSite);
        final ImageView imageTracker = (ImageView) findViewById(R.id.ImageViewTracker);
        final ImageView imageIRC = (ImageView) findViewById(R.id.imageViewIRC);
        
        
        class ThreadExtended extends Thread {

        	public void run() {
        	try {
        		
        		final String[] variableSite = connect("https://whatstatus.info/api/status","site", "tracker","irc");
        	runOnUiThread(new Runnable() {
        	public void run() {
        		if (variableSite[0].contains("1")){textSite.setTextColor(Color.GREEN);textSite.setText("Up");imageSite.setImageResource(R.drawable.up);}else{textSite.setTextColor(Color.RED);textSite.setText("Down");imageSite.setImageResource(R.drawable.down);}
        		if (variableSite[1].contains("1")){textTracker.setTextColor(Color.GREEN);textTracker.setText("Up");imageTracker.setImageResource(R.drawable.up);}else{textTracker.setTextColor(Color.RED);textTracker.setText("Down");imageTracker.setImageResource(R.drawable.down);}
        		if (variableSite[2].contains("1")){textIRC.setTextColor(Color.GREEN);textIRC.setText("Up");imageIRC.setImageResource(R.drawable.up);}else{textIRC.setTextColor(Color.RED);textIRC.setText("Down");imageIRC.setImageResource(R.drawable.down);}
        		//textError.setText("error");
        		//loadingThing.setVisibility(View.INVISIBLE);
        		}
    		});
    		} catch (Exception e) {
        			e.printStackTrace();
    			}
    		};

    	}
        	new ThreadExtended().start();

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

