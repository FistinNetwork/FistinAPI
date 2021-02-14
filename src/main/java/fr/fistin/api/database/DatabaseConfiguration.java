package fr.fistin.api.database;

import fr.fistin.api.plugin.providers.IFistinAPIProvider;
import fr.fistin.api.plugin.providers.PluginProviders;
import fr.fistin.api.utils.Internal;
import fr.flowarg.sch.ConfigurationManager;
import fr.flowarg.sch.SpigotConfigurationEntry.IntegerEntry;
import fr.flowarg.sch.SpigotConfigurationEntry.StringEntry;
import org.bukkit.configuration.file.FileConfiguration;

@Internal
public class DatabaseConfiguration implements ConfigurationManager
{
    private final FileConfiguration config = PluginProviders.getProvider(IFistinAPIProvider.class).getConfig();
    private final StringEntry levelingUser = new StringEntry("databases.leveling.credentials.user", this.config);
    private final StringEntry levelingPass = new StringEntry("databases.leveling.credentials.pass", this.config);
    private final StringEntry levelingHost = new StringEntry("databases.leveling.host", this.config);
    private final StringEntry levelingDbName = new StringEntry("databases.leveling.dbName", this.config);
    private final IntegerEntry levelingPort = new IntegerEntry("databases.leveling.port", this.config);

    public String getLevelingUser()
    {
        return this.levelingUser.get();
    }

    public String getLevelingPass()
    {
        return this.levelingPass.get();
    }

    public String getLevelingHost()
    {
        return this.levelingHost.get();
    }

    public String getLevelingDbName()
    {
        return this.levelingDbName.get();
    }

    public Integer getLevelingPort()
    {
        return this.levelingPort.get();
    }

    @Override
    public void saveConfig()
    {
        PluginProviders.getProvider(IFistinAPIProvider.class).saveConfig();
    }

    @Override
    public void loadConfig()
    {
        PluginProviders.getProvider(IFistinAPIProvider.class).reloadConfig();
    }

    @Override
    public FileConfiguration getConfig()
    {
        return this.config;
    }
}
