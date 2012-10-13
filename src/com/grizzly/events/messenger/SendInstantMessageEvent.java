package com.grizzly.events.messenger;

import com.grizzly.Grizzly;
import com.grizzly.events.Event;
import com.grizzly.events.EventRequest;
import com.grizzly.events.composers.ComposerLibrary;
import com.grizzly.habbohotel.sessions.Session;
import com.grizzly.utils.UserInputFilter;

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
