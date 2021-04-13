package fr.fistin.api.packets;

import fr.fistin.api.IFistinAPIProvider;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class FReturnToBungeePacket implements FistinPacket
{
    private final Plugin plugin;
    private final String bungeeCordChannel;
    private final String serverName;
    private final Player toSend;

    public FReturnToBungeePacket(Plugin plugin, String bungeeCordChannel, String serverName, Player toSend)
    {
        this.plugin = plugin;
        this.bungeeCordChannel = bungeeCordChannel;
        this.serverName = serverName;
        this.toSend = toSend;
    }

    public FReturnToBungeePacket(Plugin plugin, String serverName, Player toSend)
    {
        this.plugin = plugin;
        this.bungeeCordChannel = IFistinAPIProvider.BUNGEE_CORD_CHANNEL;
        this.serverName = serverName;
        this.toSend = toSend;
    }

    public Plugin getPlugin()
    {
        return this.plugin;
    }

    public String getBungeeCordChannel()
    {
        return this.bungeeCordChannel;
    }

    public String getServerName()
    {
        return this.serverName;
    }

    public Player getToSend()
    {
        return this.toSend;
    }

    @Override
    public String toString()
    {
        return "FReturnToBungeePacket{" + "plugin=" + this.plugin + ", bungeeCordChannel='" + this.bungeeCordChannel + '\'' + ", serverName='" + this.serverName + '\'' + ", toSend=" + this.toSend + '}';
    }
}
