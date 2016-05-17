package io.github.ninjinkun.reduxtwitterdemo.redux.state;

import com.twitter.sdk.android.core.Session;
import com.twitter.sdk.android.core.TwitterAuthToken;
import com.twitter.sdk.android.core.TwitterException;

public class AuthSessionState implements Cloneable {
    private Session<TwitterAuthToken> session;
    private TwitterException twitterException;

    public Session<TwitterAuthToken> getSession() {
        return session;
    }

    public void setSession(Session<TwitterAuthToken> session) {
        this.session = session;
    }

    public TwitterException getTwitterException() {
        return twitterException;
    }

    public void setTwitterException(TwitterException twitterException) {
        this.twitterException = twitterException;
    }

    @Override
    public AuthSessionState clone() {
        AuthSessionState object = null;
        try {
            object = (AuthSessionState) super.clone();
            object.setSession(session);
            object.setTwitterException(twitterException);
        } catch (CloneNotSupportedException e) {

        }
        return object;
    }
}
