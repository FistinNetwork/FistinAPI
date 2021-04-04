package fr.fistin.api.smartinvs.content;

import fr.fistin.api.smartinvs.ClickableItem;
import fr.fistin.api.smartinvs.SmartInventory;

import java.util.Optional;

public interface InventoryContents
{
    SmartInventory inventory();
    Pagination pagination();

    Optional<SlotIterator> iterator(String id);

    SlotIterator newIterator(String id, SlotIterator.Type type, int startRow, int startColumn);
    SlotIterator newIterator(SlotIterator.Type type, int startRow, int startColumn);

    SlotIterator newIterator(String id, SlotIterator.Type type, SlotPos startPos);
    SlotIterator newIterator(SlotIterator.Type type, SlotPos startPos);

    ClickableItem[][] all();

    Optional<SlotPos> firstEmpty();

    Optional<ClickableItem> get(int row, int column);
    Optional<ClickableItem> get(SlotPos slotPos);

    InventoryContents set(int row, int column, ClickableItem item);
    InventoryContents set(SlotPos slotPos, ClickableItem item);

    InventoryContents add(ClickableItem item);

    InventoryContents fill(ClickableItem item);
    InventoryContents fillRow(int row, ClickableItem item);
    InventoryContents fillColumn(int column, ClickableItem item);
    InventoryContents fillBorders(ClickableItem item);

    InventoryContents fillCorners(ClickableItem item);

    InventoryContents fillRect(int fromRow, int fromColumn,
                               int toRow, int toColumn, ClickableItem item);
    InventoryContents fillRect(SlotPos fromPos, SlotPos toPos, ClickableItem item);

    <T> T property(String name);
    <T> T property(String name, T def);

    InventoryContents setProperty(String name, Object value);
}