package io.github.ninjinkun.redux;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Store<State extends StateType> implements StoreType {
    private DispatchFunction dispatchFunction;
    private Reducer<State> reducer;
    private List<Subscription> subscriptions = new ArrayList<>();
    private boolean isDispatching = false;
    private State state;

    public Store(Reducer reducer, State state, final List<Middleware> middlewares) {
        this.reducer = reducer;
        setState(state);
        this.dispatchFunction = new DispatchFunction() {
            @Override
            public Object call(Action action) {
                DispatchFunction function = new DispatchFunction() {
                    @Override
                    public Object call(Action action) {
                        return defaultDispatch(action);
                    }
                };

                if (middlewares != null) {
                    List<Middleware> reversedMiddlewares = new ArrayList(middlewares);
                    Collections.reverse(reversedMiddlewares);
                    for (Middleware middleware : reversedMiddlewares) {
                        function = middleware.call(function, new GetState() {
                            @Override
                            public State call() {
                                return getState();
                            }
                        });
                    }
                }

                return function.call(action);
            }
        };
    }

    private void setState(State state) {
        this.state = state;

        List<Subscription> newSubscriptions = new ArrayList<>(subscriptions);
        for (Subscription subscription : subscriptions) {
            if (subscription.subscriber == null) {
                newSubscriptions.remove(subscription);
            }
        }
        subscriptions = newSubscriptions;

        for (Subscription subscription : subscriptions) {
            subscription.subscriber.newState(subscription.selector != null ? subscription.selector.call(state) : state);
        }
    }

    @Override
    public State getState() {
        return state;
    }

    @Override
    public DispatchFunction dispatchFunction() {
        return dispatchFunction;
    }


    private boolean isNewSubscriber(StoreSubscriber subscriber) {
        if (subscriptions.contains(subscriber)) {
            System.out.println("Store subscriber is already added, ignoring.");
            return false;
        }
        return true;
    }

    @Override
    public void subscribe(StoreSubscriber subscriber) {
        if (!isNewSubscriber(subscriber)) {
            return;
        }

        subscriptions.add(new Subscription<State, State>(subscriber, null));

        subscriber.newState(state);
    }

    public <SelectedState> void subscribe(StoreSubscriber<SelectedState> subscriber, Selector<State, SelectedState> selector) {
        if (!isNewSubscriber(subscriber)) {
            return;
        }

        subscriptions.add(new Subscription<State, SelectedState>(subscriber, selector));

        subscriber.newState(selector.call(state));
    }

    @Override
    public void unsubscribe(StoreSubscriber subscriber) {
        subscriptions.remove(subscriber);
    }

    private Object defaultDispatch(Action action) {
        if (isDispatching) {
            throw new RuntimeException("Illegal dispatch from reducer. Reducer may not dispatch actions.");
        }

        isDispatching = true;
        State newState = reducer.handleAction(action, state);
        isDispatching = false;

        setState(newState);

        return action;
    }

    @Override
    public Object dispatch(Action action) {
        return dispatchFunction.call(action);
    }

    @Override
    public Object dispatch(ActionCreator actionCreatorProvider) {
        Action action = actionCreatorProvider.call(state, this);

        if (action != null) {
            dispatch(action);
        }

        return action;
    }

    @Override
    public void dispatch(AsyncActionCreator asyncActionCreatorProvider) {
        dispatch(asyncActionCreatorProvider, null);
    }

    @Override
    public void dispatch(AsyncActionCreator asyncActionCreatorProvider, final DispatchCallback callback) {
        asyncActionCreatorProvider.call(state, this, new AsyncActionCreator.ActionCreatorProducer() {
            @Override
            public void call(ActionCreator actionCreator) {
                Action action = actionCreator.call(Store.this.state, Store.this);

                if (action != null) {
                    dispatch(action);
                }

                if (callback != null) {
                    callback.call(Store.this.state);
                }
            }
        });
    }
}
