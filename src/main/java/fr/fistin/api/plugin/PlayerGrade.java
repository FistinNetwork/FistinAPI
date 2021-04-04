package fr.fistin.api.plugin;

import fr.fistin.api.utils.IIdentifiable;
import org.jetbrains.annotations.NotNull;

/**
 * Represent all grades in Fistin.
 */
public enum PlayerGrade implements IIdentifiable
{
    NORMAL("fistin.normal", 0),
    VIP("fistin.vip", 1),
    STAFF("fistin.staff", 2),
    ADMIN("fistin.admin", 3);

    private final String name;
    private final int id;

    PlayerGrade(String name, int id)
    {
        this.name = name;
        this.id = id;
    }

    @Override
    public @NotNull String getName()
    {
        return this.name;
    }

    @Override
    public int getID()
    {
        return this.id == this.ordinal() ? this.ordinal() : -this.id;
    }
}
