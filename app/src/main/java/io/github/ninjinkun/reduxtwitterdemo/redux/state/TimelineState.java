package io.github.ninjinkun.reduxtwitterdemo.redux.state;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.models.Tweet;

import java.util.List;

public class TimelineState implements Cloneable {
    @Nullable
    private List<Tweet> tweets;
    @NonNull
    private FetchStatus fetchStatus = FetchStatus.Initial;
    @Nullable
    private Long sinceId;
    @Nullable
    private Long maxId;
    @Nullable
    private
    TwitterException twitterException;

    @Nullable
    public List<Tweet> getTweets() {
        return tweets;
    }

    public void setTweets(@Nullable List<Tweet> tweets) {
        this.tweets = tweets;
    }

    @NonNull
    public FetchStatus getFetchStatus() {
        return fetchStatus;
    }

    public void setFetchStatus(@NonNull FetchStatus fetchStatus) {
        this.fetchStatus = fetchStatus;
    }

    @Nullable
    public Long getSinceId() {
        return sinceId;
    }

    public void setSinceId(@Nullable Long sinceId) {
        this.sinceId = sinceId;
    }

    @Nullable
    public Long getMaxId() {
        return maxId;
    }

    public void setMaxId(@Nullable Long maxId) {
        this.maxId = maxId;
    }

    @Nullable
    public TwitterException getTwitterException() {
        return twitterException;
    }

    public void setTwitterException(@Nullable TwitterException twitterException) {
        this.twitterException = twitterException;
    }


    @Override
    public TimelineState clone() {
        TimelineState object = null;
        try {
            object = (TimelineState) super.clone();
            object.setFetchStatus(fetchStatus);
            object.setTweets(tweets);
            object.setSinceId(sinceId);
            object.setMaxId(maxId);
            object.setTwitterException(twitterException);
        } catch (CloneNotSupportedException e) {

        }
        return object;
    }
}
