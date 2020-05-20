package fr.flowarg.fistinnetwork.api.plugin;

public interface IGame extends IAPIComponent
{
	void start();
	void time();
	void abort();
	void stop();
}
