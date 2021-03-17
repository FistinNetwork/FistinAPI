package fr.fistin.api.plugin.impl;

import fr.fistin.api.item.FistinItem;
import fr.fistin.api.item.IFistinItems;
import fr.fistin.api.plugin.providers.IFistinAPIProvider;
import fr.fistin.api.plugin.providers.PluginProviders;
import fr.fistin.api.utils.PluginLocation;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class FistinItemsImpl implements IFistinItems
{
    private final Map<PluginLocation, Supplier<? extends FistinItem>> items = new HashMap<>();

    @Override
    public void registerItem(PluginLocation location, Supplier<? extends FistinItem> item)
    {
        if(!this.items.containsKey(location))
        {
            PluginProviders.getProvider(IFistinAPIProvider.class).getLogger().info("Registered new item with id (" + location.getFinalPath() + ')');
            this.items.put(location, item);
        }
    }

    @Override
    public Supplier<? extends FistinItem> getItem(PluginLocation location)
    {
        return this.items.get(location);
    }

    @Override
    public Set<String> getRegisteredItemsName()
    {
        return this.items.values().stream().map(supplier -> supplier.get().displayName()).collect(Collectors.toSet());
    }
}
