package com.codepath.apps.simpletweets.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.codepath.apps.simpletweets.R;
import com.codepath.apps.simpletweets.TwitterApplication;
import com.codepath.apps.simpletweets.TwitterClient;
import com.codepath.apps.simpletweets.activities.SearchResultsActivity;
import com.codepath.apps.simpletweets.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by amore on 11/1/15.
 */
public class SearchResultsFragment extends TweetsListFragment {
    private TwitterClient client;

    public static SearchResultsFragment newInstance(String query) {
        SearchResultsFragment searchResultsFragment = new SearchResultsFragment();
        Bundle args = new Bundle();
        args.putString("query", query);
        searchResultsFragment.setArguments(args);
        return searchResultsFragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        client = TwitterApplication.getRestClient();
        populateTimeline();
    }

    public void populateTimeline() {
        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        String query = sharedPref.getString("query", "android");

        client.getSearchResults(query, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    addAll(Tweet.fromJSONArray(response.getJSONArray("statuses")));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Toast.makeText(getContext(), "FAILED", Toast.LENGTH_LONG).show();
            }
        });
    }
}
