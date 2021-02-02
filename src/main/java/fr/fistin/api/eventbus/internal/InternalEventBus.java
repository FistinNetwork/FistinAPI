package fr.fistin.api.eventbus.internal;

import fr.fistin.api.eventbus.FistinEventListener;
import fr.fistin.api.eventbus.IFistinEvent;
import fr.fistin.api.eventbus.IFistinEventBus;

import java.util.HashSet;
import java.util.Set;

/**
 * Normally, developers don't need to check this class.
 * Internal class.
 */
public final class InternalEventBus implements IFistinEventBus
{
    private final Set<Class<? extends IFistinEvent>> registeredEvents = new HashSet<>();
    private final Set<FistinEventListener> listeners = new HashSet<>();

    @Override
    public Set<Class<? extends IFistinEvent>> getRegisteredEvents()
    {
        return this.registeredEvents;
    }

    @Override
    public Set<FistinEventListener> getListeners()
    {
        return this.listeners;
    }

    @Override
    public void registerEvent(Class<? extends IFistinEvent> eventClass)
    {
        this.registeredEvents.add(eventClass);
    }

    @Override
    public void addListener(FistinEventListener listener)
    {
        this.listeners.add(listener);
    }

    @Override
    public void clear()
    {
        this.registeredEvents.clear();
        this.listeners.clear();
    }
}
