package net.cobem.grizzly.events;

import net.cobem.grizzly.habbohotel.sessions.Session;

public interface Event 
{
	void Parse(Session Session, EventRequest Request) throws Exception;
}
