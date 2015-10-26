package com.codepath.apps.simpletweets.models;

import android.text.format.DateUtils;

import com.codepath.apps.simpletweets.activities.TimelineActivity;
import com.orm.SugarRecord;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by amore on 10/24/15.
 */
public class Tweet extends SugarRecord<Tweet> implements Serializable {

    private String body;
    private long uid;
    private String createdAt;
    private User user;
    private String mediaURL;

    public static Tweet fromJSON(JSONObject jsonObject) {
        Tweet tweet = new Tweet();
        try {
            tweet.body = jsonObject.getString("text");
            tweet.uid = jsonObject.getLong("id");
            tweet.createdAt = getRelativeTimeAgo(jsonObject.getString("created_at"));
            tweet.user = User.fromJSON(jsonObject.getJSONObject("user"));
            if (jsonObject.has("entities")) {
                if (jsonObject.getJSONObject("entities").getJSONArray("media").length() > 0) {
                    tweet.mediaURL = jsonObject.getJSONObject("entities").getJSONArray("media").getJSONObject(0).getString("media_url");
                }
            }
            if (tweet.uid < TimelineActivity.max_id) {
                TimelineActivity.max_id = tweet.uid;
            }
            if (tweet.uid > TimelineActivity.since_id) {
                TimelineActivity.since_id = tweet.uid;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return tweet;
    }

    public static ArrayList<Tweet> fromJSONArray(JSONArray jsonArray) {
        ArrayList<Tweet> tweets = new ArrayList<>();

        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                Tweet tweet = fromJSON(jsonArray.getJSONObject(i));
                if (tweet != null) {
                    tweets.add(tweet);
                    tweet.user.save();
                    tweet.save();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return tweets;
    }

    public static ArrayList<String> urlsFromJSONArray(JSONArray jsonArray) {
        ArrayList<String> urls = new ArrayList<>();

        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                JSONObject media = jsonArray.getJSONObject(i);
                if (media.getString("type") == "photo") {
                    String url = media.getString("media_url");
                    urls.add(url);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return urls;
    }

    public static String getRelativeTimeAgo(String rawJsonDate) {
        String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
        SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
        sf.setLenient(true);

        String relativeDate = "";
        try {
            long dateMillis = sf.parse(rawJsonDate).getTime();
            relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis,
                    System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        String[] dateFrags = relativeDate.split(" ");
        String formattedDate = dateFrags[0] + dateFrags[1].charAt(0);
        return formattedDate;
    }

    public String getBody() {
        return body;
    }

    public long getUid() {
        return uid;
    }

    public String getMediaURL() {
        return mediaURL;
    }

    public User getUser() {
        return user;
    }

    public String getCreatedAt() {
        return createdAt;
    }

}
