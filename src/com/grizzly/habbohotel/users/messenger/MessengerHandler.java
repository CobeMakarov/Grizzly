package com.grizzly.habbohotel.users.messenger;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import com.grizzly.Grizzly;
import com.grizzly.events.EventResponse;
import com.grizzly.events.composers.ComposerLibrary;
import com.grizzly.habbohotel.sessions.Session;
import com.grizzly.habbohotel.users.User;
import com.grizzly.habbohotel.users.UserHandler;

public class MessengerHandler 
{
	private int ID;
	private Map<Integer, User> Friends;
	private Map<Integer, User> FriendRequests;
	
	public MessengerHandler(int ID) throws SQLException
	{
		this.ID = ID;
		
		Friends = new HashMap<Integer, User>();
		FriendRequests = new HashMap<Integer, User>();
		
		LoadFriends();
		LoadFriendRequests();
	}
	
	private void LoadFriends() throws SQLException
	{
		Grizzly.GrabDatabase().SetQuery("SELECT * FROM server_friendships WHERE user_id = '" + this.ID + "'");
		
		if (Grizzly.GrabDatabase().RowCount() > 0)
		{
			ResultSet AsFirstFriend = Grizzly.GrabDatabase().GrabTable();
			
			while(AsFirstFriend.next())
			{	
				Friends.put(
						new Integer(AsFirstFriend.getInt("id")), 
						UserHandler.GenerateUser(AsFirstFriend.getInt("friend_id")));
			}
		}
		
		Grizzly.GrabDatabase().SetQuery("SELECT * FROM server_friendships WHERE friend_id = '" + this.ID + "'");
		
		if (Grizzly.GrabDatabase().RowCount() > 0)
		{
			ResultSet AsSecondFriend = Grizzly.GrabDatabase().GrabTable();
			
			while(AsSecondFriend.next())
			{	
				Friends.put(
						new Integer(AsSecondFriend.getInt("id")), 
						UserHandler.GenerateUser(AsSecondFriend.getInt("user_id")));
			}
		}
	}
	
	private void LoadFriendRequests() throws SQLException
	{
		Grizzly.GrabDatabase().SetQuery("SELECT * FROM server_friendships_pending WHERE reciever_id = '" + this.ID + "'");
		
		if (Grizzly.GrabDatabase().RowCount() > 0)
		{
			ResultSet Pending = Grizzly.GrabDatabase().GrabTable();
			
			while(Pending.next())
			{
				FriendRequests.put(
						new Integer(Pending.getInt("id")), 
						UserHandler.GenerateUser(Pending.getInt("sender_id")));
			}
		}
	}
	
	public Map<Integer, User> GrabFriends()
	{
		return Friends;
	}
	
	public Map<Integer, User> GrabRequests()
	{
		return FriendRequests;
	}
	
	public boolean IsFriend(int ID)
	{
		return Friends.containsValue(UserHandler.GenerateUser(ID));
	}
	
	public void UpdateStatus(boolean Online)
	{
		Session Session = Grizzly.GrabHabboHotel().GrabSessionHandler().GrabSessionByUserID(this.ID);
		
		Session.GrabResponse().Initialize(ComposerLibrary.UpdateFriendState);
		Session.GrabResponse().AppendInt32(0);
		Session.GrabResponse().AppendInt32(1);
		Session.GrabResponse().AppendInt32(0);
		Session.GrabResponse().AppendInt32(Session.GrabHabbo().ID);
		Session.GrabResponse().AppendString(Session.GrabHabbo().Username);
		Session.GrabResponse().AppendInt32(1);
		Session.GrabResponse().AppendBoolean(Online);
		Session.GrabResponse().AppendBoolean(false);
		Session.GrabResponse().AppendString(Session.GrabHabbo().Look);
		Session.GrabResponse().AppendInt32(0);
		Session.GrabResponse().AppendString(Session.GrabHabbo().Motto);
		Session.GrabResponse().AppendInt32(0);
		Session.GrabResponse().AppendInt32(0);
		Session.GrabResponse().AppendInt32(0);
		Send(Session, Session.ReturnResponse());
	}
	
	/*
	 * Credits Quackster
	 * TODO: Use a better method..
	 */
	public void Send(Session Session, EventResponse Message)
	{
		for (User mUser : this.GrabFriends().values())
		{
			for (Session sSession : Grizzly.GrabHabboHotel().GrabSessionHandler().GrabSessions().values())
			{
				if (Session.GrabHabbo() != null && mUser != null)
				{
					if (mUser.ID == sSession.GrabHabbo().ID)
					{
						sSession.SendResponse(Message);
					}
				}
			}
		}
	}
}
