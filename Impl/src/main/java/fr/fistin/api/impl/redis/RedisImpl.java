package fr.fistin.api.impl.redis;

import fr.fistin.api.configuration.ConfigurationProviders;
import fr.fistin.api.configuration.FistinAPIConfiguration;
import fr.fistin.api.impl.FistinAPIProvider;
import fr.fistin.api.redis.Redis;
import fr.fistin.hydra.api.protocol.data.RedisData;
import fr.fistin.hydra.api.redis.IHydraRedis;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.logging.Level;

/**
 * Created by AstFaster
 * on 03/11/2022 at 20:18
 */
public class RedisImpl implements Redis, IHydraRedis {

    private boolean connected;
    private Thread reconnectTask;

    private JedisPool jedisPool;

    private final RedisData config;

    public RedisImpl(RedisData config) {
        this.config = config;
    }

    public boolean connect() {
        final JedisPoolConfig poolConfig = new JedisPoolConfig();

        poolConfig.setJmxEnabled(false);
        poolConfig.setMaxTotal(-1);

        final String password = this.config.getPassword();

        if (password != null && !password.isEmpty()) {
            this.jedisPool = new JedisPool(poolConfig, this.config.getHostname(), this.config.getPort(), 2000, password);
        } else {
            this.jedisPool = new JedisPool(poolConfig, this.config.getHostname(), this.config.getPort(), 2000);
        }

        try {
            this.getResource().close();

            this.connected = true;

            FistinAPIProvider.log("Fistin API is now connected with Redis database.");

            this.startReconnectTask();
        } catch (Exception e) {
            FistinAPIProvider.log(Level.SEVERE, "Couldn't connect to Redis database!");
        }
        return this.connected;
    }

    private void startReconnectTask() {
        this.reconnectTask = new Thread(() -> {
            while (!Thread.interrupted()) {
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }

                this.reconnect();
            }
        });
        this.reconnectTask.start();
    }

    private void reconnect() {
        try {
            this.getResource().close();
        } catch (Exception e) {
            FistinAPIProvider.log(Level.SEVERE, "Encountered exception in Redis reconnection task. Error: " + e.getMessage());
            FistinAPIProvider.log(Level.SEVERE, "Error in Redis database connection ! Trying to reconnect...");

            this.connected = false;

            this.connect();
        }
    }

    public void disconnect() {
        FistinAPIProvider.log("Disconnecting Fistin API from Redis database...");

        if (this.reconnectTask != null && this.reconnectTask.isAlive()) {
            this.reconnectTask.interrupt();
        }

        this.connected = false;

        this.jedisPool.close();
    }

    @Override
    public JedisPool getPool() {
        return this.jedisPool;
    }

    @Override
    public Jedis getResource() {
        return this.jedisPool.getResource();
    }

    @Override
    public boolean isConnected() {
        return this.connected;
    }

    @Override
    public void process(Consumer<Jedis> action) {
        try (final Jedis jedis = this.getResource()) {
            if (jedis != null) {
                action.accept(jedis);
            }
        }
    }

    @Override
    public <T> T get(Function<Jedis, T> action) {
        try (final Jedis jedis = this.getResource()) {
            if (jedis != null) {
                return action.apply(jedis);
            }
        }
        return null;
    }

}
