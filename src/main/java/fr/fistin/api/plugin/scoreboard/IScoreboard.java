package fr.fistin.api.plugin.scoreboard;

public interface IScoreboard
{
    IScoreboardSign getScoreboardSign();
    void setScoreboardSign(IScoreboardSign scoreboardSign);
    void createScoreboard();
    void updateScoreboard();
    void destroy();
}
