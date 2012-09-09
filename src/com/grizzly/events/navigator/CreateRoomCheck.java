package com.grizzly.events.navigator;

import com.grizzly.events.Event;
import com.grizzly.events.EventRequest;
import com.grizzly.events.composers.ComposerLibrary;
import com.grizzly.habbohotel.sessions.Session;

public class CreateRoomCheck  implements Event
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
