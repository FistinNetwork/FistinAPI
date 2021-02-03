package fr.fistin.api.plugin.impl;

import fr.fistin.api.plugin.IPlayerGrade;

public enum PlayerGrades implements IPlayerGrade
{
    NORMAL("normal", 0),
    MVP("mvp", 1),
    STAFF("staff", 2),
    ADMIN("admin", 3);

    private final String name;
    private final int id;

    PlayerGrades(String name, int id)
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
