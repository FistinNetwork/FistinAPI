package fr.flowarg.fistinnetwork.api.docker;

import java.util.HashMap;
import java.util.Map;

public enum DockerCommand
{
	NONE("unknow", null);
	
	private static final Map<String, DockerCommand> DOCKER_COMMANDS = new HashMap<>();
	
	private String name;
	private Runnable task;
	
	static
	{
		for(DockerCommand command : values())
			DOCKER_COMMANDS.put(command.getName(), command);
	}
	
	DockerCommand(String name, Runnable task)
	{
		this.name = name;
		this.task = task;
	}
	
	public String getName()
	{
		return this.name;
	}
	
	public Runnable getTask() 
	{
		return this.task;
	}
	
	public static DockerCommand getDockerCommandByName(String name)
	{
		return DOCKER_COMMANDS.getOrDefault(name, NONE);
	}
}
