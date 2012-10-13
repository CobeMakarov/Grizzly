package com.grizzly.events.handshake;

import com.grizzly.Grizzly;
import com.grizzly.events.Event;
import com.grizzly.events.EventRequest;
import com.grizzly.events.composers.ComposerLibrary;
import com.grizzly.habbohotel.sessions.Session;

public class ShakeHandsEvent implements Event
{
	@Override
	public void Parse(Session Session, EventRequest Request) throws Exception 
	{
		Session.GrabResponse().Initialize(ComposerLibrary.Login);
		
		Session.SendResponse();
		
		if (Session.GrabHabbo().Rank >= 5)
		{
			Session.GrabResponse().Initialize(ComposerLibrary.ModTool);
			Session.GrabResponse().AppendInt32(-1);
			Session.GrabResponse().AppendInt32(0);
			Session.GrabResponse().AppendInt32(0);
			Session.GrabResponse().AppendBoolean(true);
			Session.GrabResponse().AppendBoolean(true);
			Session.GrabResponse().AppendBoolean(true);
			Session.GrabResponse().AppendBoolean(true);
			Session.GrabResponse().AppendBoolean(true);
			Session.GrabResponse().AppendBoolean(true);
			Session.GrabResponse().AppendBoolean(true);
			Session.GrabResponse().AppendInt32(0);
			Session.GrabResponse().AppendBoolean(true);
			Session.SendResponse();
		}
		
		/*
		 * Init Messenger
		 */
		Session.GrabHabbo().InitMessenger();
		
		Grizzly.WriteOut(Session.GrabHabbo().Username + " has successfully connected!");
		
		Grizzly.GrabDatabase().RunFastQuery("UPDATE server_clients SET active = '0' WHERE ticket = '" + Session.GrabHabbo().Ticket + "'");
	}
}
