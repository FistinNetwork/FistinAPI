package fr.fistin.api.packets;

import fr.fistin.api.FistinAPI;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class FReturnToBungeePacket implements FistinPacket
{
    private final JavaPlugin plugin;
    private final String bungeeCordChannel;
    private final String serverName;
    private final Player toSend;

    public FReturnToBungeePacket(JavaPlugin plugin, String bungeeCordChannel, String serverName, Player toSend)
    {
        this.plugin = plugin;
        this.bungeeCordChannel = bungeeCordChannel;
        this.serverName = serverName;
        this.toSend = toSend;
    }

    public FReturnToBungeePacket(JavaPlugin plugin, String serverName, Player toSend)
    {
        this.plugin = plugin;
        this.bungeeCordChannel = FistinAPI.BUNGEE_CORD_CHANNEL;
        this.serverName = serverName;
        this.toSend = toSend;
    }

    public JavaPlugin getPlugin()
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
        return "FReturnToBungeePacket{" + "plugin=" + plugin + ", bungeeCordChannel='" + bungeeCordChannel + '\'' + ", serverName='" + serverName + '\'' + ", toSend=" + toSend + '}';
    }
}
