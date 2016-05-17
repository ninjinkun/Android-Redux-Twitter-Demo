package io.github.ninjinkun.redux;

public interface StoreSubscriber<StoreSubscriberStateType> {
    void newState(final StoreSubscriberStateType state);
}
