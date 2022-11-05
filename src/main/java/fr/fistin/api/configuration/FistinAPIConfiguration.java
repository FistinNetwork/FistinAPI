package fr.fistin.api.configuration;

import fr.fistin.hydra.api.protocol.data.RedisData;

public interface FistinAPIConfiguration extends FistinConfiguration
{

    /**
     * Get the Fistin API configuration instance.
     *
     * @return The {@link FistinAPIConfiguration} instance
     */
    static FistinAPIConfiguration get() {
        return ConfigurationProviders.getConfig(FistinAPIConfiguration.class);
    }

    /**
     * Check whether Fistin API is running in local environment.
     *
     * @return <code>true</code> if yes
     */
    boolean isLocal();

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
