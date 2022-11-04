package fr.fistin.api.impl.proxy.configuration;

import fr.fistin.api.configuration.FistinAPIConfiguration;
import fr.fistin.api.impl.proxy.FistinAPI;
import fr.fistin.hydra.api.protocol.data.RedisData;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;
import org.jetbrains.annotations.ApiStatus;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

@ApiStatus.Internal
public class FistinAPIConfigurationImpl implements FistinAPIConfiguration
{

    private final String levelingUser;
    private final String levelingPass;
    private final String levelingHost;
    private final String levelingDbName;
    private final int levelingPort;
    private final String redisHostname;
    private final int redisPort;
    private final String redisPassword;
    private final boolean hydraEnable;

    private RedisData redis;

    public FistinAPIConfigurationImpl(FistinAPI plugin) throws IOException {
        final File configFile = new File(plugin.getDataFolder(), "config.yml");

        if (!configFile.exists()) {
            plugin.getDataFolder().mkdir();

            try (final InputStream inputStream = FistinAPI.class.getClassLoader().getResourceAsStream("config.yml")) {
                System.out.println(inputStream);
                if (inputStream != null) {
                    Files.copy(inputStream, configFile.toPath());
                }
            }
        }

        final Configuration config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(new File(plugin.getDataFolder(), "config.yml"));

        this.levelingUser = config.getString("databases.leveling.credentials.user");
        this.levelingPass = config.getString("databases.leveling.credentials.pass");
        this.levelingHost = config.getString("databases.leveling.host");
        this.levelingDbName = config.getString("databases.leveling.dbName");
        this.levelingPort = config.getInt("databases.leveling.port");
        this.redisHostname = config.getString("redis.hostname");
        this.redisPort = config.getInt("redis.port");
        this.redisPassword = config.getString("redis.password");
        this.hydraEnable = config.getBoolean("hydra.enable");
    }

    @Override
    public String getLevelingUser()
    {
        return this.levelingUser;
    }

    @Override
    public String getLevelingPass()
    {
        return this.levelingPass;
    }

    @Override
    public String getLevelingHost()
    {
        return this.levelingHost;
    }

    @Override
    public String getLevelingDbName()
    {
        return this.levelingDbName;
    }

    @Override
    public int getLevelingPort()
    {
        return this.levelingPort;
    }

    @Override
    public RedisData getRedis() {
        return this.redis == null ? this.redis = new RedisData(this.redisHostname, this.redisPort, this.redisPassword) : this.redis;
    }

    @Override
    public boolean isHydraEnabled() {
        return this.hydraEnable;
    }

}
