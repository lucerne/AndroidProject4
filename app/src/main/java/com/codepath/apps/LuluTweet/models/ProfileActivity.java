package com.codepath.apps.LuluTweet.models;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.codepath.apps.LuluTweet.R;
import com.codepath.apps.LuluTweet.TweetApplication;
import com.codepath.apps.LuluTweet.TweetClient;
import com.codepath.apps.LuluTweet.fragment.UserTimelineFragment;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;
import org.parceler.Parcels;

import cz.msebera.android.httpclient.Header;

public class ProfileActivity extends AppCompatActivity {
    TweetClient client;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        String screenName = Parcels.unwrap(getIntent().getParcelableExtra("screenName"));
//        Toast.makeText(this, screenName, Toast.LENGTH_SHORT).show();

        client = TweetApplication.getRestClient();
        if (screenName == null){
            client.getProfileUserInfo(new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONObject response) {
                    user = User.fromJSON(response);
                    getSupportActionBar().setTitle("@" + user.getScreenName());
                    Log.d("DEBUG", user.getScreenName());
                    populateProfileHeader(user);

                }
            });
        }
        else {
            client.getUserInfo(new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONObject response) {
                    user = User.fromJSON(response);
                    getSupportActionBar().setTitle("@" + user.getScreenName());
                    Log.d("DEBUG", user.getScreenName());
                    populateProfileHeader(user);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    super.onFailure(statusCode, headers, throwable, errorResponse);
//                    Toast.makeText(getBaseContext(), "failed to load profile", Toast.LENGTH_SHORT).show();
                }
            }, screenName);
        }

//        Get the screen name
//        String screenName = getIntent().getStringExtra("screen_name");
//
        UserTimelineFragment fragmentUserTimeline =
                UserTimelineFragment.newInstance(screenName);

        if (savedInstanceState == null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.flContainer, fragmentUserTimeline);
            ft.commit();
        }
    }

    private void populateProfileHeader(User user) {
        TextView tvUserName = (TextView) findViewById(R.id.tvUserName);
        TextView tvTagline = (TextView) findViewById(R.id.tvTagline);
        TextView tvFollowers = (TextView) findViewById(R.id.tvFollowers);
        TextView tvFollowing = (TextView) findViewById(R.id.tvFollowing);
        ImageView ivAvatar = (ImageView) findViewById(R.id.ivAvatar);

        tvUserName.setText(user.getName());
        tvTagline.setText(user.getTagline());
        tvFollowers.setText(user.getFollowersCount() + " Followers ");
        tvFollowing.setText(user.getFollowingCount() + " Following");
        Picasso.with(this).load(user.getProfileImageUrl()).into(ivAvatar);
    }

}
