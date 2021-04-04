package fr.fistin.api.smartinvs;

import fr.fistin.api.plugin.providers.IFistinAPIProvider;
import fr.fistin.api.plugin.providers.PluginProviders;
import fr.fistin.api.smartinvs.content.InventoryContents;
import fr.fistin.api.smartinvs.content.InventoryContentsWrapper;
import fr.fistin.api.smartinvs.content.InventoryProvider;
import fr.fistin.api.smartinvs.opener.InventoryOpener;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SmartInventory
{
    private String id;
    private String title;
    private InventoryType type;
    private int rows, columns;
    private boolean closeable;

    private InventoryProvider provider;
    private SmartInventory parent;

    private List<InventoryListener<? extends Event>> listeners;
    private final InventoryManager manager;
    private final InventoryContentsWrapper contentsWrapper;

    private SmartInventory(InventoryManager manager)
    {
        this.manager = manager;
        this.contentsWrapper = this.manager.getContentsWrapper();
    }

    public Inventory open(Player player)
    {
        return open(player, 0);
    }

    @SuppressWarnings("unchecked")
    public Inventory open(Player player, int page)
    {
        final Optional<SmartInventory> oldInv = this.manager.getInventory(player);

        oldInv.ifPresent(inv -> {
            inv.getListeners().stream()
                    .filter(listener -> listener.getType() == InventoryCloseEvent.class)
                    .forEach(listener -> ((InventoryListener<InventoryCloseEvent>) listener)
                            .accept(new InventoryCloseEvent(player.getOpenInventory())));
            this.manager.setInventory(player, null);
        });

        final InventoryContents contents = this.contentsWrapper.newImpl(this, player.getUniqueId());
        contents.pagination().page(page);

        this.manager.setContents(player, contents);
        this.provider.init(player, contents);

        final InventoryOpener opener = this.manager.findOpener(type)
                .orElseThrow(() -> new IllegalStateException("No opener found for the inventory type " + type.name()));
        final Inventory handle = opener.open(this, player);

        this.manager.setInventory(player, this);

        return handle;
    }

    @SuppressWarnings("unchecked")
    public void close(Player player)
    {
        listeners.stream()
                .filter(listener -> listener.getType() == InventoryCloseEvent.class)
                .forEach(listener -> ((InventoryListener<InventoryCloseEvent>) listener)
                        .accept(new InventoryCloseEvent(player.getOpenInventory())));

        this.manager.setInventory(player, null);
        player.closeInventory();

        this.manager.setContents(player, null);
    }

    public String getId()
    {
        return id;
    }

    public String getTitle()
    {
        return title;
    }

    public InventoryType getType()
    {
        return type;
    }

    public int getRows()
    {
        return rows;
    }

    public int getColumns()
    {
        return columns;
    }

    public boolean isCloseable()
    {
        return closeable;
    }

    public void setCloseable(boolean closeable)
    {
        this.closeable = closeable;
    }

    public InventoryProvider getProvider()
    {
        return provider;
    }

    public Optional<SmartInventory> getParent()
    {
        return Optional.ofNullable(parent);
    }

    public InventoryManager getManager()
    {
        return manager;
    }

    List<InventoryListener<? extends Event>> getListeners()
    {
        return listeners;
    }

    public static Builder builder()
    {
        return new Builder();
    }

    public static final class Builder
    {
        private String id = "unknown";
        private String title = "";
        private InventoryType type = InventoryType.CHEST;
        private int rows = 6, columns = 9;
        private boolean closeable = true;
        private InventoryManager manager;
        private InventoryProvider provider;
        private SmartInventory parent;

        private final List<InventoryListener<? extends Event>> listeners = new ArrayList<>();

        private Builder() {}

        public Builder id(String id)
        {
            this.id = id;
            return this;
        }

        public Builder title(String title)
        {
            this.title = title;
            return this;
        }

        public Builder type(InventoryType type)
        {
            this.type = type;
            return this;
        }

        public Builder size(int rows, int columns)
        {
            this.rows = rows;
            this.columns = columns;
            return this;
        }

        public Builder closeable(boolean closeable)
        {
            this.closeable = closeable;
            return this;
        }

        public Builder provider(InventoryProvider provider)
        {
            this.provider = provider;
            return this;
        }

        public Builder parent(SmartInventory parent)
        {
            this.parent = parent;
            return this;
        }

        public Builder listener(InventoryListener<? extends Event> listener)
        {
            this.listeners.add(listener);
            return this;
        }

        public Builder manager(InventoryManager manager)
        {
            this.manager = manager;
            return this;
        }

        public SmartInventory build()
        {
            if(this.provider == null)
                throw new IllegalStateException("The provider of the SmartInventory.Builder must be set.");

            final InventoryManager manager = this.manager != null ? this.manager : PluginProviders.getProvider(IFistinAPIProvider.class).smartInvsManager();
            final SmartInventory inv = new SmartInventory(manager);
            inv.id = this.id;
            inv.title = this.title;
            inv.type = this.type;
            inv.rows = this.rows;
            inv.columns = this.columns;
            inv.closeable = this.closeable;
            inv.provider = this.provider;
            inv.parent = this.parent;
            inv.listeners = this.listeners;

            return inv;
        }
    }

}