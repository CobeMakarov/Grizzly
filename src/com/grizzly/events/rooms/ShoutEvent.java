package com.grizzly.events.rooms;

import com.grizzly.Grizzly;
import com.grizzly.events.Event;
import com.grizzly.events.EventRequest;
import com.grizzly.events.EventResponse;
import com.grizzly.events.composers.ComposerLibrary;
import com.grizzly.habbohotel.sessions.Session;

public class ShoutEvent implements Event
{

	@Override
	public void Parse(Session Session, EventRequest Request)
	{
		if (!Session.GrabRoomUser().InRoom())
		{
			return;
		}
		
		String Str = Request.PopFixedString();
		
		EventResponse Message = new EventResponse();
		
		Message.Initialize(ComposerLibrary.Shout);
		Message.AppendInt32(Session.GrabHabbo().ID);
		Message.AppendString(Str);
		Message.AppendInt32(Grizzly.GrabHabboHotel().ParseSmile(Str));
		Message.AppendInt32(0);
		Message.AppendInt32(0);
		
		for(Session User : Session.GrabRoomUser().CurrentRoom.GrabRoomUsers().values())
		{
			User.SendResponse(Message);
		}
	}

}
