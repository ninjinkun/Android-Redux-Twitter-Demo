package io.github.ninjinkun.redux;

public class Subscription<State extends StateType, SelectedState> {
    public final StoreSubscriber<SelectedState> subscriber;
    public final Selector<State, SelectedState> selector;

    public Subscription(StoreSubscriber subscriber, Selector selector) {
        this.subscriber = subscriber;
        this.selector = selector;
    }
}
