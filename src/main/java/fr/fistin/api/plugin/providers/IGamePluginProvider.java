package fr.fistin.api.plugin.providers;

import fr.fistin.api.plugin.PlayerGrade;
import fr.fistin.api.plugin.PluginType;
import org.bukkit.entity.Player;

public interface IGamePluginProvider extends IPluginProvider
{
    void winGame(Player player);
    void looseGame(Player player);
    float gradeMultiplier(PlayerGrade grade);

    @Override
    default PluginType getPluginType()
    {
        return PluginType.GAME;
    }
}
