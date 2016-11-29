package com.example.badhri.huddle.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import com.digits.sdk.android.DigitsAuthButton;
import com.example.badhri.huddle.HuddleApplication;
import com.example.badhri.huddle.R;
import com.example.badhri.huddle.models.UserNonParse;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity {
    @BindView(R.id.etUsername)
    EditText etUsername;

    DigitsAuthButton digitsButton;
    private String username;
    UserNonParse user;

    private SharedPreferences mSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        digitsButton = (DigitsAuthButton) findViewById(R.id.auth_button);
        digitsButton.setCallback(((HuddleApplication) getApplication()).getAuthCallback());
        checkUsernameListener();
        mSettings = getApplicationContext().getSharedPreferences("Settings", 0);
        // checking if we already collected the user phone number in shared preferences;
        if (!mSettings.getString("phoneNumber", "missing").equals("missing") && !mSettings.getString("username", "missing").equals("missing")) {
            // is calling onClickCollectUsername;
            digitsButton.performClick();
        }
    }




    @OnClick(R.id.auth_button)
    public void onClickCollectUsername() {
        if (!mSettings.getString("username", "missing").equals("missing")) {
            username = mSettings.getString("username", "missing");
        } else {
            username = etUsername.getText().toString();
        }

        // checking from our list of users
        if (username.length() > 0) {
            SharedPreferences.Editor editor = mSettings.edit();
            editor.putString("username", username);
            editor.apply();
        }
    }

    private void checkUsernameListener() {
        etUsername.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //Check if 's' is empty
                if (s.length() == 0) {
                    digitsButton.setEnabled(false);
                } else {
                    digitsButton.setEnabled(true);
                }

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

}
