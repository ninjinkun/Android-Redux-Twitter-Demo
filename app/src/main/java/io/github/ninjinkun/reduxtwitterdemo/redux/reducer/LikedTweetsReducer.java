package io.github.ninjinkun.reduxtwitterdemo.redux.reducer;

import com.twitter.sdk.android.core.models.Tweet;

import java.util.List;

import io.github.ninjinkun.reduxtwitterdemo.redux.action.LikedTweetsAction;
import io.github.ninjinkun.reduxtwitterdemo.redux.state.AppState;
import io.github.ninjinkun.reduxtwitterdemo.redux.state.FetchStatus;
import io.github.ninjinkun.reduxtwitterdemo.redux.state.LikedTweetsState;
import io.github.ninjinkun.redux.Action;
import io.github.ninjinkun.redux.Reducer;
import io.github.ninjinkun.redux.StateType;

public class LikedTweetsReducer implements Reducer {
    @Override
    public StateType handleAction(Action action, StateType stateType) {
        AppState prevState = stateType != null ? (AppState) stateType : new AppState();
        AppState nextState = prevState.clone();

        if (action instanceof LikedTweetsAction.RequestGetLikedTweetsAction) {
            nextState.setLikedTweets(requestGetLikedTweets(prevState.getLikedTweets()));
        }

        if (action instanceof LikedTweetsAction.ResponsesGetLikedTweetsAction) {
            nextState.setLikedTweets(responseGetLikedTweets(prevState.getLikedTweets(), (LikedTweetsAction.ResponsesGetLikedTweetsAction) action));
        }

        if (action instanceof LikedTweetsAction.ErrorGetLikedTweetsAction) {
            nextState.setLikedTweets(errorGetLikedTweets(prevState.getLikedTweets(), (LikedTweetsAction.ErrorGetLikedTweetsAction) action));
        }

        if (action instanceof LikedTweetsAction.ResponsePostLikeAction) {
            nextState.getLikedTweets().setTweets(TweetsReducerHelper.insertLikedTweet(prevState.getLikedTweets().getTweets(), ((LikedTweetsAction.ResponsePostLikeAction) action).tweet));
        }

        if (action instanceof LikedTweetsAction.ResponseDeleteLikeAction) {
            nextState.getLikedTweets().setTweets(TweetsReducerHelper.removeLikedTweet(prevState.getLikedTweets().getTweets(), ((LikedTweetsAction.ResponseDeleteLikeAction) action).tweet));
        }

        return nextState;
    }

    private LikedTweetsState requestGetLikedTweets(LikedTweetsState prevState) {
        LikedTweetsState nextState = prevState.clone();
        nextState.setFetchStatus(FetchStatus.Fetching);
        return nextState;
    }

    private LikedTweetsState responseGetLikedTweets(LikedTweetsState prevState, LikedTweetsAction.ResponsesGetLikedTweetsAction action) {
        LikedTweetsState nextState = prevState.clone();
        nextState.setFetchStatus(FetchStatus.Success);

        List<Tweet> nextTweets = TweetsReducerHelper.mergeTweets(prevState.getTweets(), action.tweets);
        nextState.setTweets(nextTweets);

        nextState.setSinceId(nextState.getTweets().get(0).id);
        nextState.setMaxId(nextState.getTweets().get(nextState.getTweets().size() - 1).id);
        nextState.setTwitterException(null);

        return nextState;
    }

    private LikedTweetsState errorGetLikedTweets(LikedTweetsState prevState, LikedTweetsAction.ErrorGetLikedTweetsAction action) {
        LikedTweetsState nextState = prevState.clone();
        nextState.setFetchStatus(FetchStatus.Error);
        nextState.setTwitterException(action.twitterException);
        return nextState;
    }
}
