package com.grizzly.events.messenger;

import com.grizzly.Grizzly;
import com.grizzly.events.Event;
import com.grizzly.events.EventRequest;
import com.grizzly.events.composers.ComposerLibrary;
import com.grizzly.habbohotel.sessions.Session;
import com.grizzly.habbohotel.users.User;

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
