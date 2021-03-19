package fr.fistin.api.plugin.providers;

import fr.fistin.api.database.DatabaseManager;
import fr.fistin.api.eventbus.IFistinEvent;
import fr.fistin.api.eventbus.IFistinEventBus;
import fr.fistin.api.item.IFistinItems;
import fr.fistin.api.packets.PacketManager;
import fr.fistin.api.plugin.PluginType;
import fr.fistin.api.plugin.scoreboard.IScoreboardSign;
import fr.fistin.api.smartinvs.InventoryManager;
import fr.fistin.api.utils.FireworkFactory;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public interface IFistinAPIProvider extends IBukkitPluginProvider
{
    String NAMESPACE = "fistinapi";
    String BUNGEE_CORD_CHANNEL = "BungeeCord";

    @NotNull IFistinEventBus<Supplier<? extends IFistinEvent>> eventBus();
    @NotNull FireworkFactory fireworkFactory();
    @NotNull PacketManager packetManager();
    @NotNull DatabaseManager databaseManager();
    @NotNull IScoreboardSign newScoreboardSign(Player player, String objectiveName);
    @NotNull InventoryManager smartInvsManager();
    @NotNull IFistinItems items();

    @Override
    default @NotNull PluginType pluginType()
    {
        return PluginType.UTILITY;
    }
}
