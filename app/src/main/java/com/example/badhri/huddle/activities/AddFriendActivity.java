package com.example.badhri.huddle.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.badhri.huddle.R;
import com.example.badhri.huddle.models.UserNonParse;
import com.example.badhri.huddle.parseModels.FriendRequest;
import com.example.badhri.huddle.parseModels.User;
import com.example.badhri.huddle.utils.ParsePushHelper;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import java.util.List;

public class AddFriendActivity extends AppCompatActivity {
    //@BindView(R.id.tvPhoneNumber)
    EditText etPhoneNumber;

    //@BindView(R.id.btSendRequest)
    Button btSendRequest;

    private String userObjectId;
    UserNonParse user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);
        user = getIntent().getParcelableExtra("user");
        userObjectId = user.getParseId();
        etPhoneNumber = (EditText)findViewById(R.id.etPhoneNumber);
        btSendRequest = (Button)findViewById(R.id.btSendRequest);
        btSendRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ButtonClick(v);
            }
        });
    }

    public void ButtonClick(final View v) {
        final String number = etPhoneNumber.getText().toString();
        if (!number.isEmpty()) {
            //Query the user table to see if such an User exist
            ParseQuery<User> query = ParseQuery.getQuery(User.class);
            query.whereEqualTo(User.PHONE_NUMBER_KEY, Long.parseLong(number));
            query.findInBackground(new FindCallback<User>() {
                public void done(List<User> friendList, ParseException e) {
                    if (e == null) {
                        if (friendList.isEmpty()) {
                            // User not found
                            Toast.makeText(getBaseContext(), "User not found", Toast.LENGTH_LONG).show();
                        } else {
                            //Save new request to the table
                            //TODO: filter duplicate request
                            addFriendRequest(number, friendList.get(0).getObjectId(), v.getContext());
                        }
                    }
                }
            });
        }
    }

    private void addFriendRequest(final String phoneNumber, String friendId, final Context context) {
        FriendRequest request = new FriendRequest();
        request.setStatus("pending");
        request.setToUser(friendId);
        request.setFromUser(userObjectId);


        request.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if(e == null) {
                    Toast.makeText(context, "Friend request sent",
                            Toast.LENGTH_LONG).show();
                    ParsePushHelper.pushToUser(phoneNumber, "You have a new friend request", "Friend Request");

                } else {
                    Toast.makeText(context, "Error while sending friend request. Try again",
                            Toast.LENGTH_LONG).show();
                }
            }
        });

    }
}
