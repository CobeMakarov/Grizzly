package com.grizzly.events.rooms;

import com.grizzly.events.Event;
import com.grizzly.events.EventRequest;
import com.grizzly.events.composers.ComposerLibrary;
import com.grizzly.habbohotel.sessions.Session;

public class LeaveRoomEvent implements Event
{

	@Override
	public void Parse(Session Session, EventRequest Request)
	{
		if (!Session.GrabRoomUser().InRoom())
		{
			return;
		}
		
		Session.GrabResponse().Initialize(ComposerLibrary.LeavingRoom);
		Session.GrabResponse().AppendBoolean(false);
		Session.SendResponse();
		
		Session.GrabRoomUser().CurrentRoom.RemoveUser(Session);
	}
	
}
