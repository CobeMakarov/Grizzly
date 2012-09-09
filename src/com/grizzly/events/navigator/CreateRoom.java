package com.grizzly.events.navigator;

import com.grizzly.Grizzly;
import com.grizzly.events.Event;
import com.grizzly.events.EventRequest;
import com.grizzly.events.composers.ComposerLibrary;
import com.grizzly.habbohotel.sessions.Session;

public class CreateRoom implements Event
{
	@Override
	public void Parse(Session Session, EventRequest Request)
	{
		String Name = Request.PopFixedString();
		String Model = Request.PopFixedString();

		int Owner = Session.GrabHabbo().ID;
		
		int RoomID = Grizzly.GrabHabboHotel().GrabRoomHandler().CreateRoom(Name, Owner, Model);
		
		Session.GrabResponse().Initialize(ComposerLibrary.SendRoom);
		Session.GrabResponse().AppendInt32(RoomID);
		Session.GrabResponse().AppendString(Name);
		Session.SendResponse();
	}
}
