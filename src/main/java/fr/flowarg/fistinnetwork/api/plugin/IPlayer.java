package fr.flowarg.fistinnetwork.api.plugin;

import org.bukkit.entity.Player;

public interface IPlayer extends IScoreboardProvider, IRoundProvider
{
	Player getPlayer();
	void setPlayer(Player player);
}
