package fr.fistin.api.impl.smartinvs;

import fr.fistin.api.smartinvs.ClickableItem;
import fr.fistin.api.smartinvs.SmartInventory;
import fr.fistin.api.smartinvs.content.InventoryContents;
import fr.fistin.api.smartinvs.content.SlotIterator;
import fr.fistin.api.smartinvs.content.SlotPos;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

class SlotIteratorImpl implements SlotIterator
{
    private final InventoryContents contents;
    private final SmartInventory inv;
    private final Set<SlotPos> blacklisted = new HashSet<>();

    private final Type type;
    private boolean started = false;
    private boolean allowOverride = true;
    private int row, column;

    public SlotIteratorImpl(InventoryContents contents, SmartInventory inv, Type type, int startRow, int startColumn)
    {
        this.contents = contents;
        this.inv = inv;

        this.type = type;

        this.row = startRow;
        this.column = startColumn;
    }

    public SlotIteratorImpl(InventoryContents contents, SmartInventory inv, Type type)
    {
        this(contents, inv, type, 0, 0);
    }

    @Override
    public Optional<ClickableItem> get()
    {
        return this.contents.get(this.row, this.column);
    }

    @Override
    public SlotIterator set(ClickableItem item)
    {
        if (this.canPlace()) this.contents.set(this.row, this.column, item);

        return this;
    }

    @Override
    public SlotIterator previous()
    {
        if (row == 0 && column == 0)
        {
            this.started = true;
            return this;
        }

        do
        {
            if (!this.started)
            {
                this.started = true;
            }
            else
            {
                switch (type)
                {
                    case HORIZONTAL:
                        column--;

                        if (column == 0)
                        {
                            column = inv.getColumns() - 1;
                            row--;
                        }
                        break;
                    case VERTICAL:
                        row--;

                        if (row == 0)
                        {
                            row = inv.getRows() - 1;
                            column--;
                        }
                        break;
                }
            }
        } while (!canPlace() && (row != 0 || column != 0));

        return this;
    }

    @Override
    public SlotIterator next()
    {
        if (ended())
        {
            this.started = true;
            return this;
        }

        do
        {
            if (!this.started)
            {
                this.started = true;
            }
            else
            {
                switch (type)
                {
                    case HORIZONTAL:
                        column = ++column % inv.getColumns();

                        if (column == 0) row++;
                        break;
                    case VERTICAL:
                        row = ++row % inv.getRows();

                        if (row == 0) column++;
                        break;
                }
            }
        } while (!canPlace() && !ended());

        return this;
    }

    @Override
    public SlotIterator blacklist(int row, int column)
    {
        this.blacklisted.add(SlotPos.of(row, column));
        return this;
    }

    @Override
    public SlotIterator blacklist(SlotPos slotPos)
    {
        return blacklist(slotPos.getRow(), slotPos.getColumn());
    }

    @Override
    public int row() { return row; }

    @Override
    public SlotIterator row(int row)
    {
        this.row = row;
        return this;
    }

    @Override
    public int column() { return column; }

    @Override
    public SlotIterator column(int column)
    {
        this.column = column;
        return this;
    }

    @Override
    public boolean started()
    {
        return this.started;
    }

    @Override
    public boolean ended()
    {
        return row == inv.getRows() - 1 && column == inv.getColumns() - 1;
    }

    @Override
    public boolean doesAllowOverride() { return allowOverride; }

    @Override
    public SlotIterator allowOverride(boolean override)
    {
        this.allowOverride = override;
        return this;
    }

    private boolean canPlace()
    {
        return !blacklisted.contains(SlotPos.of(row, column)) && (allowOverride || !this.get().isPresent());
    }
}
