package com.codepath.apps.simpletweets.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.simpletweets.R;
import com.codepath.apps.simpletweets.activities.ProfileActivity;
import com.codepath.apps.simpletweets.models.User;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by amore on 11/1/15.
 */
public class UsersArrayAdapter extends ArrayAdapter<User> {

    public UsersArrayAdapter(Context context, List<User> users) {
        super(context, android.R.layout.simple_list_item_1, users);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final User user = getItem(position);

        ViewHolder viewHolder;

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_user, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.ivProfilePic = (ImageView) convertView.findViewById(R.id.ivProfilePic);
            viewHolder.tvUserHandle = (TextView) convertView.findViewById(R.id.tvUserHandle);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.tvUserHandle.setText("@" + user.getScreenName());

        viewHolder.ivProfilePic.setImageResource(android.R.color.transparent);
        Picasso.with(getContext()).load(user.getProfileImageUrl()).into(viewHolder.ivProfilePic);

        viewHolder.ivProfilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ProfileActivity.class);
                intent.putExtra("screenName", user.getScreenName());
                intent.putExtra("user", user);
                v.getContext().startActivity(intent);
            }
        });

        return convertView;
    }

    static class ViewHolder {
        ImageView ivProfilePic;
        TextView tvUserHandle;
    }
}
