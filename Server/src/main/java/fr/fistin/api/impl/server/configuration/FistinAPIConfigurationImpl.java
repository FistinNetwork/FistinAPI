package fr.fistin.api.impl.server.configuration;

import fr.fistin.api.configuration.FistinAPIConfiguration;
import fr.fistin.api.impl.server.FistinAPI;
import fr.fistin.hydra.api.protocol.data.RedisData;
import fr.flowarg.sch.SpigotConfigurationEntry.BooleanEntry;
import fr.flowarg.sch.SpigotConfigurationEntry.IntegerEntry;
import fr.flowarg.sch.SpigotConfigurationEntry.StringEntry;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public class FistinAPIConfigurationImpl implements FistinAPIConfiguration
{
    private final FileConfiguration config = JavaPlugin.getPlugin(FistinAPI.class).getConfig();
    private final BooleanEntry localEntry = new BooleanEntry("local", this.config);
    private final StringEntry levelingUser = new StringEntry("databases.leveling.credentials.user", this.config);
    private final StringEntry levelingPass = new StringEntry("databases.leveling.credentials.pass", this.config);
    private final StringEntry levelingHost = new StringEntry("databases.leveling.host", this.config);
    private final StringEntry levelingDbName = new StringEntry("databases.leveling.dbName", this.config);
    private final IntegerEntry levelingPort = new IntegerEntry("databases.leveling.port", this.config);
    private final StringEntry redisHostname = new StringEntry("redis.hostname", this.config);
    private final IntegerEntry redisPort = new IntegerEntry("redis.port", this.config);
    private final StringEntry redisPassword = new StringEntry("redis.password", this.config);
    private final BooleanEntry hydraEnable = new BooleanEntry("hydra.enable", this.config);

    private RedisData redis;

    @Override
    public boolean isLocal() {
        return this.localEntry.get();
    }

    @Override
    public String getLevelingUser()
    {
        return this.levelingUser.get();
    }

    @Override
    public String getLevelingPass()
    {
        return this.levelingPass.get();
    }

    @Override
    public String getLevelingHost()
    {
        return this.levelingHost.get();
    }

    @Override
    public String getLevelingDbName()
    {
        return this.levelingDbName.get();
    }

    @Override
    public int getLevelingPort()
    {
        return this.levelingPort.get();
    }

    @Override
    public RedisData getRedis() {
        return this.redis == null ? this.redis = new RedisData(this.redisHostname.get(), this.redisPort.get(), this.redisPassword.get()) : this.redis;
    }

    @Override
    public boolean isHydraEnabled() {
        return this.hydraEnable.get();
    }

}
