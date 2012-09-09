package com.grizzly.events;

import com.grizzly.habbohotel.sessions.Session;

public interface Event 
{
	void Parse(Session Session, EventRequest Request) throws Exception;
}
