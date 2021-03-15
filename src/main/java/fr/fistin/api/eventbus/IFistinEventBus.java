package fr.fistin.api.eventbus;

import java.util.Set;
import java.util.function.Supplier;

public interface IFistinEventBus<E extends Supplier<? extends IFistinEvent>>
{
    Set<Class<? extends IFistinEvent>> getRegisteredEvents();
    Set<FistinEventListener> getListeners();
    void registerEvent(Class<? extends IFistinEvent> eventClass);
    void addListener(FistinEventListener listener);
    void handleEvent(E eventSup);
    void clear();
}
