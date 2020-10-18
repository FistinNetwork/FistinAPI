package fr.flowarg.fistinnetwork.api;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.plugin.java.JavaPlugin;

import fr.flowarg.fistinnetwork.api.docker.DockerAPI;
import fr.flowarg.fistinnetwork.api.plugin.IFistinPlugin;

public class FistinAPI extends JavaPlugin
{
	private static FistinAPI fistinAPI;
	private FireworkFactory fireworkFactory;
	private DockerAPI dockerAPI;
	private List<IFistinPlugin> plugins = new ArrayList<>();
	
	@Override
	public void onEnable()
	{
		fistinAPI = this;
		this.getLogger().info("Entering initialization phase...");
		this.fireworkFactory = new FireworkFactory();
		this.fireworkFactory.registerBaseFireworks();
		this.dockerAPI = new DockerAPI();
		this.getServer().getScheduler().runTaskLater(this, () -> {
			this.getLogger().info("Found %d plugins : " + this.plugins.size() + " !");
			this.plugins.forEach(pl -> this.getLogger().info("Loaded " + pl.getName() + "."));
		}, 20L);
	}
	
	@Override
	public void onDisable()
	{
		this.getLogger().info("Entering stopping phase...");
		this.fireworkFactory = null;
		this.dockerAPI.unload();
		this.dockerAPI = null;
		this.plugins.clear();
		fistinAPI = null;
	}
	
	public FireworkFactory getFireworkFactory()
	{
		return this.fireworkFactory;
	}
	
	public DockerAPI getDockerAPI()
	{
		return this.dockerAPI;
	}
	
	public static FistinAPI getFistinAPI()
	{
		return fistinAPI;
	}
	
	public void registerPlugin(IFistinPlugin plugin)
	{
		this.plugins.add(plugin);
	}
}
