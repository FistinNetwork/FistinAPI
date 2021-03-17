package fr.fistin.api.utils;

import fr.fistin.api.plugin.providers.IFistinAPIProvider;
import fr.fistin.api.plugin.providers.PluginProviders;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.inventory.meta.FireworkMeta;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class FireworkFactory
{
	private final Map<PluginLocation, FireworkEffect> effects = new HashMap<>();

	public void registerFirework(PluginLocation location, Function<FireworkEffect.Builder, FireworkEffect> effect)
	{
		if(!this.effects.containsKey(location))
		{
			PluginProviders.getProvider(IFistinAPIProvider.class).getLogger().info("Registered new firework with id (" + location.getFinalPath() + ")");
			this.effects.put(location, effect.apply(FireworkEffect.builder()));
		}
	}
	
	public void spawnFirework(PluginLocation pluginLocation, Location location, double offsetY)
	{
		final Location location2 = offsetY == 0 ? location : new Location(location.getWorld(), location.getX(), location.getY() + offsetY, location.getZ());
		final Firework firework = (Firework)location.getWorld().spawnEntity(location2, EntityType.FIREWORK);
		final FireworkMeta fireworkMeta = firework.getFireworkMeta();
		
		if(this.effects.containsKey(pluginLocation))
			fireworkMeta.addEffect(this.effects.get(pluginLocation));
		
		fireworkMeta.setPower(0);
		firework.setFireworkMeta(fireworkMeta);
	}
	
	public void spawnFirework(PluginLocation pluginLocation, Location location)
	{
		this.spawnFirework(pluginLocation, location, 0);
	}

	public void clear()
	{
		this.effects.clear();
	}
}
