package fr.flowarg.fistinnetwork.api.plugin;

import org.bukkit.plugin.Plugin;

import fr.flowarg.fistinnetwork.api.FistinAPI;
import fr.flowarg.sch.ConfigurationManager;

public interface IFistinPlugin extends Plugin
{
	void onEnable();
	void onDisable();
	void registerCommands();
	FistinAPI getAPI();
	ConfigurationManager getConfiguration();
}
