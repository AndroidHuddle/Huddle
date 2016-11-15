package com.example.badhri.huddle.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.digits.sdk.android.DigitsAuthButton;
import com.example.badhri.huddle.MainClientApp;
import com.example.badhri.huddle.R;

public class LoginActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        DigitsAuthButton digitsButton = (DigitsAuthButton) findViewById(R.id.auth_button);
        digitsButton.setCallback(((MainClientApp) getApplication()).getAuthCallback());
    }

}
