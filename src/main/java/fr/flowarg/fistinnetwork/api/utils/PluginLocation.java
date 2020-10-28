package fr.flowarg.fistinnetwork.api.utils;

import java.util.Objects;

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

	@Override
	public boolean equals(Object o)
	{
		if (this == o) return true;
		if (o == null || this.getClass() != o.getClass()) return false;

		final PluginLocation that = (PluginLocation)o;

		if (!Objects.equals(namespace, that.namespace)) return false;
		if (!Objects.equals(path, that.path)) return false;
		return Objects.equals(finalPath, that.finalPath);
	}

	@Override
	public int hashCode()
	{
		int result = namespace != null ? namespace.hashCode() : 0;
		result = 31 * result + (path != null ? path.hashCode() : 0);
		result = 31 * result + (finalPath != null ? finalPath.hashCode() : 0);
		return result;
	}
}
