package net.cobem.grizzly.events.navigator;

import net.cobem.grizzly.Grizzly;
import net.cobem.grizzly.events.Event;
import net.cobem.grizzly.events.EventRequest;
import net.cobem.grizzly.events.composers.ComposerLibrary;
import net.cobem.grizzly.habbohotel.sessions.Session;

public class CreateRoomEvent implements Event
{
	@Override
	public void Parse(Session Session, EventRequest Request)
	{
		String Name = Request.PopFixedString();
		String Model = Request.PopFixedString();
		
		if (Name.contains("'"))
		{
			Name.replaceAll("'", "");
		}
		
		int Owner = Session.GrabHabbo().ID;
		
		int RoomID = Grizzly.GrabHabboHotel().GrabRoomHandler().CreateRoom(Name, Owner, Model);
		
		Session.GrabResponse().Initialize(ComposerLibrary.SendRoom);
		Session.GrabResponse().AppendInt32(RoomID);
		Session.GrabResponse().AppendString(Name);
		Session.SendResponse();
	}
}
