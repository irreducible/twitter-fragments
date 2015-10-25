package com.codepath.apps.simpletweets.models;

import android.text.format.DateUtils;

import com.codepath.apps.simpletweets.activities.TimelineActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by amore on 10/24/15.
 */
public class Tweet {

    private String body;
    private long uid;
    private String createdAt;
    private User user;
    private ArrayList<String> media_urls;

    public String getBody() {
        return body;
    }

    public long getUid() {
        return uid;
    }

    public ArrayList<String> getMedia_urls() {
        if (media_urls == null) {
            media_urls = new ArrayList<>();
            media_urls.add("http://cdn.vrworld.com/wp-content/uploads/2015/08/android-for-wallpaper-8.png");
        }
        return media_urls;
    }

    public User getUser() {
        return user;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public static Tweet fromJSON(JSONObject jsonObject) {
        Tweet tweet = new Tweet();
        try {
            tweet.body = jsonObject.getString("text");
            tweet.uid = jsonObject.getLong("id");
            tweet.createdAt = getRelativeTimeAgo(jsonObject.getString("created_at"));
            tweet.user = User.fromJSON(jsonObject.getJSONObject("user"));
            tweet.media_urls = urlsFromJSONArray(jsonObject.getJSONObject("entities").getJSONArray("media"));
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
                    String url = jsonArray.getJSONObject(i).getString("media_url");
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

}
