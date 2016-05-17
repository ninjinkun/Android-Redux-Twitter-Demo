package io.github.ninjinkun.reduxtwitterdemo.redux.state;

import android.support.annotation.NonNull;

import io.github.ninjinkun.redux.StateType;

public class AppState implements Cloneable, StateType {
    @NonNull
    private TimelineState timeline = new TimelineState();
    @NonNull
    private LikedTweetsState likedTweets = new LikedTweetsState();
    @NonNull
    private AuthSessionState session = new AuthSessionState();

    @NonNull
    public TimelineState getTimeline() {
        return timeline;
    }

    public void setTimeline(@NonNull TimelineState timeline) {
        this.timeline = timeline;
    }

    @NonNull
    public AuthSessionState getSession() {
        return session;
    }

    public void setSession(@NonNull AuthSessionState session) {
        this.session = session;
    }

    @NonNull
    public LikedTweetsState getLikedTweets() {
        return likedTweets;
    }

    public void setLikedTweets(@NonNull LikedTweetsState likedTweets) {
        this.likedTweets = likedTweets;
    }

    @Override
    public AppState clone() {
        AppState object = null;
        try {
            object = (AppState) super.clone();
            object.timeline = timeline.clone();
            object.session = session.clone();
            object.likedTweets = likedTweets.clone();
        } catch (CloneNotSupportedException e) {

        }
        return object;
    }
}
