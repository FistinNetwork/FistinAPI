package fr.fistin.api.impl.common.packet;

import com.google.gson.JsonObject;
import fr.fistin.api.IFistinAPIProvider;
import fr.fistin.api.packet.FistinPacket;
import fr.fistin.api.packet.FistinPacketReceiver;
import fr.fistin.api.packet.PacketException;
import fr.fistin.api.packet.PacketManager;
import org.jetbrains.annotations.NotNull;
import redis.clients.jedis.JedisPubSub;

import java.nio.charset.StandardCharsets;
import java.util.*;

public class PacketManagerImpl implements PacketManager
{
    private final PacketProcessor packetProcessor = new PacketProcessor();

    private boolean started;

    public PacketManagerImpl()
    {
        this.started = true;
    }

    @Override
    public <P extends FistinPacket> void sendPacket(@NotNull String channel, @NotNull P packet) {
        if (this.assertStarted()) {
            this.packetProcessor.processPacket(CHANNELS_PREFIX + channel, packet);
        }
    }

    @Override
    public void registerReceiver(@NotNull String channel, @NotNull FistinPacketReceiver receiver) {
        if (this.assertStarted()) {
            this.packetProcessor.processReceiver(CHANNELS_PREFIX + channel, receiver);
        }
    }

    @Override
    public <P extends FistinPacket> void registerSerializer(@NotNull Class<P> packetClass, FistinPacket.@NotNull Serializer<P> serializer) {
        if (this.assertStarted()) {
            this.packetProcessor.processSerializer(packetClass, serializer);
        }
    }

    @Override
    public void clear()
    {
        this.started = false;
        this.packetProcessor.clean();
    }

    private boolean assertStarted()
    {
        if (this.started) {
            return true;
        }
        throw new PacketException("PacketManager isn't started!");
    }

    private static final class PacketProcessor extends JedisPubSub {

        private final Map<String, List<FistinPacketReceiver>> receivers = new HashMap<>();
        private final Map<Class<? extends FistinPacket>, SerializerContext<?>> serializers = new IdentityHashMap<>();

        private final Thread subscriberThread;

        public PacketProcessor() {
            this.subscriberThread = new Thread(() -> IFistinAPIProvider.fistinAPI().redis().process(jedis -> jedis.psubscribe(this, CHANNELS_PREFIX + "*")));
            this.subscriberThread.start();
        }

        @Override
        public void onPMessage(String pattern, String channel, String message) {
            try {
                final JsonObject object = IFistinAPIProvider.GSON.fromJson(new String(Base64.getDecoder().decode(message), StandardCharsets.UTF_8), JsonObject.class);
                final Class<?> packetClass = Class.forName(object.get("id").getAsString());
                final String data = object.get("data").getAsString();
                final SerializerContext<?> serializerContext = this.serializers.get(packetClass);
                final FistinPacket packet = serializerContext == null ? (FistinPacket) IFistinAPIProvider.GSON.fromJson(data, packetClass) : serializerContext.deserialize(packetClass, data);
                final List<FistinPacketReceiver> receivers = this.receivers.get(channel);

                if (receivers != null) {
                    for (FistinPacketReceiver receiver : receivers) {
                        receiver.receive(packet);
                    }
                }
            } catch (Exception ignored) {}
        }

        <P extends FistinPacket> void processPacket(@NotNull String channel, @NotNull P packet) {
            try {
                final JsonObject object = new JsonObject();
                final SerializerContext<?> serializerContext = this.serializers.get(packet.getClass());

                object.addProperty("id", packet.getClass().getName());
                object.addProperty("data", serializerContext == null ? IFistinAPIProvider.GSON.toJson(packet) : serializerContext.serialize(packet));

                IFistinAPIProvider.fistinAPI()
                        .redis()
                        .process(jedis -> jedis.publish(channel, Base64.getEncoder().encodeToString(object.toString().getBytes(StandardCharsets.UTF_8))));
            } catch (Exception e) {
                throw new PacketException("Invalid packet: %s (%s).%n", packet.toString(), packet.getClass().getName());
            }
        }

        void processReceiver(@NotNull String channel, @NotNull FistinPacketReceiver receiver) {
            final List<FistinPacketReceiver> receivers = this.receivers.getOrDefault(channel, new ArrayList<>());

            receivers.add(receiver);

            this.receivers.put(channel, receivers);
        }

        <P extends FistinPacket> void processSerializer(@NotNull Class<P> packetClass, FistinPacket.@NotNull Serializer<P> serializer) {
            this.serializers.put(packetClass, new SerializerContext<>(packetClass, serializer));
        }

        void clean() {
            this.receivers.clear();
            this.serializers.clear();

            this.subscriberThread.interrupt();

            if (this.isSubscribed()) {
                this.punsubscribe();
            }
        }

        private static class SerializerContext<P extends FistinPacket> {

            private final Class<P> packetClass;
            private final FistinPacket.Serializer<P> serializer;

            public SerializerContext(Class<P> packetClass, FistinPacket.Serializer<P> serializer) {
                this.packetClass = packetClass;
                this.serializer = serializer;
            }

            @SuppressWarnings("unchecked")
            String serialize(FistinPacket packet) throws Exception {
                return this.serializer.serialize((P) packet);
            }

            @SuppressWarnings("unchecked")
            P deserialize(Class<?> packetClass, String input) throws Exception {
                return this.serializer.deserialize((Class<P>) packetClass, input);
            }

            public Class<P> getPacketClass() {
                return this.packetClass;
            }

            public FistinPacket.Serializer<P> getSerializer() {
                return this.serializer;
            }

        }

    }

}
