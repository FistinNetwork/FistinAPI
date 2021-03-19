package fr.fistin.api.eventbus;

import org.jetbrains.annotations.NotNull;

import java.util.Set;
import java.util.function.Supplier;

public interface IFistinEventBus<E extends Supplier<? extends IFistinEvent>>
{
    @NotNull Set<Class<? extends IFistinEvent>> registeredEvents();
    @NotNull Set<FistinEventListener> listeners();
    void registerEvent(Class<? extends IFistinEvent> eventClass);
    void addListener(FistinEventListener listener);
    void handleEvent(E eventSup);
    void clear();
    String implName();
}
