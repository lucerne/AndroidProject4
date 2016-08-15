package com.codepath.apps.LuluTweet.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.codepath.apps.LuluTweet.EndlessScrollListener;
import com.codepath.apps.LuluTweet.R;
import com.codepath.apps.LuluTweet.models.Tweet;
import com.codepath.apps.LuluTweet.models.TweetArrayAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lucerne on 8/8/16.
 */
public abstract class TweetsListFragment extends Fragment {


    private ArrayList<Tweet> tweets;
    TweetArrayAdapter aTweets;
    private ListView lvTweets;

    // infinite scroll
    private long lastMaxId;
    SwipeRefreshLayout swipeContainer;

    // reply
    private long replyTweetId;
    private String replyScreenname;
//    private OnItemSelectedListener listener;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_tweets_list, container, false);

        // Set view, data, and adapter
        lvTweets= (ListView) v.findViewById(R.id.lvTweet);
        lvTweets.setAdapter(aTweets);


        // Attach the listener to the AdapterView onCreate
        lvTweets.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public boolean onLoadMore(int page, int totalItemsCount) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to your AdapterView
                if (tweets.size() > 0) {
                    lastMaxId = tweets.get(tweets.size()-1).getUid();
                    populateTimeline(lastMaxId);
                }
                else {
                    populateTimeline(0);
                }
                // or customLoadMoreDataFromApi(totalItemsCount);
                return true; // ONLY if more data is actually being loaded; false otherwise.
            }
        });


        // Lookup the swipe container view
        swipeContainer = (SwipeRefreshLayout) v.findViewById(R.id.swipeContainer);
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                aTweets.clear();
                tweets.clear();
                populateTimeline(0);
            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

//         Launch tweet view
        lvTweets.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                launchTweetViewActivity(position);
                replyTweetId = tweets.get(position).getUid();
                replyScreenname = tweets.get(position).getUser().getScreenName();

//                launchTweetViewActivity(tweet, position, replyTweetId, replyScreenname);

            }
        });

        return v;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        tweets = new ArrayList<>();
        aTweets = new TweetArrayAdapter(getActivity(), tweets);

    }

    public void allAll(List<Tweet> tweets){
        aTweets.addAll(tweets);
    }

    // Abstract method to be overridden
    protected abstract void populateTimeline(long maxId);

//    // Define the events that the fragment will use to communicate
//    public interface OnItemSelectedListener {
//        // This can be any number of events to be sent to the activity
//        public void onTweetItemSelected(Tweet tweet);
//    }
//
//    // Store the listener (activity) that will have events fired once the fragment is attached
//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        if (context instanceof OnItemSelectedListener) {
//            listener = (OnItemSelectedListener) context;
//        } else {
//            throw new ClassCastException(context.toString()
//                    + " must implement MyListFragment.OnItemSelectedListener");
//        }
//    }

//    // Now we can fire the event when the user selects something in the fragment
//    public void onSomeClick(View v) {
//        listener.onTweetItemSelected();
//    }

}
