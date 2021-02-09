package fr.fistin.api.plugin.impl;

import fr.fistin.api.plugin.IPluginType;

public enum PluginTypes implements IPluginType
{
    GAME("game", 0),
    UTILITY("utility", 1);

    private final String name;
    private final int id;

    PluginTypes(String name, int id)
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
