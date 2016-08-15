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
public class HomeTimelineFragment extends TweetsListFragment {
    private TweetClient client;

    public static HomeTimelineFragment newInstance(int i) {
        HomeTimelineFragment fragment = new HomeTimelineFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        client = TweetApplication.getRestClient();
        populateTimeline(0);

    }

    public void populateTimeline(long offset){
        client.getTimeline( new JsonHttpResponseHandler() {
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

    public void postTimeline(Tweet tweet, int reply){
        // post to twitter
        if (reply == 1){
//            tweet.setIn_reply_to_status_id(replyTweetId);
        }

        client.postTimeline(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject jsonObj) {
                jsonObj.toString();

                // inject tweet into adapter
                Tweet tweet = Tweet.fromJSON(jsonObj);

                // show the latest tweet at the top
                aTweets.insert(tweet, 0);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable,
                                  JSONObject errorResponse) {
                errorResponse.toString();
            }
        }, tweet);
    }

}
