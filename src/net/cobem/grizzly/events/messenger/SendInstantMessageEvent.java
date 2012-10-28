package net.cobem.grizzly.events.messenger;

import net.cobem.grizzly.Grizzly;
import net.cobem.grizzly.events.Event;
import net.cobem.grizzly.events.EventRequest;
import net.cobem.grizzly.events.composers.ComposerLibrary;
import net.cobem.grizzly.habbohotel.sessions.Session;
import net.cobem.grizzly.utils.UserInputFilter;

public class SendInstantMessageEvent implements Event
{
	@Override
	public void Parse(Session Session, EventRequest Request)
	{
		int Reciever = Request.PopInt();
		
		String Message = UserInputFilter.filterString(Request.PopFixedString(), true);
		
		if ((Grizzly.GrabHabboHotel().GrabSessionHandler().GrabSessionByUserID(Reciever)) != null)
		{
			Session.GrabResponse().Initialize(ComposerLibrary.TalkOnChat);
			Session.GrabResponse().AppendInt32(Session.GrabHabbo().ID);
			Session.GrabResponse().AppendString(Message);
			Session.GrabResponse().AppendString("");
			Grizzly.GrabHabboHotel().GrabSessionHandler().GrabSessionByUserID(Reciever).SendResponse(Session.ReturnResponse());
		}
		else
		{
			Session.GrabResponse().Initialize(ComposerLibrary.TalkOnChat);
			Session.GrabResponse().AppendInt32(Reciever);
			Session.GrabResponse().AppendString("Your friend is offline!");
			Session.GrabResponse().AppendString("");
			Session.SendResponse();
		}
	}
}
