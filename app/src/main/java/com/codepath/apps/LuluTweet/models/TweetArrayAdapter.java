package com.codepath.apps.LuluTweet.models;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.codepath.apps.LuluTweet.R;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import java.util.List;

/**
 * Created by lucerne on 8/5/16.
 */
public class TweetArrayAdapter extends ArrayAdapter<Tweet> {
    public TweetArrayAdapter(Context context, List<Tweet> tweets){
        super(context, android.R.layout.simple_list_item_1, tweets);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        Tweet tweet = getItem(position);
        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_tweet, parent, false);

        }

        ImageView ivAvatar = (ImageView) convertView.findViewById(R.id.ivAvatar);
        TextView tvUserName = (TextView) convertView.findViewById(R.id.tvUserName);
        TextView tvBody = (TextView) convertView.findViewById(R.id.tvBody);
        TextView tvDate = (TextView) convertView.findViewById(R.id.tvDate);

        tvUserName.setText(tweet.getUser().getName());
        tvBody.setText(tweet.getBody());
        tvDate.setText(Tweet.getRelativeTimeAgo(tweet.getCreatedAt()));

        // set picasso image
        ivAvatar.setImageResource(android.R.color.transparent);
        Picasso.with(getContext()).load(tweet.getUser().getProfileImageUrl()).into(ivAvatar);

        // set user image tag
        ivAvatar.setTag(tweet.getUser().getScreenName().toString());

        ivAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(getContext(), "this is login successful", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getContext(), ProfileActivity.class);
                ImageView ivAvatar = (ImageView) v.findViewById(R.id.ivAvatar);
                intent.putExtra("screenName", Parcels.wrap(ivAvatar.getTag()));
                getContext().startActivity(intent);
            }
        });

        return convertView;
    }
}
