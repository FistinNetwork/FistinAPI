package fr.fistin.api.plugin.impl;

import fr.fistin.api.database.DatabaseConfiguration;
import fr.fistin.api.database.DatabaseManager;
import fr.fistin.api.eventbus.IFistinEventBus;
import fr.fistin.api.eventbus.IFistinEventExecutor;
import fr.fistin.api.eventbus.IFistinEventExecutor.IFistinEventBusRegisterer;
import fr.fistin.api.eventbus.internal.InternalEventBus;
import fr.fistin.api.eventbus.internal.InternalFistinEventExecutor;
import fr.fistin.api.packets.FReturnToBungeePacket;
import fr.fistin.api.packets.PacketManager;
import fr.fistin.api.plugin.providers.IFistinAPIProvider;
import fr.fistin.api.plugin.providers.ILevelingProvider;
import fr.fistin.api.plugin.providers.PluginProviders;
import fr.fistin.api.utils.FireworkFactory;
import fr.fistin.api.utils.Internal;
import fr.fistin.api.utils.PluginLocation;
import fr.fistin.api.utils.SetupListener;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.logging.Level;

@Internal
final class FistinAPIProvider extends JavaPlugin implements IFistinAPIProvider
{
	private FireworkFactory fireworkFactory;
	private PacketManager packetManager;
	private IFistinEventExecutor eventExecutor;
	private IFistinEventBus eventBus;
	private DatabaseManager databaseManager;

	@Override
	public void onEnable()
	{
		this.getLogger().info("Entering initialization phase...");
		this.saveDefaultConfig();
		this.reloadConfig();
		this.registerEventsFeatures();
		this.databaseManager = new DatabaseManager(new DatabaseConfiguration());
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
		PluginProviders.setProvider(IFistinAPIProvider.class, this);
		PluginProviders.setProvider(ILevelingProvider.class, new LevelingProvider());
		this.fireworkFactory.registerFirework(new PluginLocation(NAMESPACE, "firstSetup", true), bd -> bd.flicker(true).trail(true).with(FireworkEffect.Type.BURST).withColor(Color.PURPLE, Color.BLUE, Color.YELLOW).withFade(Color.ORANGE).build());

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
		this.databaseManager.close();
		this.packetManager.clear();
		this.fireworkFactory.clear();
		PluginProviders.clear();
		this.eventExecutor.clear();
	}

	@Override
	public IFistinEventBus getEventBus()
	{
		return this.eventBus;
	}

	@Override
	public IFistinEventBusRegisterer getEventRegisterer()
	{
		return this.eventExecutor.getRegisterer();
	}

	@Override
	public FireworkFactory getFireworkFactory()
	{
		return this.fireworkFactory;
	}

	@Override
	public PacketManager getPacketManager()
	{
		return this.packetManager;
	}

	@Override
	public DatabaseManager getDatabaseManager()
	{
		return this.databaseManager;
	}
}
