package io.github.ninjinkun.redux;

public interface DispatchFunction {
    Object call(Action action);
}
