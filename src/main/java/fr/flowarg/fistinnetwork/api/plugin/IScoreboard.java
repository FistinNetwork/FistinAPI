package fr.flowarg.fistinnetwork.api.plugin;

public interface IScoreboard
{
	ScoreboardSign getScoreboardSign();
	void setScoreboardSign(ScoreboardSign scoreboardSign);
	void createScoreboard();
	void updateScoreboard();
	void destroy();
}
