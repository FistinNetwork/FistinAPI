package fr.fistin.api.item;

import fr.fistin.api.utils.PluginLocation;

import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

public interface IFistinItems
{
    void registerItem(Supplier<? extends FistinItem> itemSup);
    FistinItem getItem(PluginLocation location);
    Set<String> getRegisteredItemsName();
    Map<String, PluginLocation> nameToLocation();
    void clear();
}
