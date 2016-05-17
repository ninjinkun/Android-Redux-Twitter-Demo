package io.github.ninjinkun.redux;

import java.util.List;

public class CombinedReducer implements Reducer {
    private final List<Reducer> reducers;

    public CombinedReducer(List<Reducer> reducers) {
        if (reducers.size() <= 0) {
            throw new RuntimeException("CombinedReducer is empty. It needs one or more reducers.");
        }
        this.reducers = reducers;
    }

    @Override
    public StateType handleAction(Action action, StateType stateType) {
        StateType nextState = stateType;
        for (Reducer reducer : reducers) {
            nextState = reducer.handleAction(action, nextState);
        }
        return nextState;
    }
}
