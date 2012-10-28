package net.cobem.grizzly.events.rooms;

import net.cobem.grizzly.events.Event;
import net.cobem.grizzly.events.EventRequest;
import net.cobem.grizzly.events.composers.ComposerLibrary;
import net.cobem.grizzly.habbohotel.sessions.Session;

public class EndTypingEvent implements Event
{
	@Override
	public void Parse(Session Session, EventRequest Request) 
	{
		Session.GrabResponse().Initialize(ComposerLibrary.TypingToggle);
		Session.GrabResponse().AppendInt32(Session.GrabHabbo().ID);
		Session.GrabResponse().AppendInt32(0);
		Session.GrabActor().CurrentRoom.SendMessage(Session.GrabResponse());
	}
}
