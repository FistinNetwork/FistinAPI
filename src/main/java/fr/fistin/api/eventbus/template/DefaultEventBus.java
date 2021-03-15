package fr.fistin.api.eventbus.template;

import fr.fistin.api.eventbus.FistinEventHandler;
import fr.fistin.api.eventbus.FistinEventListener;
import fr.fistin.api.eventbus.IFistinEvent;
import fr.fistin.api.eventbus.IFistinEventBus;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Supplier;

public class DefaultEventBus implements IFistinEventBus<Supplier<? extends IFistinEvent>>
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
    public void handleEvent(Supplier<? extends IFistinEvent> eventSup)
    {
        final IFistinEvent event = eventSup.get();
        if(this.registeredEvents.contains(event.getClass()))
        {
            this.listeners.forEach(listener -> {
                final Class<? extends FistinEventListener> clazz = listener.getClass();
                for (Method method : clazz.getDeclaredMethods())
                {
                    if (method.isAnnotationPresent(FistinEventHandler.class))
                    {
                        if (method.getParameterCount() == 1 && method.getParameterTypes()[0] == event.getClass())
                        {
                            try
                            {
                                method.setAccessible(true);
                                method.invoke(listener, event);
                            } catch (IllegalAccessException | InvocationTargetException e)
                            {
                                throw new RuntimeException(e);
                            }
                        }
                    }
                }
            });
        }
    }

    @Override
    public void clear()
    {
        this.registeredEvents.clear();
        this.listeners.clear();
    }
}
