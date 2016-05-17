package io.github.ninjinkun.reduxtwitterdemo.redux.reducer;

import io.github.ninjinkun.reduxtwitterdemo.redux.action.AuthSessionAction;
import io.github.ninjinkun.reduxtwitterdemo.redux.state.AppState;
import io.github.ninjinkun.reduxtwitterdemo.redux.state.AuthSessionState;
import io.github.ninjinkun.redux.Action;
import io.github.ninjinkun.redux.Reducer;
import io.github.ninjinkun.redux.StateType;

public class AuthSessionReducer implements Reducer {
    @Override
    public StateType handleAction(Action action, StateType stateType) {
        AppState prevState = stateType != null ? (AppState) stateType : new AppState();
        AppState nextState = prevState.clone();

        if (action instanceof AuthSessionAction.LoadAuthSessionAction) {
            nextState.setSession(loadAuthSession(prevState.getSession(), (AuthSessionAction.LoadAuthSessionAction) action));
        }

        if (action instanceof AuthSessionAction.ErrorAuthSessionAction) {
            nextState.setSession(errorAuthSession(prevState.getSession(), (AuthSessionAction.ErrorAuthSessionAction) action));
        }

        return nextState;
    }

    private AuthSessionState loadAuthSession(AuthSessionState prevState, AuthSessionAction.LoadAuthSessionAction action) {
        AuthSessionState nextState = prevState.clone();
        nextState.setSession(action.session);
        return nextState;
    }

    private AuthSessionState errorAuthSession(AuthSessionState prevState, AuthSessionAction.ErrorAuthSessionAction action) {
        AuthSessionState nextState = prevState.clone();
        nextState.setTwitterException(action.exception);
        return nextState;
    }
}
