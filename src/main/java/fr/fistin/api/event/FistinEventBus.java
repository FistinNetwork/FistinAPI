package fr.fistin.api.eventbus;

import org.jetbrains.annotations.NotNull;

import java.util.Set;
import java.util.function.Supplier;

public interface FistinEventBus<E extends Supplier<? extends FistinEvent>>
{
    @NotNull Set<Class<? extends FistinEvent>> registeredEvents();
    @NotNull Set<FistinEventListener> listeners();
    void registerEvent(Class<? extends FistinEvent> eventClass);
    void addListener(FistinEventListener listener);
    void handleEvent(E eventSup);
    boolean handleParent();
    void handleParent(boolean handleParent);
    void clean();
    String implName();
}
