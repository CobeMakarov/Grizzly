package com.grizzly.events.rooms;

import com.grizzly.Grizzly;
import com.grizzly.events.Event;
import com.grizzly.events.EventRequest;
import com.grizzly.events.composers.ComposerLibrary;
import com.grizzly.habbohotel.sessions.Session;

public class EndTypingEvent implements Event
{
	@Override
	public void Parse(Session Session, EventRequest Request) 
	{
		Session.GrabResponse().Initialize(ComposerLibrary.TypingToggle);
		Session.GrabResponse().AppendInt32(Session.GrabHabbo().ID);
		Session.GrabResponse().AppendInt32(0);
		Session.GrabRoomUser().CurrentRoom.SendMessage(Session.GrabResponse());
		
		Grizzly.WriteOut("stop typin");
	}
}
