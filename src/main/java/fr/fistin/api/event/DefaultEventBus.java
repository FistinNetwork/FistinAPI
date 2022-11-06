package fr.fistin.api.eventbus;

import fr.fistin.api.utils.FistinAPIException;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.function.Supplier;

public class DefaultEventBus implements FistinEventBus<Supplier<? extends FistinEvent>>
{
    private static final Map<DefaultEventBus, Collection<EventExecution>> EVENT_EXECUTIONS = new HashMap<>();

    private final Set<Class<? extends FistinEvent>> registeredEvents = new HashSet<>();
    private final Set<FistinEventListener> listeners = new HashSet<>();
    private boolean handleParent;

    public DefaultEventBus(boolean handleParent)
    {
        this.handleParent = handleParent;
    }

    public DefaultEventBus() {}

    @Override
    public @NotNull Set<Class<? extends FistinEvent>> registeredEvents()
    {
        return Collections.unmodifiableSet(this.registeredEvents);
    }

    @Override
    public @NotNull Set<FistinEventListener> listeners()
    {
        return Collections.unmodifiableSet(this.listeners);
    }

    @Override
    public void registerEvent(Class<? extends FistinEvent> eventClass)
    {
        this.registeredEvents.add(eventClass);
    }

    @Override
    public void addListener(FistinEventListener listener)
    {
        this.listeners.add(listener);
    }

    @Override
    public void handleEvent(Supplier<? extends FistinEvent> eventSup)
    {
        final Collection<EventExecution> executions = EVENT_EXECUTIONS.getOrDefault(this, new ArrayList<>());

        executions.add(new EventExecution(eventSup.get().getName(), System.currentTimeMillis()));

        EVENT_EXECUTIONS.put(this, executions);

        final FistinEvent event = eventSup.get();

        if (!this.registeredEvents.contains(event.getClass())) {
            if (!this.handleParent) {
                return;
            } else if (this.registeredEvents.stream().noneMatch(aClass -> aClass.isInstance(event))) {
                return;
            }
        }

        this.listeners.forEach(listener -> {
            final Class<? extends FistinEventListener> clazz = listener.getClass();

            Arrays.stream(clazz.getDeclaredMethods()).filter(method -> method.isAnnotationPresent(FistinEventHandler.class)).filter(method -> method.getParameterCount() == 1).filter(method -> method.getParameterTypes()[0] == event.getClass() || method.getParameterTypes()[0].isInstance(event)).forEach(method -> {
                try
                {
                    method.setAccessible(true);
                    method.invoke(listener, event);
                } catch (IllegalAccessException | InvocationTargetException e)
                {
                    throw new FistinAPIException(e);
                }
            });
        });
    }

    @Override
    public boolean handleParent()
    {
        return this.handleParent;
    }

    @Override
    public void handleParent(boolean handleParent)
    {
        this.handleParent = handleParent;
    }

    @Override
    public void clean()
    {
        this.registeredEvents.clear();
        this.listeners.clear();

        EVENT_EXECUTIONS.remove(this);
    }

    @Override
    public String implName()
    {
        return "default";
    }

    public static Map<DefaultEventBus, Collection<EventExecution>> getEventExecutions()
    {
        return Collections.unmodifiableMap(EVENT_EXECUTIONS);
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
