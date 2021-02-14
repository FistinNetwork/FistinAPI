package fr.fistin.api.plugin.impl;

import fr.fistin.api.configuration.ConfigurationProviders;
import fr.fistin.api.database.DatabaseConfiguration;
import fr.fistin.api.database.DatabaseManager;
import fr.fistin.api.eventbus.IFistinEventBus;
import fr.fistin.api.eventbus.IFistinEventExecutor;
import fr.fistin.api.eventbus.IFistinEventExecutor.IFistinEventBusRegisterer;
import fr.fistin.api.eventbus.internal.InternalEventBus;
import fr.fistin.api.eventbus.internal.InternalFistinEventExecutor;
import fr.fistin.api.packets.FReturnToBungeePacket;
import fr.fistin.api.packets.PacketManager;
import fr.fistin.api.plugin.providers.IFistinAPIProvider;
import fr.fistin.api.plugin.providers.ILevelingProvider;
import fr.fistin.api.plugin.providers.PluginProviders;
import fr.fistin.api.plugin.scoreboard.IScoreboardSign;
import fr.fistin.api.utils.FireworkFactory;
import fr.fistin.api.utils.Internal;
import fr.fistin.api.utils.PluginLocation;
import fr.fistin.api.utils.SetupListener;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.logging.Level;

/**
 * Entry point of the FistinAPI, need to be public to be recognized by PluginClassLoader.
 */
@Internal
public final class FistinAPIProvider extends JavaPlugin implements IFistinAPIProvider
{
    private FireworkFactory fireworkFactory;
    private PacketManager packetManager;
    private IFistinEventExecutor eventExecutor;
    private IFistinEventBus eventBus;
    private DatabaseManager databaseManager;

    @Override
    public void onEnable()
    {
        this.getLogger().info("==========================");
        this.getLogger().info("Hello, Starting Fistin API");

        this.preInit();
        this.init();
        this.postInit();
    }

    private void preInit()
    {
        this.saveDefaultConfig();
        this.reloadConfig();
        PluginProviders.setProvider(IFistinAPIProvider.class, this);
        PluginProviders.setProvider(ILevelingProvider.class, new LevelingProvider());
        ConfigurationProviders.setConfig(DatabaseConfiguration.class, new DatabaseConfiguration());
    }

    private void init()
    {
        this.eventExecutor = new InternalFistinEventExecutor();
        this.eventBus = new InternalEventBus();
        this.databaseManager = new DatabaseManager();
        this.fireworkFactory = new FireworkFactory();
        this.packetManager = new PacketManager();
    }

    private void postInit()
    {
        this.eventExecutor.getRegisterer().addEventBus(this.eventBus);
        this.fireworkFactory.registerFirework(new PluginLocation(NAMESPACE, "firstSetup", true), bd -> bd.flicker(true).trail(true).with(FireworkEffect.Type.BURST).withColor(Color.PURPLE, Color.BLUE, Color.YELLOW).withFade(Color.ORANGE).build());

        this.packetManager.registerPacket(FReturnToBungeePacket.class, packet -> {
            try
            {
                final ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
                final DataOutputStream out = new DataOutputStream(byteArray);

                out.writeUTF("Connect");
                out.writeUTF(packet.getServerName());
                packet.getToSend().sendPluginMessage(packet.getPlugin(), packet.getBungeeCordChannel(), byteArray.toByteArray());
            }
            catch (IOException e)
            {
                this.getLogger().log(Level.SEVERE, e.getMessage(), e);
            }
        });

        this.getServer().getPluginManager().registerEvents(new SetupListener(), this);
        this.getCommand("providers").setExecutor(new CommandProviders());
    }
    
    @Override
    public void onDisable()
    {
        this.databaseManager.clear();
        this.packetManager.clear();
        this.fireworkFactory.clear();
        this.eventExecutor.clear();
        PluginProviders.clear();
        ConfigurationProviders.clear();
        PluginLocation.clear();

        this.getLogger().info("Stopping Fistin API, bye !");
        this.getLogger().info("==========================");
    }

    @Override
    public IFistinEventBus getEventBus()
    {
        return this.eventBus;
    }

    @Override
    public IFistinEventBusRegisterer getEventRegisterer()
    {
        return this.eventExecutor.getRegisterer();
    }

    @Override
    public FireworkFactory getFireworkFactory()
    {
        return this.fireworkFactory;
    }

    @Override
    public PacketManager getPacketManager()
    {
        return this.packetManager;
    }

    @Override
    public DatabaseManager getDatabaseManager()
    {
        return this.databaseManager;
    }

    @Override
    public IScoreboardSign newScoreboardSign(Player player, String objectiveName)
    {
        return new ScoreboardSign(player, objectiveName);
    }

    private static class CommandProviders implements CommandExecutor
    {
        @Override
        public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
        {
            if(label.equalsIgnoreCase("providers"))
            {
                sender.sendMessage("Loaded PluginProviders: ");
                PluginProviders.getProvidersClasses().forEach(aClass -> sender.sendMessage("- " + aClass.getName()));
                return true;
            }
            return false;
        }
    }
}
