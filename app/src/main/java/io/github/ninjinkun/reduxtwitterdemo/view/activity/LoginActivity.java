package io.github.ninjinkun.reduxtwitterdemo.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;

import io.github.ninjinkun.reduxtwitterdemo.R;
import io.github.ninjinkun.reduxtwitterdemo.redux.StoreCreator;
import io.github.ninjinkun.reduxtwitterdemo.redux.action.AuthSessionAction;

public class LoginActivity extends AppCompatActivity {
    private TwitterLoginButton loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginButton = (TwitterLoginButton) findViewById(R.id.login_button);
        loginButton.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                StoreCreator.store.dispatch(new AuthSessionAction.LoadAuthSessionAction(result.data));
                finish();
            }

            @Override
            public void failure(TwitterException exception) {
                StoreCreator.store.dispatch(new AuthSessionAction.ErrorAuthSessionAction(exception));
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        loginButton.onActivityResult(requestCode, resultCode, data);
    }
}
