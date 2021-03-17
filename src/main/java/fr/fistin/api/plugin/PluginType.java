package fr.fistin.api.plugin;

import fr.fistin.api.utils.IIdentifiable;
import org.jetbrains.annotations.NotNull;

public enum PluginType implements IIdentifiable
{
    GAME("game", 0),
    UTILITY("utility", 1);

    private final String name;
    private final int id;

    PluginType(String name, int id)
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
