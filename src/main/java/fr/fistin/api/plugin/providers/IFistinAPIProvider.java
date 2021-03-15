package fr.fistin.api.plugin.providers;

import fr.fistin.api.database.DatabaseManager;
import fr.fistin.api.eventbus.IFistinEvent;
import fr.fistin.api.eventbus.IFistinEventBus;
import fr.fistin.api.packets.PacketManager;
import fr.fistin.api.plugin.PluginType;
import fr.fistin.api.plugin.scoreboard.IScoreboardSign;
import fr.fistin.api.utils.FireworkFactory;
import org.bukkit.entity.Player;

import java.util.function.Supplier;

public interface IFistinAPIProvider extends IPluginProvider, IBukkitPluginProvider
{
    String NAMESPACE = "fistinapi";
    String BUNGEE_CORD_CHANNEL = "BungeeCord";

    IFistinEventBus<Supplier<? extends IFistinEvent>> getEventBus();
    FireworkFactory getFireworkFactory();
    PacketManager getPacketManager();
    DatabaseManager getDatabaseManager();
    IScoreboardSign newScoreboardSign(Player player, String objectiveName);

    @Override
    default PluginType getPluginType()
    {
        return PluginType.UTILITY;
    }
}
