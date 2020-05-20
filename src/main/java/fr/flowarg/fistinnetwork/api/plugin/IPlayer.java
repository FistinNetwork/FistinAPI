package fr.flowarg.fistinnetwork.api.plugin;

import org.bukkit.entity.Player;

public interface IPlayer extends IScoreboardProvider, IRoundProvider, IAPIComponent
{
	Player getPlayer();
	void setPlayer(Player player);
}
