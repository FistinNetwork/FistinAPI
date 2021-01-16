package fr.fistin.api.utils;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class PluginLocation
{
	private static final Map<String, PluginLocation> REGISTERED_LOCATIONS = new HashMap<>();

	private final String namespace;
	private final String path;
	private final String finalPath;

	public PluginLocation(String namespace, String path)
	{
		this(namespace, path, false);
	}

	public PluginLocation(String path)
	{
		this(path, false);
	}

	public PluginLocation(String namespace, String path, boolean register)
	{
		this(namespace + ':' + path, register);
	}
	
	public PluginLocation(String path, boolean register)
	{
		this.namespace = downString(path.substring(0, path.lastIndexOf(':')));
		this.path = downString(path.substring(path.lastIndexOf(':') + 1));
		this.finalPath = downString(path);

		if(register) REGISTERED_LOCATIONS.putIfAbsent(this.finalPath, this);
	}

	public static PluginLocation getOrRegisterLocation(String finalPath)
	{
		final String okString = downString(finalPath);
		PluginLocation location = REGISTERED_LOCATIONS.get(okString);
		if(location == null)
			location = new PluginLocation(okString, true);
		return location;
	}

	public static PluginLocation getOrRegisterLocation(String namespace, String path)
	{
		return getOrRegisterLocation(namespace + ':' + path);
	}

	private static String downString(String str)
	{
		return str.toLowerCase(Locale.ROOT).replace(' ', '_');
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

		if (!Objects.equals(this.namespace, that.namespace)) return false;
		if (!Objects.equals(this.path, that.path)) return false;
		return Objects.equals(this.finalPath, that.finalPath);
	}

	@Override
	public String toString()
	{
		return "PluginLocation{" + "namespace='" + this.namespace + '\'' + ", path='" + this.path + '\'' + ", finalPath='" + this.finalPath + '\'' + '}';
	}
}
