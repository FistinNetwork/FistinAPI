package fr.fistin.api.item;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.function.Supplier;

public class FistinItem
{
    private final String displayName;
    private final ItemStack enclosingItem;

    public FistinItem(String displayName, Supplier<ItemStack> enclosingItem)
    {
        this.displayName = displayName;
        this.enclosingItem = enclosingItem.get();
        final ItemMeta meta = this.enclosingItem.getItemMeta();
        meta.setDisplayName(this.displayName);
        this.enclosingItem.setItemMeta(meta);
    }

    public void onItemUse() {}

    public String displayName()
    {
        return this.displayName;
    }

    public ItemStack enclosingItem()
    {
        return new ItemStack(this.enclosingItem);
    }
}
