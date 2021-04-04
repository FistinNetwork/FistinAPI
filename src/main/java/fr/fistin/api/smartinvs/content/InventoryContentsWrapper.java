package fr.fistin.api.smartinvs.content;

import fr.fistin.api.smartinvs.SmartInventory;

import java.util.UUID;

@FunctionalInterface
public interface InventoryContentsWrapper
{
    InventoryContents newImpl(SmartInventory inv, UUID player);
}
