package fr.fistin.api.utils;

import fr.fistin.api.plugin.IPlayerGrade;
import fr.fistin.api.plugin.impl.PlayerGrades;
import org.bukkit.entity.Player;

public class LuckPermsToFistinAPI
{
    public static IPlayerGrade getGradeForPlayer(Player player)
    {
        return getGradeForPlayer(player.getName());
    }

    public static IPlayerGrade getGradeForPlayer(String playerName)
    {
        // TODO use LuckPermsAPI.
        return PlayerGrades.NORMAL;
    }
}
