package io.github.ninjinkun.redux;

public interface Middleware {
    DispatchFunction call(final DispatchFunction dispatchFunction, final GetState getState);
}
