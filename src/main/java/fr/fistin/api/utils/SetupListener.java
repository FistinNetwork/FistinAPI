package fr.fistin.api.utils;

import fr.fistin.api.plugin.providers.IFistinAPIProvider;
import fr.fistin.api.plugin.providers.PluginProviders;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.io.File;
import java.io.IOException;

@Internal
public class SetupListener implements Listener
{
    @EventHandler
    public void onFirstPlayerJoin(PlayerJoinEvent event)
    {
        final IFistinAPIProvider api = PluginProviders.getProvider(IFistinAPIProvider.class);
        final File file = new File(api.getDataFolder(), "fistinapi.ptdr.t.ki");
        if(!file.exists())
        {
            try
            {
                file.getParentFile().mkdirs();
                file.createNewFile();

                api.getServer().getScheduler().scheduleSyncDelayedTask(api, () -> {
                    api.getLogger().info("Welcome in FistinAPI" + api.getDescription().getVersion() + " !");
                    Bukkit.broadcastMessage(ChatColor.DARK_BLUE.toString() + ChatColor.UNDERLINE.toString() + "Welcome in FistinAPI " + api.getDescription().getVersion() + " !");
                    api.getFireworkFactory().spawnFirework(PluginLocation.getOrRegisterLocation(IFistinAPIProvider.NAMESPACE, "firstSetup"), event.getPlayer().getLocation(), 5F);
                }, 40L);
            } catch (IOException e)
            {
                api.getLogger().warning("Cannot create file " + file.getAbsolutePath() + " !");
                e.printStackTrace();
            }
        }
    }
}
