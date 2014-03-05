package com.metrafonic.whatstatus.checker;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jjoe64.graphview.BarGraphView;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GraphViewSeries;
import com.jjoe64.graphview.LineGraphView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class Activity_MoreStats extends ActionBarActivity implements ActionBar.OnNavigationListener {

    /**
     * The serialization (saved instance state) Bundle key representing the
     * current dropdown position.
     */
    private static final String STATE_SELECTED_NAVIGATION_ITEM = "selected_navigation_item";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_morestats);

        // Set up the action bar to show a dropdown list.
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);

        // Set up the dropdown list navigation in the action bar.
        actionBar.setListNavigationCallbacks(
                // Specify a SpinnerAdapter to populate the dropdown list.
                new ArrayAdapter<String>(
                        actionBar.getThemedContext(),
                        android.R.layout.simple_list_item_1,
                        android.R.id.text1,
                        new String[] {
                                getString(R.string.title_moresection1),
                                getString(R.string.title_moresection2),
                        }),
                this);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        // Restore the previously serialized current dropdown position.
        if (savedInstanceState.containsKey(STATE_SELECTED_NAVIGATION_ITEM)) {
            getSupportActionBar().setSelectedNavigationItem(
                    savedInstanceState.getInt(STATE_SELECTED_NAVIGATION_ITEM));
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        // Serialize the current dropdown position.
        outState.putInt(STATE_SELECTED_NAVIGATION_ITEM,
                getSupportActionBar().getSelectedNavigationIndex());
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity__more_stats, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_help) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(int position, long id) {
        // When the given dropdown item is selected, show its contents in the
        // container view.
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, PlaceholderFragment.newInstance(position + 1))
                .commit();
        return true;
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            final View rootView = inflater.inflate(R.layout.fragment_morestats, container, false);
            final TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            //textView.setText(Integer.toString(getArguments().getInt(ARG_SECTION_NUMBER)));

            final AsyncHttpClient client = new AsyncHttpClient();

            final GraphViewSeries exampleSeries = new GraphViewSeries(new GraphView.GraphViewData[] {});
            final GraphView graphView = new LineGraphView(
                    getActivity() // context
                    , "Uptime in Hours (Last 48 hours)" // heading
            );
            graphView.addSeries(exampleSeries);
            client.get("https://whatstatus.info/api/2/uptime/tracker", new AsyncHttpResponseHandler(){
               @Override
                public void onSuccess(String response){
                   JSONArray jsonResponse = null;
                   //List<String> jsonValues = new ArrayList<String>();
                   graphView.setHorizontalLabels(new String[] {"48h ago", "36h","24h", "12h", "now"});
                   //graphView.setVerticalLabels(new String[] {"0", "1"});
                   graphView.getGraphViewStyle().setNumVerticalLabels(4);
                   graphView.getGraphViewStyle().setVerticalLabelsWidth(50);
                   try {
                       jsonResponse = new JSONArray(response);
                       for (int i = 0; i < jsonResponse.length(); i++){

                           //jsonValues.add(jsonResponse.getJSONObject(i).getString("status"));
                           GraphView.GraphViewData newData = new GraphView.GraphViewData(48-(i*2), Integer.parseInt(jsonResponse.getJSONObject(i).getString("status")));
                           exampleSeries.appendData(newData, false);
                       }
                           /*long numberx = Long.parseLong(jsonResponse.names().get(i).toString());
                           int numbery = Integer.parseInt(jsonResponse.getString(jsonResponse.names().getString(i)));
                           String first4char = jsonResponse.names().get(i).toString().substring(8, 12);
                           int intForFirst4Char = Integer.parseInt(first4char);
                           textView.append("\n x=" + numberx + " y=" + numbery + " 4=" + first4char);
                           textView.setText(jsonResponse.names());
                           GraphView.GraphViewData newData = new GraphView.GraphViewData(i,i+numbery);
                           exampleSeries.appendData(newData, false);*/

                       graphView.redrawAll();
                   } catch (JSONException e) {
                       e.printStackTrace();
                   }

               }
            });
             // data

            LinearLayout layout = (LinearLayout) rootView.findViewById(R.id.graph1);
            layout.addView(graphView);

            //exampleSeries.appendData(new GraphView.GraphViewData(2, 2), true, 10);


            return rootView;
        }

    }
    public static java.util.Date getDate(String date, String format) throws ParseException {


        SimpleDateFormat sf = new SimpleDateFormat(format);
        sf.setLenient(true);
        return sf.parse(date);
    }

}
