package fr.fistin.api.utils;

import java.util.concurrent.atomic.AtomicInteger;

@FunctionalInterface
public interface FistinRunnableTimer
{
    default void onTimerPass(int timer) {}
    default void onTimerEnd() {}

    AtomicInteger timer();
}
