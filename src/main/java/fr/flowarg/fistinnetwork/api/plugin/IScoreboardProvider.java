package fr.flowarg.fistinnetwork.api.plugin;

public interface IScoreboardProvider
{
	IScoreboard getScoreboard();
	void setScoreboard(IScoreboard scoreboard);
	void updateScoreboard();
	void destroyScoreboard();
	void createScoreboard();
}
