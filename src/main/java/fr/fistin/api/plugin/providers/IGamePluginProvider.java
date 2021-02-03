package fr.fistin.api.plugin.providers;

import fr.fistin.api.plugin.IPlayerGrade;

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
}
