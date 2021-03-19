package fr.fistin.api.eventbus;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class DefaultEventBus implements IFistinEventBus<Supplier<? extends IFistinEvent>>
{
    private static final List<EventExecution> EVENT_EXECUTIONS = new ArrayList<>();

    private final Set<Class<? extends IFistinEvent>> registeredEvents = new HashSet<>();
    private final Set<FistinEventListener> listeners = new HashSet<>();

    @Override
    public @NotNull Set<Class<? extends IFistinEvent>> registeredEvents()
    {
        return this.registeredEvents;
    }

    @Override
    public @NotNull Set<FistinEventListener> listeners()
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
        EVENT_EXECUTIONS.add(new EventExecution(eventSup.get().getName(), System.currentTimeMillis()));
        final IFistinEvent event = eventSup.get();
        if(this.registeredEvents.contains(event.getClass()))
        {
            this.listeners.forEach(listener -> {
                final Class<? extends FistinEventListener> clazz = listener.getClass();
                Arrays.stream(clazz.getDeclaredMethods())
                        .filter(method -> method.isAnnotationPresent(FistinEventHandler.class))
                        .filter(method -> method.getParameterCount() == 1)
                        .filter(method -> method.getParameterTypes()[0] == event.getClass())
                        .collect(Collectors.toList())
                        .forEach(method -> {
                            try
                            {
                                method.setAccessible(true);
                                method.invoke(listener, event);
                            } catch (IllegalAccessException | InvocationTargetException e)
                            {
                                throw new RuntimeException(e);
                            }
                        });
            });
        }
    }

    @Override
    public void clear()
    {
        this.registeredEvents.clear();
        this.listeners.clear();
    }

    @Override
    public String implName()
    {
        return "default";
    }

    public static List<EventExecution> getEventExecutions()
    {
        return EVENT_EXECUTIONS;
    }

    public static class EventExecution
    {
        private final String name;
        private final long timestamp;

        public EventExecution(String name, long timestamp)
        {
            this.name = name;
            this.timestamp = timestamp;
        }

        public String getName()
        {
            return this.name;
        }

        public long getTimestamp()
        {
            return this.timestamp;
        }
    }
}
