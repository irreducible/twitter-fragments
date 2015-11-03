package com.codepath.apps.simpletweets.activities;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.codepath.apps.simpletweets.R;
import com.codepath.apps.simpletweets.TwitterApplication;
import com.codepath.apps.simpletweets.TwitterClient;
import com.codepath.apps.simpletweets.dialogs.ReplyDialog;
import com.codepath.apps.simpletweets.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.apache.http.Header;
import org.json.JSONObject;
import org.scribe.builder.api.TwitterApi;

public class DetailedViewActivity extends AppCompatActivity {
    private long tweetId;
    private String screenName;

    private TwitterClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#3687C5")));

        setContentView(R.layout.activity_detailed_view);
        Tweet tweet = (Tweet) getIntent().getSerializableExtra("tweet");

        tweetId = tweet.getUid();
        screenName = tweet.getUser().getScreenName();

        ImageView ivDetailedProfileImage = (ImageView) findViewById(R.id.ivDetailedProfileImage);
        TextView tvDetailedUserName = (TextView) findViewById(R.id.tvDetailedUserName);
        TextView tvDetailedBody = (TextView) findViewById(R.id.tvDetailedBody);
        TextView tvDetailedTimestamp = (TextView) findViewById(R.id.tvDetailedTimestamp);
        ImageView ivDetailedMedia = (ImageView) findViewById(R.id.ivDetailedMedia);
        CheckBox cbFavorite = (CheckBox) findViewById(R.id.cbFavorite);
        Button btRetweet = (Button) findViewById(R.id.btRetweet);

        client = TwitterApplication.getRestClient();
        cbFavorite.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    client.favoriteTweet(tweetId, new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            Toast.makeText(getBaseContext(), "Tweet Favorited", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                else {
                    client.unfavoriteTweet(tweetId, new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            Toast.makeText(getBaseContext(), "Tweet Unfavorited", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

        btRetweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                client.reTweet(tweetId, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        Toast.makeText(getBaseContext(), "Retweeted", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        tvDetailedUserName.setText(tweet.getUser().getScreenName());
        tvDetailedBody.setText(tweet.getBody());
        tvDetailedTimestamp.setText(tweet.getCreatedAt());

        ivDetailedProfileImage.setImageResource(android.R.color.transparent);
        Picasso.with(this).load(tweet.getUser().getProfileImageUrl().replace("_normal", "_bigger")).into(ivDetailedProfileImage);

        if (tweet.getMediaURL() != null) {
            ivDetailedMedia.setVisibility(View.VISIBLE);
            Picasso.with(this).load(tweet.getMediaURL() + ":large").into(ivDetailedMedia);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_detailed_view, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void showReplyDialog(MenuItem item) {
        FragmentManager fm = getSupportFragmentManager();
        ReplyDialog replyDialog = ReplyDialog.newInstance(tweetId, screenName);
        replyDialog.show(fm, "fragment_edit_name");
    }
}
