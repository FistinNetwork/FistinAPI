package fr.flowarg.fistinnetwork.api.docker;

import org.bukkit.scheduler.BukkitRunnable;

import fr.flowarg.fistinnetwork.api.FistinAPI;
import fr.flowarg.fistinnetwork.api.plugin.IAPIComponent;
import fr.flowarg.fistinnetwork.api.plugin.IFistinPlugin;

public class DockerAPI implements IAPIComponent
{
	public void callCommand(String name)
	{
		new BukkitRunnable()
		{
			@Override
			public void run()
			{
				final DockerCommand command = DockerCommand.getDockerCommandByName(name);
				DockerAPI.this.getPlugin().getLogger().info(String.format("Calling %s command !", command.getName()));
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
