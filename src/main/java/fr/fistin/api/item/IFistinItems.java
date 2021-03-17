package fr.fistin.api.item;

import fr.fistin.api.utils.PluginLocation;

import java.util.Set;
import java.util.function.Supplier;

public interface IFistinItems
{
    void registerItem(PluginLocation location, Supplier<? extends FistinItem> item);
    Supplier<? extends FistinItem> getItem(PluginLocation location);
    Set<String> getRegisteredItemsName();
}
