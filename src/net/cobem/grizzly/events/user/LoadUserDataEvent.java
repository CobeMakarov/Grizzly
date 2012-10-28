package net.cobem.grizzly.events.user;

import net.cobem.grizzly.events.*;
import net.cobem.grizzly.events.composers.ComposerLibrary;
import net.cobem.grizzly.events.messenger.InitializeFriendListEvent;
import net.cobem.grizzly.habbohotel.sessions.Session;

public class LoadUserDataEvent implements Event
{
	@Override
	public void Parse(Session Session, EventRequest Request) throws Exception 
	{
		EventResponse Message = new EventResponse();
		
		Message.Initialize(ComposerLibrary.UserInfo);
        Message.AppendInt32(Session.GrabHabbo().ID);
        Message.AppendString(Session.GrabHabbo().Username);
        Message.AppendString(Session.GrabHabbo().Look);
        Message.AppendString(Session.GrabHabbo().Gender.toString());
        Message.AppendString(Session.GrabHabbo().Motto);
        Message.AppendString(Session.GrabHabbo().Username.toLowerCase());
        Message.AppendBoolean(true);
        Message.AppendInt32(8); //8
        Message.AppendInt32(1); //3
        Message.AppendInt32(1); //3
        Message.AppendBoolean(true);
        Message.AppendString("26-07-2012");
        Message.AppendBoolean(false);
        Message.AppendBoolean(false);
		Session.SendResponse(Message);

		/*
		 * Credits
		 */
		
		Message.Initialize(ComposerLibrary.SendCredits);
		Message.AppendString("" + Session.GrabHabbo().Credits + ".0");
		Session.SendResponse(Message);
		
		/*
		 * Club
		 */
		Message.Initialize(ComposerLibrary.ClubData);
		Message.AppendString("club_vip");
        Message.AppendInt32(200); // DaysLeft
        Message.AppendInt32(200); // Months Left
        Message.AppendInt32(200); // Years left wtf?
        Message.AppendInt32(2); // VIP (1 = no / 2 = yes)
        Message.AppendBoolean(false);
        Message.AppendBoolean(true);
        Message.AppendInt32(100);
        Message.AppendInt32(20); // Days I have
        Message.AppendInt32(100);
		Session.SendResponse(Message);
		
		/*
		 * Friends
		 */
		(new InitializeFriendListEvent()).Parse(Session, Request);
		
		/*
		 * Reload Friends Messenger
		 */
		Session.GrabHabbo().RefreshMessenger(true);
		
		//Session.SendAlert("Welcome to Grizzly Alpha Testing!", "http://cobem.net/");
	}
}

