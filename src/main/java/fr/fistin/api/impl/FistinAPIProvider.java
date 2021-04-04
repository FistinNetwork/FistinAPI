package fr.fistin.api.impl;

import fr.fistin.api.configuration.ConfigurationProviders;
import fr.fistin.api.configuration.FistinAPIConfiguration;
import fr.fistin.api.database.DatabaseManager;
import fr.fistin.api.eventbus.DefaultEventBus;
import fr.fistin.api.eventbus.IFistinEvent;
import fr.fistin.api.eventbus.IFistinEventBus;
import fr.fistin.api.impl.smartinvs.InventoryContentsImpl;
import fr.fistin.api.item.IFistinItems;
import fr.fistin.api.packets.FReturnToBungeePacket;
import fr.fistin.api.packets.PacketManager;
import fr.fistin.api.plugin.providers.IFistinAPIProvider;
import fr.fistin.api.plugin.providers.ILevelingProvider;
import fr.fistin.api.plugin.providers.PluginProviders;
import fr.fistin.api.plugin.scoreboard.IScoreboardSign;
import fr.fistin.api.smartinvs.InventoryManager;
import fr.fistin.api.utils.FireworkFactory;
import fr.fistin.api.utils.PluginLocation;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.function.Supplier;
import java.util.logging.Level;

/**
 * Entry point of the FistinAPI, need to be public to be recognized by PluginClassLoader.
 */
@ApiStatus.Internal
public final class FistinAPIProvider extends JavaPlugin implements IFistinAPIProvider
{
    private FireworkFactory fireworkFactory;
    private PacketManager packetManager;
    private IFistinEventBus<Supplier<? extends IFistinEvent>> eventBus;
    private DatabaseManager databaseManager;
    private InventoryManager smartInvsManager;
    private IFistinItems items;

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
        ConfigurationProviders.setConfig(FistinAPIConfiguration.class, new FistinAPIConfiguration());
    }

    private void init()
    {
        this.eventBus = new DefaultEventBus();
        this.databaseManager = new DatabaseManager();
        this.fireworkFactory = new FireworkFactory();
        this.packetManager = new PacketManager();
        this.items = new FistinItemsImpl();
        this.smartInvsManager = new InventoryManager(this, InventoryContentsImpl::new);
        this.smartInvsManager.init();
    }

    private void postInit()
    {
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
        this.getServer().getPluginManager().registerEvents(new ItemListener(), this);
        this.getCommand("fistindebug").setExecutor(new DebugCommand(this));
        this.getCommand("fgive").setExecutor((sender, command, label, args) -> {
            if(args.length == 1)
            {
                if(sender instanceof Player)
                {
                    final PluginLocation loc = PluginLocation.getLocation(args[0]);
                    if(loc != null)
                        ((Player)sender).getInventory().addItem(this.items.getItem(loc).enclosingItem());
                    else sender.sendMessage("\u00A7cItem name not valid!\u00A7r");
                    return true;
                }
            }
            return false;
        });
    }
    
    @Override
    public void onDisable()
    {
        this.databaseManager.clear();
        this.packetManager.clear();
        this.fireworkFactory.clear();
        this.eventBus.clear();
        this.items.clear();

        PluginProviders.clear();
        ConfigurationProviders.clear();
        PluginLocation.clear();

        try
        {
            final Field cache = Cache.class.getDeclaredField("cache");
            cache.setAccessible(true);
            cache.set(null, null);
        }
        catch (Exception e)
        {
            this.getLogger().log(Level.SEVERE, e.getMessage(), e);
        }

        this.getLogger().info("Stopped Fistin API, bye !");
        this.getLogger().info("==========================");
    }

    @Override
    public @NotNull IFistinEventBus<Supplier<? extends IFistinEvent>> eventBus()
    {
        return this.eventBus;
    }

    @Override
    public @NotNull FireworkFactory fireworkFactory()
    {
        return this.fireworkFactory;
    }

    @Override
    public @NotNull PacketManager packetManager()
    {
        return this.packetManager;
    }

    @Override
    public @NotNull DatabaseManager databaseManager()
    {
        return this.databaseManager;
    }

    @Override
    public @NotNull IScoreboardSign newScoreboardSign(Player player, String objectiveName)
    {
        return new ScoreboardSign(player, objectiveName);
    }

    @Override
    public @NotNull InventoryManager smartInvsManager()
    {
        return this.smartInvsManager;
    }

    @Override
    public @NotNull IFistinItems items()
    {
        return this.items;
    }
}
