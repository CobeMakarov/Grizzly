package com.grizzly.events.rooms;

import com.grizzly.events.Event;
import com.grizzly.events.EventRequest;
import com.grizzly.events.composers.ComposerLibrary;
import com.grizzly.habbohotel.sessions.Session;

public class WaveEvent implements Event
{
	@Override
	public void Parse(Session Session, EventRequest Request)
	{
		Session.GrabResponse().Initialize(ComposerLibrary.Wave);
		Session.GrabResponse().AppendInt32(Session.GrabHabbo().ID);
		Session.GrabRoomUser().CurrentRoom.SendMessage(Session.GrabResponse());
	}
}
