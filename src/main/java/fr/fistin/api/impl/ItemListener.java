package fr.fistin.api.impl;

import fr.fistin.api.item.FistinItem;
import fr.fistin.api.item.IFistinItems;
import fr.fistin.api.plugin.providers.IFistinAPIProvider;
import fr.fistin.api.plugin.providers.PluginProviders;
import fr.fistin.api.utils.PluginLocation;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

class ItemListener implements Listener
{
    @EventHandler(priority = EventPriority.HIGH)
    public void onItemClickEvent(PlayerInteractEvent event)
    {
        if(event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK)
        {
            final ItemStack item = event.getItem();
            if(item != null && item.getType() != Material.AIR)
            {
                final String name = item.getItemMeta().getDisplayName();
                final IFistinItems items = PluginProviders.getProvider(IFistinAPIProvider.class).items();

                if(name != null && items.getRegisteredItemsName().contains(name))
                {
                    final PluginLocation location = items.nameToLocation().get(name);
                    if(location != null && item.getItemMeta().getLore().contains("Location: " + location.getFinalPath()))
                    {
                        final FistinItem fistinItem = items.getItem(location);
                        if(item.getType() == fistinItem.enclosingItemType())
                            fistinItem.onItemRightClick(event.getPlayer(), event.getAction() == Action.RIGHT_CLICK_AIR ? FistinItem.ClickType.AIR : FistinItem.ClickType.BLOCK, event.getClickedBlock());
                    }
                }
            }
        }
    }
}
