package fr.fistin.api.impl.smartinvs;

import fr.fistin.api.smartinvs.ClickableItem;
import fr.fistin.api.smartinvs.SmartInventory;
import fr.fistin.api.smartinvs.content.InventoryContents;
import fr.fistin.api.smartinvs.content.Pagination;
import fr.fistin.api.smartinvs.content.SlotIterator;
import fr.fistin.api.smartinvs.content.SlotPos;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.ApiStatus;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@ApiStatus.Internal
public class InventoryContentsImpl implements InventoryContents
{
    private final SmartInventory inv;
    private final UUID player;
    private final ClickableItem[][] contents;
    private final Pagination pagination = new PaginationImpl();
    private final Map<String, SlotIterator> iterators = new HashMap<>();
    private final Map<String, Object> properties = new HashMap<>();

    public InventoryContentsImpl(SmartInventory inv, UUID player)
    {
        this.inv = inv;
        this.player = player;
        this.contents = new ClickableItem[inv.getRows()][inv.getColumns()];
    }

    @Override
    public SmartInventory inventory()
    {
        return this.inv;
    }

    @Override
    public Pagination pagination()
    {
        return this.pagination;
    }

    @Override
    public Optional<SlotIterator> iterator(String id)
    {
        return Optional.ofNullable(this.iterators.get(id));
    }

    @Override
    public SlotIterator newIterator(String id, SlotIterator.Type type, int startRow, int startColumn)
    {
        final SlotIterator iterator = new SlotIteratorImpl(this, inv, type, startRow, startColumn);
        this.iterators.put(id, iterator);
        return iterator;
    }

    @Override
    public SlotIterator newIterator(String id, SlotIterator.Type type, SlotPos startPos)
    {
        return this.newIterator(id, type, startPos.getRow(), startPos.getColumn());
    }

    @Override
    public SlotIterator newIterator(SlotIterator.Type type, int startRow, int startColumn)
    {
        return new SlotIteratorImpl(this, inv, type, startRow, startColumn);
    }

    @Override
    public SlotIterator newIterator(SlotIterator.Type type, SlotPos startPos)
    {
        return this.newIterator(type, startPos.getRow(), startPos.getColumn());
    }

    @Override
    public ClickableItem[][] all()
    {
        return this.contents;
    }

    @Override
    public Optional<SlotPos> firstEmpty()
    {
        for (int row = 0; row < contents.length; row++)
        {
            for (int column = 0; column < contents[0].length; column++)
            {
                if (!this.get(row, column).isPresent()) return Optional.of(new SlotPos(row, column));
            }
        }

        return Optional.empty();
    }

    @Override
    public Optional<ClickableItem> get(int row, int column)
    {
        if (row >= contents.length) return Optional.empty();
        if (column >= contents[row].length) return Optional.empty();

        return Optional.ofNullable(contents[row][column]);
    }

    @Override
    public Optional<ClickableItem> get(SlotPos slotPos)
    {
        return get(slotPos.getRow(), slotPos.getColumn());
    }

    @Override
    public InventoryContents set(int row, int column, ClickableItem item)
    {
        if (row >= contents.length) return this;
        if (column >= contents[row].length) return this;

        contents[row][column] = item;
        this.update(row, column, item != null ? item.getItem() : null);
        return this;
    }

    @Override
    public InventoryContents set(SlotPos slotPos, ClickableItem item)
    {
        return this.set(slotPos.getRow(), slotPos.getColumn(), item);
    }

    @Override
    public InventoryContents add(ClickableItem item)
    {
        for (int row = 0; row < contents.length; row++)
        {
            for (int column = 0; column < contents[0].length; column++)
            {
                if (contents[row][column] == null)
                {
                    this.set(row, column, item);
                    return this;
                }
            }
        }
        return this;
    }

    @Override
    public InventoryContents fill(ClickableItem item)
    {
        for (int row = 0; row < contents.length; row++)
            for (int column = 0; column < contents[row].length; column++)
                this.set(row, column, item);

        return this;
    }

    @Override
    public InventoryContents fillRow(int row, ClickableItem item)
    {
        if (row >= contents.length) return this;

        for (int column = 0; column < contents[row].length; column++)
            this.set(row, column, item);

        return this;
    }

    @Override
    public InventoryContents fillColumn(int column, ClickableItem item)
    {
        for (int row = 0; row < contents.length; row++)
            this.set(row, column, item);

        return this;
    }

    @Override
    public InventoryContents fillBorders(ClickableItem item)
    {
        this.fillRect(0, 0, inv.getRows() - 1, inv.getColumns() - 1, item);
        return this;
    }

    @Override
    public InventoryContents fillCorners(ClickableItem item)
    {
        for (int i = 0; i < 2; i++)
        {
            this.set(i, 8, item);
            this.set(i, 0, item);

            if (i == 0)
            {
                this.set(i, 7, item);
                this.set(i, 1, item);
            }
        }

        for (int i = 4; i < 6; i++)
        {
            this.set(i, 0, item);
            this.set(i, 8, item);

            if (i == 5)
            {
                this.set(i, 1, item);
                this.set(i, 7, item);
            }
        }

        return this;
    }

    @Override
    public InventoryContents fillRect(int fromRow, int fromColumn, int toRow, int toColumn, ClickableItem item)
    {
        for (int row = fromRow; row <= toRow; row++)
        {
            for (int column = fromColumn; column <= toColumn; column++)
            {
                if (row != fromRow && row != toRow && column != fromColumn && column != toColumn) continue;

                this.set(row, column, item);
            }
        }

        return this;
    }

    @Override
    public InventoryContents fillRect(SlotPos fromPos, SlotPos toPos, ClickableItem item)
    {
        return this.fillRect(fromPos.getRow(), fromPos.getColumn(), toPos.getRow(), toPos.getColumn(), item);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T property(String name)
    {
        return (T)this.properties.get(name);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T property(String name, T def)
    {
        return this.properties.containsKey(name) ? (T)this.properties.get(name) : def;
    }

    @Override
    public InventoryContents setProperty(String name, Object value)
    {
        this.properties.put(name, value);
        return this;
    }

    private void update(int row, int column, ItemStack item)
    {
        if (!this.inv.getManager().getOpenedPlayers(this.inv).contains(this.player)) return;

        final Inventory topInventory = Bukkit.getPlayer(this.player).getOpenInventory().getTopInventory();
        topInventory.setItem(this.inv.getColumns() * row + column, item);
    }
}
