package com.codepath.apps.simpletweets.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.codepath.apps.simpletweets.R;
import com.codepath.apps.simpletweets.activities.ProfileActivity;
import com.codepath.apps.simpletweets.activities.TimelineActivity;
import com.codepath.apps.simpletweets.models.Tweet;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by amore on 10/24/15.
 */
public class TweetsArrayAdapter extends ArrayAdapter<Tweet> {

    public TweetsArrayAdapter(Context context, List<Tweet> tweets) {
        super(context, android.R.layout.simple_list_item_1, tweets);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Tweet tweet = getItem(position);

        ViewHolder viewHolder;

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_tweet, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.ivProfileImage = (ImageView) convertView.findViewById(R.id.ivProfileImage);
            viewHolder.tvUserName = (TextView) convertView.findViewById(R.id.tvUserName);
            viewHolder.tvBody = (TextView) convertView.findViewById(R.id.tvBody);
            viewHolder.tvTimestamp = (TextView) convertView.findViewById(R.id.tvTimestamp);
            viewHolder.ivMedia = (ImageView) convertView.findViewById(R.id.ivMedia);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.tvUserName.setText("@" + tweet.getUser().getScreenName());
        viewHolder.tvBody.setText(tweet.getBody());
        viewHolder.tvTimestamp.setText(tweet.getCreatedAt());

        viewHolder.ivProfileImage.setImageResource(android.R.color.transparent);
        Picasso.with(getContext()).load(tweet.getUser().getProfileImageUrl()).into(viewHolder.ivProfileImage);

        viewHolder.ivProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ProfileActivity.class);
                intent.putExtra("screenName", tweet.getUser().getScreenName());
                intent.putExtra("user", tweet.getUser());
                v.getContext().startActivity(intent);
            }
        });

        if (tweet.getMediaURL() != null) {
            viewHolder.ivMedia.setVisibility(View.VISIBLE);
            Picasso.with(getContext()).load(tweet.getMediaURL()).into(viewHolder.ivMedia);
        } else {
            viewHolder.ivMedia.setVisibility(View.GONE);
        }

        return convertView;
    }

    static class ViewHolder {
        ImageView ivProfileImage;
        TextView tvUserName;
        TextView tvBody;
        TextView tvTimestamp;
        ImageView ivMedia;
    }

}
