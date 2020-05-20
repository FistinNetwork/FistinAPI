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
	
	public void registerFireworks()
	{
		final FireworkEffect.Builder builder = FireworkEffect.builder();
		
		final FireworkEffect giveTntFirework = builder.flicker(false).trail(false).with(Type.CREEPER).withColor(Color.RED).build();
		final FireworkEffect endTntTag = builder.flicker(true).trail(true).with(Type.STAR).withColor(Color.RED, Color.GREEN, Color.BLUE).withFade(Color.ORANGE).build();
		
		this.effects.put(new PluginLocation("tnttag", "giveTntFirework"), giveTntFirework);
		this.effects.put(new PluginLocation("tnttag", "endTntTag"), endTntTag);
	}
	
	public void spawnFirework(PluginLocation pluginLocation, Location location, double offsetY)
	{
		Location location2 = location;
		if(offsetY != 0)
			location2 = new Location(location.getWorld(), location.getX(), location.getY() + offsetY, location.getZ());
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
