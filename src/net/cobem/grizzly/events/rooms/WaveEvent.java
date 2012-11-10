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
		/*
		 * Wave = 1
		 * Blow Kiss = 2
		 * Laugh = 3
		 * Hopstep = 6 (wdf is this)
		 * Thumbs Up = 7
		 */
		int Whatever = Request.PopInt();
		
		Session.GrabResponse().Initialize(ComposerLibrary.Wave);
		Session.GrabResponse().AppendInt32(Session.GrabHabbo().ID);
		Session.GrabResponse().AppendInt32(Whatever);
		Session.GrabActor().CurrentRoom.SendMessage(Session.GrabResponse());
	}
}
