package net.cobem.grizzly.events.user;

import net.cobem.grizzly.Grizzly;
import net.cobem.grizzly.events.Event;
import net.cobem.grizzly.events.EventRequest;
import net.cobem.grizzly.events.EventResponse;
import net.cobem.grizzly.events.composers.ComposerLibrary;
import net.cobem.grizzly.habbohotel.sessions.Session;
import net.cobem.grizzly.habbohotel.users.User;
import net.cobem.grizzly.habbohotel.users.UserHandler;

public class LoadUserProfileEvent implements Event
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
