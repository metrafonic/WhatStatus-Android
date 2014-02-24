package com.metrafonic.nativeminecraftquery;

import java.util.ArrayList;
import java.util.Locale;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends FragmentActivity implements ActionBar.TabListener {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link android.support.v4.app.FragmentPagerAdapter} derivative, which
     * will keep every loaded fragment in memory. If this becomes too memory
     * intensive, it may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    ViewPager mViewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_main);
        StrictMode.ThreadPolicy policy = new StrictMode.
                ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        // Set up the action bar.
        final ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the app.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        // When swiping between different sections, select the corresponding
        // tab. We can also use ActionBar.Tab#select() to do this if we have
        // a reference to the Tab.
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                actionBar.setSelectedNavigationItem(position);
            }
        });

        // For each of the sections in the app, add a tab to the action bar.
        for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
            // Create a tab with text corresponding to the page title defined by
            // the adapter. Also specify this Activity object, which implements
            // the TabListener interface, as the callback (listener) for when
            // this tab is selected.
            actionBar.addTab(
                    actionBar.newTab()
                            .setText(mSectionsPagerAdapter.getPageTitle(i))
                            .setTabListener(this));
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.action_settings:
                Intent intent = new Intent(MainActivity.this,
                        AppSettingsHelper.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    
    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        // When the given tab is selected, switch to the corresponding page in
        // the ViewPager.
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a DummySectionFragment (defined as a static inner class
            // below) with the page number as its lone argument.
            Fragment fragment = new DummySectionFragment();
            Bundle args = new Bundle();
            args.putInt(DummySectionFragment.ARG_SECTION_NUMBER, position + 1);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public int getCount() {
            // Show 2 total pages.
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            switch (position) {
                case 0:
                    return getString(R.string.title_section1).toUpperCase(l);
                case 1:
                    return getString(R.string.title_section2).toUpperCase(l);
                case 2:
                    return getString(R.string.title_section3).toUpperCase(l);
            }
            return null;
        }
    }




    /**
     * A dummy fragment representing a section of the app, but that simply
     * displays dummy text.
     */
    public static class DummySectionFragment extends Fragment {



        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        public static final String ARG_SECTION_NUMBER = "section_number";

        public DummySectionFragment() {
        }


        static public enum WhatAbout { ART, LIFE, MONEY, WORLD };
        static public WhatAbout[] wa = WhatAbout.values();
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {

            final SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity());

            StrictMode.ThreadPolicy policy = new StrictMode.
                    ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            final View rootView = inflater.inflate(R.layout.fragment_main_dummy, container, false);
            final TextView serverTextView = (TextView) rootView.findViewById(R.id.serverTextView);
            final TextView modeTextView = (TextView) rootView.findViewById(R.id.modeTextView);
            final TextView mapNameTextView = (TextView) rootView.findViewById(R.id.mapNameTextView);
            final TextView onlinePlayersTextView = (TextView) rootView.findViewById(R.id.onlinePlayersTextView);
            final TextView maxPlayersTextView = (TextView) rootView.findViewById(R.id.maxPlayersTextView);
            final TextView logTextView = (TextView) rootView.findViewById(R.id.textView4);
            final Button queryButton = (Button) rootView.findViewById(R.id.buttonQuery);
            final ScrollView scroll = (ScrollView) rootView.findViewById(R.id.scrollView);
            final EditText inputCommand = (EditText) rootView.findViewById(R.id.editText);

            //FULLSTAT
            final TextView gameIdTextView = (TextView) rootView.findViewById(R.id.textViewGameId);
            final TextView gameVersionTextView = (TextView) rootView.findViewById(R.id.textViewVersion);
            final TextView gamePluginsTextView = (TextView) rootView.findViewById(R.id.textViewPlugins);
            final Button showPlayerListButton = (Button) rootView.findViewById(R.id.buttonViewPlayerList);
            //ENDFULLSTAT

            //BIGSCREEN
            final TextView playerListTextView = (TextView) rootView.findViewById(R.id.textViewPlayers);



            scroll.post(new Runnable() {
                public void run() {
                    scroll.fullScroll(View.FOCUS_DOWN);
                }
            });

            showPlayerListButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intentPlayerListView = new Intent(getActivity(),playerListActivity.class);
                    if (getArguments().getInt(ARG_SECTION_NUMBER)==1){
                        intentPlayerListView.putExtra("ip", sharedPrefs.getString("server1", "127.0.0.1"));
                        intentPlayerListView.putExtra("port", Integer.parseInt(sharedPrefs.getString("port1", "25565")));
                    }else{
                        intentPlayerListView.putExtra("ip", sharedPrefs.getString("server2", "127.0.0.1"));
                        intentPlayerListView.putExtra("port", Integer.parseInt(sharedPrefs.getString("port2", "25565")));
                    }
                    startActivity(intentPlayerListView);

                }
            });

            final Handler updater1 = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    String text = (String)msg.obj;
                    try{
                        if (getArguments().getInt(ARG_SECTION_NUMBER)==1){
                            MCQuery mcQuery = new MCQuery(sharedPrefs.getString("server1", "127.0.0.1"), Integer.parseInt(sharedPrefs.getString("port1", "25565")));
                            String status;
                            QueryResponse response = mcQuery.fullStat();
                            String motd = response.getMOTD();
                            String gameMode = response.getGameMode();
                            String mapName = response.getMapName();
                            String onlinePlayers = Integer.toString(response.getOnlinePlayers());
                            String maxPlayers = Integer.toString(response.getMaxPlayers());

                            //FULLSTAT
                            String gameID = response.getGameID();
                            String gameVersion = response.getVersion();
                            String gamePlugins = response.getPlugins();
                            ArrayList<String> gamePlayerList = response.getPlayerList();
                            //ENDFULLSTAT

                            serverTextView.setText(motd);
                            modeTextView.setText(gameMode);
                            mapNameTextView.setText(mapName);
                            onlinePlayersTextView.setText(onlinePlayers);
                            maxPlayersTextView.setText(maxPlayers);

                            //FULLSTAT
                            gameIdTextView.setText(gameID);
                            gameVersionTextView.setText(gameVersion);
                            gamePluginsTextView.setText(gamePlugins);

                            //BIGCREEN
                            int listSize = gamePlayerList.size();
                            playerListTextView.setText("");
                            for (int i = 0; i<listSize; i++){
                                playerListTextView.append(" - " + gamePlayerList.get(i) + "\n\n");
                            }

                            logTextView.append(text + mcQuery.fullStat() + "\n");
                        }else{
                            MCQuery mcQuery = new MCQuery(sharedPrefs.getString("server2", "127.0.0.1"), Integer.parseInt(sharedPrefs.getString("port2", "25565")));
                            String status;
                            QueryResponse response = mcQuery.fullStat();
                            String motd = response.getMOTD();
                            String gameMode = response.getGameMode();
                            String mapName = response.getMapName();
                            String onlinePlayers = Integer.toString(response.getOnlinePlayers());
                            String maxPlayers = Integer.toString(response.getMaxPlayers());

                            //FULLSTAT
                            String gameID = response.getGameID();
                            String gameVersion = response.getVersion();
                            String gamePlugins = response.getPlugins();
                            ArrayList<String> gamePlayerList = response.getPlayerList();
                            //ENDFULLSTAT

                            serverTextView.setText(motd);
                            modeTextView.setText(gameMode);
                            mapNameTextView.setText(mapName);
                            onlinePlayersTextView.setText(onlinePlayers);
                            maxPlayersTextView.setText(maxPlayers);

                            //FULLSTAT
                            gameIdTextView.setText(gameID);
                            gameVersionTextView.setText(gameVersion);
                            gamePluginsTextView.setText(gamePlugins);

                            //BIGCREEN
                            int listSize = gamePlayerList.size();
                            playerListTextView.setText("");
                            for (int i = 0; i<listSize; i++){
                                playerListTextView.append(" - " + gamePlayerList.get(i) + "\n\n");
                            }

                            logTextView.append(text + mcQuery.fullStat() + "\n");
                        }
                        scroll.post(new Runnable() {
                            public void run() {
                                scroll.fullScroll(View.FOCUS_DOWN);
                            }
                        });



                    }catch (Exception e){
                        //final worker worker1 = new worker();
                        //final String trueorfalse = worker1.work();
                        if (getArguments().getInt(ARG_SECTION_NUMBER)==1){

                        logTextView.append("FAILED TO CONNECT TO MINECRAFT SERVER!\nMake sure you have included:\n    enable-query=true\n    query.port=25565\nin your server.properties file on the minecraft server\n\n" + "IP: " + sharedPrefs.getString("server1", "NOT SET!\nGo to the app settings by pressing the menu button and set the server ip and port") + "\nPort: " + sharedPrefs.getString("port1", "NOT SET!\n" + "Go to the app settings by pressing the menu button and set the server ip and port") + "\n");
                            Toast.makeText(getActivity(), logTextView.getText(), Toast.LENGTH_LONG).show();
                        }else{
                            logTextView.append("FAILED TO CONNECT TO MINECRAFT SERVER!\nMake sure you have included:\n    enable-query=true\n    query.port=25565\nin your server.properties file on the minecraft server\n\n" + "IP: " + sharedPrefs.getString("server2", "NOT SET!\nGo to the app settings by pressing the menu button and set the server ip and port") + "\nPort: " + sharedPrefs.getString("port2", "NOT SET!\n" + "Go to the app settings by pressing the menu button and set the server ip and port") + "\n");
                            Toast.makeText(getActivity(), logTextView.getText(), Toast.LENGTH_LONG).show();
                        }
                        scroll.post(new Runnable() {
                            public void run() {
                                scroll.fullScroll(View.FOCUS_DOWN);
                            }
                        });
                    };
                }
            };

            queryButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String textToSend = inputCommand.getText().toString() + "\n";
                    //updater1.sendMessage(updater1.obtainMessage());
                    Message msg = new Message();
                    msg.obj = textToSend;
                    updater1.sendMessage(msg);


                }
            });
            //queryButton.performClick();
            return rootView;


        }
    }

    }
