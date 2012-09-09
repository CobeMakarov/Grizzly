package com.grizzly.events.user;

import com.grizzly.Grizzly;
import com.grizzly.events.Event;
import com.grizzly.events.EventRequest;
import com.grizzly.events.EventResponse;
import com.grizzly.events.composers.ComposerLibrary;
import com.grizzly.habbohotel.sessions.Session;
import com.grizzly.habbohotel.users.User;
import com.grizzly.habbohotel.users.UserHandler;

public class LoadUserProfile implements Event
{
	@Override
	public void Parse(Session Session, EventRequest Request) throws Exception 
	{
		Integer Id = Request.PopInt(); //User's ID
		
		User Habbo = UserHandler.GenerateUser(Id);
		
		if (Habbo.GrabMessenger() == null)
		{
			Habbo.InitMessenger();
		}
		
		EventResponse Message = new EventResponse();

		Message.Initialize(ComposerLibrary.LoadProfile);
		Message.AppendInt32(Habbo.ID);
		Message.AppendString(Habbo.Username);
		Message.AppendString(Habbo.Look);
		Message.AppendString(Habbo.Motto);
		Message.AppendString("1-1-1969");
		Message.AppendInt32(0);			// Achievement Score
		Message.AppendInt32(Habbo.GrabMessenger().GrabFriends().size()); 		// Friend Count
		Message.AppendBoolean(Habbo.GrabMessenger().IsFriend(Session.GrabHabbo().ID)); 	// Is friend
		Message.AppendBoolean(false); 	// Friend request sent
		Message.AppendBoolean((Grizzly.GrabHabboHotel().GrabSessionHandler().GrabSession(Session.GrabChannel()) != null)); 	// Is online		// ?
		Message.AppendInt32(0);
		
		Message.AppendInt32(0); 
		Message.AppendBoolean(true); // enable profile
		
		Session.SendResponse(Message);
	}
}
