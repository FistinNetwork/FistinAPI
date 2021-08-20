package fr.fistin.api.impl;

import fr.fistin.api.IFistinAPIProvider;
import fr.fistin.api.configuration.FistinAPIConfiguration;
import fr.fistin.api.plugin.providers.PluginProviders;
import fr.fistin.api.utils.Utils;
import fr.flowarg.sch.SpigotConfigurationEntry.BooleanEntry;
import fr.flowarg.sch.SpigotConfigurationEntry.IntegerEntry;
import fr.flowarg.sch.SpigotConfigurationEntry.StringEntry;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
class FistinAPIConfigurationImpl implements FistinAPIConfiguration
{
    private final FileConfiguration config = Utils.unsafeGet(Plugin.class, (Plugin)PluginProviders.getProvider(IFistinAPIProvider.class), "getConfig", Utils.TypeGet.METHOD);
    private final StringEntry levelingUser = new StringEntry("databases.leveling.credentials.user", this.config);
    private final StringEntry levelingPass = new StringEntry("databases.leveling.credentials.pass", this.config);
    private final StringEntry levelingHost = new StringEntry("databases.leveling.host", this.config);
    private final StringEntry levelingDbName = new StringEntry("databases.leveling.dbName", this.config);
    private final IntegerEntry levelingPort = new IntegerEntry("databases.leveling.port", this.config);
    private final StringEntry hydraHost = new StringEntry("hydra.host", this.config);
    private final IntegerEntry hydraPort = new IntegerEntry("hydra.port", this.config);
    private final StringEntry hydraPass = new StringEntry("hydra.pass", this.config);
    private final BooleanEntry hydraEnable = new BooleanEntry("hydra.enable", this.config);

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
    public String getHydraHost()
    {
        return this.hydraHost.get();
    }

    @Override
    public int getHydraPort()
    {
        return this.hydraPort.get();
    }

    @Override
    public String getHydraPass()
    {
        return this.hydraPass.get();
    }

    @Override
    public boolean getHydraEnable()
    {
        return this.hydraEnable.get();
    }
}
