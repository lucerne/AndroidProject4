package com.codepath.apps.LuluTweet;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.codepath.apps.LuluTweet.fragment.HomeTimelineFragment;
import com.codepath.apps.LuluTweet.fragment.MentionsTimelineFragment;
import com.codepath.apps.LuluTweet.fragment.TweetsListFragment;
import com.codepath.apps.LuluTweet.models.ProfileActivity;
import com.codepath.apps.LuluTweet.models.SmartFragmentStatePagerAdapter;
import com.codepath.apps.LuluTweet.models.Tweet;

import org.parceler.Parcels;

public class TimeLineActivity extends AppCompatActivity {

    private TweetsListFragment fragmentTweetsList;
    private SmartFragmentStatePagerAdapter adapterViewPager;

    // reply
    private long replyTweetId;
    private String replyScreenname;
    // compose
    private final int COMPOSE_REQUEST_CODE = 1;
    private final int VIEW_REQUEST_CODE = 2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_line);


//        get view pager
        ViewPager vpPager = (ViewPager) findViewById(R.id.viewpager);
//        set viewpager adapter for the pager
        adapterViewPager = new TweetsPagerAdapter(getSupportFragmentManager());
        vpPager.setAdapter(adapterViewPager);
//        find pager sliding tabs

        // Give the TabLayout the ViewPager
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(vpPager);
    }

    // Menu icons are inflated just as they were with actionbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_timeline, menu);
        return true;
    }

    public void onProfileView(MenuItem mi){
//        launch profile view
        Intent intent = new Intent(this, ProfileActivity.class);
        startActivity(intent);
        intent.putExtra("screenName", Parcels.wrap(""));
        startActivity(intent);
    }

    // return the order of fragment in the view pager
    public class TweetsPagerAdapter extends SmartFragmentStatePagerAdapter{
        final int PAGE_COUNT = 2;
        private String tabTitles[] = {"Home", "Mentions"};

        // tab icon
        private int[] imageResId = {
                R.drawable.home_icon,
                R.drawable.mentions_icon
        };

        public TweetsPagerAdapter(FragmentManager fm){
            super(fm);
        }

        // order and creation of fragments within the pager
        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                return HomeTimelineFragment.newInstance(0);
            } else if (position == 1) {
//                return new MentionsTimelineFragment();
                return MentionsTimelineFragment.newInstance(1);
            } else {
                return null;
            }
        }

        // tab title
        @Override
        public CharSequence getPageTitle(int position){
            Drawable image = ContextCompat.getDrawable(getBaseContext(), imageResId[position]);
            image.setBounds(0, 0, image.getIntrinsicWidth(), image.getIntrinsicHeight());
            SpannableString sb = new SpannableString(" ");
            ImageSpan imageSpan = new ImageSpan(image, ImageSpan.ALIGN_BOTTOM);
            sb.setSpan(imageSpan, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            return tabTitles[position];
        }

//        how many fragments there are to swipe
        @Override
        public int getCount() {
            return PAGE_COUNT;
        }
    }


    public void onCompose(MenuItem mi){
        launchComposeActivity(0);
    }

    public void launchComposeActivity(int reply){
        Tweet tweet = new Tweet();
        Intent intent = new Intent(this, ComposeActivity.class);

        // reply screen name
        if (reply == 1 && replyScreenname != null) {
            tweet.setIn_reply_to_username("@" + replyScreenname + " ");
        }
        intent.putExtra("tweet", Parcels.wrap(tweet));
        intent.putExtra("reply", Parcels.wrap(reply));
        startActivityForResult(intent, COMPOSE_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == COMPOSE_REQUEST_CODE) {
                final Tweet tweet = (Tweet) Parcels.unwrap(data.getParcelableExtra("tweet"));
                Log.d("DEBUG", tweet.toString());

                final int reply = (int) Parcels.unwrap(data.getParcelableExtra("reply"));

                // fragment to refresh

                HomeTimelineFragment fg = (HomeTimelineFragment) adapterViewPager.getRegisteredFragment(0);
                fg.postTimeline(tweet, reply);
            }
            // reply to earlier tweet
            if (requestCode == VIEW_REQUEST_CODE) {
                launchComposeActivity(1);
            }
        }
    }

    public void launchTweetViewActivity(Tweet tweet, int position, int i, String s){

        // view detailed tweet
        Intent intent = new Intent(this, DetailedTweetActivity.class);
        intent.putExtra("tweet", Parcels.wrap(tweet));
        startActivityForResult(intent, VIEW_REQUEST_CODE);
    }

}
