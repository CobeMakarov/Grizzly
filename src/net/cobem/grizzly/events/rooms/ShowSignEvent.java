package net.cobem.grizzly.events.rooms;

import net.cobem.grizzly.events.Event;
import net.cobem.grizzly.events.EventRequest;
import net.cobem.grizzly.habbohotel.sessions.Session;

public class ShowSignEvent implements Event
{

	@Override
	public void Parse(Session Session, EventRequest Request)
	{
		int Sign = Request.PopInt();
		
		Session.GrabActor().UpdateStatus("sign " + Sign);
		Session.GrabActor().SignTimer = 5;
	}

}
