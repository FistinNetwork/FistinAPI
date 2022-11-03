package fr.fistin.api.redis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Created by AstFaster
 * on 03/11/2022 at 20:14
 *
 * Represents the abstraction of a Redis connection.
 */
public interface Redis {

    /**
     * Get the {@link JedisPool} instance
     *
     * @return A {@link JedisPool} instance
     */
    JedisPool getPool();

    /**
     * Get a Redis client connecting from the pool
     *
     * @return A {@link Jedis} object
     */
    Jedis getResource();

    /**
     * Check whether Fistin API is connected to Redis or not.
     *
     * @return <code>true</code> if yes
     */
    boolean isConnected();

    /**
     * Process an action in Redis database.
     *
     * @param action The action to process
     */
    void process(Consumer<Jedis> action);

    /**
     * Apply a get action in Redis database.
     *
     * @param action The action to apply
     * @return The result of the applied action
     * @param <T> The type of the result
     */
    <T> T get(Function<Jedis, T> action);

}
