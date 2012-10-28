package net.cobem.grizzly.events.rooms;

import net.cobem.grizzly.Grizzly;
import net.cobem.grizzly.events.Event;
import net.cobem.grizzly.events.EventRequest;
import net.cobem.grizzly.events.composers.ComposerLibrary;
import net.cobem.grizzly.habbohotel.sessions.Session;
import net.cobem.grizzly.habbohotel.users.GenderType;

public class ChangeLooksEvent implements Event
{

	@Override
	public void Parse(Session Session, EventRequest Request)
	{
		GenderType Gender = GenderType.valueOf(Request.PopFixedString());
		String Look = Request.PopFixedString();
		
		Session.GrabResponse().Initialize(ComposerLibrary.UpdateInfo);
		Session.GrabResponse().AppendInt32(Session.GrabHabbo().ID);
		Session.GrabResponse().AppendString(Look);
		Session.GrabResponse().AppendString(Gender.toString().toLowerCase());
		Session.GrabResponse().AppendString(Session.GrabHabbo().Motto);
		Session.GrabResponse().AppendInt32(0); 
		Session.GrabActor().CurrentRoom.SendMessage(Session.GrabResponse());
		
		Session.GrabResponse().Initialize(ComposerLibrary.UpdateInfo);
		Session.GrabResponse().AppendInt32(-1);
		Session.GrabResponse().AppendString(Look);
		Session.GrabResponse().AppendString(Gender.toString().toLowerCase());
		Session.GrabResponse().AppendString(Session.GrabHabbo().Motto);
		Session.GrabResponse().AppendInt32(0); 
		Session.GrabActor().CurrentRoom.SendMessage(Session.GrabResponse());
		
		Session.GrabHabbo().Gender = Gender;
		Session.GrabHabbo().Look = Look;
		
		Grizzly.GrabDatabase().RunFastQuery("UPDATE server_users SET gender = '" + 
		Session.GrabHabbo().Gender.toString() + "', look = '" + 
		Session.GrabHabbo().Look + "' WHERE id = '" + 
		Session.GrabHabbo().ID + "'");
	}

}
