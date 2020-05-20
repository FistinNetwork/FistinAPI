package fr.flowarg.fistinnetwork.api.plugin;

public interface IGameProvider extends IAPIComponent
{
	IGame getGame();
	void setGame(IGame game);
}
