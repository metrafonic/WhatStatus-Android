package com.metrafonic.whatstatus.checker;

import android.app.DownloadManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.net.URL;

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

        new LongOperation().execute("");
        return rootView;
    }

    private class LongOperation extends AsyncTask<String, Integer, String> {

        private static final int DEFAULT_NUMBER_OF_TWEETS = 20;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... params) {
            String result = params[0];
            Token token = new Token(SecretData.TOKEN_ACCESS,
                    SecretData.TOKEN_SECRET);
            @SuppressWarnings("deprecation")
            Credential cred = new Credential(SecretData.USERNAME,
                    SecretData.CONSUMER_KEY, SecretData.CONSUMER_SECRET, token);
            UserAccountManager manager = UserAccountManager.getInstance(cred);
            try {
                if (manager.verifyCredential()) {
                    Timeline timeline = Timeline.getInstance(manager);
                    DownloadManager.Query query = QueryComposer.count(DEFAULT_NUMBER_OF_TWEETS);
                    timeline.startGetUserTweets(query,
                            new SearchDeviceListener() {
                                int count = 0;

                                @Override
                                public void tweetFound(Tweet tweet) {
                                    if (++count == 1) {
                                        exampleTweetText = getTweetText(tweet);
                                        showLogMessage(getTweetText(tweet));
                                    }
                                    // showLogMessage(tweet.toString());
                                }

                                private String getTweetText(Tweet tweet) {
                                    return tweet.getString("TWEET_CONTENT");
                                }

                                @Override
                                public void searchFailed(Throwable fail) {
                                    showLogMessage(fail.getMessage());
                                }

                                @Override
                                public void searchCompleted() {
                                    showLogMessage("Tweet search completed");
                                    Main.this.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            showShortToast(exampleTweetText);
                                        }
                                    });
                                }
                            });
                } else {
                    Log.d(TAG, "details weren't verified");
                }
            } catch (IOException e) {
                e.printStackTrace();
                showLogMessage("IO Error");
            } catch (LimitExceededException e) {
                e.printStackTrace();
                showLogMessage("API Limit Reached");
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

        }
    }
}
