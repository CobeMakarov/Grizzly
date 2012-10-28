package net.cobem.grizzly.events.rooms;

import net.cobem.grizzly.Grizzly;
import net.cobem.grizzly.events.Event;
import net.cobem.grizzly.events.EventRequest;
import net.cobem.grizzly.events.composers.ComposerLibrary;
import net.cobem.grizzly.habbohotel.sessions.Session;

public class LeaveRoomEvent implements Event
{

	@Override
	public void Parse(Session Session, EventRequest Request)
	{
		if (!Session.GrabActor().InRoom())
		{
			return;
		}
		
		Session.GrabResponse().Initialize(ComposerLibrary.LeavingRoom);
		Session.GrabResponse().AppendBoolean(false);
		Session.SendResponse();
		
		if (Session.GrabActor().CurrentRoom.GrabParty().size() == 1)
		{
			Grizzly.GrabHabboHotel().GrabRoomHandler().RemoveRoom(Session.GrabActor().CurrentRoom.ID, false);
		}
		
		Session.GrabActor().CurrentRoom.RemoveUser(Session);
				
		System.gc();
	}
	
}
