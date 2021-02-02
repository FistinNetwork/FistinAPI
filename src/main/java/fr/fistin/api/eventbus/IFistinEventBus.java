package fr.fistin.api.eventbus;

import java.util.Set;

public interface IFistinEventBus
{
    Set<Class<? extends IFistinEvent>> getRegisteredEvents();
    Set<FistinEventListener> getListeners();
    void registerEvent(Class<? extends IFistinEvent> eventClass);
    void addListener(FistinEventListener listener);
    void clear();
}
