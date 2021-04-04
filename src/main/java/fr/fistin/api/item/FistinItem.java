package fr.fistin.api.item;

import fr.fistin.api.utils.PluginLocation;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

public class FistinItem
{
    private final String displayName;
    private final ItemStack enclosingItem;
    private final PluginLocation location;

    public FistinItem(PluginLocation location, String displayName, Supplier<ItemStack> enclosingItem)
    {
        this.location = location;
        this.displayName = displayName;
        this.enclosingItem = enclosingItem.get();

        final ItemMeta meta = this.enclosingItem.getItemMeta();
        meta.setDisplayName(this.displayName);

        final String finalLocationPath = "Location: " + location.getFinalPath();
        if(meta.hasLore() && !meta.getLore().contains(finalLocationPath))
        {
            final List<String> lore = meta.getLore();
            lore.addAll(Collections.singletonList(finalLocationPath));
            meta.setLore(lore);
        }
        else meta.setLore(Collections.singletonList(finalLocationPath));
        this.enclosingItem.setItemMeta(meta);
    }

    public FistinItem(PluginLocation location, String displayName, Material enclosingItemMaterial)
    {
        this(location, displayName, () -> new ItemStack(enclosingItemMaterial));
    }

    public void onItemRightClick(@NotNull Player player, @NotNull ClickType type, @Nullable Block clickedBlock) {}

    public void onItemLeftClick(@NotNull Player player, @NotNull ClickType type, @Nullable Block clickedBlock) {}

    public String displayName()
    {
        return this.displayName;
    }

    public ItemStack enclosingItem()
    {
        return new ItemStack(this.enclosingItem);
    }

    public Material enclosingItemType()
    {
        return this.enclosingItem.getType();
    }

    public PluginLocation location()
    {
        return this.location;
    }

    public enum ClickType
    {
        AIR,
        BLOCK
    }
}
