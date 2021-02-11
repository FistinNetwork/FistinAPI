package fr.fistin.api.plugin.providers;

import fr.fistin.api.database.DatabaseManager;
import fr.fistin.api.eventbus.IFistinEventBus;
import fr.fistin.api.eventbus.IFistinEventExecutor.IFistinEventBusRegisterer;
import fr.fistin.api.packets.PacketManager;
import fr.fistin.api.plugin.PluginType;
import fr.fistin.api.utils.FireworkFactory;

public interface IFistinAPIProvider extends IPluginProvider, IBukkitPluginProvider
{
    String NAMESPACE = "fistinapi";
    String BUNGEE_CORD_CHANNEL = "BungeeCord";

    IFistinEventBus getEventBus();
    IFistinEventBusRegisterer getEventRegisterer();
    FireworkFactory getFireworkFactory();
    PacketManager getPacketManager();
    DatabaseManager getDatabaseManager();

    @Override
    default PluginType getPluginType()
    {
        return PluginType.UTILITY;
    }
}
