package fr.flowarg.fistinnetwork.api;

import org.bukkit.plugin.java.JavaPlugin;

import fr.flowarg.fistinnetwork.api.docker.DockerAPI;
import fr.flowarg.fistinnetwork.api.plugin.IFistinPlugin;
import fr.flowarg.sch.ConfigurationManager;

public class FistinAPI extends JavaPlugin implements IFistinPlugin
{
	private static FistinAPI fistinAPI;
	private FireworkFactory fireworkFactory;
	private DockerAPI dockerAPI;
	
	@Override
	public void onEnable()
	{
		fistinAPI = this;
		this.getLogger().info("Entering initialization phase...");
		this.fireworkFactory = new FireworkFactory();
		this.fireworkFactory.registerFireworks();
		this.dockerAPI = new DockerAPI();
	}
	
	@Override
	public void onDisable()
	{
		this.getLogger().info("Entering disabling phase...");
		this.fireworkFactory = null;
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

	@Override
	public void registerCommands() {}

	@Override
	public FistinAPI getAPI()
	{
		return this;
	}

	@Override
	public ConfigurationManager getConfiguration()
	{
		return null;
	}
}
