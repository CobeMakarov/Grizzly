package net.cobem.grizzly.habbohotel.misc;

import net.cobem.grizzly.habbohotel.sessions.Session;

public interface ChatCommand 
{
	int MinimumRank();
	
	void Execute(Session mSession, String[] Arguments);
}
