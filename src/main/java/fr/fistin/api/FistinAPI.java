package fr.fistin.api;

import fr.fistin.api.packets.PacketManager;
import fr.fistin.api.packets.FReturnToBungeePacket;
import fr.fistin.api.utils.SetupListener;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.logging.Level;

public class FistinAPI extends JavaPlugin
{
	public static final String NAMESPACE = "fistinapi";
	private static FistinAPI fistinAPI;
	private FireworkFactory fireworkFactory;
	private PacketManager packetManager;

	@Override
	public void onEnable()
	{
		fistinAPI = this;
		this.getLogger().info("Entering initialization phase...");
		this.fireworkFactory = new FireworkFactory();
		this.fireworkFactory.registerBaseFireworks();
		this.packetManager = new PacketManager();
		this.registerBasePackets();
		this.getServer().getPluginManager().registerEvents(new SetupListener(), this);
	}

	private void registerBasePackets()
	{
		this.packetManager.registerPacket(FReturnToBungeePacket.class, packet -> {
			try
			{
				final ByteArrayOutputStream byteArray  = new ByteArrayOutputStream();
				final DataOutputStream out = new DataOutputStream(byteArray);

				out.writeUTF("Connect");
				out.writeUTF(packet.getServerName());
				packet.getToSend().sendPluginMessage(packet.getPlugin(), packet.getBungeeCordChannel(), byteArray.toByteArray());
			}
			catch (IOException e)
			{
				this.getLogger().log(Level.SEVERE, e.getMessage(), e);
			}
		});
	}
	
	@Override
	public void onDisable()
	{
		this.packetManager.stop();
		this.fireworkFactory.clear();
	}
	
	public FireworkFactory getFireworkFactory()
	{
		return this.fireworkFactory;
	}

	public PacketManager getPacketManager()
	{
		return this.packetManager;
	}

	public static FistinAPI getFistinAPI()
	{
		return fistinAPI;
	}
}
