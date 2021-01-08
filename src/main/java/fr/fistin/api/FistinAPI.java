package fr.fistin.api;

import fr.fistin.api.utils.PluginLocation;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public class FistinAPI extends JavaPlugin implements Listener
{
	public static final String NAMESPACE = "fistinapi";
	private static FistinAPI fistinAPI;
	private FireworkFactory fireworkFactory;

	@Override
	public void onEnable()
	{
		fistinAPI = this;
		this.getLogger().info("Entering initialization phase...");
		this.fireworkFactory = new FireworkFactory();
		this.fireworkFactory.registerBaseFireworks();
		this.getServer().getPluginManager().registerEvents(this, this);
	}

	@EventHandler
	public void onFirstPlayerJoin(PlayerJoinEvent event)
	{
		final File file = new File(this.getDataFolder(), "fistinapi.ptdr.t.ki");
		if(!file.exists())
		{
			try
			{
				file.getParentFile().mkdirs();
				file.createNewFile();
				this.getLogger().info("Welcome in FistinAPI" + this.getDescription().getVersion() + " !");
				Bukkit.broadcastMessage(ChatColor.DARK_BLUE.toString() + ChatColor.UNDERLINE.toString() + "Welcome in FistinAPI" + this.getDescription().getVersion() + " !");
				this.fireworkFactory.spawnFirework(new PluginLocation(NAMESPACE, "firstSetup"), event.getPlayer().getLocation(), 5F);
			} catch (IOException e)
			{
				this.getLogger().warning("Cannot create file " + file.getAbsolutePath() + " !");
				e.printStackTrace();
			}
		}
	}
	
	@Override
	public void onDisable()
	{
		this.fireworkFactory.clear();
	}
	
	public FireworkFactory getFireworkFactory()
	{
		return this.fireworkFactory;
	}
	
	public static FistinAPI getFistinAPI()
	{
		return fistinAPI;
	}
}
