package io.github.ninjinkun.redux;

public interface Selector<State extends StateType, SelectedState> {
    SelectedState call(final State state);
}
