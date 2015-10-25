package com.codepath.apps.simpletweets.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.simpletweets.R;
import com.codepath.apps.simpletweets.models.Tweet;
import com.squareup.picasso.Picasso;

public class DetailedViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_view);
        Tweet tweet = (Tweet) getIntent().getSerializableExtra("tweet");

        ImageView ivDetailedProfileImage = (ImageView) findViewById(R.id.ivDetailedProfileImage);
        TextView tvDetailedUserName = (TextView) findViewById(R.id.tvDetailedUserName);
        TextView tvDetailedBody = (TextView) findViewById(R.id.tvDetailedBody);
        TextView tvDetailedTimestamp = (TextView) findViewById(R.id.tvDetailedTimestamp);
        ImageView ivDetailedMedia = (ImageView) findViewById(R.id.ivDetailedMedia);

        tvDetailedUserName.setText(tweet.getUser().getScreenName());
        tvDetailedBody.setText(tweet.getBody());
        tvDetailedTimestamp.setText(tweet.getCreatedAt());

        ivDetailedProfileImage.setImageResource(android.R.color.transparent);
        Picasso.with(this).load(tweet.getUser().getProfileImageUrl()).into(ivDetailedProfileImage);

        if (tweet.getMediaURL() != null) {
            ivDetailedMedia.setVisibility(View.VISIBLE);
            Picasso.with(this).load(tweet.getMediaURL()).into(ivDetailedMedia);
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
}
