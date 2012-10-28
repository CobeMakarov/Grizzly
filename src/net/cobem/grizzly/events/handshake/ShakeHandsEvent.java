package net.cobem.grizzly.events.handshake;

import net.cobem.grizzly.Grizzly;
import net.cobem.grizzly.events.Event;
import net.cobem.grizzly.events.EventRequest;
import net.cobem.grizzly.events.composers.ComposerLibrary;
import net.cobem.grizzly.habbohotel.sessions.Session;

public class ShakeHandsEvent implements Event
{
	@Override
	public void Parse(Session Session, EventRequest Request) throws Exception 
	{
		Session.GrabResponse().Initialize(ComposerLibrary.Login);
		
		Session.SendResponse();
		
		/*if (Session.GrabHabbo().Rank >= 5)
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
		}*/
		
		/*
		 * Init Messenger
		 */
		Session.GrabHabbo().InitMessenger();
		
		Grizzly.WriteOut(Session.GrabHabbo().Username + " has successfully connected!");
		
		Grizzly.GrabDatabase().RunFastQuery("DELETE FROM server_clients WHERE ticket = '" + Session.GrabHabbo().Ticket + "'");
		Grizzly.GrabDatabase().RunFastQuery("UPDATE server_users SET online = '1' WHERE id = '" + Session.GrabHabbo().ID + "'");
	}
}
