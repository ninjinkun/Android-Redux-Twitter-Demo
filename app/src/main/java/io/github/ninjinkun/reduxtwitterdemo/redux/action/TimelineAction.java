package io.github.ninjinkun.reduxtwitterdemo.redux.action;

import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterApiClient;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.models.Tweet;

import java.util.List;

import io.github.ninjinkun.reduxtwitterdemo.redux.state.AppState;
import io.github.ninjinkun.redux.Action;
import io.github.ninjinkun.redux.StateType;
import io.github.ninjinkun.redux.Store;
import io.github.ninjinkun.redux.StoreType;

public class TimelineAction {
    public static class RequestGetTimelineAction implements Action {
    }

    public static class ResponsesGetTimelineAction implements Action {
        public final List<Tweet> tweets;

        public ResponsesGetTimelineAction(List<Tweet> tweets) {
            this.tweets = tweets;
        }
    }

    public static class ErrorGetTimelineAction implements Action {
        public final TwitterException twitterException;

        public ErrorGetTimelineAction(TwitterException twitterException) {
            this.twitterException = twitterException;
        }
    }

    public static StoreType.AsyncActionCreator<AppState> fetchTimeline(final Long sinceId, final Long maxId) {
        return new StoreType.AsyncActionCreator<AppState>() {
            @Override
            public void call(final AppState appState, final Store store, final ActionCreatorProducer actionCreatorProducer) {
                store.dispatch(new RequestGetTimelineAction());

                TwitterApiClient twitterApiClient = TwitterCore.getInstance().getApiClient();
                twitterApiClient.getStatusesService().homeTimeline(null, sinceId, maxId, null, null, null, null, new Callback<List<Tweet>>() {
                    @Override
                    public void success(final Result<List<Tweet>> result) {
                        actionCreatorProducer.call(new StoreType.ActionCreator() {
                            @Override
                            public Action call(StateType stateType, Store store) {
                                return new ResponsesGetTimelineAction(result.data);
                            }
                        });
                    }

                    @Override
                    public void failure(final TwitterException exception) {
                        actionCreatorProducer.call(new StoreType.ActionCreator() {
                            @Override
                            public Action call(StateType stateType, Store store) {
                                return new ErrorGetTimelineAction(exception);
                            }
                        });
                    }
                });
            }
        };
    }
}
