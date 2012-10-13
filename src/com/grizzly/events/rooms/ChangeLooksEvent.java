package com.grizzly.events.rooms;

import com.grizzly.Grizzly;
import com.grizzly.events.Event;
import com.grizzly.events.EventRequest;
import com.grizzly.events.composers.ComposerLibrary;
import com.grizzly.habbohotel.sessions.Session;
import com.grizzly.habbohotel.users.GenderType;

public class ChangeLooksEvent implements Event
{

	@Override
	public void Parse(Session Session, EventRequest Request)
	{
		/*GenderType Gender = GenderType.valueOf(Request.PopFixedString());
		String Look = Request.PopFixedString();
		
		Session.GrabResponse().Initialize(ComposerLibrary.UpdateInfo);
		Session.GrabResponse().AppendInt32(Session.GrabHabbo().ID);
		Session.GrabResponse().AppendString(Look);
		Session.GrabResponse().AppendString(Gender.toString().toLowerCase());
		Session.GrabResponse().AppendString(Session.GrabHabbo().Motto);
		Session.GrabResponse().AppendInt32(0); 
		Session.GrabRoomUser().CurrentRoom.SendMessage(Session.GrabResponse());
		
		Session.GrabResponse().Initialize(ComposerLibrary.UpdateInfo);
		Session.GrabResponse().AppendInt32(-1);
		Session.GrabResponse().AppendString(Look);
		Session.GrabResponse().AppendString(Gender.toString().toLowerCase());
		Session.GrabResponse().AppendString(Session.GrabHabbo().Motto);
		Session.GrabResponse().AppendInt32(0); 
		Session.GrabRoomUser().CurrentRoom.SendMessage(Session.GrabResponse());
		
		Session.GrabHabbo().Gender = Gender;
		Session.GrabHabbo().Look = Look;
		
		Grizzly.GrabDatabase().RunFastQuery("UPDATE server_users SET gender = '" + 
		Session.GrabHabbo().Gender.toString() + "', look = '" + 
		Session.GrabHabbo().Look + "' WHERE id = '" + 
		Session.GrabHabbo().ID + "'");*/
	}

}
