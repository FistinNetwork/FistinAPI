package fr.fistin.api.configuration;

import fr.fistin.hydra.api.protocol.data.RedisData;

public interface FistinAPIConfiguration extends FistinConfiguration
{
    String getLevelingUser();
    String getLevelingPass();
    String getLevelingHost();
    String getLevelingDbName();
    int getLevelingPort();

    /**
     * Get the Redis credentials
     *
     * @return A {@link RedisData} object
     */
    RedisData getRedis();

    /**
     * Check whether Fistin API is using Hydra communication system.
     *
     * @return <code>true</code> if yes
     */
    boolean isHydraEnabled();

}
