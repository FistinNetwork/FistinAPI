package fr.flowarg.fistinnetwork.api.docker;

import org.bukkit.scheduler.BukkitRunnable;

import fr.flowarg.fistinnetwork.api.FistinAPI;
import fr.flowarg.fistinnetwork.api.plugin.IAPIComponent;
import fr.flowarg.fistinnetwork.api.plugin.IFistinPlugin;

public class DockerAPI implements IAPIComponent
{
	public void callCommand(String name)
	{
		// TODO gérer la commande unknow
		new BukkitRunnable()
		{
			@Override
			public void run()
			{
				final DockerCommand command = DockerCommand.getDockerCommandByName(name);
				command.getTask().run();
			}
		}.runTask(this.getPlugin());
	}
	
	@Override
	public IFistinPlugin getPlugin()
	{
		return FistinAPI.getFistinAPI();
	}
}
