package fr.fistin.api.plugin.providers;

import fr.fistin.api.plugin.PlayerGrade;
import fr.fistin.api.plugin.PluginType;

public interface IGamePluginProvider extends IPluginProvider
{
    int xpForWin();
    int xpForLoose();
    int coinsForWin();
    int coinsForLoose();
    boolean canWinPointsOnWin();
    boolean canWinPointsOnLoose();
    boolean canWinXpOnWin();
    boolean canWinXpOnLoose();
    int gradeMultiplier(PlayerGrade grade);

    @Override
    default PluginType getPluginType()
    {
        return PluginType.GAME;
    }
}
