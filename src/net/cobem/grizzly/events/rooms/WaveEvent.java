package net.cobem.grizzly.events.rooms;

import net.cobem.grizzly.events.Event;
import net.cobem.grizzly.events.EventRequest;
import net.cobem.grizzly.events.composers.ComposerLibrary;
import net.cobem.grizzly.habbohotel.sessions.Session;

public class WaveEvent implements Event
{
	@Override
	public void Parse(Session Session, EventRequest Request)
	{
		Session.GrabResponse().Initialize(ComposerLibrary.Wave);
		Session.GrabResponse().AppendInt32(Session.GrabHabbo().ID);
		Session.GrabResponse().AppendInt32(Request.PopInt());
		Session.GrabActor().CurrentRoom.SendMessage(Session.GrabResponse());
	}
}
