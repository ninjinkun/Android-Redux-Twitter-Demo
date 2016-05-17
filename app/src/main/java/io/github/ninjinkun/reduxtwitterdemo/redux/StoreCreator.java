package io.github.ninjinkun.reduxtwitterdemo.redux;


import android.util.Log;

import java.util.Arrays;

import io.github.ninjinkun.reduxtwitterdemo.redux.reducer.AuthSessionReducer;
import io.github.ninjinkun.reduxtwitterdemo.redux.reducer.LikedTweetsReducer;
import io.github.ninjinkun.reduxtwitterdemo.redux.reducer.TimelineReducer;
import io.github.ninjinkun.reduxtwitterdemo.redux.state.AppState;
import io.github.ninjinkun.redux.Action;
import io.github.ninjinkun.redux.CombinedReducer;
import io.github.ninjinkun.redux.DispatchFunction;
import io.github.ninjinkun.redux.GetState;
import io.github.ninjinkun.redux.Middleware;
import io.github.ninjinkun.redux.Store;

public class StoreCreator {
    public static class Logger implements Middleware {
        private static final String TAG = "Redux";

        @Override
        public DispatchFunction call(final DispatchFunction dispatchFunction, GetState getState) {
            return new DispatchFunction() {
                @Override
                public Object call(Action action) {
                    Log.d(TAG, action.getClass().getName());
                    return dispatchFunction.call(action);
                }
            };
        }
    }

    public static Store<AppState> store = new Store<>(
            new CombinedReducer(Arrays.asList(new AuthSessionReducer(), new TimelineReducer(), new LikedTweetsReducer())),
            new AppState(),
            Arrays.asList((Middleware) new Logger())
    );
}
