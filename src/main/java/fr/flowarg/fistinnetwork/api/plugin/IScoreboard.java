package fr.flowarg.fistinnetwork.api.plugin;

public interface IScoreboard extends IAPIComponent
{
	ScoreboardSign getScoreboardSign();
	void setScoreboardSign(ScoreboardSign scoreboardSign);
	void createScoreboard();
	void updateScoreboard();
	void destroy();
}
