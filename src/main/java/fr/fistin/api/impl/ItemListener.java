package fr.fistin.api.impl;

import fr.fistin.api.item.FistinItem;
import fr.fistin.api.item.FistinItem.ClickType;
import fr.fistin.api.item.IFistinItems;
import fr.fistin.api.plugin.providers.IFistinAPIProvider;
import fr.fistin.api.plugin.providers.PluginProviders;
import fr.fistin.api.utils.PluginLocation;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

final class ItemListener implements Listener
{
    @EventHandler(priority = EventPriority.HIGH)
    public void onItemClickEvent(PlayerInteractEvent event)
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
                    if(fistinItem != null && item.getType() == fistinItem.enclosingItemType())
                    {
                        final Action action = event.getAction();
                        if(action != Action.PHYSICAL)
                        {
                            final ClickType clickType = action == Action.RIGHT_CLICK_AIR || action == Action.LEFT_CLICK_AIR ? ClickType.AIR : ClickType.BLOCK;
                            final Player player = event.getPlayer();
                            final Block block = event.getClickedBlock();

                            if(action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK)
                                fistinItem.onItemRightClick(player, clickType, block);
                            else if(action == Action.LEFT_CLICK_AIR || action == Action.LEFT_CLICK_BLOCK)
                                fistinItem.onItemLeftClick(player, clickType, block);
                        }
                    }
                }
            }
        }
    }
}
