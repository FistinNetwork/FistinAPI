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
    public void clear()
    {
        this.started = false;
        this.packetProcessor.clear();
    }

    private boolean assertStarted()
    {
        if (this.started) {
            return true;
        }
        throw new PacketException("PacketManager isn't started!");
    }

    static final class PacketProcessor extends JedisPubSub
    {

        private final Map<String, List<FistinPacketReceiver>> receivers = new HashMap<>();

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
                final FistinPacket packet = (FistinPacket) IFistinAPIProvider.GSON.fromJson(object.getAsJsonObject("data"), packetClass);
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

                object.addProperty("id", packet.getClass().getName());
                object.add("data", IFistinAPIProvider.GSON.toJsonTree(packet));

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

        void clear() {
            this.subscriberThread.interrupt();

            if (this.isSubscribed()) {
                this.punsubscribe();
            }
        }

    }

}
