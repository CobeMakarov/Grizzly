package com.grizzly.events.messenger;

import com.grizzly.Grizzly;
import com.grizzly.events.Event;
import com.grizzly.events.EventRequest;
import com.grizzly.habbohotel.sessions.Session;
import com.grizzly.habbohotel.users.User;
import com.grizzly.habbohotel.users.UserHandler;

public class AcceptFriendRequestEvent implements Event
{
	@Override
	public void Parse(Session Session, EventRequest Request) throws Exception 
	{
		int Count = Request.PopInt();
		
		for (int i = 0; i < Count; i++)
		{
			User mUser = UserHandler.GenerateUser(Request.PopInt());
			
			Grizzly.GrabDatabase().RunFastQuery(
					"INSERT INTO server_friendships (friend_one_id, friend_two_id) VALUES ('" + Session.GrabHabbo().ID +
					"', '" + mUser.ID + "')");

			Grizzly.GrabDatabase().RunFastQuery("DELETE FROM server_friendships_pending WHERE sender_id = '" + mUser.ID + 
					"' AND reciever_id = '" + Session.GrabHabbo().ID + "'");
			
			if ((Grizzly.GrabHabboHotel().GrabSessionHandler().GrabSessionByUserID(mUser.ID) != null))
			{
				mUser.RefreshMessenger(true);
				
				Session.GrabHabbo().RefreshMessenger(true);
			}
		}
	}
}
