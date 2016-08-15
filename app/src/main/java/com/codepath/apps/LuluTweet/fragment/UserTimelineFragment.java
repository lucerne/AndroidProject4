package com.codepath.apps.LuluTweet.fragment;

import android.app.Fragment;
import android.os.Bundle;

import com.codepath.apps.LuluTweet.TweetApplication;
import com.codepath.apps.LuluTweet.TweetClient;
import com.codepath.apps.LuluTweet.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

/**
 * Created by lucerne on 8/11/16.
 */
public class UserTimelineFragment extends TweetsListFragment {
    private TweetClient client;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        client = TweetApplication.getRestClient();
        populateTimeline(0);

    }

    public static UserTimelineFragment newInstance(String screen_name){
        UserTimelineFragment userFragment = new UserTimelineFragment();
        Bundle args = new Bundle();
        args.putString("screen_name", screen_name);
        userFragment.setArguments(args);
        return userFragment;
    }


    public void populateTimeline(long offset){
        String screen_name = getArguments().getString("screen_name");
        client.getUserTimeline(screen_name, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                response.toString();

                if (response != null) {
                    allAll(Tweet.fromJSONArray(response));
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable,
                                  JSONObject errorResponse) {
                errorResponse.toString();
            }
        });
    }
}

