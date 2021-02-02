package fr.fistin.api.eventbus.internal;

import fr.fistin.api.eventbus.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

/**
 * Normally, developers don't need to check this class.
 * Internal class.
 */
public final class InternalFistinEventExecutor implements IFistinEventExecutor
{
    private final Set<IFistinEventBus> eventBuses = new HashSet<>();

    @Override
    public IFistinEventBusRegisterer getRegisterer()
    {
        return this::addEventBus;
    }

    private void addEventBus(IFistinEventBus bus)
    {
        this.eventBuses.add(bus);
    }

    @Override
    public void handleEvent(IFistinEvent event)
    {
        for (IFistinEventBus bus : this.eventBuses)
        {
            if(bus.getRegisteredEvents().contains(event.getClass()))
            {
                bus.getListeners().forEach(listener -> {
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
                                    System.err.println("An error has occurred on a reflection operation: " + e.getMessage());
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                });
            }
        }
    }

    @Override
    public void clear()
    {
        this.eventBuses.forEach(IFistinEventBus::clear);
        this.eventBuses.clear();
    }
}
