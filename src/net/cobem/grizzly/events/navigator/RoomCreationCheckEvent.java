package net.cobem.grizzly.events.navigator;

import net.cobem.grizzly.events.Event;
import net.cobem.grizzly.events.EventRequest;
import net.cobem.grizzly.events.composers.ComposerLibrary;
import net.cobem.grizzly.habbohotel.sessions.Session;

public class RoomCreationCheckEvent  implements Event
{
	@Override
	public void Parse(Session Session, EventRequest Request)
	{
		Session.GrabResponse().Initialize(ComposerLibrary.CanCreateNewRoom);
		Session.GrabResponse().AppendInt32(0);
		Session.GrabResponse().AppendInt32(25);
		Session.SendResponse();
	}
}
