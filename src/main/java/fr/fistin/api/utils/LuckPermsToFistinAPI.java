package fr.fistin.api.utils;

import fr.fistin.api.plugin.PlayerGrade;
import org.bukkit.entity.Player;

public class LuckPermsToFistinAPI
{
    public static PlayerGrade getGradeForPlayer(Player player)
    {
        return getGradeForPlayer(player.getName());
    }

    public static PlayerGrade getGradeForPlayer(String playerName)
    {
        // TODO use LuckPermsAPI.
        return PlayerGrade.NORMAL;
    }
}
