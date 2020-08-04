package fr.flowarg.fistinnetwork.api.docker;

import org.bukkit.scheduler.BukkitRunnable;

import fr.flowarg.fistinnetwork.api.FistinAPI;

public class DockerAPI
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
	
	public void unload()
	{
		DockerCommand.unload();
	}
	
	public FistinAPI getPlugin()
	{
		return FistinAPI.getFistinAPI();
	}
}
