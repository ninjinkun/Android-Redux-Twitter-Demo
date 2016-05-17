package io.github.ninjinkun.reduxtwitterdemo.redux.reducer;

import com.twitter.sdk.android.core.models.Tweet;

import java.util.List;

import io.github.ninjinkun.reduxtwitterdemo.redux.action.LikedTweetsAction;
import io.github.ninjinkun.reduxtwitterdemo.redux.action.TimelineAction;
import io.github.ninjinkun.reduxtwitterdemo.redux.state.AppState;
import io.github.ninjinkun.reduxtwitterdemo.redux.state.FetchStatus;
import io.github.ninjinkun.reduxtwitterdemo.redux.state.TimelineState;
import io.github.ninjinkun.redux.Action;
import io.github.ninjinkun.redux.Reducer;
import io.github.ninjinkun.redux.StateType;

public class TimelineReducer implements Reducer {
    @Override
    public StateType handleAction(Action action, StateType stateType) {
        AppState prevState = stateType != null ? (AppState) stateType : new AppState();
        AppState nextState = prevState.clone();

        if (action instanceof TimelineAction.RequestGetTimelineAction) {
            nextState.setTimeline(requestGetTimeline(prevState.getTimeline()));
        }

        if (action instanceof TimelineAction.ResponsesGetTimelineAction) {
            nextState.setTimeline(responseGetTimeline(prevState.getTimeline(), (TimelineAction.ResponsesGetTimelineAction) action));
        }

        if (action instanceof TimelineAction.ErrorGetTimelineAction) {
            nextState.setTimeline(errorGetTimeline(prevState.getTimeline(), (TimelineAction.ErrorGetTimelineAction) action));
        }

        if (action instanceof LikedTweetsAction.ResponsePostLikeAction) {
            nextState.getTimeline().setTweets(TweetsReducerHelper.updateLikedTweet(prevState.getTimeline().getTweets(), ((LikedTweetsAction.ResponsePostLikeAction) action).tweet));
        }

        if (action instanceof LikedTweetsAction.ResponseDeleteLikeAction) {
            nextState.getTimeline().setTweets(TweetsReducerHelper.updateLikedTweet(prevState.getTimeline().getTweets(), ((LikedTweetsAction.ResponseDeleteLikeAction) action).tweet));
        }

        return nextState;
    }

    private TimelineState requestGetTimeline(TimelineState prevState) {
        TimelineState nextState = prevState.clone();
        nextState.setFetchStatus(FetchStatus.Fetching);
        return nextState;
    }

    private TimelineState responseGetTimeline(TimelineState prevState, TimelineAction.ResponsesGetTimelineAction action) {
        TimelineState nextState = prevState.clone();
        nextState.setFetchStatus(FetchStatus.Success);

        List<Tweet> nextTweets = TweetsReducerHelper.mergeTweets(prevState.getTweets(), action.tweets);
        nextState.setTweets(nextTweets);

        nextState.setSinceId(nextState.getTweets().get(0).id);
        nextState.setMaxId(nextState.getTweets().get(nextState.getTweets().size() - 1).id);
        nextState.setTwitterException(null);

        return nextState;
    }

    private TimelineState errorGetTimeline(TimelineState prevState, TimelineAction.ErrorGetTimelineAction action) {
        TimelineState nextState = prevState.clone();
        nextState.setFetchStatus(FetchStatus.Error);
        nextState.setTwitterException(action.twitterException);
        return nextState;
    }
}
