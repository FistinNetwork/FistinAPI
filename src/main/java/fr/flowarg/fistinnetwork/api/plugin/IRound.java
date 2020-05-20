package fr.flowarg.fistinnetwork.api.plugin;

public interface IRound extends IAPIComponent
{
	int getCount();
	void startRound();
	void endRound();
	int getTimeLeft();
}
