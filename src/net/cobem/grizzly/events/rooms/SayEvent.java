package net.cobem.grizzly.events.rooms;

import net.cobem.grizzly.Grizzly;
import net.cobem.grizzly.events.Event;
import net.cobem.grizzly.events.EventRequest;
import net.cobem.grizzly.events.EventResponse;
import net.cobem.grizzly.events.composers.ComposerLibrary;
import net.cobem.grizzly.habbohotel.sessions.Session;

public class SayEvent implements Event
{
	@Override
	public void Parse(Session Session, EventRequest Request)
	{
		if (!Session.GrabActor().InRoom())
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
		
		Session.GrabActor().CurrentRoom.SendMessage(Message);
	}
}
