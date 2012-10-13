package com.grizzly.habbohotel.misc;

import com.grizzly.habbohotel.sessions.Session;

public interface ChatCommand 
{
	int MinimumRank();
	
	void Execute(Session mSession, String[] Arguments);
}
