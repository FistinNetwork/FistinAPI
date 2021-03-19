package fr.fistin.api.impl;

import fr.fistin.api.eventbus.DefaultEventBus;
import fr.fistin.api.plugin.providers.IFistinAPIProvider;
import fr.fistin.api.plugin.providers.PluginProviders;
import fr.fistin.api.utils.PluginLocation;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

class DebugCommand implements CommandExecutor
{
    private final IFistinAPIProvider api;

    DebugCommand(IFistinAPIProvider api)
    {
        this.api = api;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
    {
        if(label.equalsIgnoreCase("fistindebug"))
        {
            if(args.length >= 1)
            {
                switch (args[0].toLowerCase(Locale.ROOT))
                {
                    case "providers":
                        this.sendProviders(sender);
                        return true;
                    case "items":
                        this.sendItems(sender);
                        return true;
                    case "fireworks":
                        this.sendFireworks(sender);
                        return true;
                    case "events":
                        if(args.length > 1)
                        {
                            this.sendEvents(sender, args[1]);
                            return true;
                        }
                        else return false;
                }
            }
        }
        return false;
    }

    private void sendProviders(CommandSender sender)
    {
        sender.sendMessage("-- PluginProviders --");
        PluginProviders.getProvidersName().forEach(name -> sender.sendMessage("* " + name));
    }

    private void sendItems(CommandSender sender)
    {
        sender.sendMessage("-- Items --");
        this.api.items().getRegisteredItemsName().forEach(name -> sender.sendMessage("* " + name));
    }

    private void sendFireworks(CommandSender sender)
    {
        sender.sendMessage("-- Fireworks --");
        this.api.fireworkFactory()
                .effectsLocation()
                .stream()
                .map(PluginLocation::getFinalPath)
                .forEach(name -> sender.sendMessage("* " + name));
    }

    private void sendEvents(CommandSender sender, String bus)
    {
        sender.sendMessage("-- Events --");
        if(bus.equalsIgnoreCase(this.api.eventBus().implName()))
            DefaultEventBus.getEventExecutions().forEach(eventExecution -> sender.sendMessage("* " + eventExecution.getName() + " -> "  + new SimpleDateFormat("hh:mm:ss").format(new Date(eventExecution.getTimestamp()))));
    }
}
