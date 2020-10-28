package fr.flowarg.fistinnetwork.api;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.inventory.meta.FireworkMeta;

import fr.flowarg.fistinnetwork.api.utils.PluginLocation;

public class FireworkFactory
{
	private final Map<PluginLocation, FireworkEffect> effects = new HashMap<>();
	private final FireworkEffect.Builder builder = FireworkEffect.builder();

	public void registerFirework(PluginLocation location, FireworkEffect effect)
	{
		this.effects.putIfAbsent(location, effect);
	}
	
	void registerBaseFireworks()
	{
		final FireworkEffect firstSetup = this.builder.flicker(true).trail(true).with(Type.BURST).withColor(Color.PURPLE, Color.BLUE, Color.YELLOW).withFade(Color.ORANGE).build();
		this.effects.putIfAbsent(new PluginLocation(FistinAPI.NAMESPACE, "firstSetup"), firstSetup);
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
}
