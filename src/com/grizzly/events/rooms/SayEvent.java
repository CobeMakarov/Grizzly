package com.grizzly.events.rooms;

import com.grizzly.Grizzly;
import com.grizzly.events.Event;
import com.grizzly.events.EventRequest;
import com.grizzly.events.EventResponse;
import com.grizzly.events.composers.ComposerLibrary;
import com.grizzly.habbohotel.sessions.Session;

public class SayEvent implements Event
{
	@Override
	public void Parse(Session Session, EventRequest Request)
	{
		if (!Session.GrabRoomUser().InRoom())
		{
			return;
		}
		
		String Str = Request.PopFixedString();
		
		if (Str == "o/")
		{
			(new WaveEvent()).Parse(Session, Request);
		}
		
		if (Str.startsWith(":"))
		{
			if (Grizzly.GrabHabboHotel().GrabChatCommandParser().ParseChatCommand(Session, Str.replace(":", "")))
			{
				return;
			}
		}
		
		EventResponse Message = new EventResponse();
		
		Message.Initialize(ComposerLibrary.Chat);
		Message.AppendInt32(Session.GrabHabbo().ID);
		Message.AppendString(Str);
		Message.AppendInt32(Grizzly.GrabHabboHotel().ParseSmile(Str));
		Message.AppendInt32(0);
		Message.AppendInt32(0);
		
		for(Session User : Session.GrabRoomUser().CurrentRoom.GrabRoomUsers().values())
		{
			User.SendResponse(Message);
		}
		
		//(new EndTypingEvent()).Parse(Session, Request);
	}
}
