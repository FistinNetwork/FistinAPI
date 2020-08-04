package fr.flowarg.fistinnetwork.api.plugin;

public interface IRound
{
	int getCount();
	void startRound();
	void endRound();
	int getTimeLeft();
}
