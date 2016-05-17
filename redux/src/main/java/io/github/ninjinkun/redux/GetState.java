package io.github.ninjinkun.redux;

public interface GetState<State extends StateType> {
    State call();
}
