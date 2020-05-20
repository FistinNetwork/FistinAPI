package fr.flowarg.fistinnetwork.api.plugin;

import javax.annotation.Nullable;

public interface IRoundProvider extends IAPIComponent
{
	@Nullable
	IRound getCurrentRound();
	void setCurrentRound(IRound round);
}
