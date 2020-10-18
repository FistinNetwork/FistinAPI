package fr.flowarg.fistinnetwork.api.utils;

public class PluginLocation
{
	private final String namespace;
	private final String path;
	private final String finalPath;
	
	public PluginLocation(String namespace, String path)
	{
		this.namespace = namespace;
		this.path = path;
		this.finalPath = namespace + ':' + path;
	}
	
	public PluginLocation(String path)
	{
		this.namespace = path.substring(0, path.lastIndexOf(':'));
		this.path = path.substring(path.lastIndexOf(':') + 1);
		this.finalPath = path;
	}
	
	public String getFinalPath()
	{
		return this.finalPath;
	}
	
	public String getNamespace()
	{
		return this.namespace;
	}
	
	public String getPath()
	{
		return this.path;
	}
}
