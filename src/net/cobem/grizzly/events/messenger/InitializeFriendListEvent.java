package net.cobem.grizzly.events.messenger;

import net.cobem.grizzly.Grizzly;
import net.cobem.grizzly.events.Event;
import net.cobem.grizzly.events.EventRequest;
import net.cobem.grizzly.events.composers.ComposerLibrary;
import net.cobem.grizzly.habbohotel.sessions.Session;
import net.cobem.grizzly.habbohotel.users.User;

public class InitializeFriendListEvent implements Event
{
	@Override
	public void Parse(Session Session, EventRequest Request) throws Exception 
	{
		Session.GrabResponse().Initialize(ComposerLibrary.LoadFriends);
		Session.GrabResponse().AppendInt32(1100);
		Session.GrabResponse().AppendInt32(300);
		Session.GrabResponse().AppendInt32(800);
		Session.GrabResponse().AppendInt32(1100);
		
		Session.GrabResponse().AppendInt32(0); //Categories
		
		Session.GrabResponse().AppendInt32(Session.GrabHabbo().GrabMessenger().GrabFriends().size());
		
		for(User mUser : Session.GrabHabbo().GrabMessenger().GrabFriends().values())
		{
			Session.GrabResponse().AppendInt32(mUser.ID);
			Session.GrabResponse().AppendString(mUser.Username);
			Session.GrabResponse().AppendInt32(1);
			Session.GrabResponse().AppendBoolean((Grizzly.GrabHabboHotel().GrabSessionHandler().GrabSessionByUserID(mUser.ID) != null));
			Session.GrabResponse().AppendBoolean(true);
			Session.GrabResponse().AppendString(mUser.Look);
			Session.GrabResponse().AppendInt32(0);
			Session.GrabResponse().AppendString(mUser.Motto);
			Session.GrabResponse().AppendInt32(0);
			Session.GrabResponse().AppendBoolean(false);
		}
		
		Session.SendResponse();
		
		Session.GrabResponse().Initialize(ComposerLibrary.PendingFriends);
		Session.GrabResponse().AppendInt32(Session.GrabHabbo().GrabMessenger().GrabRequests().size());
		Session.GrabResponse().AppendInt32(Session.GrabHabbo().GrabMessenger().GrabRequests().size());
		
		for(User mUser : Session.GrabHabbo().GrabMessenger().GrabRequests().values())
		{
			Session.GrabResponse().AppendInt32(mUser.ID);
			Session.GrabResponse().AppendString(mUser.Username);
			Session.GrabResponse().AppendString(mUser.Look);
		}
		
		Session.SendResponse();
	}
}
