package fr.fistin.api.impl.common.network;

import com.google.gson.JsonObject;
import fr.fistin.api.IFistinAPIProvider;
import fr.fistin.api.eventbus.FistinEvent;
import fr.fistin.api.eventbus.FistinEventBus;
import fr.fistin.api.eventbus.FistinEventHandler;
import fr.fistin.api.eventbus.FistinEventListener;
import fr.fistin.api.packet.FistinPacket;
import fr.fistin.api.utils.FistinAPIException;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Supplier;

/**
 * Created by AstFaster
 * on 06/11/2022 at 13:39
 */
public class NetworkEventBus implements FistinEventBus<Supplier<? extends FistinEvent>> {

    private static final String CHANNEL = "events";

    private final Set<Class<? extends FistinEvent>> registeredEvents = new HashSet<>();
    private final Set<FistinEventListener> listeners = new HashSet<>();

    private boolean handleParent = false;

    public NetworkEventBus() {
        IFistinAPIProvider.fistinAPI().packetManager().registerReceiver(CHANNEL, packet -> {
            if (packet instanceof Packet) {
                final Packet eventPacket = (Packet) packet;
                final FistinEvent event = eventPacket.getEvent();

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
                        try {
                            method.setAccessible(true);
                            method.invoke(listener, event);
                        } catch (IllegalAccessException | InvocationTargetException e) {
                            throw new FistinAPIException(e);
                        }
                    });
                });
            }
        });

        IFistinAPIProvider.fistinAPI().packetManager().registerSerializer(Packet.class, new FistinPacket.Serializer<Packet>() {
            @Override
            public String serialize(Packet packet) {
                final FistinEvent event = packet.getEvent();
                final JsonObject jsonObject = new JsonObject();

                jsonObject.addProperty("class", event.getClass().getName());
                jsonObject.add("event", IFistinAPIProvider.GSON.toJsonTree(event));

                return jsonObject.toString();
            }

            @Override
            public Packet deserialize(Class<Packet> packetClass, String input) throws Exception {
                final JsonObject jsonObject = IFistinAPIProvider.GSON.fromJson(input, JsonObject.class);
                final Class<?> eventClass = Class.forName(jsonObject.get("class").getAsString());
                final FistinEvent event = (FistinEvent) IFistinAPIProvider.GSON.fromJson(jsonObject.getAsJsonObject("event"), eventClass);

                return new Packet(event);
            }
        });
    }

    @Override
    public @NotNull Set<Class<? extends FistinEvent>> registeredEvents() {
        return Collections.unmodifiableSet(this.registeredEvents);
    }

    @Override
    public @NotNull Set<FistinEventListener> listeners() {
        return Collections.unmodifiableSet(this.listeners);
    }

    @Override
    public void registerEvent(Class<? extends FistinEvent> eventClass) {
        this.registeredEvents.add(eventClass);
    }

    @Override
    public void addListener(FistinEventListener listener) {
        this.listeners.add(listener);
    }

    @Override
    public void handleEvent(Supplier<? extends FistinEvent> eventSup) {
        final FistinEvent event = eventSup.get();

        if (!this.registeredEvents.contains(event.getClass())) {
            return;
        }

        IFistinAPIProvider.fistinAPI().packetManager().sendPacket(CHANNEL, new Packet(event));
    }

    @Override
    public boolean handleParent() {
        return this.handleParent;
    }

    @Override
    public void handleParent(boolean handleParent) {
        this.handleParent = handleParent;
    }

    @Override
    public void clean() {
        this.registeredEvents.clear();
        this.listeners.clear();
    }

    @Override
    public String implName() {
        return "network";
    }

    private static class Packet implements FistinPacket {

        private final FistinEvent event;

        public Packet(FistinEvent event) {
            this.event = event;
        }

        public FistinEvent getEvent() {
            return this.event;
        }

    }

}
