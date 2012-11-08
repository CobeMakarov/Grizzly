package net.cobem.grizzly.events.rooms;

import net.cobem.grizzly.events.Event;
import net.cobem.grizzly.events.EventRequest;
import net.cobem.grizzly.habbohotel.rooms.Room;
import net.cobem.grizzly.habbohotel.sessions.Session;

public class ChangeStateEvent implements Event
{

	@Override
	public void Parse(Session Session, EventRequest Request)
	{
		if(!Session.GrabActor().CurrentRoom.GrabRightHolders().contains(Session.GrabHabbo().ID))
		{
			return;
		}
		
		Room mRoom = Session.GrabActor().CurrentRoom;
		
		int ID = Request.PopInt();
	}

}
