package io.github.ninjinkun.reduxtwitterdemo.redux.action;

import android.support.annotation.NonNull;

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

public class LikedTweetsAction {
    public static class RequestGetLikedTweetsAction implements Action {
    }

    public static class ResponsesGetLikedTweetsAction implements Action {
        public final List<Tweet> tweets;

        public ResponsesGetLikedTweetsAction(List<Tweet> tweets) {
            this.tweets = tweets;
        }
    }

    public static class ErrorGetLikedTweetsAction implements Action {
        public final TwitterException twitterException;

        public ErrorGetLikedTweetsAction(TwitterException twitterException) {
            this.twitterException = twitterException;
        }
    }

    public static class RequestPostLikeAction implements Action {
        public final Long tweetId;

        public RequestPostLikeAction(Long tweetId) {
            this.tweetId = tweetId;
        }
    }

    public static class ResponsePostLikeAction implements Action {
        public final Tweet tweet;

        public ResponsePostLikeAction(Tweet tweet) {
            this.tweet = tweet;
        }
    }

    public static class ErrorPostLikeAction implements Action {
        public final Long tweetId;
        public final TwitterException exception;

        public ErrorPostLikeAction(Long tweetId, TwitterException exception) {
            this.tweetId = tweetId;
            this.exception = exception;
        }
    }

    public static class RequestDeleteLikeAction implements Action {
        public final Long tweetId;

        public RequestDeleteLikeAction(Long tweetId) {
            this.tweetId = tweetId;
        }
    }

    public static class ResponseDeleteLikeAction implements Action {
        public final Tweet tweet;

        public ResponseDeleteLikeAction(Tweet tweet) {
            this.tweet = tweet;
        }
    }

    public static class ErrorDeleteLikeAction implements Action {
        public final Long tweetId;
        public final TwitterException exception;

        public ErrorDeleteLikeAction(Long tweetId, TwitterException exception) {
            this.tweetId = tweetId;
            this.exception = exception;
        }
    }

    public static StoreType.AsyncActionCreator<AppState> fetchLikedTweets(final Long sinceId, final Long maxId) {
        return new StoreType.AsyncActionCreator<AppState>() {
            @Override
            public void call(final AppState appState, final Store store, final ActionCreatorProducer actionCreatorProducer) {
                store.dispatch(new RequestGetLikedTweetsAction());

                String sinceIdString = sinceId != null ? String.valueOf(sinceId) : null;
                String maxIdString = maxId != null ? String.valueOf(maxId) : null;

                TwitterApiClient twitterApiClient = TwitterCore.getInstance().getApiClient();
                twitterApiClient.getFavoriteService().list(null, null, null, sinceIdString, maxIdString, null, new Callback<List<Tweet>>() {
                    @Override
                    public void success(final Result<List<Tweet>> result) {
                        actionCreatorProducer.call(new StoreType.ActionCreator() {
                            @Override
                            public Action call(StateType stateType, Store store) {
                                return new ResponsesGetLikedTweetsAction(result.data);
                            }
                        });
                    }

                    @Override
                    public void failure(final TwitterException exception) {
                        actionCreatorProducer.call(new StoreType.ActionCreator() {
                            @Override
                            public Action call(StateType stateType, Store store) {
                                return new ErrorGetLikedTweetsAction(exception);
                            }
                        });
                    }
                });
            }
        };
    }

    public static StoreType.AsyncActionCreator<AppState> postLike(@NonNull final Long tweetId) {
        return new StoreType.AsyncActionCreator<AppState>() {
            @Override
            public void call(final AppState appState, final Store store, final ActionCreatorProducer actionCreatorProducer) {
                store.dispatch(new RequestPostLikeAction(tweetId));

                TwitterApiClient twitterApiClient = TwitterCore.getInstance().getApiClient();

                twitterApiClient.getFavoriteService().create(tweetId, null, new Callback<Tweet>() {
                    @Override
                    public void success(final Result<Tweet> result) {
                        actionCreatorProducer.call(new StoreType.ActionCreator() {
                            @Override
                            public Action call(StateType stateType, Store store) {
                                return new ResponsePostLikeAction(result.data);
                            }
                        });
                    }

                    @Override
                    public void failure(final TwitterException exception) {
                        actionCreatorProducer.call(new StoreType.ActionCreator() {
                            @Override
                            public Action call(StateType stateType, Store store) {
                               return new ErrorPostLikeAction(tweetId, exception);
                            }
                        });
                    }
                });
            }
        };
    }

    public static StoreType.AsyncActionCreator<AppState> deleteLike(@NonNull final Long tweetId) {
        return new StoreType.AsyncActionCreator<AppState>() {
            @Override
            public void call(final AppState appState, final Store store, final ActionCreatorProducer actionCreatorProducer) {
                store.dispatch(new RequestDeleteLikeAction(tweetId));

                TwitterApiClient twitterApiClient = TwitterCore.getInstance().getApiClient();

                twitterApiClient.getFavoriteService().destroy(tweetId, null, new Callback<Tweet>() {
                    @Override
                    public void success(final Result<Tweet> result) {
                        actionCreatorProducer.call(new StoreType.ActionCreator() {
                            @Override
                            public Action call(StateType stateType, Store store) {
                                return new ResponseDeleteLikeAction(result.data);
                            }
                        });
                    }

                    @Override
                    public void failure(final TwitterException exception) {
                        actionCreatorProducer.call(new StoreType.ActionCreator() {
                            @Override
                            public Action call(StateType stateType, Store store) {
                                return new ErrorPostLikeAction(tweetId, exception);
                            }
                        });
                    }
                });
            }
        };
    }
}
