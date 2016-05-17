package io.github.ninjinkun.redux;

public interface StoreType<State extends StateType, Subscriber extends StoreSubscriber> {
    State getState();

    DispatchFunction dispatchFunction();

    void subscribe(Subscriber subscriber);

    void unsubscribe(Subscriber subscriber);

    Object dispatch(Action action);

    Object dispatch(ActionCreator actionCreator);

    void dispatch(AsyncActionCreator asyncActionCreator);

    void dispatch(AsyncActionCreator asyncActionCreator, DispatchCallback callback);

    interface ActionCreator<State extends StateType> {
        Action call(final State state, final Store store);
    }

    interface DispatchCallback<State extends StateType> {
        void call(final State state);
    }

    interface AsyncActionCreator<State extends StateType> {
        void call(final State state, final Store store, final ActionCreatorProducer actionCreatorProducer);

        interface ActionCreatorProducer {
            void call(final ActionCreator actionCreator);
        }
    }
}
