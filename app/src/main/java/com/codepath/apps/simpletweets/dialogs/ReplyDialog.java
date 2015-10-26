package com.codepath.apps.simpletweets.dialogs;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.simpletweets.R;
import com.codepath.apps.simpletweets.TwitterApplication;
import com.codepath.apps.simpletweets.TwitterClient;
import com.codepath.apps.simpletweets.activities.TimelineActivity;
import com.codepath.apps.simpletweets.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.apache.http.Header;
import org.json.JSONObject;


public class ReplyDialog extends DialogFragment {
    private Button btCancel;
    private Button btTweet;
    private TextView tvUser;
    private ImageView ivUser;
    private EditText etCompose;
    private TextView tvCount;

    private TwitterClient client;
    private User self;

    public ReplyDialog() {
        // Empty constructor is required for DialogFragment
        // Make sure not to add arguments to the constructor
        // Use `newInstance` instead as shown below
    }

    public static ReplyDialog newInstance(long inReplyToId, String screenName) {
        ReplyDialog frag = new ReplyDialog();
        Bundle args = new Bundle();
        args.putLong("inReplyToId", inReplyToId);
        args.putString("screenName", screenName);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        View view = inflater.inflate(R.layout.layout_compose, container);

        tvUser = (TextView) view.findViewById(R.id.tvUser);
        ivUser = (ImageView) view.findViewById(R.id.ivUser);

        client = TwitterApplication.getRestClient();

        client.getUserInfo(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                self = User.fromJSON(response);
                tvUser.setText(self.getName());

                Picasso.with(getContext()).load(self.getProfileImageUrl()).into(ivUser);
            }
        });

        etCompose = (EditText) view.findViewById(R.id.etCompose);
        etCompose.setText("@" + getArguments().getString("screenName"));
        etCompose.setSelection(etCompose.getText().length());

        tvCount = (TextView) view.findViewById(R.id.tvCount);

        etCompose.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tvCount.setText("" + (140 - etCompose.getEditableText().toString().length()));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        btCancel = (Button) view.findViewById(R.id.btCancel);
        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        btTweet = (Button) view.findViewById(R.id.btTweet);
        btTweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                client.postTweet(etCompose.getEditableText().toString(), getArguments().getLong("inReplyToId"), new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        dismiss();
                    }
                });
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

}
