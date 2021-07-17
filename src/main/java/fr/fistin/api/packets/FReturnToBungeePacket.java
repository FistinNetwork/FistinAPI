package fr.fistin.api.packets;

import fr.fistin.api.IFistinAPIProvider;

import java.util.UUID;

public class FReturnToBungeePacket implements FistinPacket
{
    private final Object plugin;
    private final String bungeeCordChannel;
    private final String serverName;
    private final UUID toSend;

    public FReturnToBungeePacket(Object plugin, String bungeeCordChannel, String serverName, UUID toSend)
    {
        this.plugin = plugin;
        this.bungeeCordChannel = bungeeCordChannel;
        this.serverName = serverName;
        this.toSend = toSend;
    }

    public FReturnToBungeePacket(Object plugin, String serverName, UUID toSend)
    {
        this(plugin, IFistinAPIProvider.BUNGEE_CORD_CHANNEL, serverName, toSend);
    }

    public Object getPlugin()
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

    public UUID getToSend()
    {
        return this.toSend;
    }

    @Override
    public String toString()
    {
        return "FReturnToBungeePacket{" + "plugin=" + this.plugin + ", bungeeCordChannel='" + this.bungeeCordChannel + '\'' + ", serverName='" + this.serverName + '\'' + ", toSend=" + this.toSend + '}';
    }
}
