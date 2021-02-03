package fr.fistin.api;

import fr.fistin.api.eventbus.IFistinEventBus;
import fr.fistin.api.eventbus.IFistinEventExecutor;
import fr.fistin.api.eventbus.IFistinEventExecutor.IFistinEventBusRegisterer;
import fr.fistin.api.eventbus.internal.InternalEventBus;
import fr.fistin.api.eventbus.internal.InternalFistinEventExecutor;
import fr.fistin.api.packets.FReturnToBungeePacket;
import fr.fistin.api.packets.PacketManager;
import fr.fistin.api.plugin.providers.PluginProviders;
import fr.fistin.api.utils.PluginLocation;
import fr.fistin.api.utils.SetupListener;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.logging.Level;

public class FistinAPI extends JavaPlugin
{
	public static final String NAMESPACE = "fistinapi";
	public static final String BUNGEE_CORD_CHANNEL = "BungeeCord";

	private static FistinAPI fistinAPI;
	private FireworkFactory fireworkFactory;
	private PacketManager packetManager;
	private IFistinEventExecutor eventExecutor;
	private IFistinEventBus eventBus;

	@Override
	public void onEnable()
	{
		fistinAPI = this;
		this.getLogger().info("Entering initialization phase...");
		this.registerEventsFeatures();
		this.fireworkFactory = new FireworkFactory();
		this.packetManager = new PacketManager();
		this.registerBaseFeatures();
		this.getServer().getPluginManager().registerEvents(new SetupListener(), this);
	}

	private void registerEventsFeatures()
	{
		this.eventExecutor = new InternalFistinEventExecutor();
		this.eventBus = new InternalEventBus();
		this.eventExecutor.getRegisterer().addEventBus(this.eventBus);
	}

	private void registerBaseFeatures()
	{
		this.fireworkFactory.registerFirework(new PluginLocation(FistinAPI.NAMESPACE, "firstSetup", true), bd -> bd.flicker(true).trail(true).with(FireworkEffect.Type.BURST).withColor(Color.PURPLE, Color.BLUE, Color.YELLOW).withFade(Color.ORANGE).build());

		this.packetManager.registerPacket(FReturnToBungeePacket.class, packet -> {
			try
			{
				final ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
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
		PluginProviders.clear();
		this.eventExecutor.clear();
	}

	public IFistinEventBus getEventBus()
	{
		return this.eventBus;
	}

	public IFistinEventBusRegisterer getEventRegisterer()
	{
		return this.eventExecutor.getRegisterer();
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
