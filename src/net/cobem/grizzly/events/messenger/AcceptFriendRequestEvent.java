package net.cobem.grizzly.events.messenger;

import net.cobem.grizzly.Grizzly;
import net.cobem.grizzly.events.Event;
import net.cobem.grizzly.events.EventRequest;
import net.cobem.grizzly.habbohotel.sessions.Session;
import net.cobem.grizzly.habbohotel.users.User;
import net.cobem.grizzly.habbohotel.users.UserHandler;

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
