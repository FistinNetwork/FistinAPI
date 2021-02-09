package fr.fistin.api.plugin.providers;

import fr.fistin.api.plugin.IPlayerGrade;
import fr.fistin.api.plugin.IPluginType;
import fr.fistin.api.plugin.impl.PluginTypes;

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
    int gradeMultiplier(IPlayerGrade grade);

    @Override
    default IPluginType getPluginType()
    {
        return PluginTypes.GAME;
    }
}
