package io.github.ninjinkun.redux;

public interface Reducer<State extends StateType> {
    /**
     * Reducer method. It must be pure function.
     * @param action Dispatched action. NonNull.
     * @param state Previous sate. Nullable.
     * @return New state. NonNull.
     */
    State handleAction(final Action action, final State state);
}
