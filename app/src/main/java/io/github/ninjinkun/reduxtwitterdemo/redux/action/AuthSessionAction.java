package io.github.ninjinkun.reduxtwitterdemo.redux.action;

import com.twitter.sdk.android.core.Session;
import com.twitter.sdk.android.core.TwitterAuthToken;
import com.twitter.sdk.android.core.TwitterException;

import io.github.ninjinkun.redux.Action;

public class AuthSessionAction {
    public static class LoadAuthSessionAction implements Action {
        public final Session<TwitterAuthToken> session;

        public LoadAuthSessionAction(Session<TwitterAuthToken> session) {
            this.session = session;
        }
    }

    public static class ErrorAuthSessionAction implements Action {
        public final TwitterException exception;

        public ErrorAuthSessionAction(TwitterException exception) {
            this.exception = exception;
        }
    }
}
