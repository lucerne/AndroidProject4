package com.codepath.apps.LuluTweet.fragment;

import android.os.Bundle;

import com.codepath.apps.LuluTweet.TweetApplication;
import com.codepath.apps.LuluTweet.TweetClient;
import com.codepath.apps.LuluTweet.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

/**
 * Created by lucerne on 8/9/16.
 */
public class MentionsTimelineFragment extends TweetsListFragment{
    private TweetClient client;

    public static MentionsTimelineFragment newInstance(int i) {
        MentionsTimelineFragment fragment = new MentionsTimelineFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        client = TweetApplication.getRestClient();
        populateTimeline(0);

    }


    public void populateTimeline(long offset){
        client.getMentionsTimeline( new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                response.toString();

                if (response != null) {
                    allAll(Tweet.fromJSONArray(response));
                    swipeContainer.setRefreshing(false);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable,
                                  JSONObject errorResponse) {
                errorResponse.toString();
            }
        }, offset);
    }
}
