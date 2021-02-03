package fr.fistin.api;

import fr.fistin.api.utils.PluginLocation;
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
	private final FireworkEffect.Builder builder = FireworkEffect.builder();

	public void registerFirework(PluginLocation location, Function<FireworkEffect.Builder, FireworkEffect> effect)
	{
		if(!this.effects.containsKey(location))
		{
			FistinAPI.getFistinAPI().getLogger().info("Registered new firework with id (" + location.getFinalPath() + ")");
			this.effects.put(location, effect.apply(this.builder));
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

	void clear()
	{
		this.effects.clear();
	}
}
