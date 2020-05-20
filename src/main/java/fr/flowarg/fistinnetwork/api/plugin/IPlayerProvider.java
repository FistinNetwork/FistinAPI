package fr.flowarg.fistinnetwork.api.plugin;

public interface IPlayerProvider extends IAPIComponent
{
	IPlayer getPlayer();
	void setPlayer(IPlayer player);
}
