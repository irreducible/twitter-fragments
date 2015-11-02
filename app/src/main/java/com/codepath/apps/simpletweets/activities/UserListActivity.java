package com.codepath.apps.simpletweets.activities;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.codepath.apps.simpletweets.R;
import com.codepath.apps.simpletweets.TwitterApplication;
import com.codepath.apps.simpletweets.TwitterClient;
import com.codepath.apps.simpletweets.adapters.UsersArrayAdapter;
import com.codepath.apps.simpletweets.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class UserListActivity extends AppCompatActivity {
    private TwitterClient client;

    private UsersArrayAdapter aUsers;
    private ArrayList<User> users;
    private ListView lvUsers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#3687C5")));

        String screenName = getIntent().getStringExtra("screenName");
        String list = getIntent().getStringExtra("list");

        lvUsers = (ListView) findViewById(R.id.lvUsers);

        if (list.equals("followers")) {
            client = TwitterApplication.getRestClient();
            client.getUserFollowers(screenName, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    try {
                        users = User.fromJSONArray(response.getJSONArray("users"));
                        aUsers = new UsersArrayAdapter(UserListActivity.this, users);
                        lvUsers.setAdapter(aUsers);
                        aUsers.addAll(users);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            });
        }
        else if (list.equals("following")) {
            client = TwitterApplication.getRestClient();
            client.getUserFriends(screenName, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    try {
                        users = User.fromJSONArray(response.getJSONArray("users"));
                        aUsers = new UsersArrayAdapter(UserListActivity.this, users);
                        lvUsers.setAdapter(aUsers);
                        aUsers.addAll(users);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_user_list, menu);
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
