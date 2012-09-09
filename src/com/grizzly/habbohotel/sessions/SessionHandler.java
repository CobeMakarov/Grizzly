package com.grizzly.habbohotel.sessions;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.jboss.netty.channel.Channel;

import com.grizzly.Grizzly;

public class SessionHandler 
{
	private Map<Channel, Session> SessionList = new HashMap<Channel, Session>();
	
	public Session CreateSession(Channel Channel)
	{
		Session mSession = new Session(Channel, (SessionList.size() + 1));
		
		SessionList.put(Channel, mSession);
		
		Grizzly.WriteOut("Started communication with " + mSession.GrabIP() + " [" + mSession.GrabID() + "]");
		
		return mSession;
	}
	
	public void KillSession(Channel Channel)
	{
		Session mSession = GrabSession(Channel);
		
		try 
		{
			mSession.GrabHabbo().RefreshMessenger(false);
		} 
		catch (SQLException e) 
		{ }
		
		SessionList.remove(Channel);
		
		Grizzly.WriteOut("Killed communication with " + mSession.GrabIP() + " [" + mSession.GrabID() + "]");
		
		mSession.GrabChannel().disconnect();
	}
	
	public Session GrabSession(Channel Channel)
	{
		if (SessionList.containsKey(Channel))
		{
			return SessionList.get(Channel);
		}
		else
		{
			Grizzly.WriteOut("For some reason Grizzly tried to retrieve an empty session!!");
			return null;
		}
	}
	
	public Session GrabSessionByUserID(int ID)
	{
		for(Session mSession : this.SessionList.values())
		{
			if (mSession.GrabHabbo().ID == ID)
			{
				return mSession;
			}
		}
		
		return null;
	}
	
	public Map<Channel, Session> GrabSessions()
	{
		return SessionList;
	}
}
