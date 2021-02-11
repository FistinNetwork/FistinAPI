package fr.fistin.api.plugin;

import fr.fistin.api.utils.IIdentifiable;

public enum PlayerGrade implements IIdentifiable
{
    NORMAL("normal", 0),
    VIP("vip", 1),
    STAFF("staff", 2),
    ADMIN("admin", 3);

    private final String name;
    private final int id;

    PlayerGrade(String name, int id)
    {
        this.name = name;
        this.id = id;
    }

    @Override
    public String getName()
    {
        return this.name;
    }

    @Override
    public int getID()
    {
        return this.id == this.ordinal() ? this.ordinal() : -this.id;
    }
}
